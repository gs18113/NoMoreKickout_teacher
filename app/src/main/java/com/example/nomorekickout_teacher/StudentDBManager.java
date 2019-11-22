package com.example.nomorekickout_teacher;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class StudentDBManager extends SQLiteOpenHelper {

    public static final String ROOT_DIR = "/data/data/com.example.nomorekickout_teacher/databases/";
    private static final String DATABASE_NAME = "db.db";

    public StudentDBManager(Context context) {
        super(context, DATABASE_NAME, null, 1);
        setDB(context);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
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

        String sql = "select * from studentInfo order by ID";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("ID",cursor.getString(1));
                data.put("building",cursor.getString(2));
                data.put("room",cursor.getString(3));
                data.put("name",cursor.getString(4));
                data.put("latecnt",cursor.getString(5));
                data.put("alarm",cursor.getString(6));

                arraylist.add(data);

            } while (cursor.moveToNext());
        }

        db.close();
        cursor.close();
        return arraylist;
    }
}
