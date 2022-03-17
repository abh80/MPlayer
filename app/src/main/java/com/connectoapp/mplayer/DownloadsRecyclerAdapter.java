package com.connectoapp.mplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DownloadsRecyclerAdapter extends RecyclerView.Adapter<DownloadsRecyclerAdapter.MyViewHolder> {
    private ArrayList<ManifestItem> songs;
    private Context ctx;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.songitem_layout, parent, false);
        ctx = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String title = songs.get(position).getTitle();
        String thumbnail = songs.get(position).getThumbnail();
        String channel = songs.get(position).getChannel();
        String id = songs.get(position).getId();
        holder.mVidChannel.setText(channel);
        holder.mVidTitle.setText(title);
        holder.mItemView.setOnClickListener(view -> {
            Player.play(ctx.getApplicationInfo().dataDir + "/" + id + ".mp3", songs.get(position));
        });

        Picasso.get().load(thumbnail).into(holder.mVidThumbnail);

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public DownloadsRecyclerAdapter(ArrayList<ManifestItem> songs, Context ctx) {
        this.songs = songs;
        this.ctx = ctx;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mVidTitle;
        private ImageView mVidThumbnail;
        private TextView mVidChannel;
        private Button mButton;
        private View mItemView;
        private Button pButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mVidTitle = itemView.findViewById(R.id.vidtitle);
            mVidThumbnail = itemView.findViewById(R.id.vidThumbnail);
            mVidChannel = itemView.findViewById(R.id.vidChannel);
            mButton = itemView.findViewById(R.id.downloadBtn);
            mItemView = itemView.findViewById(R.id.vidCard);
            pButton = itemView.findViewById(R.id.playBtn);
        }
    }
}
