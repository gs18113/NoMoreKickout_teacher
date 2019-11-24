package com.example.nomorekickout_teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class CheckActivity extends AppCompatActivity {

    Spinner selectBuilding, selectFloor;
    ArrayAdapter<String> ad_building, ad_floor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        Intent intent = getIntent();

        selectBuilding=(Spinner)findViewById(R.id.selectbuilding);
        selectFloor=(Spinner)findViewById(R.id.selectfloor);

        String[] strings = new String[5]; //number of buildings+1

        ad_building=new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, strings);
    }

    public void changePlace(View view) {
        //
    }

    public void getBack(View view) {finish();}
}
