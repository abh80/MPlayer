package com.connectoapp.mplayer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DownloadsFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private ArrayList<ManifestItem> songs;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.downloads_fragment, container, false);
        initView(view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }
    private void initView(View view){
        recyclerView = view.findViewById(R.id.downloadsRecyclerView);
        songs = YTDL.manifest;
        setAdapter();
    }
    private void setAdapter() {
        DownloadsRecyclerAdapter adapter = new DownloadsRecyclerAdapter(songs, requireActivity().getApplicationContext());
        recyclerView.setAdapter(adapter);
    }
}
