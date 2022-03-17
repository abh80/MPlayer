package com.connectoapp.mplayer;


import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private ArrayList<VideoItem> videos;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private static View mView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        initView(view);
        recyclerView = view.findViewById(R.id.videos_holder);
        videos = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

    private void handleSearchSubmit(String text) {
        if (mView != null) {
            mView.findViewById(R.id.not_found_text).setVisibility(View.INVISIBLE);
            MainActivity.toggleLoader(true);
            try {
                Request.getReq("https://ytsr-api-zeta.vercel.app/", "q=" + URLEncoder.encode(text, "utf8"), (json) -> {
                    requireActivity().runOnUiThread(() -> MainActivity.toggleLoader(false));
                    videos.clear();
                    if (json != null) {
                        for (int i = 0; i < json.length(); i++) {
                            try {
                                JSONObject obj = json.getJSONObject(i);
                                String thumbnail = obj.getJSONArray("thumbnails").getJSONObject(0).getString("url");
                                videos.add(new VideoItem(obj.getString("title"), thumbnail, obj.getJSONObject("author").getString("name"), obj.getString("id")));
                                setAdapter();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        mView.findViewById(R.id.not_found_text).setVisibility(View.VISIBLE);
                    }
                    return null;
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }

    private void setAdapter() {
        SearchRecyclerAdapter adapter = new SearchRecyclerAdapter(videos, requireActivity().getApplicationContext());
        recyclerView.setAdapter(adapter);
    }

    private void initView(View view) {
        SearchFragment.mView = view;
        TextInputEditText text = view.findViewById(R.id.yt_searchbar);
        text.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                if (text.getText() != null && !"".equals(text.getText().toString())) {
                    handleSearchSubmit(text.getText().toString());
                    text.clearFocus();
                    InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(text.getWindowToken(), 0);
                }

                return true;
            }
            InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(text.getWindowToken(), 0);
            return false;
        });
    }
}
