package com.example.nomorekickout_teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class CheckActivity extends AppCompatActivity {

    Spinner selectBuilding, selectFloor;
    ArrayAdapter<String> ad_building, ad_floor;
    Map<String, Boolean> building = new HashMap<>();
    Map<Integer, Boolean> floor = new HashMap<>();
    DormDBManager dbManager;
    ArrayList<HashMap<String, String>> arrayList;
    ArrayList<MyItem> myItems = new ArrayList<>();
    ListView listView;
    myAdapter adapter;
    String late;

    ServerManager serverManager = new ServerManager("http://34.84.59.141", new ServerManager.OnResult() {
        @Override
        public void handleResult(Pair<String, String> s) {
            if(s.first.equals("wakeAll")){
                if(s.second == null){
                    Toast.makeText(CheckActivity.this, "통신 오류, 재시도중...", Toast.LENGTH_SHORT).show();
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            serverManager.execute(Pair.create("qtype", "wakeAll"));
                        }
                    }, 2000);
                }
                else{
                    Toast.makeText(CheckActivity.this, "알람 전송 완료", Toast.LENGTH_SHORT).show();
                }
            }
            else if (s.first.equals("addLate")) {
                if (s.second==null) {
                    Toast.makeText(CheckActivity.this, "통신 오류, 재시도중...", Toast.LENGTH_SHORT).show();
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            serverManager.execute(
                                    Pair.create("qtype", "addLate"),
                                    Pair.create("json", late)
                            );
                        }
                    }, 2000);
                }
                else {
                    Toast.makeText(CheckActivity.this, "지각 처리 완료", Toast.LENGTH_SHORT).show();
                }
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        startActivity(new Intent(this, SyncActivity.class));

        Intent intent = getIntent();

        selectBuilding=(Spinner)findViewById(R.id.selectbuilding);
        selectFloor=(Spinner)findViewById(R.id.selectfloor);

        dbManager = new DormDBManager(this);
        arrayList = dbManager.getAll();

        ArrayList<String> buildingnames = new ArrayList<>();
        buildingnames.add("모든 건물");
        statics.initialize();
        //Log.v("해명해", arrayList.toString());
        for (int i=0; i<arrayList.size(); i++) {
            String now = arrayList.get(i).get("building");
            statics.putmap(Integer.parseInt(arrayList.get(i).get("ID")), false);
            //Log.v("what is this", arrayList.get(i).get("ID"));
            if (building.containsKey(now)||now.equals("")) continue;
            buildingnames.add(now);
            building.put(now, true);
        }

        String[] strings = new String[buildingnames.size()];
        for (int i=0; i<buildingnames.size(); i++) strings[i]=buildingnames.get(i);

        ad_building=new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, strings);

        selectBuilding.setAdapter(ad_building);

        ArrayList<String> floors = new ArrayList<>();
        floors.add("모든 층");
        for (int i=0; i<arrayList.size(); i++) {
            if (arrayList.get(i).get("room").equals("")) continue;
            Integer now = Integer.parseInt(arrayList.get(i).get("room"));
            now=now/100;
            if (floor.containsKey(now)) continue;
            floor.put(now, true);
            floors.add(now.toString());
        }

        String[] strings2 = new String[floors.size()];
        for (int i=0; i<floors.size(); i++) strings2[i]=floors.get(i);

        ad_floor=new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, strings2);

        selectFloor.setAdapter(ad_floor);

        listView=(ListView)findViewById(R.id.listview);
        adapter = new myAdapter(myItems,CheckActivity.this);
        listView.setAdapter(adapter);
        changeList();
    }

    public void changeList() {
        String nowBuilding=selectBuilding.getSelectedItem().toString();
        String nowFloor=selectFloor.getSelectedItem().toString(), sql;
        ArrayList<HashMap<String,String>> dbarray;
        myItems.clear();
        if (nowBuilding.equals("모든 건물")) {
            if (nowFloor.equals("모든 층")) {
                sql = "select * from dormInfo";
            }
            else {
                sql = "select * from dormInfo where room between "+nowFloor+"00 and "+nowFloor+"99";
            }
        }
        else {
            if (nowFloor.equals("모든 층")) {
                sql = "select * from dormInfo where building='"+nowBuilding+"'";
            }
            else {
                sql = "select * from dormInfo where building='"+nowBuilding+"' and room between "+nowFloor+"00 and "+((Integer.parseInt(nowFloor)+1)*100-1);
            }
        }
        dbarray=dbManager.getList(sql);

        //Log.v("gegegegege", dbarray.toString());

        for (int i=0; i<dbarray.size(); i++) {
            //Log.v("print this", dbarray.get(i).get("ID"));
            //Log.v("and this", statics.getmap(Integer.parseInt(dbarray.get(i).get("ID"))).toString());
            myItems.add(new MyItem(dbarray.get(i).get("building")+" "+dbarray.get(i).get("room")+"호", statics.getmap(Integer.parseInt(dbarray.get(i).get("ID"))), Integer.parseInt(dbarray.get(i).get("ID"))));
        }

        adapter.notifyDataSetChanged();
    }

    public void changePlace(View view) {changeList();}

    public void sendAlarm(View view) {serverManager.execute(Pair.create("qtype", "wakeAll"));}

    public void getBack(View view) {
        arrayList=dbManager.getAll();
        late="[";
        Boolean flag=false;
        for (int i=0; i<arrayList.size(); i++) {
            if (arrayList.get(i).get("isawake").equals("0")) {
                if (flag) late+=",";
                else flag=true;
                late+=arrayList.get(i).get("ID");
            }
        }
        late+="]";
        Log.v("late", late);
        serverManager.execute(
                Pair.create("qtype", "addLate"),
                Pair.create("json", late)
        );
        finish();
    }
}