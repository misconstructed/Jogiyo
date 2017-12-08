package com.example.misconstructed.jogiyo;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.misconstructed.jogiyo.VO.AlarmVo;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by misconstructed on 2017. 12. 8..
 */
class ListAdapter extends BaseAdapter {
    private ArrayList<AlarmVo> items = new ArrayList();

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
        ListItemView view = (ListItemView) convertView;
        if (convertView == null)
            view = new ListItemView(getApplicationContext());
        AlarmVo item = items.get(position);
        view.setName(item.getAlarm_name());
        //view.setCheck(item.isActivate());
        view.setTime(item.getTime());
        //view.setPlace(String.format("%/3g%n",item.getX()) + " , " + String.format("%/3g%n", item.getY()));
        return view;
    }
    void addItem(AlarmVo item) { items.add(item); }
}