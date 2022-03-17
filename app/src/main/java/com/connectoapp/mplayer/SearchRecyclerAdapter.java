package com.connectoapp.mplayer;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.Image;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.MyViewHolder> {
    private ArrayList<VideoItem> videos;
    private Context ctx;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchitem_layout, parent, false);
        ctx = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String title = videos.get(position).getTitle();
        String thumbnail = videos.get(position).getThumbnail();
        String channel = videos.get(position).getChannel();
        String id = videos.get(position).getId();
        holder.mVidChannel.setText(channel);
        holder.mVidTitle.setText(title);
        if(YTDL.manifest.stream().anyMatch(x -> x.id.equals(id))){
            holder.mButton.setVisibility(View.GONE);
            holder.pButton.setVisibility(View.VISIBLE);
        }
        holder.mItemView.setOnClickListener(view -> view.findViewById(R.id.extraControls).setVisibility(View.VISIBLE));
        holder.pButton.setOnClickListener(l->{

        });
        holder.mButton.setOnClickListener(l -> {
            ProgressDialog progressDialog = new ProgressDialog(ctx);
            progressDialog.setCancelable(true);
            progressDialog.setTitle("Just a moment");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCanceledOnTouchOutside(false);

            progressDialog.show();
            YTDL.download(ctx.getApplicationInfo().dataDir, id, (progress, isDone) -> {
                progressDialog.setProgress((int) Math.ceil(progress));
                if (isDone) {
                    System.out.println("Done");
                    progressDialog.dismiss();
                    YTDL.manifest.add(new ManifestItem(id, title, thumbnail, channel));
                    YTDL.saveManifest(ctx.getApplicationInfo().dataDir);
                }
                return null;
            });
        });
        Picasso.get().load(thumbnail).into(holder.mVidThumbnail);

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public SearchRecyclerAdapter(ArrayList<VideoItem> videos, Context ctx) {
        this.videos = videos;
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
