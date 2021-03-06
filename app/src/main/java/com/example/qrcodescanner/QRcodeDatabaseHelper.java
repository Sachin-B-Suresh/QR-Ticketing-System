package com.example.qrcodescanner;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

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
        ContentValues contentValues= new ContentValues();
        contentValues.put(COL1,"asdf123!@#");
        db.insert(TABLE_NAME, null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
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

    public void populateDB(Uri uri, Activity act) throws Exception {
        InputStream inputStream = act.getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            insertRecord(line.trim());
        }
        inputStream.close();
        //parcelFileDescriptor.close();
        /*
        File file = new File(new URI(uri.getPath()));
        FileReader fileReader = new FileReader(file);
        BufferedReader buffer = new BufferedReader(fileReader);
        String line;
        while ((line = buffer.readLine()) != null) {
            insertRecord(line.trim());
        }
        */
    }


    public boolean checkQRcodeExist(String qrcode){
        String[] columns = {"code"};
        SQLiteDatabase db= this.getReadableDatabase();

        String selection = "code=?";
        String[] selectionArgs = {qrcode};

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

    public boolean consumeQRcode(String qrcode)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {"code"};
        db.delete(TABLE_NAME, "code = ?", new String[] { qrcode });
        db.close();
        return true;
    }
}