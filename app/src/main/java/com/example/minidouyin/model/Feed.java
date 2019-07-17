package com.example.minidouyin.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Feed {
    @SerializedName("feeds")
    private List<Video> feeds;
    @SerializedName("state")
    private String success;
    public List<Video> getFeeds (){
        return feeds;
    }
    public String getSuccess(){
        return success;
    }
}
