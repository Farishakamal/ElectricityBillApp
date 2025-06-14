package com.example.electricitybillapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Electricity.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "bill_table";

    public static final String COL_ID = "ID";
    public static final String COL_MONTH = "MONTH";
    public static final String COL_UNIT = "UNIT";
    public static final String COL_REBATE = "REBATE";
    public static final String COL_TOTAL = "TOTAL";
    public static final String COL_FINAL = "FINAL";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_MONTH + " TEXT, " +
                COL_UNIT + " INTEGER, " +
                COL_REBATE + " REAL, " +
                COL_TOTAL + " REAL, " +
                COL_FINAL + " REAL)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public boolean insertData(String month, int unit, double rebate, double total, double finalCost) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_MONTH, month);
        values.put(COL_UNIT, unit);
        values.put(COL_REBATE, rebate);
        values.put(COL_TOTAL, total);
        values.put(COL_FINAL, finalCost);

        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY ID DESC", null);
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

}
