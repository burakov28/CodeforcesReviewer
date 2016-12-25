package com.ivan_pc.codeforcesreviewer.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ivan-PC on 26.12.2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_FILE_NAME = "contests.db";

    private static volatile DBHelper instance;

    static DBHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DBHelper.class) {
                if (instance == null) {
                    instance = new DBHelper(context);
                }
            }
        }
        return instance;
    }

    private DBHelper(Context context) {
        super(context, DB_FILE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CacheContract.TABLE_NAME + " (" + CacheContract.ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CacheContract.TIME + " INTEGER, " +
                CacheContract.URL + " TEXT, " +
                CacheContract.JSON_STRING + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
