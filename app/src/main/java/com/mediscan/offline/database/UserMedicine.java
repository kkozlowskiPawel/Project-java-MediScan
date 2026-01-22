package com.mediscan.offline.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "user_medicines")
public class UserMedicine implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String form;
    public String strength;
    public String leafletUrl;
    public int currentQuantity;

    public UserMedicine(String name, String form, String strength, String leafletUrl, int currentQuantity) {
        this.name = name;
        this.form = form;
        this.strength = strength;
        this.leafletUrl = leafletUrl;
        this.currentQuantity = currentQuantity;
    }
}