package com.bikroybaba.seller.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bikroybaba.seller.model.remote.response.Rating;
import com.bikroybaba.seller.repository.RatingRepo;

public class RatingViewModel extends AndroidViewModel {

    private final RatingRepo ratingRepo;

    public RatingViewModel(@NonNull Application application) {
        super(application);
        ratingRepo = new RatingRepo(application);
    }

    public LiveData<Rating> getRating(){
        return ratingRepo.getRating();
    }
}
