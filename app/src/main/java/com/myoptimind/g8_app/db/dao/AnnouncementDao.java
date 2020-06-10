package com.myoptimind.g8_app.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.myoptimind.g8_app.models.Announcement;
import com.myoptimind.g8_app.models.UserAnnouncement;

import java.util.List;
import java.util.Observable;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface AnnouncementDao {

    @Query("SELECT * FROM announcements")
    LiveData<List<Announcement>> getAll();

    @Query("SELECT * FROM announcements WHERE is_checked = 0")
    LiveData<List<Announcement>> getAllUnread();

    @Query("SELECT * FROM announcements WHERE formatted_announcement_type = 'BULLETIN'")
    LiveData<List<Announcement>> getAllBulletin();

    @Query("SELECT * FROM announcements WHERE formatted_announcement_type = 'MEMO'")
    LiveData<List<Announcement>> getAllMemo();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertAnnouncement(Announcement announcement);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAnnouncement(List<Announcement> announcements);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertUserAnnouncement(UserAnnouncement userAnnouncement);

    @Delete
    Completable deleteUserAnnouncement(UserAnnouncement userAnnouncement);

/*    @Query("SELECT * FROM user_announcement where user_id = :userId AND announcement_id = :announcementId")
    UserAnnouncement getUserAnnouncement(String userId,String announcementId);*/

    @Query("SELECT * FROM user_announcement where user_id = :userId AND announcement_id = :announcementId")
    Single<UserAnnouncement> getUserAnnouncement(String userId, String announcementId);

    /*@Query("SELECT * FROM announcements " +
            "LEFT JOIN user_announcement " +
            "ON announcements.id != user_announcement.announcement_id " +
            "WHERE user_announcement.user_id = :userId")
    LiveData<List<Announcement>> getAllUnreadAnnouncementsForUser(String userId);*/

    @Query("SELECT * FROM announcements where id NOT IN (SELECT announcement_id FROM user_announcement where user_id = :userId )")
    LiveData<List<Announcement>> getAllUnreadAnnouncementsForUser(String userId);
}
