package com.myoptimind.g8_app.features.syncing;

import com.myoptimind.g8_app.features.syncing.response.LastPushDateResponse;
import com.myoptimind.g8_app.features.syncing.response.PullStoreResponse;
import com.myoptimind.g8_app.models.Store;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SyncService {

    /**
     * PULL (Get data from server) endpoints
     */


    @GET("sync")
    Single<PullStoreResponse> pullStores(
            @Query("table_name") String tableName,
            @Query("s_date") String startDate,
            @Query("offset") String offset,
            @Query("limit") String limit
    );


    /**
     *  PUSH (Send data to server) endpoints
     */

    @POST("sync/sync-time")
    @FormUrlEncoded
    Single<LastPushDateResponse> getLastPushDate(
            @Field("name") String tablename,
            @Field("user_id") String userId
    );


    // push store
    @POST("stores/add")
    @FormUrlEncoded
    Completable pushStore(
            @Field("store_uuid") String storeUuid,
            @Field("store_name") String storeName,
            @Field("store_address") String storeAddress,
            @Field("longitude") String longitude,
            @Field("latitude") String latitude,
            @Field("user_id") String userId
    );









}
