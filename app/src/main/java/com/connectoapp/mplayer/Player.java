package com.connectoapp.mplayer;

import android.Manifest;
import android.app.IntentService;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import androidx.media.app.NotificationCompat;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

public class Player extends Service {
    private static MediaPlayer mediaPlayer;
    public static PlayerStatus playerStatus = new PlayerStatus();
    private static NotificationManagerCompat mNotificationCompatManager;
    private static Context ctx;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        ctx = getApplicationContext();
        mNotificationCompatManager = NotificationManagerCompat.from(this);
    }

    public static void play(String path, ManifestItem song) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            playerStatus.manifestItem = song;
            playerStatus.isPlaying = true;
            updateNotification();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void pause() {
        if (mediaPlayer != null && playerStatus.isPlaying) {
            mediaPlayer.pause();
        }
    }

    public static void resume() {
        if (mediaPlayer != null && !playerStatus.isPlaying) {
            mediaPlayer.start();
        }
    }

    private static void updateNotification() {
        androidx.core.app.NotificationCompat.Builder channel = new androidx.core.app.NotificationCompat.Builder(ctx, "channel1")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Playing " + playerStatus.manifestItem.title)
                .setContentText("by " + playerStatus.manifestItem.channel)
                .addAction(R.drawable.ic_baseline_arrow_back_ios_24,"prev",null)
                .addAction(R.drawable.ic_baseline_pause_24 , "pause",null)
                .addAction(R.drawable.ic_baseline_arrow_forward_ios_24,"next",null)
                .setStyle(new NotificationCompat.MediaStyle().setShowActionsInCompactView(0,1,2))
                .setOngoing(true);
        Picasso.get().load(playerStatus.manifestItem.thumbnail).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                channel.setLargeIcon(bitmap);
                channel.setColor(getDominantColor(bitmap));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
        mNotificationCompatManager.notify(1, channel.build());
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onLowMemory() {

    }
    public static int getDominantColor(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        return color;
    }
}

class PlayerStatus {
    public ManifestItem manifestItem;
    public boolean isPlaying;
}