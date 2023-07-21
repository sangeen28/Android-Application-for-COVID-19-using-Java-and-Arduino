package com.example.temphumid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "data.db";
    public static final String TABLE_PATIENTS = "datatable";
    public static final String COLUMN_ID = "cnic";
    public static final String COLUMN_NAME = "pname";
    public static final String COLUMN_FNAME = "fathername";



    public DatabaseHelper(Context context, String name,SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_PATIENTS + "(" +
                COLUMN_ID + "TEXT"+
                COLUMN_NAME + "TEXT"+
                COLUMN_FNAME + "TEXT"+
                ");";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENTS);
        onCreate(db);


    }

    public void addPatient(Patients patients) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID,patients.getCnic());
        values.put(COLUMN_NAME,patients.getPatientname());
        values.put(COLUMN_FNAME,patients.getFname());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.insert(TABLE_PATIENTS,null,values);
        sqLiteDatabase.close();
    }

    public String readData() {
        String data = "";
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT * FROM " +TABLE_PATIENTS+ "";
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            if (cursor.getString(cursor.getColumnIndex("cnic"))!=null) {
                data = cursor.getString(cursor.getColumnIndex("pname"));
               // data = data + cursor.getString(cursor.getColumnIndex("fname"));

            }
        }
        sqLiteDatabase.close();
        return data;

    }


}
