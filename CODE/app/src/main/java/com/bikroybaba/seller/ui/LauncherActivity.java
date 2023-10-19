package com.bikroybaba.seller.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.databinding.ActivityLauncherBinding;
import com.bikroybaba.seller.model.remote.response.GetConfig;
import com.bikroybaba.seller.util.Utility;
import com.bikroybaba.seller.viewmodel.LauncherViewModel;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.moktar.zmvvm.base.base.BaseActivity;

public class LauncherActivity extends BaseActivity<LauncherViewModel, ActivityLauncherBinding> {

    private Utility utility;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        showContentView();
        setNoTitle();
        utility = new Utility(this);
        // initialized firebase
        initFirebase();
        countdownTimer();
    }

    private void countdownTimer() {
        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                if (utility.isNetworkAvailable()) {
                    observer();
                } else {
                    utility.showDialog(getResources().getString(R.string.no_internet_connection));
                }
            }
        }.start();
    }

    private void initFirebase() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        if (TextUtils.isEmpty(utility.getFirebaseToken())) {
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            utility.logger("FIREBASE TOKEN FAILED");
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        utility.setFirebaseToken(token);
                    });
        }
    }

    private void observer() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "001");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "LauncherPage");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "LauncherPage");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        viewModel.getConfigLiveData().observe(this, getConfig -> {
            if (getConfig != null) {
                saveConfig(getConfig);
                getObjectSharedPreference();
            }
        });
    }

    private void getObjectSharedPreference() {
        viewModel.getUserLiveData().observe(this, userProfile -> {
            if (userProfile != null) {
                startActivity(new Intent(LauncherActivity.this, HomeActivity.class));
            } else {
                startActivity(new Intent(LauncherActivity.this, AuthenticationActivity.class));
            }
            finish();
        });
    }

   /* public void autoStart() {
        if (Build.BRAND.equalsIgnoreCase("xiaomi")) {
            Intent intent = new Intent();
            intent.setComponent(new
                    ComponentName("com.miui.securitycenter",
                    "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            startActivity(intent);
        } else if (Build.MANUFACTURER.equalsIgnoreCase("oppo")) {
            try {
                Intent intent = new Intent();
                intent.setClassName("com.coloros.safecenter",
                        "com.coloros.safecenter.permission.startup.StartupAppListActivity");
                startActivity(intent);
            } catch (Exception e) {
                try {
                    Intent intent = new Intent();
                    intent.setClassName("com.oppo.safe",
                            "com.oppo.safe.permission.startup.StartupAppListActivity");
                    startActivity(intent);
                } catch (Exception ex) {
                    try {
                        Intent intent = new Intent();
                        intent.setClassName("com.coloros.safecenter",
                                "com.coloros.safecenter.startupapp.StartupAppListActivity");
                        startActivity(intent);
                    } catch (Exception exx) {

                    }
                }
            }
        }
    }*/

    public void saveConfig(GetConfig getConfig) {
        SharedPreferences preferences = getSharedPreferences("Configuration", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(getConfig);
        editor.putString("config", json);
        editor.apply();
    }
}
