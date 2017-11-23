package com.example.misconstructed.jogiyo;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    public void signup(View v){

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
            Toast.makeText(getApplicationContext(), name.toString(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "비밀번호 불일치", Toast.LENGTH_LONG).show();
        }
    }
}
