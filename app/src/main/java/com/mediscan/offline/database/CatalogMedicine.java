package com.mediscan.offline.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "catalog_medicines")
public class CatalogMedicine {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String eanCode;
    public String name;
    public String strength;
    public String form;
    public String leafletUrl;
    public String adminRoute;

    public CatalogMedicine(String eanCode, String name, String strength, String form, String leafletUrl, String adminRoute) {
        this.eanCode = eanCode;
        this.name = name;
        this.strength = strength;
        this.form = form;
        this.leafletUrl = leafletUrl;
        this.adminRoute = adminRoute;
    }
}