package com.example.storagequote;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Quotes.db";
    public static final String QUOTES_TABLE_NAME = "quotes";
    public static final String QUOTES_COLUMN_ID = "id";
    public static final String QUOTES_COLUMN_QUOTES = "quote";

    public DBHelper (Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL(
                "create table quotes " +
                        "(id integer primary key, quote text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS quotes");
        onCreate(db);
    }

    public boolean insertQuote (String quote) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("quote", quote);
        db.insert("quotes", null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from quotes where id="+id+"", null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries (db, QUOTES_TABLE_NAME);
        return numRows;
    }

    public boolean updateQuote (Integer id, String quote) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("quote", quote);
        db.update("quotes", contentValues, "id = ?", new String[] {Integer.toString(id)});
        return true;
    }

    public Integer deleteQuote (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("quotes", "id = ? ",
                new String[] {Integer.toString(id)});
    }

    public ArrayList<String> getAllQuotes() {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from quotes", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(QUOTES_COLUMN_QUOTES)));
            res.moveToNext();
        }
        return array_list;
    }

}
