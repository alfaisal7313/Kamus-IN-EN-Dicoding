package com.example.arsa.kamus_in_en_dicoding.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.arsa.kamus_in_en_dicoding.data.db.DatabaseContract.TABLE_WORD_EN;
import static com.example.arsa.kamus_in_en_dicoding.data.db.DatabaseContract.TABLE_WORD_ID;
import static com.example.arsa.kamus_in_en_dicoding.data.db.DatabaseContract.sqlCreateTable;
import static com.example.arsa.kamus_in_en_dicoding.data.db.DatabaseContract.sqlDeleteTable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "dictionary.db";
    private static int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sqlCreateTable(TABLE_WORD_ID));
        sqLiteDatabase.execSQL(sqlCreateTable(TABLE_WORD_EN));
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(sqlDeleteTable(TABLE_WORD_ID));
        sqLiteDatabase.execSQL(sqlDeleteTable(TABLE_WORD_EN));
        onCreate(sqLiteDatabase);
    }
}
