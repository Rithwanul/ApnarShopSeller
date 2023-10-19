package com.bikroybaba.seller.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.bikroybaba.seller.database.BBDatabase;
import com.bikroybaba.seller.database.Dao.UserProfileDao;
import com.bikroybaba.seller.database.table.UserProfile;

public class HomeRepo {

    private final UserProfileDao userProfileDao;

    public HomeRepo(Application application) {
        userProfileDao = BBDatabase.getDatabase(application).user_dao();
    }

    public LiveData<UserProfile> getUserdata() {
        return userProfileDao.getUserProfile();
    }
    public void deleteUser() {
        new DeleteAllAsyncTask(userProfileDao).execute();
    }
    private static class DeleteAllAsyncTask extends AsyncTask<String, Void, Void> {

        private final UserProfileDao mAsyncTaskDao;

        DeleteAllAsyncTask(UserProfileDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(String... params) {
            mAsyncTaskDao.deleteUser();
            return null;
        }
    }
}
