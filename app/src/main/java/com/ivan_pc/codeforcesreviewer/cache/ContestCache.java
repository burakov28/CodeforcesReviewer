package com.ivan_pc.codeforcesreviewer.cache;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.ivan_pc.codeforcesreviewer.exceptions.MyException;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Ivan-PC on 26.12.2016.
 */

public class ContestCache {
    private final Context context;

    public ContestCache(Context context) {
        this.context = context;
    }

    public String get(String url) throws MyException {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = null;
        String ans;
        try {
            cursor = db.query("contests",
                    new String[] { CacheContract.TIME, CacheContract.URL, CacheContract.JSON_STRING},
                    CacheContract.URL + "=?",
                    new String[] { url },
                    null,
                    null,
                    null);

            if (cursor != null && cursor.moveToFirst()) {
                Calendar curTime = new GregorianCalendar();
                long timeInMillis = curTime.getTimeInMillis();
                long timeContest = cursor.getLong(0);
                if (timeInMillis - timeContest > 60 * CacheContract.bestFor * 1000) {
                    db.delete(CacheContract.TABLE_NAME, CacheContract.URL + "=?",
                            new String[] { url });
                    throw new MyException("Too old data");
                }
                ans = cursor.getString(2);
            }
            else {
                throw new MyException("No such data");
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return ans;
    }

    public void put(String url, String json) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        SQLiteStatement insert = null;
        try {
            insert = db.compileStatement("INSERT INTO " + CacheContract.TABLE_NAME + " ("
                    + CacheContract.TIME + ", " + CacheContract.URL + ", " + CacheContract.JSON_STRING
                    + ") VALUES (?, ?, ?)");
            insert.bindLong(1, new GregorianCalendar().getTimeInMillis());
            insert.bindString(2, url);
            insert.bindString(3, json);
            insert.executeInsert();
        } finally {
            if (insert != null) {
                insert.close();
            }
        }
    }
}
