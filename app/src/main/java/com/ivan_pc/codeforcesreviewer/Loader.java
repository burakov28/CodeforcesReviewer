package com.ivan_pc.codeforcesreviewer;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

// It's loader, all messages sends from RunnableTask which execute downloading in WorkingThread

public class Loader extends Service {

    protected static final String GYM_URL = "http://codeforces.com/api/contest.list?gym=true&lang=";
    protected static final String COMPETITION_URL = "http://codeforces.com/api/contest.list?gym=false&lang=";
    private static final String LOG_TAG = Loader.class.getSimpleName();



    public Loader() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int taskCode = intent.getIntExtra(MainActivity.TASK_CODE, -1);
        PendingIntent pendingIntent = intent.getParcelableExtra(MainActivity.PENDING_INTENT);
        Thread th;
        RunnableTask rt;
        switch (taskCode) {
            case MainActivity.GYM_CODE:
                rt = new RunnableTask(true, pendingIntent, Loader.this,
                        intent.getStringExtra(MainActivity.LANGUAGE));
                th = new Thread(rt);
                th.start();

                break;
            case MainActivity.COMPETITIONS_CODE:
                rt = new RunnableTask(false, pendingIntent, Loader.this,
                        intent.getStringExtra(MainActivity.LANGUAGE));
                th = new Thread(rt);
                th.start();

                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
