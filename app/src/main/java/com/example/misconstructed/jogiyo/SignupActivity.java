package com.example.misconstructed.jogiyo;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.misconstructed.jogiyo.VO.UserVo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private ActionBar actionBar;

    private EditText nameText;
    private EditText idText;
    private EditText passwordText;
    private EditText passwordConfirmText;
    private Button signup;
    private String name;
    private String id;
    private String password;
    private String passwordConfirm;
    private TextView alert_id;
    private TextView alert_password;
    private TextView alert_name;
    private TextView alert_password_confirm;

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
        idText = (EditText)findViewById(R.id.id);
        passwordText = (EditText)findViewById(R.id.password);
        passwordConfirmText = (EditText)findViewById(R.id.passwordConfirm);

        name = nameText.getText().toString();
        id = idText.getText().toString();
        password =  passwordText.getText().toString();
        passwordConfirm = passwordConfirmText.getText().toString();

        validateData(name, id, password, passwordConfirm);
    }

    //email 중복 체크
    private void checkEmail(final String name, final String id, final String password){
        user_database.child(id).addValueEventListener(new ValueEventListener() {
            boolean check = false;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    UserVo user = dataSnapshot.getValue(UserVo.class);

                    //email이 중복된 경우
                    if(user == null){
                        check = true;
                        //email 중복 아닌 경우
                        makeUser(name, id, password);
                        Toast.makeText(getApplicationContext(), "회원가입 가능", Toast.LENGTH_LONG).show();
                    }
                    else if(id.equals(user.getId()) && check == false) {
                        alert_id.setText("이미 존재하는 아이디 입니다.");
                        alert_id.setTextColor(Color.RED);
                        alert_password.setText("");
                        alert_password_confirm.setText("");
                        alert_name.setText("");
                        Toast.makeText(getApplicationContext(), "회원가입 불가능", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "오류", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    //계정 생성해서 디비에 저장
    private void makeUser(String name, String id, String password)
    {
        Toast.makeText(getApplicationContext(), "회원가입 진행", Toast.LENGTH_LONG).show();
        UserVo user = new UserVo(name, id, password);
        //Log.d("sign up", "회원가입 진행");
        database.child("User").child(id).setValue(user);

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
    }

    private void validateData(String name, String id, String password, String passwordConfirm){

        alert_id = (TextView)findViewById(R.id.alert_id);
        alert_password = (TextView)findViewById(R.id.alert_password);
        alert_name = (TextView)findViewById(R.id.alert_name);
        alert_password_confirm = (TextView)findViewById(R.id.alert_password_confirm);


        //빈칸이 있는 경우
        if(name.length() <= 0 || id.length() <= 0 || password.length() <= 0 || passwordConfirm.length() <= 0){
            if(name.length() <= 0 ){
                alert_name.setText("이름을 입력하세요.");
                alert_name.setTextColor(Color.RED);
            }
            if(id.length() <= 0 ){
                alert_id.setText("아이디 입력하세요.");
                alert_id.setTextColor(Color.RED);
            }
            if(password.length() <= 0 ){
                alert_password.setText("비밀번호를 입력하세요.");
                alert_password.setTextColor(Color.RED);
            }
            if(passwordConfirm.length() <= 0 ){
                alert_password_confirm.setText("비밀번호를 재입력하세요.");
                alert_password_confirm.setTextColor(Color.RED);
            }
        }

        //비밀번호 일치하지 않는 경우
        else if(!password.equals(passwordConfirm)){
            alert_id.setText("");
            alert_name.setText("");
            alert_password_confirm.setText("");
            alert_password.setText("비밀번호가 일치하지 않습니다");
            alert_password.setTextColor(Color.RED);
        }

        //이메일 형식 유효성
        else if(!Pattern.matches("^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$", id))
        {
            //Toast.makeText(SignupActivity.this,"이메일 형식이 아닙니다",Toast.LENGTH_SHORT).show();
            alert_id.setText("아이디 형식이 아닙니다.");
            alert_id.setTextColor(Color.RED);
            alert_name.setText("");
            alert_password.setText("");
            alert_password_confirm.setText("");
        }

        //비밀번호 유효성
//        else if(!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$", password))
//        {
//            Toast.makeText(SignupActivity.this,"비밀번호 형식을 지켜주세요.",Toast.LENGTH_SHORT).show();
//            alert_password.setText("비밀번호 형식이 아닙니다.");
//            alert_password.setTextColor(Color.RED);
//            alert_name.setText("");
//            alert_email.setText("");
//            alert_password_confirm.setText("");
//        }
        else {
            checkEmail(name, id, password);
        }
    }
}
