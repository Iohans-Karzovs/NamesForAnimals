package com.demo.namesforanimals.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Name.class}, version = 2, exportSchema = false)
public abstract class NamesDatabase extends RoomDatabase {
    private static NamesDatabase maleNames;
    private static NamesDatabase femaleNames;
    private static final String MALE_NAMES_DB_NAME = "maleNames2.db";
    private static final String FEMALE_NAMES_DB_NAME = "femaleNames2.db";
    private static final Object LOCK = new Object();

    public static NamesDatabase getMaleNamesDatabase(Context context) {
        synchronized (LOCK) {
            if (maleNames == null) {
                maleNames = Room.databaseBuilder(context, NamesDatabase.class, MALE_NAMES_DB_NAME)
                        .build();
            }
        }
        return maleNames;

    }

    public static NamesDatabase getFemaleNamesDatabase(Context context) {
        synchronized (LOCK) {
            if (femaleNames == null) {
                femaleNames = Room.databaseBuilder(context, NamesDatabase.class, FEMALE_NAMES_DB_NAME)
                        .build();
            }
        }
        return femaleNames;

    }

    public abstract NamesDao namesDao();
}
