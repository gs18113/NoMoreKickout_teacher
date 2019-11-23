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

    public DormDBManager(Context context) {
        super(context, DATABASE_NAME, null, 1);
        setDB(context);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    private class SendQuery extends AsyncTask<Pair<String,Integer>,String,Integer> {

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
            Log.v("result", String.valueOf(s));
        }

        @SafeVarargs
        @Override
        protected final Integer doInBackground(Pair<String, Integer>... pairs) {
            try {
                String url="http://10.116.215.52/";
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
                localDataOutputStream.writeBytes(cred.toString());
                localDataOutputStream.flush();
                localDataOutputStream.close();

                /*BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String decodedString;
                StringBuilder stringBuilder = new StringBuilder();
                while ((decodedString = in.readLine()) != null) {
                    stringBuilder.append(decodedString);
                }
                in.close();
                Integer response = Integer.parseInt(stringBuilder.toString());
                return response;*/
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
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
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql,null);

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getInt(2)==room) return false;
            }while (cursor.moveToNext());
        }

        Log.v("asdf", building+" 1");
        SendQuery sendQuery = new SendQuery();
        sendQuery.execute(Pair.create(building,room));

        ContentValues contentValues = new ContentValues();
        contentValues.put("building", building);
        contentValues.put("room", room);

        db.insert("dormInfo", null, contentValues);

        return true;
    }

    public Boolean deleteDorm(String building, int room) {
        //
        return true;
    }
}