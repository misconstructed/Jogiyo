package com.example.misconstructed.jogiyo;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.misconstructed.jogiyo.VO.UserVo;

import java.util.ArrayList;

public class PreferencesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton add;
    private TextView label;
    private UserVo user;
    private String name;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preferences);

        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");
        if(user == null)
            Toast.makeText(getApplicationContext(), "NULL", Toast.LENGTH_LONG).show();
        else {
            Toast.makeText(getApplicationContext(), user.getUser_name() + user.getPassword() + user.getId(), Toast.LENGTH_LONG).show();
            name = user.getUser_name();
            id = user.getId();
        }

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
        //setNavHeader(name, id, navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        label=(TextView)findViewById(R.id.title);
        label.setText("Preferences");
        add=(FloatingActionButton)findViewById(R.id.add);
        add.setVisibility(View.INVISIBLE);
        TextView tmp = (TextView)findViewById(R.id.nametext);
        tmp.setText("BMMMMMMMMM");
        setView(user);
    }
    //첫 화면이니깐 preferences가 보여야함
    private void setView(UserVo user){
        ConstraintLayout my_map = (ConstraintLayout)findViewById(R.id.my_map);
        RelativeLayout check_in = (RelativeLayout)findViewById(R.id.check_in);
        RelativeLayout preferences = (RelativeLayout)findViewById(R.id.preferences);

        my_map.setVisibility(View.GONE);
        check_in.setVisibility(View.GONE);
        preferences.setVisibility(View.VISIBLE);
    }

    private void setNavHeader(String name, String id, View navigationView){
        View nav_header = (View)findViewById(R.id.nav_header);
        TextView nav_header_id = (TextView)findViewById(R.id.nav_id);
        if(nav_header == null)
            Toast.makeText(getApplicationContext(), "NULL", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(), name + id, Toast.LENGTH_LONG).show();
        //nav_header_id.

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

    //사이드바 메뉴 선택 시 동작 설정
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent = null;
        if (id == R.id.mymap_menu) {
            intent = new Intent(this, MyMapActivity.class);
            label.setText("My Map");
        } else if (id == R.id.checkin_menu) {
            Toast.makeText(getApplicationContext(), "Check In", Toast.LENGTH_LONG).show();
            intent = new Intent(this, CheckInActivity.class);
            label.setText("Check In");
        } else if (id == R.id.preferences_menu) {
            Toast.makeText(getApplicationContext(), "Preferences", Toast.LENGTH_LONG).show();
            intent = new Intent(this, PreferencesActivity.class);
            label.setText("Preferences");
        } else if (id == R.id.logout_menu) {
            Toast.makeText(getApplicationContext(), "Log Out", Toast.LENGTH_LONG).show();
            intent = new Intent(this, LoginActivity.class);
        }
        intent.putExtra("user", user);
        startActivity(intent);

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
