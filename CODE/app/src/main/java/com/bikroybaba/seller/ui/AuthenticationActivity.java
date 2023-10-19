package com.bikroybaba.seller.ui;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.databinding.ActivityAuthenticationBinding;
import com.moktar.zmvvm.base.base.BaseActivity;
import com.moktar.zmvvm.base.base.NoViewModel;

public class AuthenticationActivity extends
        BaseActivity<NoViewModel, ActivityAuthenticationBinding> implements
        NavController.OnDestinationChangedListener {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        showContentView();
        setNoTitle();
        hideActionBar(bindingView.toolbar);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_sign_in, R.id.nav_sign_up)
                .build();
        NavController navController =
                Navigation.findNavController(this, R.id.nav_host_fragment_auth);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        navController.addOnDestinationChangedListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController =
                Navigation.findNavController(this, R.id.nav_host_fragment_auth);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onDestinationChanged(@NonNull NavController controller,
                                     @NonNull NavDestination destination,
                                     @Nullable Bundle arguments) {
    }
}