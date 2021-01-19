package com.example.petmarket2020.Models;

public class NotificationsModel {
    private long notiId;
    private String content;
    private boolean isChecked;
    private String postId;
    private String time;

    public NotificationsModel() {
    }

    public long getNotiId() {
        return notiId;
    }

    public void setNotiId(long notiId) {
        this.notiId = notiId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
