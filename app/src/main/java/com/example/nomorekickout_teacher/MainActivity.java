package com.example.nomorekickout_teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {

    Button toCheck, toRequest, toRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toCheck=(Button)findViewById(R.id.tocheck);
        toCheck.setOnClickListener(this);
        toRequest=(Button)findViewById(R.id.torequest);
        toRequest.setOnClickListener(this);
        toRoom=(Button)findViewById(R.id.toroom);
        toRoom.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        if (view.getId()==R.id.tocheck) {
            intent = new Intent(getApplicationContext(), CheckActivity.class);
        }
        else if (view.getId()==R.id.torequest) {
            intent = new Intent(getApplicationContext(), RequestReviewActivity.class);
        }
        else {
            intent = new Intent(getApplicationContext(), RoomsActivity.class);
        }
        startActivity(intent);
    }
}
