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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private EditText listname;
    private TextView listtime;
    private TextView listplace;
    private Switch alarm_place;
    private EditText memo;
    private Spinner range;
    private Spinner alarm_count;

    private String key;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference alarm_database = firebaseDatabase.getReference("Alarm");
    private DatabaseReference database = firebaseDatabase.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_my_map);

        Intent intent = getIntent();
        user = (UserVo)intent.getParcelableExtra("user");
        key = intent.getStringExtra("key");
        if(user == null)
            Toast.makeText(getApplicationContext(), "NULL", Toast.LENGTH_LONG).show();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ListView listView = (ListView) findViewById(R.id.listView);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        label=(TextView)findViewById(R.id.title);
        label.setText("Check In");


        item = (AlarmVo) intent.getParcelableExtra("AlarmVo");
        listname = (EditText) findViewById(R.id.listnameDetail);
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

        range.setSelection((item.getRange()-100)/100);
        if(!alarm_place.isChecked())
        {
            range.setClickable(false);
            range.setEnabled(false);
        }
        alarm_count.setSelection(item.getAlarm_count()-1);

        alarm_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent edit = new Intent(getApplicationContext(),AddActivity.class);
                    edit.putExtra("user",user);
                    item.setPlace_alarm(alarm_place.isChecked());
                    item.setTime_alarm(!alarm_place.isChecked());
                    edit.putExtra("AlarmVo",item);
                    edit.putExtra("key",key);
                    edit.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(edit);

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
                if (itemId == 0) {
                    Toast.makeText(getApplicationContext(), "삭제", Toast.LENGTH_LONG).show();
                    alarm_database.child(key).removeValue();
                    Intent edit = new Intent(getApplicationContext(),CheckInActivity.class);
                    edit.putExtra("user",user);
                    edit.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(edit);
                }
                //수정 버튼
                if(itemId==1) {
                    update_info();
                    Intent edit = new Intent(getApplicationContext(),CheckInActivity.class);
                    edit.putExtra("user",user);
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


    private void update_info(){



        //이정재!!!! 지도에서 x,y값 받아와서 아래 변수에다가 저장해야함!!! 그러고나서 알람 정보에다가 넣어서 디비 업데이트 할꺼임!


        int x = 0;
        int y = 0;
        listname = (EditText) findViewById(R.id.listnameDetail);
        memo = (EditText)findViewById(R.id.memoDetail);
        range = (Spinner)findViewById(R.id.rangeSpinnerDetail);
        alarm_count= (Spinner)findViewById(R.id.alarmCountSpinnerDetail);
        check = (Switch)findViewById(R.id.checkDetail);

        String alarm_name = listname.getText().toString();
        String memo_text = memo.getText().toString();
        int range_int = Integer.parseInt(range.getSelectedItem().toString().substring(0,3));
        int count_int = Integer.parseInt(alarm_count.getSelectedItem().toString().substring(0,1));
        AlarmVo new_alarm = new AlarmVo(user.getId(), alarm_name, range_int,  count_int, "시간 미설정", memo_text,0, "날짜 미설정", x, y, check.isChecked(), true, false,item.isImportance());
        alarm_database.child(key).setValue(new_alarm);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 100)
            if(data.getStringExtra("result").equals("delete"))
                alarm_database.child(key).removeValue();

;
    }
}
