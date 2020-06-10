package com.myoptimind.g8_app.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.myoptimind.g8_app.models.Store;
import com.myoptimind.g8_app.models.UserStore;

import java.util.List;

@Dao
public interface StoreDao {

    @Query("SELECT * FROM store")
    LiveData<List<Store>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertStore(Store store);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStoreList(List<Store> stores);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertStoreListIgnoreStrat(List<Store> stores);

    @Query("SELECT * FROM store WHERE user_id = :userId AND created_at > :datetime OR updated_at > :datetime ORDER BY created_at,updated_at LIMIT 1")
    Store getByDateSync(String userId, String datetime);

    @Query("SELECT * FROM store WHERE user_id = :userId ORDER BY created_at LIMIT 1")
    Store getFirstCreated(String userId);

    @Query("SELECT COUNT(*) FROM store WHERE store_name LIKE :storeName")
    Integer getCountByStoreName(String storeName);

    @Query("SELECT store.* FROM store INNER JOIN user_store on store.uuid = user_store.store_uuid WHERE user_store.user_id = :userId")
    LiveData<List<Store>> getUserStores(String userId);

    @Query("SELECT * FROM store WHERE user_id = :userId")
    List<Store> getStoresCreatedByUser(String userId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertUserStore(UserStore userStore);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUserStore(List<UserStore> userStore);

    @Query("DELETE FROM user_store WHERE id > 0")
    int clearStoreOwners();

    @Query("SELECT * FROM store WHERE store_name = :storeName")
    Store getStoreByName(String storeName);



}