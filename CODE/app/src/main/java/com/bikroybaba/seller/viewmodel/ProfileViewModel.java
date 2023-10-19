package com.bikroybaba.seller.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bikroybaba.seller.database.repository.UserProfileRepository;
import com.bikroybaba.seller.database.table.UserProfile;
import com.bikroybaba.seller.http.ApiResponse;
import com.bikroybaba.seller.repository.ProfileRepo;

import okhttp3.MultipartBody;

public class ProfileViewModel extends AndroidViewModel {

    private final ProfileRepo profileRepo;
    private final UserProfileRepository userProfileRepository;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        profileRepo = new ProfileRepo(application);
        userProfileRepository = new UserProfileRepository(application);
    }

    public void updateSellerProfileImage(MultipartBody.Part profileImage){
        profileRepo.updateSellerProfileImage(profileImage);
    }
    public LiveData<ApiResponse> getProfileImageUpdateResponse(){
        return profileRepo.getProfileImageUploadResponse();
    }

    public LiveData<UserProfile> getUserProfileLiveData(){
        return userProfileRepository.getUserdata();
    }
    public void updateUserImage(String id, String image) {
        userProfileRepository.updateUserImage(id, image);
    }
}
