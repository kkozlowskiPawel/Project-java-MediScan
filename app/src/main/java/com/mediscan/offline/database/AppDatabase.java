package com.mediscan.offline.database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.concurrent.Executors;

@Database(entities = {CatalogMedicine.class, UserMedicine.class, ScheduleItem.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AppDao dao();
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "mediscan_offline.db")
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Executors.newSingleThreadExecutor().execute(() -> {
                                        AppDao dao = INSTANCE.dao();

                                        dao.insertCatalog(new CatalogMedicine(
                                                "5909991023652",
                                                "Zoledronic acid Fresenius Kabi",
                                                "4 mg/5 ml",
                                                "Koncentrat do sporządzania roztworu",
                                                "https://rejestrymedyczne.ezdrowie.gov.pl/api/rpl/medicinal-products/1/leaflet",
                                                "Dożylna"
                                        ));

                                        dao.insertCatalog(new CatalogMedicine(
                                                "5909991023683",
                                                "Edelan",
                                                "1 mg/g",
                                                "Krem",
                                                "https://rejestrymedyczne.ezdrowie.gov.pl/api/rpl/medicinal-products/2/leaflet",
                                                "Na skórę"
                                        ));
                                    });
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}