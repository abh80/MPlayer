package com.connectoapp.mplayer;

public class ManifestItem {
    public String id;
    public String title;
    public String thumbnail;
    public String channel;

    public ManifestItem(String id, String title, String thumbnail, String channel) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.channel = channel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
}
