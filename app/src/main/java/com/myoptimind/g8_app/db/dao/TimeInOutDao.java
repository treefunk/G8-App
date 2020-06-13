package com.myoptimind.g8_app.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.myoptimind.g8_app.models.TimeInOut;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface TimeInOutDao {

    @Query("SELECT COUNT(*) FROM time_in_out WHERE user_id = :userId AND created_at LIKE :datetime AND type = :type")
    Single<Integer> getByDateTimeIns(String userId, String datetime, String type);

    @Query("SELECT * FROM time_in_out WHERE user_id = :userId AND created_at LIKE :datetime AND type = :type")
    LiveData<TimeInOut> getByDate(String userId, String datetime, String type);

    @Query("SELECT * FROM time_in_out WHERE user_id = :userId AND created_at LIKE :datetime AND type = :type")
    Maybe<TimeInOut> getByDate_(String userId, String datetime, String type);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertTimeInOut(TimeInOut timeInOut);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertTimeInOutList(List<TimeInOut> timeInOuts);

    @Query("SELECT * FROM time_in_out WHERE user_id = :userId AND created_at > :datetime LIMIT 1")
    Single<TimeInOut> getByDateSync(String userId,String datetime);

    @Query("SELECT * FROM time_in_out WHERE user_id = :userId ORDER BY created_at LIMIT 1")
    Single<TimeInOut> getFirstCreated(String userId);

}
