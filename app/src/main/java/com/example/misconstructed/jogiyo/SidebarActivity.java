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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SidebarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ConstraintLayout mymap;
    private RelativeLayout checkin;
    private RelativeLayout preferences;
    private TextView label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidebar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ListView listView = (ListView) findViewById(R.id.listView);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mymap = (ConstraintLayout)findViewById(R.id.mymap);
        checkin = (RelativeLayout)findViewById(R.id.checkin);

        final ListItemAdapter adapter = new ListItemAdapter();
        adapter.addItem(new ListItem("해파리 낚시","12시 00분",true,false));
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ListItem item = (ListItem) adapter.getItem(position);
                Toast.makeText(getApplicationContext(), "선택 : " + item.getName(),
                        Toast.LENGTH_LONG).show();
            }
        });

        preferences = (RelativeLayout) findViewById(R.id.preferences);
        mymap.setVisibility(View.VISIBLE);
        checkin.setVisibility(View.INVISIBLE);
        preferences.setVisibility(View.INVISIBLE);
        label=(TextView)findViewById(R.id.title);
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
        checkin = (RelativeLayout)findViewById(R.id.checkin);
        preferences = (RelativeLayout) findViewById(R.id.preferences);

        Intent intent = null;
        if (id == R.id.mymap_menu) {
            // Handle the camera action
            Toast.makeText(getApplicationContext(), "My Map", Toast.LENGTH_LONG).show();
            mymap.setVisibility(View.VISIBLE);
            checkin.setVisibility(View.INVISIBLE);
            preferences.setVisibility(View.INVISIBLE);
            label.setText("My Map");
        } else if (id == R.id.checkin_menu) {
            Toast.makeText(getApplicationContext(), "Check In", Toast.LENGTH_LONG).show();
            mymap.setVisibility(View.INVISIBLE);
            checkin.setVisibility(View.VISIBLE);
            preferences.setVisibility(View.INVISIBLE);
            label.setText("Check In");
        } else if (id == R.id.preferences_menu) {
            Toast.makeText(getApplicationContext(), "Preferences", Toast.LENGTH_LONG).show();
            mymap.setVisibility(View.INVISIBLE);
            checkin.setVisibility(View.INVISIBLE);
            preferences.setVisibility(View.VISIBLE);
            label.setText("Preferences");
        } else if (id == R.id.logout_menu) {
            Toast.makeText(getApplicationContext(), "Log Out", Toast.LENGTH_LONG).show();
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private class ListItemAdapter extends BaseAdapter{

        private ArrayList<ListItem> items = new ArrayList();

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ListItemView listView = (ListItemView) convertView;
            if (convertView == null)
                listView = new ListItemView(getApplicationContext());

            ListItem item = items.get(position);

            listView.setName(item.getName());
            listView.setPlace();
            listView.setTime(item.getTime());
            listView.setStar(item.isStar());
            listView.setCheck(item.isCheck());

            return listView;
        }

        void addItem(ListItem item) { items.add(item); }
    }
}
