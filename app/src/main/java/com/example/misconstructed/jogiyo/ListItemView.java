package com.example.misconstructed.jogiyo;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.misconstructed.jogiyo.VO.AlarmVo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017-11-26.
 */

class ListItemView extends LinearLayout {
    TextView listname;
    TextView listtime;
    TextView listplace;
    TextView checkText;
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
        listtime = (TextView) findViewById(R.id.listtime);
        listplace = (TextView)findViewById(R.id.listplace);
        check = (Switch) findViewById(R.id.check);
        checkText=(TextView)findViewById(R.id.checkText);

    }

    void setName(String name){
        listname.setText(name);
    }

    void setTime(String time){
        listtime.setText(time);
    }

    void setPlace(double x,double y){
        listplace.setText(findAddress(x,y));
    }

    void setPlace(boolean locateCheck,double x,double y)
    {
        if(locateCheck)
            setPlace(x,y);
        else
            listplace.setText("위치 미설정");
    }

    //위도경도로 주소 가져오기
    //되는지는 확인안해봄
    private String findAddress(double X,double Y)
    {
        String answer = new String();
        Geocoder geocoder = new Geocoder(getContext());
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


    void setCheck(final AlarmVo item,final String key){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference alarm_database = firebaseDatabase.getReference("Alarm");

        if(item.isActivate())
            checkText.setText("ON");
        else
            checkText.setText("OFF");
        this.check.setChecked(item.isActivate());

    }
    Switch getCheck(){
        return check;
    }
    TextView getCheckText(){
        return checkText;
    }
}
