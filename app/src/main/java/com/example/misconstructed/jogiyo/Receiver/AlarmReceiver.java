package com.example.misconstructed.jogiyo.Receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.misconstructed.jogiyo.MyMapActivity;
import com.example.misconstructed.jogiyo.R;
import com.example.misconstructed.jogiyo.VO.AlarmVo;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("receiver", "got in the receiver");
        //알람 울릴 때 소리
        //MediaPlayer mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
        //mediaPlayer.start();

        Intent tmp = intent.getParcelableExtra("alarm");
        if(tmp == null)
            Log.e("ALERT", "is NULL");
        else
            Log.e("ALERT", "not NULL");



        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent my_map = new Intent(context, MyMapActivity.class);
        my_map.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, my_map, PendingIntent.FLAG_CANCEL_CURRENT);
        //Log.e("WOW....>>>", alarm.toString());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Jo Gi Yo")
                .setContentText("Time Alarm is On!")
                .setAutoCancel(true);

        notificationManager.notify(0, builder.build());

    }



}
