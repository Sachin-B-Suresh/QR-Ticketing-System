package com.example.qrcodescanner;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class QRcodeDatabaseHelper extends SQLiteOpenHelper {

    private final String LOGTAG="Scan QrCode";
    private static final String DATABASE_NAME = "my_qrc.sqlite";
    private static final int DATABASE_VERSION = 1;
    private final Context context;
    SQLiteDatabase db;
    private static final String DATABASE_PATH = "/data/data/com.example.qrcodescanner/databases/";
    private final String USER_TABLE = "data";
    public QRcodeDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        createDb();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void createDb(){
        boolean dbExist = checkDbExist();
        if(!dbExist){
            this.getReadableDatabase();
            copyDatabase();
        }
    }

    private boolean checkDbExist(){
        SQLiteDatabase sqLiteDatabase = null;
        try{
            String path = DATABASE_PATH + DATABASE_NAME;
            sqLiteDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        } catch (Exception ex){
        }
        if(sqLiteDatabase != null){
            sqLiteDatabase.close();
            return true;
        }
        return false;
    }

    private void copyDatabase(){
        try {
            InputStream inputStream = context.getAssets().open(DATABASE_NAME);
            String outFileName = DATABASE_PATH + DATABASE_NAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] b = new byte[1024];
            int length;
            while ((length = inputStream.read(b)) > 0){
                outputStream.write(b, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SQLiteDatabase openDatabase(){
        String path = DATABASE_PATH + DATABASE_NAME;
        db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        return db;
    }

    public void close(){
        if(db != null){
            db.close();
        }
    }

    public boolean checkQRcodeExist(String qrcode) {
        String[] columns = {"qrcode"};
        db = openDatabase();
        String selection = "qrcode=?";
        String[] selectionArgs = {qrcode};
        Cursor cursor = db.query(USER_TABLE, columns, selection, selectionArgs, null, null, null);
        int count=cursor.getCount();
        cursor.close();
        close();
        Log.d(LOGTAG,"query length :"+ count);
        if (count>0)
            return true;
        else
            return false;
    }

    public boolean consumeQRcode(String qrcode)
    {
        String[] columns = {"qrcode"};
        db = openDatabase();
        db.delete(USER_TABLE, "qrcode = ?", new String[] { qrcode });
        db.close();
        return true;
    }
}
