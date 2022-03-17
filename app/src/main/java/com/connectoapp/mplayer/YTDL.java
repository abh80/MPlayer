package com.connectoapp.mplayer;


import com.yausername.youtubedl_android.YoutubeDL;
import com.yausername.youtubedl_android.YoutubeDLException;
import com.yausername.youtubedl_android.YoutubeDLRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class YTDL {
    public static ArrayList<ManifestItem> manifest = new ArrayList<>();

    public static void readManifest(String directory) {
        String json;
        try {
            File file = new File(directory + "/manifest.json");
            if (!file.exists()) {
                file.createNewFile();
                OutputStream fo = new FileOutputStream(file);
                fo.write("[]".getBytes(StandardCharsets.UTF_8));
                fo.close();
            }
            InputStream stream = new FileInputStream(file);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            json = new String(buffer, StandardCharsets.UTF_8);

            JSONArray json2 = new JSONArray(json);
            for (int i = 0; i < json2.length(); i++) {
                JSONObject item = json2.getJSONObject(i);
                manifest.add(new ManifestItem(item.getString("id"), item.getString("title"), item.getString("thumbnail"), item.getString("channel")));
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }

    public static void saveManifest(String directory) {
        try {
            File file = new File(directory + "/manifest.json");
            OutputStream fo = new FileOutputStream(file);
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < manifest.size(); i++) {
                ManifestItem item = manifest.get(i);
                JSONObject obj = new JSONObject();
                obj.put("title", item.title);
                obj.put("id", item.id);
                obj.put("thumbnail", item.thumbnail);
                obj.put("channel", item.channel);
                jsonArray.put(i, obj);
            }
            fo.write(jsonArray.toString().getBytes(StandardCharsets.UTF_8));
            fo.close();
            System.out.println("Successfully wrote manifest");
            readManifest(directory);

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void download(String directory, String id, Function2<Float, Boolean, Void> callback) {
        System.out.println(id);
        Thread t = new Thread(() -> {
            YoutubeDLRequest request = new YoutubeDLRequest("https://youtube.com/watch?v=" + id);

            request.addOption("-o", directory + "/" + id + ".mp3");
            request.addOption("--extract-audio");
            request.addOption("--audio-format", "mp3");
            try {
                YoutubeDL.getInstance().execute(request, (progress, etaInSeconds, line) -> {
                    System.out.println(line);
                    callback.apply(progress, false);

                });
            } catch (YoutubeDLException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        t.start();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!t.isAlive()) {
                    callback.apply(100f, true);
                    timer.cancel();
                }
            }
        }, 0, 2000);
    }
}

@FunctionalInterface
interface Function2<One, Two, Three> {
    public Three apply(One one, Two two);
}