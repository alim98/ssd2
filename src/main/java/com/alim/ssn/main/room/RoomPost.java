package com.alim.ssn.main.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;
import java.util.List;

@Entity
public class RoomPost  {
    RoomPost(int id, String title , String content, String field, String grade, int studentId, String created_at, String imagePath, int commentsCount, int likesCount)
    {
        this.title=title;
        this.content=content;
        this.field=field;
        this.grade=grade;
        this.studentId=studentId;
        this.created_at=created_at;
        this.imagePath=imagePath;
        this.commentsCount=commentsCount;
        this.likesCount=likesCount;
        this.id=id;
    }
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String content;
    private String field;
    private String grade;
    private int studentId;
    private String created_at;
    private String imagePath;
    private int commentsCount;
    private int likesCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    private int size;
    private boolean isSaved;
    private boolean isLiked;

}
