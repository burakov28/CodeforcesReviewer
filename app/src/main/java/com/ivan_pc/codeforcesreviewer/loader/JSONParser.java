package com.ivan_pc.codeforcesreviewer.loader;

import android.util.Log;

import com.ivan_pc.codeforcesreviewer.models.Contest;
import com.ivan_pc.codeforcesreviewer.exceptions.MyException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ivan-PC on 18.12.2016.
 */

class JSONParser {

    private static final String LOG_TAG = JSONParser.class.getSimpleName();

    static ArrayList<Contest> parseContest(String jsonString) throws MyException, JSONException {
        ArrayList<Contest> ret = new ArrayList<>();
        JSONObject json = new JSONObject(jsonString);
        if (!json.get("status").equals("OK")) {
            Log.d(LOG_TAG, "FAILURE");
            throw new MyException("Server doesn't response");
        }

        JSONArray result = json.getJSONArray("result");
        for (int i = 0; i < result.length(); ++i) {
            JSONObject contest = result.getJSONObject(i);
            //Log.d(LOG_TAG, contest.toString());
            int id = contest.getInt("id");
            String name = contest.getString("name");
            String type = contest.getString("type");
            String phase = contest.getString("phase");
            long duration = contest.getLong("durationSeconds");
            //Log.d(LOG_TAG, "stopping");

            long startTime = -1;
            if (contest.has("startTimeSeconds")) {
                startTime = contest.getLong("startTimeSeconds");
            }

            long relativeTime = -1;
            if (contest.has("relativeTimeSeconds")) {
                relativeTime = contest.getLong("relativeTimeSeconds");
                if (relativeTime < 0) relativeTime = -1;
            }
            String prepareBy = null;
            if (contest.has("preparedBy")) prepareBy = contest.getString("preparedBy");
            //
            String description = null;
            if (contest.has("description")) description = contest.getString("description");

            String websiteURL = null;
            if (contest.has("websiteUrl")) websiteURL = contest.getString("websiteUrl");



            int difficulty = 0;
            if (contest.has("difficulty")) difficulty = contest.getInt("difficulty");

            String kind = null;
            if (contest.has("kind")) kind = contest.getString("kind");

            String icpcRegion = null;
            if (contest.has("icpcRegion")) icpcRegion = contest.getString("icpcRegion");

            String country = null;
            if (contest.has("country")) country = contest.getString("country");

            String city = null;
            if (contest.has("city")) city = contest.getString("city");

            String season = null;
            if (contest.has("season")) season = contest.getString("season");
            //Log.d(LOG_TAG, "ending iteration");
            ret.add(new Contest(id, name, type, phase, duration, startTime, relativeTime, prepareBy,
                    websiteURL, description, difficulty, kind, icpcRegion, country, city, season));
            //Log.d(LOG_TAG, "beginning iteration");
        }
        return ret;
    }
}
