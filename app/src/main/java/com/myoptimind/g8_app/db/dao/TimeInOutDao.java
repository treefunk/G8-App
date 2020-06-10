package com.myoptimind.g8_app.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.myoptimind.g8_app.models.TimeInOut;

import java.util.List;

@Dao
public interface TimeInOutDao {

    @Query("SELECT COUNT(*) FROM time_in_out WHERE user_id = :userId AND created_at LIKE :datetime AND type = :type")
    LiveData<Integer> getByDateTimeIns(String userId, String datetime, String type);

    @Query("SELECT * FROM time_in_out WHERE user_id = :userId AND created_at LIKE :datetime AND type = :type")
    LiveData<TimeInOut> getByDate(String userId, String datetime, String type);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertTimeInOut(TimeInOut timeInOut);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTimeInOutList(List<TimeInOut> timeInOuts);

    @Query("SELECT * FROM time_in_out WHERE user_id = :userId AND created_at > :datetime LIMIT 1")
    TimeInOut getByDateSync(String userId,String datetime);

    @Query("SELECT * FROM time_in_out WHERE user_id = :userId ORDER BY created_at LIMIT 1")
    TimeInOut getFirstCreated(String userId);

}
