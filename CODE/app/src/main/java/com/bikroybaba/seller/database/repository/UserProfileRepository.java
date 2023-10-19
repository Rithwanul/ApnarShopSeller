package com.bikroybaba.seller.database.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.bikroybaba.seller.database.BBDatabase;
import com.bikroybaba.seller.database.Dao.UserProfileDao;
import com.bikroybaba.seller.database.table.UserProfile;


public class UserProfileRepository {

    private final UserProfileDao userProfileDao;

    public UserProfileRepository(Application application) {
        userProfileDao = BBDatabase.getDatabase(application).user_dao();
    }

    public void updateUserImage(String id, String image) {
        new update_user_Image(userProfileDao, id, image).execute("");
    }

    public LiveData<UserProfile> getUserdata() {
        return userProfileDao.getUserProfile();
    }

    private static class update_user_Image extends AsyncTask<String, Void, Void> {
        private final UserProfileDao mAsyncTaskDao;
        String user_id;
        String image;

        update_user_Image(UserProfileDao dao, String i, String amo) {
            mAsyncTaskDao = dao;
            user_id = i;
            image = amo;
        }

        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.updateUserImage(user_id, image);
            return null;
        }
    }

/*
    public void insertUser(UserProfile userProfile) {
        new InsertAsyncTask(userProfileDao).execute(userProfile);
    }
*/

/*
    public void deleteUser() {
        new DeleteAllAsyncTask(userProfileDao).execute();
    }
*/

/*
    private static class InsertAsyncTask extends AsyncTask<UserProfile, Void, Void> {

        private final UserProfileDao mAsyncTaskDao;

        private InsertAsyncTask(UserProfileDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final UserProfile... params) {
            mAsyncTaskDao.insertUser(params[0]);
            return null;
        }
    }
*/

/*
    private static class DeleteAllAsyncTask extends AsyncTask<String, Void, Void> {

        private final UserProfileDao mAsyncTaskDao;

        private DeleteAllAsyncTask(UserProfileDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(String... params) {
            mAsyncTaskDao.deleteUser();
            return null;
        }
    }
*/

}
