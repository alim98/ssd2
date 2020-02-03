package com.alim.ssn.model;



import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

import java.util.List;


public class Post {
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("desc")
    private String content;
    @SerializedName("field")
    private String field;
    @SerializedName("grade")
    private String grade;
    @SerializedName("student_id")
    private int studentId;
    @SerializedName("created_at")
    private String created_at;

    private Timestamp tsCreatedAt;
    @SerializedName("profile_url")
    private String imagePath;
    private long commentsCount;
    @SerializedName("likes_count")
    private int likesCount;
    private List<String> tags;
    private int size;
    private boolean isSaved;
    private boolean isLiked;

    public Post() {

    }

    public Post(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public long getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(long commentsCount) {
        this.commentsCount = commentsCount;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    /*    private Drawable image;*/
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @SerializedName("is_active")

    private boolean isActive;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public Timestamp getTsCreatedAt() {
        return tsCreatedAt;
    }

    public void setTsCreatedAt(Timestamp tsCreatedAt) {
        this.tsCreatedAt = tsCreatedAt;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

   /* public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }*/
}
