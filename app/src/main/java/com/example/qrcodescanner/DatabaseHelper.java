package com.example.qrcodescanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final String LOGTAG="Scan QrCode";
    public static String DB_NAME="auth.sqlite";
    public static String TABLE_NAME="userrecords";
    public static String COL1="username";
    public static String COL2="password";
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        SQLiteDatabase db=this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create TABLE "+TABLE_NAME+"(username text primary key, password text)");
        ContentValues contentValues= new ContentValues();
        contentValues.put(COL1,"admin");
        contentValues.put(COL2,"root");
        db.insert(TABLE_NAME, null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists "+TABLE_NAME);
        onCreate(db);


    }
    public void insertRecord()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(COL1,"admin");
        contentValues.put(COL2,"root");
        db.insert(TABLE_NAME, null, contentValues);
    }

    public void getAll()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cus= db.rawQuery("select * from "+TABLE_NAME,null);
        if(cus.getCount()>0)
        {
            while (cus.moveToNext())
            {
                String code = cus.getString( cus.getColumnIndex("username") );
                Log.d(LOGTAG,"Database query result:"+code );
            }
        }
    }
    public boolean checkUserExist(String username, String password){
        String[] columns = {"username"};
        SQLiteDatabase db= this.getReadableDatabase();

        String selection = "username=? and password = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();

        cursor.close();
        close();

        if(count > 0){
            return true;
        } else {
            return false;
        }
    }
}