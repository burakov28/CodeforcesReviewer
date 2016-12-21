package com.ivan_pc.codeforcesreviewer;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    static final String TASK_CODE = "taskCode";
    static final int GYM_CODE = 1;
    static final int COMPETITIONS_CODE = 2;
    static final int FINISH_CODE = 100;
    static final int ERROR_CODE = -100;
    static final String ANSWER = "answer";
    public static final String PENDING_INTENT = "pendingIntent";
    public static final String RUSSIAN = "russian";
    public static final String ENGLISH = "english";
    private static final String LOCALE_KEY = "locale_key";
    private static final String LAST_CODE = "last_code";
    private static final String RUSSIAN_LANGUAGE = "русский";
    public static final String LANGUAGE = "lang";
    public static final String INTEREST_CONTEST = "interest_contest";

    public String chosen_locale;
    private int currentTaskCode;
    private int last_code;
    private ContestItemListener.ContestTouchListener listener;

    private TextView startTV;
    private TextView errorTV;
    private Button errorButton;
    private ProgressBar progressBar;
    private BroadcastReceiver onLanguageChangeReceiver;

    private RecyclerView contestsView;
    private ContestList adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        last_code = 0;
        if (savedInstanceState != null) {
            if (chosen_locale == null) chosen_locale = savedInstanceState.getString(LOCALE_KEY);
            last_code = savedInstanceState.getInt(LAST_CODE);
        }

        if (onLanguageChangeReceiver == null) {
            onLanguageChangeReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    updateLocale();
                }
            };
            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_LOCALE_CHANGED);
            registerReceiver(onLanguageChangeReceiver, intentFilter);
        }

        IntentFilter filter = new IntentFilter(Intent.ACTION_LOCALE_CHANGED);
        registerReceiver(onLanguageChangeReceiver, filter);


        contestsView = (RecyclerView) findViewById(R.id.contests_view);
        contestsView.setAdapter(new ContestList(this, new ArrayList<Contest>(), "en"));
        contestsView.setLayoutManager(new LinearLayoutManager(this));
        contestsView.addItemDecoration(new ContestItemDecorator(ContextCompat.getDrawable(getApplicationContext(), R.drawable.contest_list_devider)));
        listener = new ContestItemListener.ContestTouchListener() {
            private List<Contest> contests;
            private String locale;
            public void onClick(View v, int pos) {
                System.out.println("Pressed on " + pos);
                Intent intent = new Intent(MainActivity.this, ContestActivity.class);
                intent.putExtra(INTEREST_CONTEST, contests.get(pos));
                intent.putExtra("locale", locale);
                startActivity(intent);
            }
            public void getContest(List<Contest> contests, String locale) {
                this.contests = contests;
                this.locale = locale;
            }
        };
        contestsView.addOnItemTouchListener(new ContestItemListener(getApplicationContext(), contestsView, listener));


        startTV = (TextView) findViewById(R.id.startTextView);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        errorButton = (Button) findViewById(R.id.errorButton);
        errorTV = (TextView) findViewById(R.id.errorTextView);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        if (last_code == 0) {
            toStartMode();
        }
        else {
            if (last_code == COMPETITIONS_CODE) {
                executeNavigationItemSelected(R.id.nav_competitions);
            }
            else {
                executeNavigationItemSelected(R.id.nav_gym);
            }
        }

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(onLanguageChangeReceiver);
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(LOCALE_KEY, chosen_locale);
        outState.putInt(LAST_CODE, last_code);
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
        executeNavigationItemSelected(item.getItemId());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void executeNavigationItemSelected(int id) {
        PendingIntent pendingIntent;
        Intent intent;

        if (currentTaskCode != -1) {
            stopService(new Intent(this, Loader.class));
        }

        if (chosen_locale == null) {
            if (RUSSIAN_LANGUAGE.equalsIgnoreCase(Locale.getDefault().getDisplayLanguage())) {
                chosen_locale = RUSSIAN;
            }
            else {
                chosen_locale = ENGLISH;
            }
        }

        if (id == R.id.nav_competitions) {
            toStandbyMode();
            currentTaskCode = COMPETITIONS_CODE;
            pendingIntent = createPendingResult(COMPETITIONS_CODE, new Intent(), 0);
            intent = new Intent(this, Loader.class).putExtra(TASK_CODE, COMPETITIONS_CODE)
                    .putExtra(PENDING_INTENT, pendingIntent)
                    .putExtra(LANGUAGE, chosen_locale);

            startService(intent);
        } else if (id == R.id.nav_gym) {
            toStandbyMode();
            currentTaskCode = GYM_CODE;
            pendingIntent = createPendingResult(GYM_CODE, new Intent(), 0);
            intent = new Intent(this, Loader.class).putExtra(TASK_CODE, GYM_CODE)
                    .putExtra(PENDING_INTENT, pendingIntent)
                    .putExtra(LANGUAGE, chosen_locale);

            startService(intent);
        } else if (id == R.id.russian) {
            if (!chosen_locale.equals(RUSSIAN)) {
                chosen_locale = RUSSIAN;
                updateLang();
            }
        } else if (id == R.id.english) {
            if (!chosen_locale.equals(ENGLISH)) {
                chosen_locale = ENGLISH;
                updateLang();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Contest> contests;

        if (requestCode == currentTaskCode && resultCode == FINISH_CODE) {
            currentTaskCode = -1;
            stopService(new Intent(this, Loader.class));
            switch (requestCode) {
                case COMPETITIONS_CODE:
                    last_code = COMPETITIONS_CODE;
                    contests = data.getParcelableArrayListExtra(ANSWER);
                    toDisplayMode(contests);
                    break;
                case GYM_CODE:
                    last_code = GYM_CODE;
                    contests = data.getParcelableArrayListExtra(ANSWER);
                    toDisplayMode(contests);
            }
        }

        if (resultCode == ERROR_CODE) {
            if (currentTaskCode != -1) {
                currentTaskCode = -1;
                stopService(new Intent(this, Loader.class));
            }

            chosen_locale = data.getStringExtra(RunnableTask.LANGUAGE_KEY);
            last_code= data.getIntExtra(RunnableTask.IS_GYM_KEY, 0);

            toErrorMode(data.getStringExtra(RunnableTask.ERROR_MESSAGE_KEY));
        }
    }



    void updateLang() {
        if (last_code == 0) {
            return;
        }

        if (last_code == COMPETITIONS_CODE) {
            executeNavigationItemSelected(R.id.nav_competitions);
        }
        else {
            executeNavigationItemSelected(R.id.nav_gym);
        }
    }

    void updateLocale() {
        if (currentTaskCode != -1) {
            currentTaskCode = -1;
            stopService(new Intent(this, Loader.class));
        }
        String new_locale;
        if (RUSSIAN_LANGUAGE.equalsIgnoreCase(Locale.getDefault().getDisplayLanguage())) {
            new_locale = RUSSIAN;
        }
        else {
            new_locale = ENGLISH;
        }

        System.out.println("Hay!");
        if (chosen_locale == null || !new_locale.equals(chosen_locale)) {
            chosen_locale = new_locale;
            System.out.println("Here!");
            updateLang();
        }
    }

    void toDisplayMode(final List<Contest> contests) {
        progressBar.setVisibility(View.INVISIBLE);
        startTV.setVisibility(View.INVISIBLE);
        errorTV.setVisibility(View.INVISIBLE);
        errorButton.setVisibility(View.INVISIBLE);
        //TODO turn off progress bar and shows contests

        listener.getContest(contests, chosen_locale);
        String tmp = (chosen_locale.equals(RUSSIAN)) ? "ru" : "en";
        adapter = new ContestList(this, contests, tmp);
        contestsView.setAdapter(adapter);
        contestsView.setVisibility(RecyclerView.VISIBLE);
    }

    void toStandbyMode() {
        last_code = 0;
        startTV.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        errorTV.setVisibility(View.INVISIBLE);
        errorButton.setVisibility(View.INVISIBLE);
        contestsView.setVisibility(View.INVISIBLE);
    }

    void toStartMode() {
        startTV.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        errorTV.setVisibility(View.INVISIBLE);
        errorButton.setVisibility(View.INVISIBLE);
        contestsView.setVisibility(View.INVISIBLE);
    }

    void toErrorMode(String errorMessage) {
        startTV.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        errorButton.setVisibility(View.VISIBLE);
        contestsView.setVisibility(View.INVISIBLE);
        String message;
        if (errorMessage.equals(RunnableTask.CONNECTION_LOST)) {
            message = getString(R.string.connection_lost);
        }
        else {
            message = getString(R.string.wrong_data_downloaded);
        }
        errorTV.setText(message);
        errorTV.setVisibility(View.VISIBLE);
    }

    public void tryAgainPressed(View view) {
        if (last_code == COMPETITIONS_CODE) {
            executeNavigationItemSelected(R.id.nav_competitions);
        }
        else {
            executeNavigationItemSelected(R.id.nav_gym);
        }
    }
}
