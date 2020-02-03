package com.alim.ssn.main.room;

import android.content.Context;
import android.os.AsyncTask;


public class PostRepository {
    private static PostRepository instance;
    private PostDao postDao;
    public static PostRepository getInstance(Context context)
    {
        if (instance == null) {
            instance=new PostRepository(context);
        }
        return instance;
    }

    private PostRepository(Context context)
    {
        RoomDatabaseManager databaseManager=RoomDatabaseManager.getInstance(context);
        postDao=databaseManager.getPost();
    }

    public void insert(RoomPost post)
    {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                postDao.insert(post);
            }
        });
    }
}
