package com.bikroybaba.seller.ui.profile;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.databinding.FragmentProfileBinding;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.ReadExternalStoragePermission;
import com.bikroybaba.seller.util.Utility;
import com.bikroybaba.seller.viewmodel.ProfileViewModel;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfileFragment extends Fragment {

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
    private Utility utility;
    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;
    private TextView textToolHeader;
    private String userId;
    private String language;


    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_profile, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
        utility = new Utility(getActivity());
        language = utility.getLangauge();
        viewModel.getUserProfileLiveData().observe(requireActivity(), userProfile -> {
            userId = userProfile.getUserId();
            binding.profileSellerName.setText(userProfile.getUserName());
            binding.profileShopName.setText(userProfile.getShopName());
            binding.profileShopType.setText(userProfile.getShopType());
            binding.profileAddress.setText(userProfile.getShopAddress());
            binding.profileCity.setText(userProfile.getCity());
            binding.profileArea.setText(userProfile.getArea());
            binding.profileSellerEmailAddress.setText(userProfile.getEmail());
            binding.profileSellerMobileNumber.setText("+" + userProfile.getPhoneNumber());
            binding.profileCreatedAt.setText(userProfile.getShopName() + " joined " + sdf.format(new Date(Long.parseLong(userProfile.getCreatedAt()))));
            Glide.with(requireActivity())
                    .load(userProfile.getUserProfileImage())
                    .placeholder(R.drawable.ic_profile)
                    .into(binding.profileImage);

        });
        changeLanguage(language);

        binding.profileImageLayout.setOnClickListener(v -> {
            if (ReadExternalStoragePermission.isReadStoragePermissionGranted(getActivity())) {
                Intent choose = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(choose, 1);
            }
        });

        viewModel.getProfileImageUpdateResponse().observe(requireActivity(), api_response -> {
            if (api_response.getCode() == 200) {
                utility.hideProgress();
                viewModel.updateUserImage(userId, api_response.getData().getAsString());
            } else {
                utility.showDialog(api_response.getData().getAsString());
            }
        });

    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }

    private void changeLanguage(String language) {
        binding.profileShopNameTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.shop_name_bn) : getString(R.string.shop_name));
        //  binding.profileWebAddressTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.web_bn) : getString(R.string.web));
        binding.profileShopTypeTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.shop_type_bn) : getString(R.string.shop_type));
        binding.profileAddressTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.address_bn) : getString(R.string.address));
        binding.profileCityTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.city_bn) : getString(R.string.city));
        binding.profileAreaTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.area_bn) : getString(R.string.area));
        binding.profilePrivateInfoTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.private_information_bn) : getString(R.string.private_information));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null && resultCode == RESULT_OK) {
            utility.showProgress(false, "");
            final Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media
                        .getBitmap(requireContext().getContentResolver(), imageUri);
                float originalWidth = bitmap.getWidth();
                float originalHeight = bitmap.getHeight();
                if (originalWidth > 1200 && originalHeight >= originalWidth) {
                    float destWidth = 1200;
                    float destHeight = originalHeight / (originalWidth / destWidth);
                    Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, Math.round(destWidth), Math.round(destHeight), false);
                    ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
                    bitmap1.compress(Bitmap.CompressFormat.JPEG, 50, baos1);
                    byte[] byteImage = baos1.toByteArray();
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), byteImage);
                    MultipartBody.Part image = MultipartBody.Part.createFormData("userImage", "userImage", requestFile);
                    viewModel.updateSellerProfileImage(image);
                } else if (originalWidth > 1200 && originalHeight < originalWidth) {
                    float destWidth = 1400;
                    float destHeight = originalHeight / (originalWidth / destWidth);
                    Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, Math.round(destWidth), Math.round(destHeight), false);
                    ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
                    bitmap1.compress(Bitmap.CompressFormat.JPEG, 50, baos1);
                    byte[] byteImage = baos1.toByteArray();
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), byteImage);
                    MultipartBody.Part image = MultipartBody.Part.createFormData("userImage", "userImage", requestFile);
                    viewModel.updateSellerProfileImage(image);
                } else {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos); //decodedFoodBitmap is the bitmap object
                    byte[] byteImage = baos.toByteArray();
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), byteImage);
                    MultipartBody.Part image = MultipartBody.Part.createFormData("userImage", "userImage", requestFile);
                    viewModel.updateSellerProfileImage(image);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        textToolHeader = toolbar.findViewById(R.id.title);
//        textToolHeader.setText(language.equals(KeyWord.BANGLA) ? KeyWord.PROFILE_BN : getActivity().getResources().getString(R.string.profile));
    }
}