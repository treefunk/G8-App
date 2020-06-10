package com.myoptimind.g8_app.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.myoptimind.g8_app.models.SalesReport;

import java.util.List;

@Dao
public interface SalesReportDao {

    @Query("SELECT * FROM sales_report WHERE user_id = :userId AND datetime LIKE :datetime AND store_uuid = :storeUuid ")
    SalesReport getByDate(String userId, String datetime, String storeUuid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertSalesReport(SalesReport salesReport);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSalesReportList(List<SalesReport> salesReports);

    @Query("SELECT * FROM sales_report WHERE user_id = :userId AND store_uuid = :storeUuid AND has_synced = 0 AND created_at > :datetime OR updated_at > :datetime ORDER BY created_at,updated_at LIMIT 1")
    SalesReport getByDateSync(String userId, String datetime, String storeUuid);

    @Query("SELECT * FROM sales_report WHERE user_id = :userId AND store_uuid = :storeUuid AND has_synced = 0 ORDER BY created_at,updated_at LIMIT 1")
    SalesReport getFirstCreated(String userId, String storeUuid);

    @Delete
    int removeSalesReport(SalesReport salesReport);

}