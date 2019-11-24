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

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CheckActivity extends AppCompatActivity {

    Spinner selectBuilding, selectFloor;
    ArrayAdapter<String> ad_building, ad_floor;
    Map<String, Boolean> building = new HashMap<>();
    Map<Integer, Boolean> floor = new HashMap<>();
    Map<Integer, Boolean> isAwake = new HashMap<>();
    DormDBManager dbManager;
    ArrayList<HashMap<String, String>> arrayList;
    ArrayList<MyItem> myItems = new ArrayList<>();
    ListView listView;
    myAdapter adapter;

    ServerManager serverManager = new ServerManager("http://34.84.59.141", new ServerManager.OnResult() {
        @Override
        public void handleResult(Pair<String, String> s) {
            if (s.first.equals("wakeAll"));
            else if (s.first.equals("getRoomAwake"));
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        Intent intent = getIntent();

        selectBuilding=(Spinner)findViewById(R.id.selectbuilding);
        selectFloor=(Spinner)findViewById(R.id.selectfloor);

        dbManager = new DormDBManager(this);
        arrayList = dbManager.getAll();

        ArrayList<String> buildingnames = new ArrayList<>();
        buildingnames.add("모든 건물");
        for (int i=0; i<arrayList.size(); i++) {
            String now = arrayList.get(i).get("building");
            if (building.containsKey(now)||now.equals("")) continue;
            buildingnames.add(now);
            building.put(now, true);
            isAwake.put(Integer.parseInt(arrayList.get(i).get("ID")), false);
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

        Log.v("gegegegege", dbarray.toString());

        for (int i=0; i<dbarray.size(); i++) {
            myItems.add(new MyItem(dbarray.get(i).get("building")+" "+dbarray.get(i).get("room")+"호", dbarray.get(i).get("isawake").equals("1"), Integer.parseInt(dbarray.get(i).get("ID"))));
        }

        adapter.notifyDataSetChanged();
    }

    public void changePlace(View view) {changeList();}

    public void sendAlarm(View view) {serverManager.execute(Pair.create("qtype", "wakeAll"));}

    public void getBack(View view) {finish();}
}