package com.alim.ssn.main.room;

import androidx.room.Dao;
import androidx.room.Insert;



@Dao
public interface PostDao {

    @Insert
    void insert(RoomPost post);
}
