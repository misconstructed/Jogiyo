package com.example.misconstructed.jogiyo;

import android.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.misconstructed.jogiyo.VO.UserVo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import static android.R.attr.value;

public class SignupActivity extends AppCompatActivity {

    ActionBar actionBar;

    EditText nameText;
    EditText emailText;
    EditText passwordText;
    EditText passwordConfirmText;
    Button signup;
    String name;
    String email;
    String password;
    String passwordConfirm;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference user_database = firebaseDatabase.getReference("User");
    private DatabaseReference database = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        //action bar 환경설정
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Sign Up");
        //actionBar.setElevation(0);


    }

    public void signup_data(View v){

        signup = (Button)findViewById(R.id.signup_button);
        nameText = (EditText)findViewById(R.id.name);
        emailText = (EditText)findViewById(R.id.email);
        passwordText = (EditText)findViewById(R.id.password);
        passwordConfirmText = (EditText)findViewById(R.id.passwordConfirm);

        name = nameText.getText().toString();
        email = emailText.getText().toString();
        password =  passwordText.getText().toString();
        passwordConfirm = passwordConfirmText.getText().toString();

        //빈칸이 있는 경우
        if(name.length() <= 0 || email.length() <= 0 || password.length() <= 0 || passwordConfirm.length() <= 0){
            Toast.makeText(getApplicationContext(), "모두 입력하세요", Toast.LENGTH_LONG).show();
        }
        //이메일 비밀번호 등 조건 걸어야 함
        //비밀번호 일치 확인
         else if(password.equals(passwordConfirm)){
            //이메일 중복도 디비에서 확인
            //Toast.makeText(getApplicationContext(), name.toString(), Toast.LENGTH_LONG).show();
            //Intent intent = new Intent(this, LoginActivity.class);
            //startActivity(intent);
            Log.d("wow", "sign up start");
            checkEmail(name, email, password);

        } else {
            Toast.makeText(getApplicationContext(), "비밀번호 불일치", Toast.LENGTH_LONG).show();
        }
    }

    //email 중복 체크
    private void checkEmail(final String name, final String email, final String password){
        user_database.child(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //try{
                    UserVo user = dataSnapshot.getValue(UserVo.class);
                if(user.equals(null)){
                    Toast.makeText(getApplicationContext(), "NULL", Toast.LENGTH_LONG).show();

                }
                    Toast.makeText(getApplicationContext(), user.getEmail() + user.getPassword()+ user.getUser_name(), Toast.LENGTH_LONG).show();
                    //Log.i("sign up", "중복 이메일");
                    //email이 중복된 경우
                    if(email.equals(user.getEmail())){
                        new android.app.AlertDialog.Builder(SignupActivity.this)
                                .setTitle("중복 이메일")
                                .setMessage("이미 가입한 이메일입니다")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        emailText.setText(null);
                                    }
                                }).show();
                        Toast.makeText(getApplicationContext(), "회원가입 불가능", Toast.LENGTH_LONG).show();
                    } else {
                        //email 중복 아닌 경우
                        //makeUser(name, email, password);
                        Toast.makeText(getApplicationContext(), "회원가입 가능", Toast.LENGTH_LONG).show();

                    }

            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }



    //계정 생성해서 디비에 저장
    private void makeUser(String name, String email, String password)
    {
        Toast.makeText(getApplicationContext(), "회원가입 진행", Toast.LENGTH_LONG).show();
        UserVo user = new UserVo(name, email, password);
        Log.d("sign up", "회원가입 진행");
        database.child("User").child(email).setValue(user);

        //회원가입 완료, 확인 버튼 누르면 로그인 화면으로 전환
        new android.app.AlertDialog.Builder(SignupActivity.this)
                .setTitle("회원가입 완료")
                .setMessage("회원가입을 완료했습니다. 로그인해주세요.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }}
