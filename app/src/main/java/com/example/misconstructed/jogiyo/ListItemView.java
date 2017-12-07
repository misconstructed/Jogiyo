package com.example.misconstructed.jogiyo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by Administrator on 2017-11-26.
 */

class ListItemView extends LinearLayout {
    TextView listname;
    TextView listplace;
    TextView listtime;
    TextView checkText;
    ImageButton star;
    boolean starCheck;
    Switch check;

    public ListItemView(Context context){
        super(context);
        init(context);
    }

    public ListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.list_item, this, true);
        listname = (TextView) findViewById(R.id.listname);
        listplace = (TextView) findViewById(R.id.listplace);
        listtime = (TextView) findViewById(R.id.listtime);
        checkText=(TextView)findViewById(R.id.checkText);
        star = (ImageButton) findViewById(R.id.star);
        check = (Switch) findViewById(R.id.check);

        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    checkText.setText("ON");
                }
                else
                {
                    checkText.setText("OFF");
                }
            }
        });

        star.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(starCheck)
                {
                    star.setImageResource(android.R.drawable.btn_star_big_off);
                    starCheck=false;
                }
                else
                {
                    star.setImageResource(android.R.drawable.btn_star_big_on);
                    starCheck=true;
                }
            }
        });
    }

    void setName(String name){  listname.setText(name); }

    void setPlace(){    listplace.setText("미구현입니다.");   }

    void setTime(String time){  listtime.setText(time); }

    void setStar(boolean star){
        if(star) {
            this.star.setImageResource(android.R.drawable.btn_star_big_on);
            starCheck = true;
        }
        else {
            this.star.setImageResource(android.R.drawable.btn_star_big_off);
            starCheck = false;
        }
    }

    void setCheck(boolean check){
        if(check)
            checkText.setText("ON");
        else
            checkText.setText("OFF");
        this.check.setChecked(check);
    }
}
