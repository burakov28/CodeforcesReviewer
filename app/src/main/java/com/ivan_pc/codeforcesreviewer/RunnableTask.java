package com.ivan_pc.codeforcesreviewer;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Ivan-PC on 19.12.2016.
 */

// it downloads JSONString

public class RunnableTask implements Runnable {
    private static final String LOG_TAG = RunnableTask.class.getSimpleName();
    HttpURLConnection connection;
    PendingIntent pendingIntent;
    private final boolean isGym;
    Loader loader;

    RunnableTask (boolean isGym, PendingIntent pendingIntent, Loader loader) {
        this.isGym = isGym;
        this.pendingIntent = pendingIntent;
        this.loader = loader;
    }

    String getJSONString(String urlString) throws IOException {
        URL url;
        BufferedReader reader = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new IOException();
        }
        StringBuilder jsonString;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try { reader.close(); }
                catch (IOException e) {
                    Log.d(LOG_TAG, "Can't close reader");
                    e.printStackTrace();
                }
            }
        }
        return jsonString.toString();
    }


    void downloadTasks(boolean isGym, PendingIntent pendingIntent) {
        String jsonString;

        try {
            if (isGym) {
                jsonString = getJSONString(Loader.GYM_URL);
            } else {
                jsonString = getJSONString(Loader.COMPETITION_URL);
            }
        } catch (IOException e) {
            // TODO: send message that we don't have the Internet!
            return;
        }
        Log.d(LOG_TAG, jsonString);
        ArrayList<Contest> contests = null;
        try {
            contests = JSONParser.parseContest(jsonString);
        } catch (MyException e) {
            // TODO: sending message that Server isn't response
        } catch (JSONException e) {
            // TODO: we have problems with parsing
        }
        Intent intent = new Intent().putExtra(MainActivity.ANSWER, contests);
        try {
            pendingIntent.send(loader, MainActivity.FINISH_CODE, intent);
        } catch (PendingIntent.CanceledException e) {
            Log.d(LOG_TAG, "Can't cancel service");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        downloadTasks(isGym, pendingIntent);
    }
}
