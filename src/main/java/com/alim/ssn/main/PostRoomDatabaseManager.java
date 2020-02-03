package com.alim.ssn.main;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.alim.ssn.model.Post;

import java.util.List;

public class PostRoomDatabaseManager {










































  /*  private static final String KEY_POST_ID = "id";
    private static final String KEY_POST_TITILE = "title";
    private static final String KEY_POST_DESC = "content";
    private static final String KEY_POST_FILE = "file";
    private static final String KEY_POST_LIKES = "likes_count";
    private static final String KEY_POST_COMMENTS = "comments";
    private static final String KEY_POST_SIZE = "size";
    private static final String KEY_POST_STUDENT_ID = "student_id";
    private static final String KEY_POST_CREATED_AT = "created_at";

    public PostRoomDatabaseManager(@Nullable Context context) {
        super(context, "SSD", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_POSTS_TABLE = "CREATE TABLE post ("
                + KEY_POST_ID + " INTEGER PRIMARY KEY, "
                + KEY_POST_TITILE + " TEXT,"
                + KEY_POST_DESC + " TEXT,"
                + KEY_POST_FILE + " TEXT,"
                + KEY_POST_LIKES + " TEXT,"
                + KEY_POST_COMMENTS + " TEXT,"
                + KEY_POST_SIZE + " TEXT,"
                + KEY_POST_STUDENT_ID + " TEXT, "
                + KEY_POST_CREATED_AT + " TEXT)";
        db.execSQL(CREATE_POSTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addPosts(List<Post> posts) {
        SQLiteDatabase db = getWritableDatabase();

        for (int i = 0; i < posts.size(); i++) {
            Post post = posts.get(i);
            ContentValues cv = new ContentValues();
            cv.put(KEY_POST_ID, post.getId());
            cv.put(KEY_POST_TITILE, post.getTitle());
            cv.put(KEY_POST_TITILE, post.getContent());
            cv.put(KEY_POST_TITILE, post.getImagePath());
            cv.put(KEY_POST_TITILE, post.getLikesCount());
            cv.put(KEY_POST_TITILE, post.getCommentsCount());
            cv.put(KEY_POST_TITILE, post.getSize());
            cv.put(KEY_POST_TITILE, post.getStudentId());
            cv.put(KEY_POST_TITILE, post.getCreated_at());
            db.insert("post", null, cv);

        }

    }*/
}
