package com.alim.ssn.model;

import com.google.gson.annotations.SerializedName;

public class Tag {
    @SerializedName("tag_id")
    private String tagId;

    @SerializedName("title")
    private String title;

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
