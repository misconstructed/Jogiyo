package com.example.misconstructed.jogiyo;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Administrator on 2017-11-30.
 */

public class ChooseItemAdapter extends BaseAdapter {
    private ArrayList<Integer> items = new ArrayList();

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
        return items.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ChooseItemView chooseView = (ChooseItemView) convertView;
        if (convertView == null)
            chooseView = new ChooseItemView(getApplicationContext());

        int item = items.get(position);

        chooseView.setItem(item);
        return chooseView;
    }

    void addItem(int n) { items.add(n); }
}
