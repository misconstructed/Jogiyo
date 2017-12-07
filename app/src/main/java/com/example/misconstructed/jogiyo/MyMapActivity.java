package com.example.misconstructed.jogiyo;


import android.content.DialogInterface;
import android.content.Intent;

import android.location.Location;

import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.misconstructed.jogiyo.VO.UserVo;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MyMapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private FloatingActionButton add;
    private TextView label;
    private UserVo user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_my_map);

        Intent intent = getIntent();
        user = (UserVo)intent.getParcelableExtra("user");
        if(user == null)
            Toast.makeText(getApplicationContext(), "NULL", Toast.LENGTH_LONG).show();
        Log.e("My Map User", user.toString());

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

        label=(TextView)findViewById(R.id.title);
        label.setText("My Map");

        add=(FloatingActionButton)findViewById(R.id.add_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
        setView(user);
    }

    public void test(View v){


    }

    //첫 화면이니깐 my map이 보여야함
    private void setView(UserVo user){
        LinearLayout my_map = (LinearLayout)findViewById(R.id.my_map);
        RelativeLayout check_in = (RelativeLayout)findViewById(R.id.check_in);
        RelativeLayout preferences = (RelativeLayout)findViewById(R.id.preferences);
        RelativeLayout check_in_detail = (RelativeLayout) findViewById(R.id.check_in_detail);
        LinearLayout add = (LinearLayout)findViewById(R.id.add);

        my_map.setVisibility(View.VISIBLE);
        check_in.setVisibility(View.GONE);
        preferences.setVisibility(View.GONE);
        check_in_detail.setVisibility(View.GONE);
        add.setVisibility(View.GONE);
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
            intent.putExtra("user", user);
        } else if (id == R.id.checkin_menu) {
            //Toast.makeText(getApplicationContext(), "Check In", Toast.LENGTH_LONG).show();
            intent = new Intent(this, CheckInActivity.class);
            intent.putExtra("user", user);
        } else if (id == R.id.preferences_menu) {
            //Toast.makeText(getApplicationContext(), "Preferences", Toast.LENGTH_LONG).show();
            intent = new Intent(this, PreferencesActivity.class);
            intent.putExtra("user", user);
        } else if (id == R.id.logout_menu) {
            //Toast.makeText(getApplicationContext(), "Log Out", Toast.LENGTH_LONG).show();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MyMapActivity.this);
            alertDialogBuilder.setTitle("프로그램 종료");
            alertDialogBuilder
                    .setMessage("프로그램을 종료할 것입니까?")
                    .setCancelable(false)
                    .setPositiveButton("종료",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // 프로그램을 종료한다
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            })
                    .setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // 다이얼로그를 취소한다
                                    dialog.cancel();
                                }
                            }).create().show();
        }
        if(intent != null)
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
