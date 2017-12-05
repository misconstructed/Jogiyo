package com.example.misconstructed.jogiyo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.misconstructed.jogiyo.VO.AlarmVo;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("receiver", "got in the receiver");

        MediaPlayer mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
        mediaPlayer.start();
        //PendingIntent p_intent = PendingIntent.getD
        //intent.getI
        //AlarmVo alarm = intent.getParcelableExtra("alarm_data");

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent my_map = new Intent(context, MyMapActivity.class);
        my_map.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, my_map, PendingIntent.FLAG_CANCEL_CURRENT);
        //Log.e("WOW....>>>", alarm.toString());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Jp Gi Yo")
                .setContentText("Time Alarm is On!")
                .setAutoCancel(true);

        notificationManager.notify(0, builder.build());

    }
}
