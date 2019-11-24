package com.example.nomorekickout_teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CheckActivity extends AppCompatActivity {

    Spinner selectBuilding, selectFloor;
    ArrayAdapter<String> ad_building, ad_floor;
    Map<String, Boolean> building = new HashMap<>();
    Map<Integer, Boolean> floor = new HashMap<>();
    DormDBManager dbManager;
    ArrayList<HashMap<String, String>> arrayList;
    ListView listView;
    ServerManager serverManager = new ServerManager("http://34.84.59.141", new ServerManager.OnResult() {
        @Override
        public void handleResult(Pair<String, String> s) {
            if(s.first.equals("wakeAll"));
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
            floors.add(now.toString()+"층");
        }

        String[] strings2 = new String[floors.size()];
        for (int i=0; i<floors.size(); i++) strings2[i]=floors.get(i);

        ad_floor=new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, strings2);

        selectFloor.setAdapter(ad_floor);

        //
    }

    public void changeList() {
        String nowBuilding=selectBuilding.getSelectedItem().toString();
        String nowFloor=selectFloor.getSelectedItem().toString();
        if (nowBuilding.equals("모든 건물")) {
            if (nowFloor.equals("모든 층")) {
                //
            }
            else {
                //
            }
        }
        else {
            if (nowFloor.equals("모든 층")) {
                //
            }
            else {
                //
            }
        }
    }

    public void changePlace(View view) {changeList();}

    public void sendAlarm(View view) {serverManager.execute(Pair.create("qtype", "wakeAll"));}

    public void getBack(View view) {finish();}
}
