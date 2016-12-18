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

public class Loader extends Service {

    protected static final String GYM_URL = "http://codeforces.com/api/contest.list?gym=true";
    protected static final String COMPETITION_URL = "http://codeforces.com/api/contest.list?gym=false";
    //TODO private static final String NEWS_URL = ""
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
                rt = new RunnableTask(true, true, pendingIntent, Loader.this);
                th = new Thread(rt);
                th.run();
                //downloadTasks(true, pendingIntent);
                break;
            case MainActivity.COMPETITIONS_CODE:
                rt = new RunnableTask(true, false, pendingIntent, Loader.this);
                th = new Thread(rt);
                th.start();
                //downloadTasks(false, pendingIntent);
                break;
            case MainActivity.NEWS_CODE:
                //TODO: downloadNews(pendingIntent);
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
