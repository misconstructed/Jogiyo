package com.example.misconstructed.jogiyo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TimePicker;

public class AddActivity extends AppCompatActivity {

    int year,month,day,mHour,mMinute;
    EditText title,time1,time2;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        GridView grid = (GridView)findViewById(R.id.chooseView);
        final ChooseItemAdapter adapter = new ChooseItemAdapter();
        adapter.addItem(0);
        adapter.addItem(2);
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                            if (id == 0)
                                                finish();
                                        }
                                    });


        title=(EditText) findViewById(R.id.title);
        time1=(EditText)findViewById(R.id.time1);
        time2=(EditText)findViewById(R.id.time2);
        Calendar cal = new GregorianCalendar();
        year=cal.get(Calendar.YEAR);
        month=cal.get(Calendar.MONTH);
        day=cal.get(Calendar.DAY_OF_MONTH);

        UpdateDate();

    }

    public void mDateOnClick(View v){

        new DatePickerDialog(AddActivity.this, mDateSetListener, year, month, day).show();

    }

    //날짜 대화상자 리스너 부분

    DatePickerDialog.OnDateSetListener mDateSetListener =

            new DatePickerDialog.OnDateSetListener() {


                @Override

                public void onDateSet(DatePicker view, int Year, int monthOfYear,

                                      int dayOfMonth) {

                    // TODO Auto-generated method stub

                    //사용자가 입력한 값을 가져온뒤

                    year = Year;

                    month = monthOfYear;

                    day = dayOfMonth;

                    //텍스트뷰의 값을 업데이트함

                    UpdateDate();

                }

            };

    //텍스트뷰의 값을 업데이트 하는 메소드

    void UpdateDate(){

        time1.setText(String.format("%d년 %d월 %d일", year, month + 1, day));


    }

    public void mTimeOnClick(View v){
        new TimePickerDialog(AddActivity.this, mTimeSetListener, mHour, mMinute, false).show();


    }

    TimePickerDialog.OnTimeSetListener mTimeSetListener =

            new TimePickerDialog.OnTimeSetListener() {



                @Override

                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    // TODO Auto-generated method stub

                    //사용자가 입력한 값을 가져온뒤

                    mHour = hourOfDay;

                    mMinute = minute;



                    //텍스트뷰의 값을 업데이트함

                    UpdateTime();



                }

            };
    //텍스트뷰의 값을 업데이트 하는 메소드

    void UpdateTime(){

        time2.setText(String.format("%d시 %d분", mHour, mMinute));

    }











}
