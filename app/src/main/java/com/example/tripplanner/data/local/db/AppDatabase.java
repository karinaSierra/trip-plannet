package com.example.tripplanner.data.local.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tripplanner.data.local.dao.ItemDao;
import com.example.tripplanner.data.local.dao.TripDao;
import com.example.tripplanner.data.local.dao.UserDao;
import com.example.tripplanner.data.local.entity.ItemEntity;
import com.example.tripplanner.data.local.entity.TripEntity;
import com.example.tripplanner.data.local.entity.UserEntity;

@Database(
        entities = {
                UserEntity.class,
                TripEntity.class,
                ItemEntity.class
        },
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();

    public abstract TripDao tripDao();

    public abstract ItemDao itemDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "tripplanner_db"
                    )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
