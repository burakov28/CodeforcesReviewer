package com.ivan_pc.codeforcesreviewer.loader;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.ivan_pc.codeforcesreviewer.models.Contest;
import com.ivan_pc.codeforcesreviewer.MainActivity;
import com.ivan_pc.codeforcesreviewer.cache.ContestCache;
import com.ivan_pc.codeforcesreviewer.exceptions.MyException;

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
    private static final String RU = "ru";
    private static final String EN = "en";

    public static final String IS_GYM_KEY = "is_gym_key";
    public static final String LANGUAGE_KEY = "language_key";
    public static final String ERROR_MESSAGE_KEY = "error_message_key";
    public static final String CONNECTION_LOST = "connection_lost";
    public static final String WRONG_DATA_DOWNLOADED = "wrong_data_downloaded";

    private HttpURLConnection connection;
    private PendingIntent pendingIntent;
    private final boolean isGym;
    private String language;
    private Loader loader;
    private ContestCache cache;

    RunnableTask (boolean isGym, PendingIntent pendingIntent, Loader loader, String language) {
        this.isGym = isGym;
        this.pendingIntent = pendingIntent;
        this.loader = loader;
        this.language = language;
        cache = new ContestCache(loader);
    }

    private String getFromCache(String urlString) {
        String ret = null;
        try {
            ret = cache.get(urlString);
        } catch (MyException e) {
            Log.d(LOG_TAG, e.cause);
        }
        return ret;
    }

    private String getJSONString(String urlString) throws IOException {

        String answer = getFromCache(urlString);

        if (answer != null) {
            Log.d(LOG_TAG, "downloaded from cache");
            return answer;
        }
        else {
            Log.d(LOG_TAG, "cache miss");
            Log.d(LOG_TAG, urlString);
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
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.d(LOG_TAG, "Can't close reader");
                        e.printStackTrace();
                    }
                }
            }
            answer = jsonString.toString();
            cache.put(urlString, answer);
            return answer;
        }
    }


    private void downloadTasks(boolean isGym, PendingIntent pendingIntent) {
        String jsonString;
        int code;
        if (isGym) {
            code = MainActivity.GYM_CODE;
        }
        else {
            code = MainActivity.COMPETITIONS_CODE;
        }

        Intent errorIntent = new Intent().putExtra(IS_GYM_KEY, code)
                .putExtra(LANGUAGE_KEY, language);
        try {
            String ending;
            if (language.equals(MainActivity.RUSSIAN)) {
                ending = RU;
            }
            else {
                ending = EN;
            }
            if (isGym) {
                jsonString = getJSONString(Loader.GYM_URL + ending);
            } else {
                jsonString = getJSONString(Loader.COMPETITION_URL + ending);
            }
        } catch (IOException e) {

            errorIntent.putExtra(ERROR_MESSAGE_KEY, CONNECTION_LOST);
            try {
                pendingIntent.send(loader, MainActivity.ERROR_CODE, errorIntent);
            } catch (PendingIntent.CanceledException pe) {
                Log.d(LOG_TAG, "Can't cancel service");
                pe.printStackTrace();
            }
            return;
        }
        //Log.d(LOG_TAG, jsonString);
        ArrayList<Contest> contests = null;
        try {
            contests = JSONParser.parseContest(jsonString);
        } catch (MyException | JSONException e) {

            errorIntent.putExtra(ERROR_MESSAGE_KEY, WRONG_DATA_DOWNLOADED);
            try {
                pendingIntent.send(loader, MainActivity.ERROR_CODE, errorIntent);
            } catch (PendingIntent.CanceledException pe) {
                Log.d(LOG_TAG, "Can't cancel service");
                pe.printStackTrace();
            }

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
