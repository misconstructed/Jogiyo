package com.example.misconstructed.jogiyo;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.regex.Pattern;

public class NavHeaderActivity extends AppCompatActivity {

    TextView name;
    TextView id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header);

        id = (TextView)findViewById(R.id.id);
        id.setText("AAAAAAAAAAA");


    }

}
