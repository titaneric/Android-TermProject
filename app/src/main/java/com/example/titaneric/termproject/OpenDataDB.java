package com.example.titaneric.termproject;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by TitanEric on 12/21/2016.
 */

public class OpenDataDB extends SQLiteOpenHelper {
    private static String DB_NAME ="";
    private SQLiteDatabase db;
    private final Context myContext;
    private static String DB_PATH = "";
    public void onCreate(SQLiteDatabase db){

    }

    public OpenDataDB(Context context,String dbName){
        super(context, DB_NAME = dbName, null, 1);
        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.myContext = context;

    }
    public void createDataBase() throws IOException {
        if(!checkDataBase()){
            this.getReadableDatabase();
            this.close();
            try
            {
                //Copy the database from assests
                copyDataBase();
                //Log.e(TAG, "createDatabase database created");
            }
            catch (IOException mIOException)
            {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }
    private void copyDataBase() throws IOException
    {
        InputStream mInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer))>0)
        {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }
    private boolean checkDataBase()
    {
        File dbFile = new File(DB_PATH + DB_NAME);
        //Log.v("dbFile", dbFile + "   "+ dbFile.exists());
        return dbFile.exists();
    }
    public boolean openDataBase() throws SQLException
    {
        String mPath = DB_PATH + DB_NAME;
        //Log.v("mPath", mPath);
        db = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return db != null;
    }
    @Override
    public synchronized void close()
    {
        if(db != null)
            db.close();
        super.close();
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
