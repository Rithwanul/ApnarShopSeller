package com.bikroybaba.seller.database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.bikroybaba.seller.database.table.UserProfile;

@Dao
public interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserProfile userProfile);

    @Query("DELETE FROM user_profile")
    void deleteUser();

    @Query("SELECT * FROM user_profile")
    LiveData<UserProfile> getUserProfile();

    @Query("UPDATE user_profile SET userProfileImage=:new_image WHERE userid = :user_id")
    void updateUserImage(String user_id, String new_image);
}
