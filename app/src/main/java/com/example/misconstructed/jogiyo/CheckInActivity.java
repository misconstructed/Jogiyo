package com.example.misconstructed.jogiyo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.misconstructed.jogiyo.VO.AlarmVo;
import com.example.misconstructed.jogiyo.VO.UserVo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CheckInActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton add;
    private TextView label;
    private ImageButton searchButton;
    private EditText searchText;
    private UserVo user;
    private String name;
    private String id;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference alarm_database = firebaseDatabase.getReference("Alarm");
    private DatabaseReference database = firebaseDatabase.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_map);

        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");
        if(user == null)
            Toast.makeText(getApplicationContext(), "NULL", Toast.LENGTH_LONG).show();
        Log.e("Check IN ::", user.toString());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        label.setText("Check In");

        //검색기능
        searchButton=(ImageButton)findViewById(R.id.search);
        searchText=(EditText)findViewById(R.id.editText);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setList(user,searchText.getText().toString());
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchText.getWindowToken(),0);
            }
        });

        setView(user);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setList(user,"");
        searchText=(EditText)findViewById(R.id.editText);
        searchText.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);

    }


    public void findAlarm(View v){
        EditText finding_name_text = (EditText)findViewById(R.id.editText);
        final String finding_name = finding_name_text.getText().toString();
        final ListAdapter adapter = new ListAdapter();
        ListView listView = (ListView) findViewById(R.id.listView);
        final List<AlarmVo> list = new ArrayList<AlarmVo>();

        if(finding_name.length() <= 0)
            Toast.makeText(getApplicationContext(), "내용을 입력하세요", Toast.LENGTH_LONG).show();
        else{
            alarm_database.addValueEventListener(new ValueEventListener() {
                AlarmVo alarm;
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //iterator로 전체 알람 조회
                    for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                        final AlarmVo alarm = userSnapshot.getValue(AlarmVo.class);
                        //회원정보가 맞는 경우
                        if(alarm.getId().equals(user.getId())){
                            if(alarm.getAlarm_name().contains(finding_name))
                                adapter.addItem(alarm,userSnapshot.getKey());
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            listView.setAdapter(adapter);
        }
    }

    private void setList(final UserVo user,final String search){
        final ListAdapter adapter = new ListAdapter();
        ListView listView = (ListView) findViewById(R.id.listView);
        final List<String> list = new ArrayList<String>();

        adapter.clearItem();
        list.clear();

        alarm_database.addValueEventListener(new ValueEventListener() {
            AlarmVo alarm;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //iterator로 전체 알람 조회
                adapter.clearItem();
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    final AlarmVo alarm = userSnapshot.getValue(AlarmVo.class);
                    //회원정보가 맞는 경우
                    if(alarm.getId().equals(user.getId())){
                        if(alarm.getAlarm_name().contains(search)) {
                            adapter.addItem(alarm,userSnapshot.getKey());
                            Log.e("datasnapshot :::::::", userSnapshot.getKey());
                            list.add(userSnapshot.getKey());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                AlarmVo item = (AlarmVo)adapterView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(),CheckInDetailActivity.class);
                intent.putExtra("AlarmVo",item);
                intent.putExtra("user", user);
                intent.putExtra("key", list.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });


    }

    //첫 화면이니깐 my map이 보여야함
    private void setView(UserVo user){
        LinearLayout my_map = (LinearLayout)findViewById(R.id.my_map);
        RelativeLayout check_in = (RelativeLayout)findViewById(R.id.check_in);
        RelativeLayout preferences = (RelativeLayout)findViewById(R.id.preferences);
        RelativeLayout check_in_detail = (RelativeLayout)findViewById(R.id.check_in_detail);
        LinearLayout add = (LinearLayout)findViewById(R.id.add);

        my_map.setVisibility(View.GONE);
        check_in.setVisibility(View.VISIBLE);
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
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CheckInActivity.this);
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


}
