package com.myoptimind.g8_app.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.myoptimind.g8_app.models.TimeSlip;

@Dao
public interface TimeSlipDao {

    @Query("SELECT * FROM timeslip WHERE user_id = :userId AND created_at LIKE :date")
    LiveData<TimeSlip> getByDate(String userId, String date);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTimeSlip(TimeSlip timeSlip);

    @Query("SELECT * FROM timeslip WHERE user_id = :userId AND created_at > :datetime LIMIT 1")
    TimeSlip getByDateSync(String userId, String datetime);

    @Query("SELECT * FROM timeslip WHERE user_id = :userId ORDER BY created_at LIMIT 1")
    TimeSlip getFirstCreated(String userId);

}
