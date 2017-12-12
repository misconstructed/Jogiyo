package com.example.misconstructed.jogiyo;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.misconstructed.jogiyo.VO.AlarmVo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;



/**
 * Created by misconstructed on 2017. 12. 8..
 */
class ListAdapter extends BaseAdapter {
    private ArrayList<AlarmVo> items = new ArrayList();
    private List<String> list = new ArrayList<String>();

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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference alarm_database = firebaseDatabase.getReference("Alarm");

        ListItemView view = (ListItemView) convertView;
        if (convertView == null)
            view = new ListItemView(getApplicationContext());
        final AlarmVo item = items.get(position);
        final String key = list.get(position);
        view.setName(item.getAlarm_name());
        view.setCheck(item,key);
        view.setTime(item.getTime());
        view.setPlace(item.isPlace_alarm(),item.getX(),item.getY());
        final Switch check=view.getCheck();
        final TextView checkText = view.getCheckText();

        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    checkText.setText("ON");
                    item.setActivate(true);
                    alarm_database.child(key).setValue(item);
                    items.remove(position);
                    items.add(position,item);
                    notifyDataSetChanged();
                }
                else
                {
                    checkText.setText("OFF");
                    item.setActivate(false);
                    alarm_database.child(key).setValue(item);
                    items.remove(position);
                    items.add(position,item);
                    notifyDataSetChanged();

                }
            }
        });;
        return view;
    }
    void addItem(AlarmVo item,String key) {
        items.add(item);
        list.add(key);
    }
    void clearItem(){
        items.clear();
        list.clear();
    }
}