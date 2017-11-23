package com.example.misconstructed.jogiyo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private GoogleSignInClient googleSignInClient;
    private CallbackManager callbackManager;
    private List<String> permissionNeeds = Arrays.asList("email");
    private Button facebook_login_button;
    private Button login_button;
    private EditText emailText;
    private EditText passwordText;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_login);
        facebookLogin();
        googleLogin();
        findViewById(R.id.google_login_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

            }
        });

        login_button = (Button)findViewById(R.id.login_button);
    }

    public void login(View v){
        emailText = (EditText)findViewById(R.id.email);
        passwordText = (EditText)findViewById(R.id.password);
        email = emailText.getText().toString();
        password = passwordText.getText().toString();
        Log.i("ID", email);
        Log.i("PW", password);
        if(email.equals("test") && password.equals("test")){
            Intent intent = new Intent(this, SidebarActivity.class);
            startActivity(intent);
        }
    }

    private void googleLogin(){
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void facebookLogin(){
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Tag", "로그인이 성공");
            }

            @Override
            public void onCancel() {
                Log.d("Tag", "로그인 하려다 맘");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Tag", "fail : " + error.getLocalizedMessage());
            }
        });
        facebook_login_button = (Button) findViewById(R.id.facebook_login_button);
        facebook_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, permissionNeeds);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //updateUI(account);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void signup(View v){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}




