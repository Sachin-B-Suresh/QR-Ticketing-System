package com.example.qrcodescanner;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class QRcodeDatabaseHelper extends SQLiteOpenHelper {
    private final String LOGTAG="Scan QrCode";
    public static String DB_NAME="qrc.sqlite";
    public static String TABLE_NAME="qrrecords";
    public static String COL1="code";
    public QRcodeDatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        SQLiteDatabase db=this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create TABLE "+TABLE_NAME+"(code text primary key)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists "+TABLE_NAME);
        onCreate(db);


    }
    public void insertRecord(String code)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(COL1,code);
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
                String code = cus.getString( cus.getColumnIndex("code") );
                Log.d(LOGTAG,"Database query result:"+code );
            }
        }
    }

    public void populateDB(Uri uri) throws IOException {
        FileReader file = new FileReader(uri.getPath());
        BufferedReader buffer = new BufferedReader(file);
        String line;
        while ((line = buffer.readLine()) != null) {
            insertRecord(line.trim());
        }
    }
}