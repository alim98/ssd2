package com.alim.ssn.model;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

public class Recent {
    private String action;
    @SerializedName("post_id")
    private String postId;
    @SerializedName("student_id")
    private int stId;
    @SerializedName("action_id")
    private String actionId;
    @SerializedName("created_at")
    private String createdAt;

    private Timestamp tsCreatedAt;
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public int getStId() {
        return stId;
    }

    public void setStId(int stId) {
        this.stId = stId;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getTsCreatedAt() {
        return tsCreatedAt;
    }

    public void setTsCreatedAt(Timestamp tsCreatedAt) {
        this.tsCreatedAt = tsCreatedAt;
    }
}
