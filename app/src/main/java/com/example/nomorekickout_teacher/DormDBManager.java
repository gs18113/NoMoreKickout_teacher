package com.example.nomorekickout_teacher;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class DormDBManager extends SQLiteOpenHelper {
    public static final String ROOT_DIR = "/data/data/com.example.nomorekickout_teacher/databases/";
    private static final String DATABASE_NAME = "db";
    private Integer returnval;
    private Boolean finished=false;
    SQLiteDatabase db;

    public DormDBManager(Context context) {
        super(context, DATABASE_NAME, null, 1);
        setDB(context);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    private class SendQuery extends AsyncTask<Pair<String,Integer>,String,Pair<Integer,Pair<String,Integer>>> {

        @Override
        protected void onPostExecute(Pair<Integer,Pair<String,Integer>> s) {
            super.onPostExecute(s);
            Log.v("result", String.valueOf(s.first));
            returnval=s.first;

            String sql = "select * from dormInfo where ID="+returnval;

            Cursor cursor = db.rawQuery(sql, null);

            if (cursor.moveToFirst()) {
                do {
                    Log.v("asfd", cursor.getString(0)+" "+cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3));
                } while (cursor.moveToNext());
            }


            ContentValues contentValues = new ContentValues();
            contentValues.put("ID", returnval);
            contentValues.put("building", s.second.first);
            contentValues.put("room", s.second.second);
            contentValues.put("members", "");

            Log.v("asdf", returnval+"*******************");


            db.insert("dormInfo", null, contentValues);
        }

        @SafeVarargs
        @Override
        protected final Pair<Integer,Pair<String,Integer>> doInBackground(Pair<String, Integer>... pairs) {
            try {
                String url="http://34.84.59.141/";
                URL object = new URL(url);

                HttpURLConnection con = (HttpURLConnection) object.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                con.setRequestProperty("Accept","application/json");
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setChunkedStreamingMode(0);

                JSONObject cred = new JSONObject();
                Log.v("asdf", pairs[0].first+" 2");
                cred.put("building", pairs[0].first);
                cred.put("room", pairs[0].second);
                cred.put("members", "");
                Log.v("asdf", cred.toString());

                con.connect();

                DataOutputStream localDataOutputStream = new DataOutputStream(con.getOutputStream());
                localDataOutputStream.writeBytes("qtype=addRoom&json="+cred.toString());
                localDataOutputStream.flush();

                int status = con.getResponseCode();
                Log.v("asdf", status+"$$$$$$$$$$$$$$$$$$$");

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String decodedString;
                StringBuilder stringBuilder = new StringBuilder();
                while ((decodedString = in.readLine()) != null) {
                    stringBuilder.append(decodedString);
                }
                localDataOutputStream.close();
                in.close();
                con.disconnect();
                Integer response = Integer.parseInt(stringBuilder.toString());
                return Pair.create(response, pairs[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Pair.create(0, Pair.create("",0));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public static void setDB(Context context) {
        File folder = new File(ROOT_DIR);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        AssetManager assetmanager = context.getResources().getAssets();

        File out = new File(ROOT_DIR+DATABASE_NAME);

        InputStream is = null;
        FileOutputStream fos = null;

        int filesize = 0;

        try {
            is = assetmanager.open(DATABASE_NAME, AssetManager.ACCESS_BUFFER);

            filesize = is.available();

            if (out.length()<=0) {
                byte[] temp = new byte[filesize];
                is.read(temp);
                is.close();

                out.createNewFile();
                fos = new FileOutputStream(out);
                fos.write(temp);
                fos.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<HashMap<String,String>> getAll() {
        ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();

        String sql = "select * from dormInfo order by ID";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("ID",cursor.getString(0));
                data.put("building",cursor.getString(1));
                data.put("room",cursor.getString(2));
                data.put("members",cursor.getString(3));

                arraylist.add(data);

            } while (cursor.moveToNext());
        }

        db.close();
        cursor.close();
        return arraylist;
    }

    public Boolean addDorm(String building, int room) {
        String sql="select * from dormInfo where building='"+building+"'";
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql,null);

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getInt(2)==room) return false;
            }while (cursor.moveToNext());
        }

        Log.v("asdf", building+" 1");
        SendQuery sendQuery = new SendQuery();
        sendQuery.execute(Pair.create(building,room));

        return true;
    }

    public Boolean deleteDorm(String building, int room) {
        String sql="select * from dormInfo where building='"+building+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql,null);

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getInt(2)==room) {
                    db.delete("dormInfo", "ID="+cursor.getInt(0), null);
                    //Log.v("asdf", cursor.getInt(0)+"%%%%%%%%%%%%%%");
                    return true;
                }
            }while (cursor.moveToNext());
        }

        return false;
    }
}