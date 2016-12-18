package com.ivan_pc.codeforcesreviewer;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    static final String TASK_CODE = "taskCode";
    static final int GYM_CODE = 1;
    static final int COMPETITIONS_CODE = 2;
    static final int NEWS_CODE = 3;
    static final int FINISH_CODE = 100;
    static final String ANSWER = "answer";
    public static final String PENDING_INTENT = "pendingIntent";


    private int currentTaskCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

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
            Log.d(LOG_TAG, "competition started");
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
        } else if (id == R.id.nav_news) {
            currentTaskCode = NEWS_CODE;
            pendingIntent = createPendingResult(NEWS_CODE, new Intent(), 0);
            intent = new Intent(this, Loader.class).putExtra(TASK_CODE, NEWS_CODE)
                    .putExtra(PENDING_INTENT, pendingIntent);
            startService(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Contest> contests;

        //TODO: List<News> news;

        if (requestCode == currentTaskCode && resultCode == FINISH_CODE) {
            currentTaskCode = -1;
            stopService(new Intent(this, Loader.class));
            switch (requestCode) {
                // TODO: implements request's answers
                case COMPETITIONS_CODE:
                    contests = data.getParcelableArrayListExtra(ANSWER);

                    break;
                case GYM_CODE:
                    contests = data.getParcelableArrayListExtra(ANSWER);

                    break;
                case NEWS_CODE:
                    //TODO: news = (ArrayList<News>) data.getSerializableExtra(ANSWER);
            }
        }
    }
}
