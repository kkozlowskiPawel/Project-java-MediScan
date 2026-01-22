package com.mediscan.offline.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface AppDao {
    @Insert
    void insertCatalog(CatalogMedicine medicine);

    @Query("SELECT * FROM catalog_medicines WHERE eanCode = :ean LIMIT 1")
    CatalogMedicine findInCatalog(String ean);

    @Insert
    void insertUserMedicine(UserMedicine medicine);

    @Query("SELECT * FROM user_medicines ORDER BY id DESC")
    List<UserMedicine> getAllUserMedicines();

    @Query("SELECT * FROM user_medicines WHERE name LIKE '%' || :search || '%'")
    List<UserMedicine> searchUserMedicines(String search);

    @Update
    void updateUserMedicine(UserMedicine medicine);

    @Insert
    void insertSchedule(ScheduleItem item);

    @Query("SELECT * FROM schedule_items WHERE date = :date ORDER BY time ASC")
    List<ScheduleItem> getScheduleForDate(String date);
}