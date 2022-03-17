package com.connectoapp.mplayer;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Notification extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        NotificationChannel channel = new NotificationChannel("channel1","FIRSTCHANNEL", NotificationManager.IMPORTANCE_HIGH);
        channel.enableLights(true);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
