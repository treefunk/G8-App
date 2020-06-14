package com.myoptimind.g8_app.features.syncing;

import com.myoptimind.g8_app.features.syncing.response.LastPushDateResponse;
import com.myoptimind.g8_app.features.syncing.response.PullResponse;
import com.myoptimind.g8_app.features.syncing.response.PushSalesResponse;
import com.myoptimind.g8_app.features.syncing.response.PushStoreResponse;
import com.myoptimind.g8_app.features.syncing.response.PushTimeInResponse;
import com.myoptimind.g8_app.models.Store;
import com.myoptimind.g8_app.models.TimeInOut;

import io.reactivex.Observable;
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
    Single<PullResponse<Store>> pullStores(
            @Query("table_name") String tableName,
            @Query("s_date") String startDate,
            @Query("offset") String offset,
            @Query("limit") String limit
    );

    @GET("sync")
    Single<PullResponse<TimeInOut>> pullTimeIns(
            @Query("table_name") String tableName,
            @Query("s_date") String startDate,
            @Query("user_id") String userId
    );
/*

    @GET("sync")
    Single<PullResponse<TimeInOut>> pullSales(
            @Query("table_name") String tableName,
            @Query("s_date") String startDate,
            @Query("user_id") String userId
    );
*/




    /**
     *  PUSH (Send data to server) endpoints
     */

    // get last push date in server
    @POST("sync/sync-time")
    @FormUrlEncoded
    Observable<LastPushDateResponse> getLastPushDate(
            @Field("name") String tablename,
            @Field("user_id") String userId
    );


    // push store
    @POST("stores/add")
    @FormUrlEncoded
    Observable<PushStoreResponse> pushStore(
            @Field("uuid") String storeUuid,
            @Field("store_name") String storeName,
            @Field("store_address") String storeAddress,
            @Field("longitude") String longitude,
            @Field("latitude") String latitude,
            @Field("user_id") String userId,
            @Field("created_at") String createdAt,
            @Field("updated_at") String updatedAt
    );

    // push time in / time out
    @POST("attendance/time")
    @FormUrlEncoded
    Observable<PushTimeInResponse> pushTimeInOut(
            @Field("user_id") String userId,
            @Field("store_id") String storeId,
            @Field("uuid") String uuid,
            @Field("type") String type,
            @Field("created_at") String createdAt
    );

    //push sales
    @POST("sales")
    @FormUrlEncoded
    Observable<PushSalesResponse> pushSales(
            @Field("uuid") String uuid,
            @Field("user_id") String userId,
            @Field("sale_date") String saleDate,
            @Field("sales") String sales,
            @Field("store_uuid") String storeUuid,
            @Field("created_at") String createdAt,
            @Field("updated_at") String updatedAt
    );









}
