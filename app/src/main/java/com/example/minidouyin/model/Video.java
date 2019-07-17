package com.example.minidouyin.model;

import com.google.gson.annotations.SerializedName;

public class Video {

    @SerializedName("_id")
    private String _id;


    @SerializedName("student_id")
    private String studentId;
    @SerializedName("user_name")
    private String userName;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("video_url")
    private String videoUrl;
    @SerializedName("image_w")
    private int imageWidth;
    @SerializedName("image_h")
    private int imageHeight;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int w) {
        this.imageWidth = w;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int h) {
        this.imageHeight = h;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
