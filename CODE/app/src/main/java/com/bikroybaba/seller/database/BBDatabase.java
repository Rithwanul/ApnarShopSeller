package com.bikroybaba.seller.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.bikroybaba.seller.database.Dao.UserProfileDao;
import com.bikroybaba.seller.database.table.UserProfile;


@Database(entities = {UserProfile.class},version = 2,exportSchema = false)
public abstract class BBDatabase extends RoomDatabase {

    public abstract UserProfileDao user_dao();

    private static BBDatabase INSTANCE;

    public static BBDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BBDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), BBDatabase.class, "bb_database")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this codelab.
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
