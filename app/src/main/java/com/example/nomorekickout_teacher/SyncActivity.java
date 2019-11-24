package com.example.nomorekickout_teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Pair;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;

public class SyncActivity extends AppCompatActivity {

    DormDBManager dormDBManager;

    ServerManager serverManager = new ServerManager("http://34.84.59.141", new ServerManager.OnResult() {
        @Override
        public void handleResult(Pair<String, String> s) {
            Gson gson = new Gson();
            String json = s.second;

            SQLiteDatabase db = dormDBManager.getReadableDatabase();
            db.execSQL("delete from dormInfo");

            try {
                JSONArray jsonArray = new JSONArray(json);
                int index = 0;
                while (index < jsonArray.length()) {
                    Dorm dorm = gson.fromJson(jsonArray.get(index).toString(), Dorm.class);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("ID", dorm.ID);
                    contentValues.put("building", dorm.building);
                    contentValues.put("room", dorm.room);
                    contentValues.put("members", dorm.members);
                    contentValues.put("isawake", dorm.isawake);

                    db.insert("dormInfo", null, contentValues);
                    index++;
                }
                finish();
            } catch (Exception e) {
                Toast toast = Toast.makeText(getApplicationContext(), "요청 통신 오류. 재시도중...", Toast.LENGTH_SHORT);
                toast.show();
                serverManager.execute(Pair.create("qtype", "getAllRooms"));
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        dormDBManager = new DormDBManager(this);
        serverManager.execute(
                Pair.create("qtype", "getAllRooms")
        );
    }

    @Override
    public void onBackPressed() {

    }
}
