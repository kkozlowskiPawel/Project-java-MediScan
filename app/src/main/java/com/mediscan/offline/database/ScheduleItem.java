package com.mediscan.offline.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "schedule_items")
public class ScheduleItem {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String date;
    public String time;
    public String medicineName;
    public int dosage;

    public ScheduleItem(String date, String time, String medicineName, int dosage) {
        this.date = date;
        this.time = time;
        this.medicineName = medicineName;
        this.dosage = dosage;
    }
}