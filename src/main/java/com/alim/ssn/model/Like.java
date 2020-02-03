package com.alim.ssn.model;

import java.util.Calendar;

public class Like {
    private String id;
    private String authorId;
    private int postId;
    private long createDate;
    private String uniqueId;//postid + authorId
public Like(){

}
    public Like(String authorId, int postId) {
        this.authorId = authorId;
        this.postId=postId;
        this.createDate= Calendar.getInstance().getTimeInMillis();
        this.uniqueId=postId+authorId;
    }

    public long getCreateDate() {
        return createDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
