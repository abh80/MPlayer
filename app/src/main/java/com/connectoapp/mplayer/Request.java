package com.connectoapp.mplayer;

import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.function.Function;

public class Request {
    public static void getReq(String url, String queryString, Function<JSONArray, Object> callback) {

        new Thread(() -> {
            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                    (com.android.volley.Request.Method.GET, url + "?" + queryString, null, callback::apply, error -> {
                        error.printStackTrace();
                        callback.apply(null);
                    });
            jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 50000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }

                @Override
                public void retry(VolleyError error) {
                }
            });

            MainActivity.requestQueue.add(jsonObjectRequest);
        }).start();
    }
}
