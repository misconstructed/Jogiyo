package com.example.misconstructed.jogiyo;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.misconstructed.jogiyo.Receiver.AlarmReceiver;
import com.example.misconstructed.jogiyo.Service.LocationService;
import com.example.misconstructed.jogiyo.VO.AlarmVo;
import com.example.misconstructed.jogiyo.VO.UserVo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private EditText alarm_name_text;
    private TextView time_text;
    private Spinner range_spinner;
    private Spinner count_spinner;
    private String alarm_name;
    private EditText memo_text;
    private String memo;
    private String range;
    private String count;
    private String time;
    private String alarm_date;
    private boolean place_alarm;
    private boolean time_alarm;

    private int tmp = 0;

    UserVo user;

    AlarmManager alarmManager;
    Calendar calendar;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference user_database = firebaseDatabase.getReference("Alarm");
    private DatabaseReference database = firebaseDatabase.getReference();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");
        if(user == null)
            Toast.makeText(getApplicationContext(), "NULL", Toast.LENGTH_LONG).show();


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

        TextView label=(TextView)findViewById(R.id.title);
        label.setText("My Map");


        Button date_button = (Button)findViewById(R.id.date_confirm);
        Button time_button = (Button)findViewById(R.id.time_confirm);
        date_button.setClickable(false);
        time_button.setClickable(false);

        //getData();
        setView(user);
        setSwitch();
    }

    //OnClick 시간 설정
    public void show_time_picker(View v){
        TimePickerDialog dialog = new TimePickerDialog(this, time_listener, 00, 00, true);
        dialog.show();
    }

    //TimePicker 다이얼로그 만들기
    private TimePickerDialog.OnTimeSetListener time_listener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Toast.makeText(getApplicationContext(), hourOfDay + "시 " + minute + "분", Toast.LENGTH_SHORT).show();
            TextView time = (TextView)findViewById(R.id.time_text);
            time.setText(hourOfDay+":"+minute);
        }
    };

    //switch로 위치기반 활성화 OR 시간기반 활성화 설정
    private void setSwitch(){
        final Switch locationSwitch = (Switch)findViewById(R.id.location_switch);
        locationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Button date_button = (Button)findViewById(R.id.date_confirm);
                Button time_button = (Button)findViewById(R.id.time_confirm);
                TextView date_text = (TextView)findViewById(R.id.date_text);
                TextView time_text = (TextView)findViewById(R.id.time_text);
                Spinner range_spinner = (Spinner)findViewById(R.id.range_spinner);
                if(isChecked){
                    date_text.setText("날짜 미설정");
                    time_text.setText("시간 미설정");
                    time_button.setClickable(false);
                    date_button.setClickable(false);
                    range_spinner.setClickable(true);
                    range_spinner.setEnabled(true);
                } else {
                    date_text.setText("01월 01일");
                    time_text.setText("00:00");
                    date_button.setClickable(true);
                    time_button.setClickable(true);
                    range_spinner.setClickable(false);
                    range_spinner.setEnabled(false);
                }
            }
        });
    }

    //화면에 보이는 화면 설정
    private void setView(UserVo user){
        LinearLayout my_map = (LinearLayout)findViewById(R.id.my_map);
        RelativeLayout check_in = (RelativeLayout)findViewById(R.id.check_in);
        RelativeLayout preferences = (RelativeLayout)findViewById(R.id.preferences);
        RelativeLayout check_in_detail = (RelativeLayout)findViewById(R.id.check_in_detail);
        LinearLayout add = (LinearLayout)findViewById(R.id.add);

        my_map.setVisibility(View.GONE);
        check_in.setVisibility(View.GONE);
        preferences.setVisibility(View.GONE);
        check_in_detail.setVisibility(View.GONE);
        add.setVisibility(View.VISIBLE);
    }

    //화면의 데이터 받아와서 알람 객체 생성
    private AlarmVo getData(){
        EditText alarm_name_text = (EditText)findViewById(R.id.alarm_name);
        time_text = (TextView)findViewById(R.id.time_text);
        range_spinner = (Spinner)findViewById(R.id.range_spinner);
        count_spinner = (Spinner)findViewById(R.id.alarm_count_spinner);
        memo_text = (EditText)findViewById(R.id.memo_text);
        AlarmVo alarm;

        Switch locationSwitch = (Switch)findViewById(R.id.location_switch);
        if(locationSwitch.isChecked() == true) {
            place_alarm = true;
            time_alarm = false;
        } else {
            place_alarm = false;
            time_alarm = true;
        }

        memo = memo_text.getText().toString();
        alarm_name = alarm_name_text.getText().toString();
        time = time_text.getText().toString();
        range = range_spinner.getSelectedItem().toString().substring(0,3);
        count = count_spinner.getSelectedItem().toString().substring(0,1);

        Log.e("Add activity before::", user.toString());
        Log.e("Add activity user::", user.getId());
        if(place_alarm == false)
            alarm = new AlarmVo(user.getId(), alarm_name, Integer.parseInt(range), Integer.parseInt(count), time, memo, 0, alarm_date, 0, 0, true, place_alarm, time_alarm);
        else
            alarm = new AlarmVo(user.getId(), alarm_name, Integer.parseInt(range), Integer.parseInt(count), "시간 미설정", memo, 0, "날짜 미설정", 0, 0, true, place_alarm, time_alarm);
        return alarm;
    }

    //알람 디비에 등록
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void confirm_alarm(View v){
        AlarmVo alarm = getData();
        database.child("Alarm").push().setValue(alarm);
        Toast.makeText(getApplicationContext(), "등록 완료", Toast.LENGTH_LONG).show();

        setAlarm(alarm);
        Intent intent = new Intent(AddActivity.this, MyMapActivity.class);
        intent.putExtra("user", user);
        //Log.e("Add activity CA::", user.toString());
        startActivity(intent);
    }

    //날짜 선택 다이얼로그 보여주기
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void show_date_picker(View v){
        GregorianCalendar calendar = new GregorianCalendar();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day= calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        new DatePickerDialog(AddActivity.this, dateSetListener, year, month, day).show();
    }

    //날짜 선택
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String msg = String.format("%d/%d/%d", year,monthOfYear+1, dayOfMonth);
            alarm_date = msg;
            TextView date_text = (TextView)findViewById(R.id.date_text);
            date_text.setText(monthOfYear+1+"월 " + dayOfMonth+"일");
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }
    };

    //알람 디비에 등록
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setAlarm(AlarmVo alarm){
        String time_string;
        String hour_string;
        String min_string;
        String date_string;
        int hour;
        int min;
        int year;
        int month;
        int day;
        int charAt, charAt2;
        //시간 기반 알람인 경우
        if(alarm.isTime_alarm() == true) {
            time_string = alarm.getTime();
            charAt = time_string.indexOf(":");
            hour_string = time_string.substring(0, charAt);
            min_string = time_string.substring(charAt + 1, time_string.length());
            hour = Integer.parseInt(hour_string);
            min = Integer.parseInt(min_string);

            date_string = alarm.getDate();
            charAt = date_string.indexOf("/");
            year = Integer.parseInt(date_string.substring(0, charAt));
            charAt2 = date_string.indexOf("/", 5);
            //Toast.makeText(getApplicationContext(), charAt +"  " + charAt2, Toast.LENGTH_LONG).show();
            Log.d("IMPOR", "charAt : " + charAt + " CharAt2 : " + charAt2);
            month = Integer.parseInt(date_string.substring(charAt + 1, charAt2));
            day = Integer.parseInt(date_string.substring(charAt2 + 1, date_string.length()));

            Log.d("IMPORTANT", year + " / " + month + " / " + day);
            Log.d("TIME", hour + " : " + min);

            //전송할 날짜 지정
            calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month - 1);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, min);
            calendar.set(Calendar.SECOND, 1);
            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
            intent.putExtra("alarm", alarm);

            //두번째 인자 => 고유 코드
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            //Log.e("TIME", String.valueOf(calendar.getTimeInMillis()));

            if (alarm.getAlarm_count() == 1)
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 0, pendingIntent);
            else
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
        } else {
            Intent service_intent = new Intent(AddActivity.this, LocationService.class);
            service_intent.putExtra("user", user);
            Log.e("문제 일어나기 전::", user.toString());
            startService(service_intent);
        }
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
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddActivity.this);
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

        startActivity(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }









    private class ListItemAdapter extends BaseAdapter {

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
