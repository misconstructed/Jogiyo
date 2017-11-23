package com.example.misconstructed.jogiyo;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SidebarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ConstraintLayout mymap;
    private ConstraintLayout checkin;
    private RelativeLayout preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidebar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("My Map");
        //toolbar.setElevation();
        //toolbar.set

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mymap = (ConstraintLayout)findViewById(R.id.mymap);
        checkin = (ConstraintLayout)findViewById(R.id.checkin);
        preferences = (RelativeLayout) findViewById(R.id.preferences);
        mymap.setVisibility(View.VISIBLE);
        checkin.setVisibility(View.INVISIBLE);
        preferences.setVisibility(View.INVISIBLE);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        mymap = (ConstraintLayout)findViewById(R.id.mymap);
        checkin = (ConstraintLayout)findViewById(R.id.checkin);
        preferences = (RelativeLayout) findViewById(R.id.preferences);

        Intent intent = null;
        if (id == R.id.mymap_menu) {
            // Handle the camera action
            Toast.makeText(getApplicationContext(), "My Map", Toast.LENGTH_LONG).show();
            mymap.setVisibility(View.VISIBLE);
            checkin.setVisibility(View.INVISIBLE);
            preferences.setVisibility(View.INVISIBLE);
        } else if (id == R.id.checkin_menu) {
            Toast.makeText(getApplicationContext(), "Check In", Toast.LENGTH_LONG).show();
            mymap.setVisibility(View.INVISIBLE);
            checkin.setVisibility(View.VISIBLE);
            preferences.setVisibility(View.INVISIBLE);
        } else if (id == R.id.preferences_menu) {
            Toast.makeText(getApplicationContext(), "Preferences", Toast.LENGTH_LONG).show();
            mymap.setVisibility(View.INVISIBLE);
            checkin.setVisibility(View.INVISIBLE);
            preferences.setVisibility(View.VISIBLE);
        } else if (id == R.id.logout_menu) {
            Toast.makeText(getApplicationContext(), "Log Out", Toast.LENGTH_LONG).show();
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
