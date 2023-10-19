package com.bikroybaba.seller.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.bikroybaba.seller.http.ApiResponse;
import com.bikroybaba.seller.model.remote.request.UpdateNotificationRequest;
import com.bikroybaba.seller.model.remote.response.NotificationResponse;
import com.bikroybaba.seller.repository.NotificationRepo;

import java.util.List;

/**
 * Created by Anik Roy on 03,December,2020
 */
public class NotificationViewModel extends AndroidViewModel {

    private final NotificationRepo notificationRepo;
    public NotificationViewModel(@NonNull Application application) {
        super(application);
        notificationRepo = new NotificationRepo(application);
    }

    public MutableLiveData<List<NotificationResponse>> getNotification(){

        return notificationRepo.getNotification();
    }

    public void updateNotification(UpdateNotificationRequest updateNotificationRequest){

        notificationRepo.updateNotification(updateNotificationRequest);
    }

    public MutableLiveData<ApiResponse> getUpdatedNotificationResponse(){

        return notificationRepo.getUpdatedNotificationResponse();
    }
}
