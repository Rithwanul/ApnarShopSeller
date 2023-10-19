package com.bikroybaba.seller.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.database.table.UserProfile;
import com.bikroybaba.seller.databinding.FragmentSignInBinding;
import com.bikroybaba.seller.model.remote.request.LogIn;
import com.bikroybaba.seller.model.remote.response.Profile;
import com.bikroybaba.seller.ui.HomeActivity;
import com.bikroybaba.seller.ui.webview.WebViewActivity;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;
import com.bikroybaba.seller.viewmodel.SignInViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

public class SignInFragment extends Fragment {

    private FragmentSignInBinding binding;
    private SignInViewModel viewModel;
    private Utility utility;
    private Gson gson;
    private String language;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_sign_in, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        observeViewModel();
        changeLanguage();
    }

    private void initView() {
        binding.signInBtn.setOnClickListener(v -> validation());
        binding.signInRegisterForShop.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_nav_sign_in_to_nav_sign_up)
        );
        binding.signInForgetPasTv.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            View view = getLayoutInflater().inflate(R.layout.dialog_forget_password, null);

            TextInputLayout inputLayout = view.findViewById(R.id.dialog_phone_layout);
            TextInputEditText phone_email = view.findViewById(R.id.forgot_password_phone_emil_et);
            Button forget_password = view.findViewById(R.id.forgot_password);
            TextView title = view.findViewById(R.id.title);

            title.setText(language.equalsIgnoreCase(KeyWord.BANGLA) ? getString(R.string.dialog_title_bn) : getString(R.string.dialog_title));
            forget_password.setText(language.equalsIgnoreCase(KeyWord.BANGLA) ? getString(R.string.forgot_password_bn) : getString(R.string.forgot_password));
            inputLayout.setHint(language.equalsIgnoreCase(KeyWord.BANGLA) ? getString(R.string.phone_number_email_bn) : getString(R.string.phone_number_email));

            builder.setView(view);
            final AlertDialog dialog = builder.create();
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

            dialog.show();
            forget_password.setOnClickListener(v1 -> {
                if (!TextUtils.isEmpty(phone_email.getText())) {
                    LogIn logIn = new LogIn(phone_email.getText().toString().trim());
                    observeForgetPassword(logIn);
                    dialog.dismiss();

                } else {
                    phone_email.setError("Enter valid phone number/email address");
                }
            });

        });
        binding.textTermsOfService.setOnClickListener(v -> {
            navigateToWebView(getString(R.string.url_terms_and_conditions));
        });
        binding.textPrivacyPolicy.setOnClickListener(v -> {
            navigateToWebView(getString(R.string.url_privacy_policy));
        });
    }

    private void changeLanguage() {
        //Change Language -----------Start--------
        binding.signInWelcomeTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.welcome_bn) : getString(R.string.welcome));
        binding.signInAapnarshopTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.to_aapnershop_bn) : getString(R.string.to_aapnershop));
        binding.signInAapnarshopTv.setTextColor(requireActivity().getResources().getColor(R.color.app_yellow));
        binding.signInPhoneLayout.setHint(language.equals(KeyWord.BANGLA) ? getString(R.string.phone_number_email_bn) : getString(R.string.phone_number_email));
        binding.signInPasswordLayout.setHint(language.equals(KeyWord.BANGLA) ? getString(R.string.password_bn) : getString(R.string.password));
        binding.signInForgetPasTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.forget_your_password_bn) : getString(R.string.forget_your_password));
        binding.signInBtn.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.sign_in_bn) : getString(R.string.sign_in));
        binding.signInMessageTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.do_you_have_any_shop_bn) : getString(R.string.do_you_have_any_shop));
        binding.signInRegisterForShop.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.register_for_online_shop_bn) : getString(R.string.register_for_online_shop));
        binding.message.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.by_signing_up_you_agree_with_the_bn) : getString(R.string.by_signing_up_you_agree_with_the));
        binding.textTermsOfService.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.terms_of_service_bn) : getString(R.string.terms_of_service));
        binding.textPrivacyPolicy.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.privacy_policy_bn) : getString(R.string.privacy_policy));
        binding.and.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.and_bn) : getString(R.string.and));
        //------------End------------------
    }

    private void observeViewModel() {
        viewModel = new ViewModelProvider(this).get(SignInViewModel.class);
        requireActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        //Disable night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        gson = new Gson();
        utility = new Utility(getActivity());
        language = utility.getLangauge();
        viewModel.getLoginResponse().observe(requireActivity(), apiResponse -> {
            utility.hideProgress();
            if (apiResponse.getCode() == 200) {
                Profile profile = gson.fromJson(apiResponse.getData(), Profile.class);
                UserProfile userProfile = new UserProfile(
                        profile.getSellerId(), profile.getSellerName(),
                        profile.getSellerPhoneNumber(), profile.getSellerImage(), profile.getEmail(),
                        profile.getShopAddress(), profile.getShopType(), profile.getShopName(),
                        profile.getCity(), profile.getArea(), profile.getCreatedAt(), profile.getShopId());
                // insert user info into local database
                viewModel.insertUser(userProfile);
                utility.setUserId(userProfile.getUserId());
                startActivity(new Intent(requireActivity(), HomeActivity.class));
                requireActivity().finish();
            } else if (apiResponse.getCode() == 201) {
                utility.showDialog(apiResponse.getData().getAsString());
            } else if (apiResponse.getCode() == 202) {
                utility.showDialog(apiResponse.getData().getAsString());
            } else if (apiResponse.getCode() == 203) {
                utility.showDialog(apiResponse.getData().getAsString());
            } else {
                utility.showDialog(apiResponse.getData().getAsString());

            }
        });

        viewModel.getForgetPasswordResponse().observe(requireActivity(), api_response -> {
            if (api_response.getCode() == 200) {
                utility.showDialog(api_response.getData().getAsString());
            } else if (api_response.getCode() == 202) {
                utility.showDialog(api_response.getData().getAsString());
            } else if (api_response.getCode() == 203) {
                utility.showDialog(api_response.getData().getAsString());
            } else if (api_response.getCode() == 333) {
                utility.showDialog(api_response.getData().getAsString());

            } else {
                utility.showDialog(api_response.getData().getAsString());

            }
        });
    }

    private void observeForgetPassword(LogIn logIn) {
        viewModel.forgetPassword(logIn);
    }


    private void validation() {
        try {
            if (TextUtils.isEmpty(binding.signInPhoneEmilEt.getText()) ||
                    !utility.validateMsisdn(binding.signInPhoneEmilEt.getText().toString())) {
                binding.signInPhoneLayout.setError("Enter valid phone number");
            } else if (TextUtils.isEmpty(binding.signInPasswordEt.getText())) {
                binding.signInPasswordLayout.setError("Enter password");
            } else {
                if (utility.isNetworkAvailable()) {
                    utility.showProgress(false, "Please wait..");
                    LogIn logIn = new LogIn(binding.signInPhoneEmilEt.getText().toString(),
                            binding.signInPasswordEt.getText().toString());
                    viewModel.sendLogin(logIn);
                } else {
                    utility.showDialog(getString(R.string.check_internet_connection));
                }
            }
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void navigateToWebView(String url) {
        WebViewActivity.loadUrl(requireActivity(), url, getString(R.string.app_name));
    }
}