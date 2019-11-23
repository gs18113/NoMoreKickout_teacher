package com.example.nomorekickout_teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class RoomsActivity extends AppCompatActivity {

    EditText addBuilding, addRoom, deleteBuilding, deleteRoom;
    ArrayList<HashMap<String, String>> arrayList;
    DormDBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        Intent intent = getIntent();

        addBuilding=(EditText)findViewById(R.id.addbuilding);
        addRoom=(EditText)findViewById(R.id.addroom);
        deleteBuilding=(EditText)findViewById(R.id.deletebuilding);
        deleteRoom=(EditText)findViewById(R.id.deleteroom);

        dbManager = new DormDBManager(this);
        arrayList = dbManager.getAll();
    }

    public void addConfirm(View view) {
        Boolean done = dbManager.addDorm(addBuilding.getText().toString(), Integer.parseInt(addRoom.getText().toString()));
        if (!done) {
            Toast.makeText(getApplicationContext(), "이미 존재하는 방입니다.", Toast.LENGTH_LONG).show();
        }
    }

    public void deleteConfirm(View view) {
        Boolean done = dbManager.deleteDorm(deleteBuilding.getText().toString(), Integer.parseInt(deleteRoom.getText().toString()));
        if (!done) {
            Toast.makeText(getApplicationContext(), "존재하지 않는 방입니다.", Toast.LENGTH_LONG).show();
        }
    }

    public void getBack(View view) {finish();}
}
