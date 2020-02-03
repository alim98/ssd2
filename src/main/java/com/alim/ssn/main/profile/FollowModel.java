package com.alim.ssn.main.profile;

import com.google.gson.annotations.SerializedName;

public class FollowModel {
    @SerializedName("follower_id")
    private int followerId;

    @SerializedName("following_id")
    private int followingId;

    public int getFollowerId() {
        return followerId;
    }

    public void setFollowerId(int followerId) {
        this.followerId = followerId;
    }

    public int getFollowingId() {
        return followingId;
    }

    public void setFollowingId(int followingId) {
        this.followingId = followingId;
    }
}
