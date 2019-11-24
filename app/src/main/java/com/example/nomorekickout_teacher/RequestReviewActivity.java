package com.example.nomorekickout_teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class RequestReviewActivity extends AppCompatActivity {

    ListView requestList;
    ArrayList<MyItem2> myItem2s=new ArrayList<>();
    myAdapter2 adapter2;
    Pair[] requestParams;

    ServerManager serverManager = new ServerManager("http://34.84.59.141", new ServerManager.OnResult() {
        @Override
        public void handleResult(Pair<String, String> s) {
            try {
                if (s.first.equals("getAllRequests")) {
                    Gson gson = new Gson();
                    String json = s.second;
                    //Log.v("why not showing?", json);
                    ArrayList<Requests> request_list = new ArrayList<>();

                    try {
                        JSONArray jsonArray = new JSONArray(json);

                        int index = 0;
                        while (index < jsonArray.length()) {
                            Requests requests = gson.fromJson(jsonArray.get(index).toString(), Requests.class);
                            request_list.add(requests);
                            index++;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    myItem2s.clear();
                    for (int i = 0; i < request_list.size(); i++) {
                        String title = "";

                        if (request_list.get(i).getRequestType().equals("1")) {//change user
                            title = request_list.get(i).getName() + "학생이 " + request_list.get(i).getBuilding() + " " + request_list.get(i).getRoom() + "호로 변경 요청";
                        } else {//new user
                            title = request_list.get(i).getName() + "학생이 " + request_list.get(i).getBuilding() + " " + request_list.get(i).getRoom() + "호로 가입 요청";
                        }

                        myItem2s.add(new MyItem2(title, Integer.parseInt(request_list.get(i).getRID())));
                    }

                    adapter2.notifyDataSetChanged();
                }
                else if (s.first.equals("answerRequest")) {
                    Log.v("error?", s.second);
                }
            } catch (Exception e) {
                Toast toast = Toast.makeText(getApplicationContext(), "요청 통신 오류. 재시도중...", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_review);

        Intent intent = getIntent();

        requestList=(ListView)findViewById(R.id.requestlist);
        adapter2=new myAdapter2(myItem2s,RequestReviewActivity.this);
        requestList.setAdapter(adapter2);
        getAllRequest();
    }

    public void getAllRequest() {
        serverManager.execute(
                Pair.create("qtype", "getAllRequests")
        );
    }

    public void allClear(View view) {

        for (int i=0; i<myItem2s.size(); i++) {
            //Log.v("why is this still here", myItem2s.get(i).getTitle()+" "+myItem2s.get(i).getRID());
            requestParams=new Pair[]{
                    Pair.create("qtype", "answerRequest"),
                    Pair.create("RID", myItem2s.get(i).getRID().toString()),
                    Pair.create("confirm", "1")
            };
            serverManager.execute(requestParams);
        }

        myItem2s.clear();
        adapter2.notifyDataSetChanged();
    }

    public void refresh(View view) {getAllRequest();}

    public void goMain(View view) {finish();}
}