package com.example.misconstructed.jogiyo.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.misconstructed.jogiyo.MyMapActivity;
import com.example.misconstructed.jogiyo.R;
import com.example.misconstructed.jogiyo.VO.AlarmVo;
import com.example.misconstructed.jogiyo.VO.UserVo;
import com.google.android.gms.location.LocationListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LocationService extends Service implements LocationListener {

    private Thread LocationThread = null;
    private UserVo user = null;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference alarm_database = firebaseDatabase.getReference("Alarm");
    private DatabaseReference database = firebaseDatabase.getReference();

    private double latitude;
    private double longitude;
    private Location location;
    private Location alarm_location;
    private LocationListener locationListener;

    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        user = intent.getParcelableExtra("user");
        alarm_location = new Location("Alarm Location");
        location = new Location("Current Location");
        //여기가 문제
        //Log.e("문제 일어나고 나서::", user.toString());
        Log.e("WOW", "service started!!!!!!!!!!!!!!!!!!!!!+====================++~~~~!!!!!!!!!!");
        Intent return_intent = new Intent(this, MyMapActivity.class);
        return_intent.putExtra("user", user);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, return_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification noti = new NotificationCompat.Builder(this)
                .setContentTitle("Jo Gi Yo")
                .setContentText("Running JoGiYo for "+user.getName())
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pIntent)
                .build();

        startForeground(1234, noti);

        if (LocationThread == null) {
            LocationThread = new Thread("Location Alarm Thread") {
                public void run() {
                    while(true) {
                        iterator(user);
                        try {
                            sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            };
            LocationThread.start();
        }
        return START_STICKY;
    }

    private void iterator(final UserVo user){
        Log.e("ITERATOR", "iterato started !!!!!!!!!!!!!!!!!!!!!!!!-----------=======================++!!!!!!!!!!!");
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                Log.e("IMPORTANT", "log : "+longitude + " lat : "+ latitude);
            }
        };
        alarm_database.addValueEventListener(new ValueEventListener() {
            AlarmVo alarm;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //iterator로 전체 알람 조회
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    AlarmVo alarm = userSnapshot.getValue(AlarmVo.class);
                    //회원정보가 맞는 경우
                    if(alarm.getId().equals(user.getId())){

                        //위치기반 알람인 경우
                        if(alarm.isPlace_alarm() == true) {
                            Log.e("Alarm data ::::", alarm.toString());
                            //여기에서 위치 비교해야됨. 근데 현재 위치를 못구해서 못함.
                            if(alarm.isActivate() == true){
                                //위치 비교함 alarm.getRange();로 해서 지정한 거리 안에 있으면 알람
                                //그냥 알람 활성화되면 alarm.isActivate() = false로 바꿔줘야 함
                                //안그러면 계속 범위 안에 들어와서 계속 알람 울릴거같음..

                            }
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {

    }
}

