package com.example.misconstructed.jogiyo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2017-11-30.
 */

public class ChooseItemView extends LinearLayout {

    TextView item;
    ImageView imageView;

    public ChooseItemView(Context context){
        super(context);
        init(context);
    }

    public ChooseItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.choose_item, this, true);
        item=(TextView)findViewById(R.id.menuText);
        imageView=(ImageView)findViewById(R.id.detailMenu);
    }

    void setItem(int n){
        if(n==0) {
            item.setText("삭제");
            imageView.setImageResource(R.drawable.ic_delete_black_24dp);
        }
        if(n==1) {
            item.setText("수정");
            imageView.setImageResource(R.drawable.ic_mode_edit_black_24dp);
        }
        if(n==2) {
            item.setText("공유");
            imageView.setImageResource(R.drawable.ic_share_black_24dp);
        }

    }
}
