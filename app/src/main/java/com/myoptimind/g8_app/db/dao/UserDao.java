package com.myoptimind.g8_app.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.myoptimind.g8_app.models.User;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface UserDao {

/*    @Insert
    long insertUser1(User user);*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertUser(User user);

    @Delete
    int deleteUser(User user);

    @Query("SELECT * FROM User LIMIT 1")
    LiveData<User> getUserLive();

    @Query("SELECT * FROM User LIMIT 1")
    User getUser();

    @Query("DELETE FROM User WHERE id > 0")
    void clearUsers();


}
