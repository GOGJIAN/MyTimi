package com.jianjian.android.mytimi.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.jianjian.android.mytimi.MainActivity;
import com.jianjian.android.mytimi.R;
import com.jianjian.android.mytimi.tools.TimiFetchr;

public class PollService extends IntentService {

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context,PollService.class);
        return intent;
    }

    private static final String TAG = "PollService";

    public PollService() {
        super(TAG);
    }



    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(!isNetworkConnected())
            return;

        String str = new TimiFetchr().getNotification();
        Log.d(TAG, "onHandleIntent: " + str);
        Intent i = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,0);
        Notification notification;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new NotificationCompat.Builder(this, NotificationChannel.DEFAULT_CHANNEL_ID)
                    .setTicker("hello")
                    .setSmallIcon(R.drawable.ic_common)
                    .setContentTitle("hello1")
                    .setContentText(str)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();

        }else{
            notification = new NotificationCompat.Builder(this)
                    .setTicker("hello")
                    .setSmallIcon(R.drawable.ic_common)
                    .setContentTitle("hello1")
                    .setContentText(str)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0,notification);
    }

    private boolean isNetworkConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        assert cm != null;
        boolean isConnectionAvailable = cm.getActiveNetworkInfo()!=null;
        return isConnectionAvailable&&cm.getActiveNetworkInfo().isConnected();
    }

    public static void setServiceAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = PollService.newIntent(context);
        PendingIntent pendingIntent = PendingIntent.getService(context,0,intent,0);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),
                60*1000,pendingIntent);

    }
}
