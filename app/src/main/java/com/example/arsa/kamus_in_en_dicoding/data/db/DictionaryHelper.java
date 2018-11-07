package com.example.arsa.kamus_in_en_dicoding.data.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.example.arsa.kamus_in_en_dicoding.data.db.DatabaseContract.DictionaryColumn;
import com.example.arsa.kamus_in_en_dicoding.data.model.Word;

import java.util.ArrayList;

import static com.example.arsa.kamus_in_en_dicoding.data.db.DatabaseContract.DictionaryColumn.COLOMN_MEANS;
import static com.example.arsa.kamus_in_en_dicoding.data.db.DatabaseContract.DictionaryColumn.COLOMN_WORD;

public class DictionaryHelper {
    private static String TAG = DictionaryHelper.class.getSimpleName();
    private Context context;
    private DatabaseHelper mHelper;
    private SQLiteDatabase mDatabase;

    public DictionaryHelper(Context context) {
        this.context = context;
    }

    public DictionaryHelper open() {
        mHelper = new DatabaseHelper(context);
        mDatabase = mHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDatabase.close();
    }

    public ArrayList<Word> getByWord(String tableName, String queryWord) {
        String sql = "SELECT * FROM " + tableName + " WHERE " + COLOMN_WORD + " LIKE '%" + queryWord.trim() + "%'";
        Log.d(TAG, "getByWord: " + sql);
        return doQuery(sql);
    }

    public ArrayList<Word> getAllWord(String tableName) {
        String sql = "SELECT * FROM " + tableName + " ORDER BY " + DictionaryColumn._ID + " ASC";
        return doQuery(sql);
    }

    public ArrayList<Word> doQuery(String sqlQuery) {
        Cursor cursor = mDatabase.rawQuery(sqlQuery, null);
        cursor.moveToFirst();
        ArrayList<Word> arrayLis = new ArrayList<>();
        if (cursor.getCount() > 0) {
            do {
                Word wordModel = new Word();
                wordModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DictionaryColumn._ID)));
                wordModel.setWord(cursor.getString(cursor.getColumnIndexOrThrow(COLOMN_WORD)));
                wordModel.setMeans(cursor.getString(cursor.getColumnIndexOrThrow(DictionaryColumn.COLOMN_MEANS)));

                arrayLis.add(wordModel);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }

        cursor.close();
        return arrayLis;
    }

    public void beginTransaction() {
        mDatabase.beginTransaction();
    }

    public void insertTransaction(String tableName, Word word) {
        String sql = "INSERT INTO " + tableName + " (" + COLOMN_WORD + ", " + COLOMN_MEANS + ") VALUES (?, ?)";
        SQLiteStatement stmt = mDatabase.compileStatement(sql);
        stmt.bindString(1, word.getWord());
        stmt.bindString(2, word.getMeans());
        stmt.execute();
        stmt.clearBindings();
    }

    public void setTransactionSuccess() {
        mDatabase.setTransactionSuccessful();
    }

    public void endTransaction() {
        mDatabase.endTransaction();
    }
}
