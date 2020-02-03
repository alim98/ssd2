package com.alim.ssn.like;

import com.google.gson.annotations.SerializedName;

public class LikeModel {
    @SerializedName("is_liked")
    private boolean isLiked;
    @SerializedName("like_id")
    private String likeId;
    private String response;
    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getLikeId() {
        return likeId;
    }

    public void setLikeId(String likeId) {
        this.likeId = likeId;
    }
}
