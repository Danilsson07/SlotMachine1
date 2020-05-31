package com.example.slotmachine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "slotmachine_db";
    public static final String TABLE_NAME = "users";
    public static final String COL1 = "ID";
    public static final String COL2 = "USERNAME";
    public static final String COL3 = "PASSWORD";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db){
        String createTable = "CREATE TABLE "+ TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "USERNAME TEXT, PASSWORD TEXT)";
        db.execSQL(createTable);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
    }

    public boolean addData(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2,username);
        contentValues.put(COL3, password);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result==-1){
            return false;
        } else{
            return true;
        }
    }

    public String getPassword(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT "+ COL3 +" FROM "+TABLE_NAME +" WHERE "+COL2+ " = \'"+ username+"\'";
        Cursor data = db.rawQuery(query,null);
        if(data.getCount()>0) {
            if(data.moveToFirst()) {
                return data.getString(0);
            } else return null;
        }else return null;
    }



}
