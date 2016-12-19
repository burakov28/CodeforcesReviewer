package com.ivan_pc.codeforcesreviewer;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    static final String TASK_CODE = "taskCode";
    static final int GYM_CODE = 1;
    static final int COMPETITIONS_CODE = 2;
    static final int FINISH_CODE = 100;
    static final String ANSWER = "answer";
    public static final String PENDING_INTENT = "pendingIntent";
    public static final String RUSSIAN = "russian";
    public static final String ENGLISH = "english";
    private static final String LOCALE_KEY = "locale_key";
    private static final String LAST_LOCALE_KEY = "last_key";

    public String locale;

    private OnLanguageChangeReceiver onLanguageChangeReceiver = new OnLanguageChangeReceiver();
    private int currentTaskCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            locale = savedInstanceState.getString(LOCALE_KEY);
        }
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter(Intent.ACTION_LOCALE_CHANGED);
        registerReceiver(onLanguageChangeReceiver, filter);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setDrawerListener(toggle);
        toggle.syncState();

        //TODO: add recyclerView which shows information about contest in short view
        //TODO: after pressing on a contest start a new activity with full information about the contest
        //TODO: add buttons to open the contest in browser. If it hasn't been started, show remaining time to start
        //TODO: if it has been started, show remaining time, remember about time zones
        //TODO: add progress bar while downloading is in process
        //TODO: information about Contest class int Contest.java :)
        //TODO: implement news showing, but I want to eliminate it

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(onLanguageChangeReceiver);
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(LOCALE_KEY, locale);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        PendingIntent pendingIntent;
        Intent intent;

        if (currentTaskCode != -1) {
            stopService(new Intent(this, Loader.class));
        }

        if (id == R.id.nav_competitions) {
            currentTaskCode = COMPETITIONS_CODE;
            pendingIntent = createPendingResult(COMPETITIONS_CODE, new Intent(), 0);
            intent = new Intent(this, Loader.class).putExtra(TASK_CODE, COMPETITIONS_CODE)
                    .putExtra(PENDING_INTENT, pendingIntent);
            startService(intent);
        } else if (id == R.id.nav_gym) {
            currentTaskCode = GYM_CODE;
            pendingIntent = createPendingResult(GYM_CODE, new Intent(), 0);
            intent = new Intent(this, Loader.class).putExtra(TASK_CODE, GYM_CODE)
                    .putExtra(PENDING_INTENT, pendingIntent);
            startService(intent);
        } else if (id == R.id.russian) {
            if (locale == null || !locale.equals(RUSSIAN)) {
                locale = RUSSIAN;
                updateLang(locale);
            }
        } else if (id == R.id.english) {
            if (locale == null || !locale.equals(ENGLISH)) {
                locale = ENGLISH;
                updateLang(locale);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Contest> contests;

        if (requestCode == currentTaskCode && resultCode == FINISH_CODE) {
            currentTaskCode = -1;
            stopService(new Intent(this, Loader.class));
            switch (requestCode) {
                // TODO: implements request's answers
                case COMPETITIONS_CODE:
                    contests = data.getParcelableArrayListExtra(ANSWER);
                    toDisplayMode(contests);
                    break;
                case GYM_CODE:
                    contests = data.getParcelableArrayListExtra(ANSWER);
                    toDisplayMode(contests);
            }


        }
    }

    void toDisplayMode(List<Contest> contests) {
        //TODO turn off progress bar and shows contests
    }

    void toStandbyMode() {
        //TODO turn on progress bar and disable RecyclingView
    }

    void updateLang(String nec_locale) {
        toStandbyMode();

    }
}
