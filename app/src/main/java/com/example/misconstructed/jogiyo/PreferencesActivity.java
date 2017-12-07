package com.example.misconstructed.jogiyo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.misconstructed.jogiyo.VO.UserVo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PreferencesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView label;
    private UserVo user;
    private EditText nametext;
    private EditText idtext;
    private EditText newpasswordtext;
    private EditText newpasswordconfirmtext;
    private EditText passwordtext;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference user_database = firebaseDatabase.getReference("User");
    private DatabaseReference database = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preferences);

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
        label.setText("Preferences");

        setView(user);
        preferencesControl(user);
    }

    private void preferencesControl(UserVo user){
        nametext = (EditText)findViewById(R.id.name);
        idtext= (EditText)findViewById(R.id.id);
        nametext.setText(user.getName());
        idtext.setText(user.getId());
    }

    public void saveChanges(View v){
        TextView alert_name = (TextView)findViewById(R.id.alert_name);
        TextView alert_newpassword = (TextView)findViewById(R.id.alert_newpassword);
        TextView alert_newpasswordconfirm = (TextView)findViewById(R.id.alert_newpasswordconfirm);
        TextView alert_password = (TextView)findViewById(R.id.alert_password);

        nametext = (EditText)findViewById(R.id.name);
        newpasswordtext= (EditText)findViewById(R.id.newPassword);
        newpasswordconfirmtext= (EditText)findViewById(R.id.newPasswordConfirm);
        passwordtext= (EditText)findViewById(R.id.password);

        String name = nametext.getText().toString();
        String password = passwordtext.getText().toString();
        String newpassword = newpasswordtext.getText().toString();
        String newpasswordconfirm = newpasswordconfirmtext.getText().toString();

        //정상적으로 수정 진행
        if(password.equals(user.getPassword())){
            //이름 입력 안한 경우
            if(name.length() <= 0){
                alert_name.setText("이름을 입력하세요.");
                alert_newpassword.setText("");
                alert_newpasswordconfirm.setText("");
                alert_password.setText("");
                alert_name.setTextColor(Color.RED);
            } else {
                //새 비밀번호 입력X
                if (newpassword.length() <= 0 && newpasswordconfirm.length() <= 0) {
                    user.setName(name);
                    database.child("User").child(user.getId()).setValue(user);
                    Intent intent = new Intent(this, PreferencesActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);

                    Toast.makeText(getApplicationContext(), "회원정보를 수정했습니다", Toast.LENGTH_LONG).show();
                } else {
                    //비밀번호까지 바꾼 경우
                    //새 비밀번호 일치
                    if (newpassword.equals(newpasswordconfirm)) {
                        user.setName(name);
                        user.setPassword(newpassword);
                        database.child("User").child(user.getId()).setValue(user);
                        Intent intent = new Intent(this, PreferencesActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        alert_name.setText("");
                        alert_newpassword.setText("");
                        alert_newpasswordconfirm.setText("");
                        alert_password.setText("");
                        passwordtext.setText("");
                        newpasswordtext.setText("");
                        newpasswordconfirmtext.setText("");
                        Toast.makeText(getApplicationContext(), "회원정보를 수정했습니다", Toast.LENGTH_LONG).show();
                    } else {
                        //새 비밀번호 불일치
                        alert_name.setText("");
                        alert_newpassword.setText("새 비밀번호가 일치하지 않습니다");
                        alert_newpasswordconfirm.setText("");
                        alert_password.setText("");
                        alert_newpassword.setTextColor(Color.RED);
                    }
                }
            }
        } else {
            //기존 비밀번호 불일치 - 진행X
            alert_name.setText("");
            alert_newpassword.setText("");
            alert_newpasswordconfirm.setText("");
            alert_password.setText("비밀번호가 일치하지 않습니다");
            alert_password.setTextColor(Color.RED);
        }
    }

    //첫 화면이니깐 preferences가 보여야함
    private void setView(UserVo user){
        LinearLayout my_map = (LinearLayout)findViewById(R.id.my_map);
        RelativeLayout check_in = (RelativeLayout)findViewById(R.id.check_in);
        RelativeLayout preferences = (RelativeLayout)findViewById(R.id.preferences);
        RelativeLayout check_in_detail = (RelativeLayout)findViewById(R.id.check_in_detail);
        LinearLayout add = (LinearLayout)findViewById(R.id.add);

        my_map.setVisibility(View.GONE);
        check_in.setVisibility(View.GONE);
        preferences.setVisibility(View.VISIBLE);
        check_in_detail.setVisibility(View.GONE);
        add.setVisibility(View.GONE);
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
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PreferencesActivity.this);
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