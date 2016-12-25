package com.example.titaneric.termproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

/**
 * Created by TitanEric on 12/25/2016.
 */

public class weatherDB extends SQLiteOpenHelper {
    //String location = "";
    public weatherDB(Context context){//, String loc){
        super(context, "DB", null, 1);
        //this.location = loc;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE weather (location TEXT, time TEXT, weather TEXT, maxT TEXT, minT TEXT, comfortIndex TEXT, dropPercent TEXT)";//, location);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "drop TABLE weather";

        db.execSQL(query);
        query = "CREATE TABLE weather (location TEXT, time TEXT, weather TEXT, maxT TEXT, minT TEXT, comfortIndex TEXT, dropPercent TEXT)";//, location);
        db.execSQL(query);
    }
    public void insertData(String location, String time, String weather, String maxT, String minT, String comfortIndex, String dropPercent){
        SQLiteDatabase db = this.getWritableDatabase();
        //String secStr = Integer.toString(sec);
        ContentValues values = new ContentValues();
        values.put("location", location);
        values.put("time", time);
        values.put("weather", weather);
        values.put("maxT", maxT);
        values.put("minT", minT);
        values.put("comfortIndex", comfortIndex);
        values.put("dropPercent", dropPercent);
        db.insert("weather", null, values);

    }
    public HashMap lookForOtherAttribute(String location, String timeRange){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql =String.format("SELECT * FROM weather WHERE location = \"%s\" AND time = \"%s\"", location, timeRange);
        Cursor c = db.rawQuery(sql, null);
        if(c!=null){
            c.moveToFirst();
        }
        HashMap m = new HashMap();

        for(int i=0;i<c.getColumnCount();i++){
            m.put(c.getColumnName(i), c.getString(i));
        }
        return m;
    }
    public WeatherData[] getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        //String secStr = Integer.toString(sec);
        String query = "SELECT * FROM %s";//, location);
        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null)
            cursor.moveToFirst();

        WeatherData[] wdArray = new WeatherData[cursor.getCount()];
        for(int i=0;i<cursor.getCount();i++){
            wdArray[i] = new WeatherData(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),cursor.getString(6));
        }

        return wdArray;

    }
}
