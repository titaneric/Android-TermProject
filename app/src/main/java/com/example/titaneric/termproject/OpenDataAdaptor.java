package com.example.titaneric.termproject;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.util.HashMap;
import java.io.IOException;

/**
 * Created by TitanEric on 12/21/2016.
 */

public class OpenDataAdaptor {
    private final Context mContext;
    private SQLiteDatabase mDb;
    private OpenDataDB mDbHelper;

    public OpenDataAdaptor(Context context, String dbName)
    {
        this.mContext = context;
        mDbHelper = new OpenDataDB(mContext, dbName);
    }

    public OpenDataAdaptor createDatabase() throws SQLException
    {
        try
        {
            mDbHelper.createDataBase();
        }
        catch (IOException mIOException)
        {
            //Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public OpenDataAdaptor open() throws SQLException
    {
        try
        {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        }
        catch (SQLException mSQLException)
        {
            //Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close()
    {
        mDbHelper.close();
    }

    public String[] getTableName(){
        Cursor c = mDb.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if(c!=null){
            c.moveToFirst();
        }
        String[] tableList = new String[c.getCount()];
        for(int i=0;i<tableList.length;i++){
            tableList[i] = new String();
        }
        int i=0;
        while(!c.isLast()){
            tableList[i] = c.getString(0);
            c.moveToNext();
            i++;
        }
        return tableList;
    }

    public String[] getTestData(String tableName)
    {
        try
        {
            String sql =String.format("SELECT * FROM %s", tableName);

            Cursor mCur = mDb.rawQuery(sql, null);
            mCur.moveToFirst();
            String[] placeArray = new String[mCur.getCount()];
            for(int i=0;i<placeArray.length;i++){
                placeArray[i] = new String();
            }
            int i=0;
            while(!mCur.isLast()){
                placeArray[i] = mCur.getString(0);
                mCur.moveToNext();
                i++;
            }
            return placeArray;
        }
        catch (SQLException mSQLException)
        {
            //Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    public HashMap lookForOtherAttribute(String id, String tableName){
        String sql =String.format("SELECT * FROM %s WHERE location = \"%s\"", tableName, id);
        Cursor c = mDb.rawQuery(sql, null);
        if(c!=null){
            c.moveToFirst();
        }
        HashMap m = new HashMap();

        for(int i=0;i<c.getColumnCount();i++){
            m.put(c.getColumnName(i), c.getString(i));
        }
        return m;
    }
}
