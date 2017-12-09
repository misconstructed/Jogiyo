package com.example.misconstructed.jogiyo;


import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.misconstructed.jogiyo.VO.AlarmVo;
import com.example.misconstructed.jogiyo.VO.UserVo;

import java.io.IOException;
import java.util.List;

public class CheckInDetailActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private FloatingActionButton add;
    private TextView label;
    private UserVo user;
    private GridView menu;
    private Switch check;
    private TextView checkText;
    private String alarm_date;

    private boolean firstRangeChange = false;
    private boolean firstCountChange = false;

    private AlarmVo item;
    private TextView listname;
    private TextView listtime;
    private TextView listplace;
    private Switch alarm_place;
    private EditText memo;
    private Spinner range;
    private Spinner alarm_count;

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


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ListView listView = (ListView) findViewById(R.id.listView);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        item = (AlarmVo) intent.getParcelableExtra("AlarmVo");
        listname = (TextView)findViewById(R.id.listnameDetail);
        listtime = (TextView)findViewById(R.id.listtimeDetail);
        listplace = (TextView)findViewById(R.id.listplaceDetail);
        memo = (EditText)findViewById(R.id.memoDetail);
        range = (Spinner)findViewById(R.id.rangeSpinnerDetail);
        alarm_count= (Spinner)findViewById(R.id.alarmCountSpinnerDetail);
        check=(Switch)findViewById(R.id.checkDetail);
        checkText=(TextView)findViewById(R.id.checkTextDetail);
        alarm_place=(Switch)findViewById(R.id.locationSwitchDetail);

        listname.setText(item.getAlarm_name());

        if(item.isActivate()) {
            check.setChecked(true);
            checkText.setText("ON");
        }
        else {
            check.setChecked(false);
            checkText.setText("OFF");
        }

        if(item.isTime_alarm())
            listtime.setText(item.getTime()+" "+item.getDate());
        else
            listtime.setText("시간 미설정");

        if(item.isPlace_alarm()) {
            listplace.setText(findAddress(item.getX(),item.getY()));
            alarm_place.setChecked(true);
        }
        else {
            listplace.setText("위치 미설정");
            alarm_place.setChecked(false);
        }

        memo.setText(item.getMemo());
        memo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                {
                    Intent edit = new Intent(getApplicationContext(),AddActivity.class);
                    edit.putExtra("user",user);
                    edit.putExtra("AlarmVo",item);
                    edit.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(edit);
                }
            }
        });
        memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent edit = new Intent(getApplicationContext(),AddActivity.class);
                    edit.putExtra("user",user);
                    edit.putExtra("AlarmVo",item);
                    edit.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(edit);

            }
        });

        range.setSelection((item.getRange()-100)/100);
        alarm_count.setSelection(item.getAlarm_count()-1);

        alarm_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent edit = new Intent(getApplicationContext(),AddActivity.class);
                    edit.putExtra("user",user);
                    item.setPlace_alarm(alarm_place.isChecked());
                    item.setTime_alarm(!alarm_place.isChecked());
                    edit.putExtra("AlarmVo",item);
                    edit.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(edit);

            }
        });


        range.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(firstRangeChange) {
                    Intent edit = new Intent(getApplicationContext(), AddActivity.class);
                    edit.putExtra("user", user);
                    item.setRange(Integer.parseInt(range.getSelectedItem().toString().substring(0,3)));
                    edit.putExtra("AlarmVo", item);
                    edit.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(edit);
                }
                firstRangeChange = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        alarm_count.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(firstCountChange) {
                    Intent edit = new Intent(getApplicationContext(), AddActivity.class);
                    edit.putExtra("user", user);
                    item.setAlarm_count(Integer.parseInt(alarm_count.getSelectedItem().toString().substring(0,1)));
                    edit.putExtra("AlarmVo", item);
                    edit.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(edit);
                }
                firstCountChange = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        final ChooseItemAdapter adapter = new ChooseItemAdapter();
        adapter.addItem(0);adapter.addItem(1);adapter.addItem(2);
        menu=(GridView)findViewById(R.id.menuDetail);
        menu.setAdapter(adapter);
        menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                int itemId = (int)adapter.getItemId(position);
                //삭제 버튼
                if (itemId == 0)
                    Toast.makeText(getApplicationContext(),"삭제",Toast.LENGTH_LONG).show();
                //수정 버튼
                if(itemId==1) {
                    Intent edit = new Intent(getApplicationContext(),AddActivity.class);
                    edit.putExtra("user",user);
                    edit.putExtra("AlarmVo",item);
                    edit.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(edit);
                    //Toast.makeText(getApplicationContext(), "수정", Toast.LENGTH_LONG).show();
                }
                //공유 버튼
                if(itemId==2)
                    Toast.makeText(getApplicationContext(),"공유",Toast.LENGTH_LONG).show();
            }
        });
        menu.setSelection(R.color.sidebar_id);

        //스위치리스너
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    checkText.setText("ON");
                    //db에 넣는거 하면될듯
                }
                else
                {
                    checkText.setText("OFF");
                    //db
                }
            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        setView(user);
    }


    //위도경도로 주소 가져오기
    //되는지는 확인안해봄
    private String findAddress(double X,double Y)
    {
        String answer = new String();
        Geocoder geocoder = new Geocoder(getApplicationContext());
        List<Address> address;
        try{
            if(geocoder!=null){
                address = geocoder.getFromLocation(X,Y,1);
                if(!address.isEmpty())
                    answer=address.get(0).getAddressLine(0).toString();
                else
                    answer="주소를 가져올 수 없습니다.";
            }
        }
        catch(IOException e)
        {
            answer="주소를 가져올 수 없습니다.";
        }
        return answer;
    }

    public void test(View v){


    }

    //첫 화면이니깐 my map이 보여야함
    private void setView(UserVo user){
        LinearLayout my_map = (LinearLayout)findViewById(R.id.my_map);
        RelativeLayout check_in = (RelativeLayout)findViewById(R.id.check_in);
        RelativeLayout preferences = (RelativeLayout)findViewById(R.id.preferences);
        RelativeLayout check_in_detail = (RelativeLayout)findViewById(R.id.check_in_detail);
        LinearLayout add = (LinearLayout)findViewById(R.id.add);

        my_map.setVisibility(View.GONE);
        check_in.setVisibility(View.GONE);
        preferences.setVisibility(View.GONE);
        check_in_detail.setVisibility(View.VISIBLE);
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
            intent = new Intent(this, CheckInDetailActivity.class);
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
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CheckInDetailActivity.this);
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
