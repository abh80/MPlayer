package com.connectoapp.mplayer;

public class VideoItem {
    private String title;
    private String thumbnail;
    private String channel;
    private String id;

    public VideoItem(String title, String thumbnail, String channel, String id) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.channel = channel;
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
