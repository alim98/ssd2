package com.alim.ssn.main.room;

import android.content.Context;
import android.os.AsyncTask;
import android.telecom.Call;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;


@Database(entities = {RoomPost.class},version = 1)
public abstract class RoomDatabaseManager extends RoomDatabase {

public abstract PostDao getPost();

public static RoomDatabaseManager postDb;
public static synchronized RoomDatabaseManager getInstance(Context context)
{
    if (postDb == null) {
        postDb=buildDatabaseInstance(context);
    }
    return postDb;
}
    private static RoomDatabaseManager buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                RoomDatabaseManager.class,
                "SSD.db")
                .allowMainThreadQueries().build();
    }
    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }

    private static Callback roomCallback=new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            new PopulateDbAsynTask(postDb );
            super.onCreate(db);
        }
    };

private static Callback roomCallback1=new Callback() {
    @Override
    public void onOpen(@NonNull SupportSQLiteDatabase db) {
        super.onOpen(db);
    }
};

    private static class PopulateDbAsynTask extends AsyncTask<Void, Void, Void>{
        private PostDao postDao;
        PopulateDbAsynTask(RoomDatabaseManager databaseManager){
            postDao=databaseManager.getPost();

        }
        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }
    }
}
