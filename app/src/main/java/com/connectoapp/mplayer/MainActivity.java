package com.connectoapp.mplayer;

import android.Manifest;
import android.annotation.SuppressLint;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.yausername.ffmpeg.FFmpeg;
import com.yausername.youtubedl_android.YoutubeDL;
import com.yausername.youtubedl_android.YoutubeDLException;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    public static boolean isMenuOpen = false;

    public int height;
    public static int width;


    @SuppressLint("StaticFieldLeak")
    private static View mView;
    public static RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        YTDL.readManifest(getApplicationInfo().dataDir);
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "hey", 1, perms);
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        try {
            YoutubeDL.getInstance().init(getApplication());
            FFmpeg.getInstance().init(getApplication());
        } catch (YoutubeDLException e) {
            Log.e("YTDL", "failed to initialize youtubedl-android", e);
        }

        initView();
        mView = findViewById(R.id.main);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        if (!isMyServiceRunning(Player.class)) {
            startService(new Intent(this, Player.class));
        }

    }

    public static void toggleLoader(boolean start) {
        if (mView != null) {
            if (!start) {
                mView.findViewById(R.id.progress_bar).setVisibility(View.INVISIBLE);
            } else {
                mView.findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
            }
        }
    }

    private float x1;
    private float deltaX;

    private boolean isRollingMenu = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            x1 = event.getX();
        }
        int threshold = 50;
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float x2 = event.getX();
            deltaX = x2 - x1;
            if (!isRollingMenu && Math.abs(deltaX) > threshold) {
                findViewById(R.id.menu).setVisibility(View.VISIBLE);
                MenuHandler.handleSwipe(findViewById(R.id.main), deltaX, width);
                x1 = x2;
                isRollingMenu = true;
            } else if (isRollingMenu) {
                MenuHandler.handleSwipe(findViewById(R.id.main), deltaX, width);
                x1 = x2;
            }

        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            MenuHandler.Apply(findViewById(R.id.main), width, deltaX);
            isRollingMenu = false;
        }
        return super.onTouchEvent(event);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        MenuHandler.initializeMenuItemClick(findViewById(R.id.main), width, this);
        findViewById(R.id.menu_btn).setOnClickListener(l -> {
            MenuHandler.toggle(MainActivity.this.findViewById(R.id.main), width);
        });
    }

    @SuppressLint("SetTextI18n")
    public void handleMenuChange(String item, String prev) {
        switch (prev) {
            case "home":
                ((ImageView) findViewById(R.id.imageView)).setColorFilter(ContextCompat.getColor(this, R.color.menu_disabled));
                ((TextView) findViewById(R.id.textView2)).setTextColor(getColor(R.color.menu_disabled));
                break;

            case "search":
                ((ImageView) findViewById(R.id.imageView2)).setColorFilter(ContextCompat.getColor(this, R.color.menu_disabled));
                ((TextView) findViewById(R.id.textView4)).setTextColor(getColor(R.color.menu_disabled));
                break;

            case "downloads":
                ((ImageView) findViewById(R.id.imageView3)).setColorFilter(ContextCompat.getColor(this, R.color.menu_disabled));
                ((TextView) findViewById(R.id.textView5)).setTextColor(getColor(R.color.menu_disabled));
                break;
        }

        switch (item) {
            case "home":
                ((TextView) findViewById(R.id.textView)).setText("Home");
                ((ImageView) findViewById(R.id.imageView)).setColorFilter(ContextCompat.getColor(this, R.color.menu_enabled));
                ((TextView) findViewById(R.id.textView2)).setTextColor(getColor(R.color.menu_enabled));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder, new HomeFragment(), null).addToBackStack(null).commit();
                break;

            case "search":
                ((TextView) findViewById(R.id.textView)).setText("Search");
                ((ImageView) findViewById(R.id.imageView2)).setColorFilter(ContextCompat.getColor(this, R.color.menu_enabled));
                ((TextView) findViewById(R.id.textView4)).setTextColor(getColor(R.color.menu_enabled));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder, new SearchFragment(), null).addToBackStack(null).commit();
                break;

            case "downloads":
                ((TextView) findViewById(R.id.textView)).setText("Downloads");
                ((ImageView) findViewById(R.id.imageView3)).setColorFilter(ContextCompat.getColor(this, R.color.menu_enabled));
                ((TextView) findViewById(R.id.textView5)).setTextColor(getColor(R.color.menu_enabled));
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder, new DownloadsFragment(), null).addToBackStack(null).commit();
                break;
        }
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}