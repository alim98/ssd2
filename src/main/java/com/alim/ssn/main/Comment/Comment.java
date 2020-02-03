package com.alim.ssn.main.Comment;

import com.google.gson.annotations.SerializedName;

public class Comment {
    @SerializedName("student_id")
    private int stId;

    @SerializedName("post_id")
    private String postId;

    public int getStId() {
        return stId;
    }

    public void setStId(int stId) {
        this.stId = stId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @SerializedName("comment")
    private String comment;
}
