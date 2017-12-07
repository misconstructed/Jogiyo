package com.example.misconstructed.jogiyo;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.misconstructed.jogiyo.Service.LocationService;
import com.example.misconstructed.jogiyo.VO.UserVo;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private GoogleSignInClient googleSignInClient;
    private CallbackManager callbackManager;
    private List<String> permissionNeeds = Arrays.asList("email");
    private Button facebook_login_button;
    private Button login_button;
    private EditText idText;
    private EditText passwordText;
    private String id;
    private String password;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference user_database = firebaseDatabase.getReference("User");
    private DatabaseReference database = firebaseDatabase.getReference();

    private Handler handler;
    private Message message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_login);

        handler = new Handler();


        login_button = (Button)findViewById(R.id.login_button);
    }

    //로그인 onclick
    public void login(View v) {
        idText = (EditText) findViewById(R.id.id);
        passwordText = (EditText) findViewById(R.id.password);
        id = idText.getText().toString();
        password = passwordText.getText().toString();
        checkLogin(id, password);
    }

    //로그인 유효한지 확인
    private void checkLogin(final String id, final String password){
        user_database.child(id).addValueEventListener(new ValueEventListener() {
            boolean check = false;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    UserVo user = dataSnapshot.getValue(UserVo.class);

                    if(user == null){
                                check = true;
                                idText.setText("");
                                passwordText.setText("");
                    }
                    else if(id.equals(user.getId()) && check == false) {
                        if(!password.equals(user.getPassword())){
                            passwordText.setText("");
                            Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                        } else {
                            Intent service_intent = new Intent(getApplicationContext(), LocationService.class);
                            service_intent.putExtra("user", user);
                            Log.e("문제 일어나기 직전 :::", user.toString());
                            startService(service_intent);
                            startApp(user);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    //로그인 진행 앱 첫 화면으로 이동
    private void startApp(UserVo user){
        Intent intent = new Intent(getApplicationContext(), MyMapActivity.class);
        intent.putExtra("user", user);
        Log.e("Login activity::", user.toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //회원가입 화면으로 넘어감
    public void signup(View v){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}




