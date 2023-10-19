package com.bikroybaba.seller.ui.auth;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.adapter.GenericAdapter;
import com.bikroybaba.seller.databinding.FragmentSignUpBinding;
import com.bikroybaba.seller.model.entity.Area;
import com.bikroybaba.seller.model.remote.request.City;
import com.bikroybaba.seller.model.remote.request.District;
import com.bikroybaba.seller.model.remote.request.Division;
import com.bikroybaba.seller.model.remote.request.Registration;
import com.bikroybaba.seller.model.remote.response.ShopCategory;
import com.bikroybaba.seller.ui.webview.WebViewActivity;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;
import com.bikroybaba.seller.viewmodel.SignUpViewModel;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class SignUpFragment extends Fragment {

    private FragmentSignUpBinding binding;
    private SignUpViewModel signUpViewModel;
    private final List<ShopCategory> shopCategoryList = new ArrayList<>();
    private final List<Division> divisionList = new ArrayList<>();
    private final List<District> districtList = new ArrayList<>();
    private final List<City> cityList = new ArrayList<>();
    private final List<Area> areaList = new ArrayList<>();
    private Utility utility;
    private String shopType, areaId, language, email = "";

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_sign_up, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        observeViewModel();
        changeLanguage();
    }

    private void changeLanguage() {
        binding.signUpWelcomeTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.welcome_bn) : getString(R.string.welcome));
        binding.signUpAapnarshopTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.to_aapnershop_bn) : getString(R.string.to_aapnershop));
        binding.signUpAapnarshopTv.setTextColor(requireActivity().getResources().getColor(R.color.app_yellow));
        binding.message.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.by_signing_up_you_agree_with_the_bn) : getString(R.string.by_signing_up_you_agree_with_the));
        binding.textTermsOfService.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.terms_of_service_bn) : getString(R.string.terms_of_service));
        binding.textPrivacyPolicy.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.privacy_policy_bn) : getString(R.string.privacy_policy));
        binding.and.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.and_bn) : getString(R.string.and));
        binding.signUpFullNameLayout.setHint(language.equals(KeyWord.BANGLA) ? getString(R.string.full_name_bn) : getString(R.string.full_name));
        binding.signUpPhoneLayout.setHint(language.equals(KeyWord.BANGLA) ? getString(R.string.phone_number_email_bn) : getString(R.string.phone_number_email));
        binding.signUpEmailLayout.setHint(language.equals(KeyWord.BANGLA) ? getString(R.string.email_bn) : getString(R.string.email));
        binding.signUpShopNameLayout.setHint(language.equals(KeyWord.BANGLA) ? getString(R.string.shop_name_bn) : getString(R.string.shop_name));
        binding.signUpShopAddressLayout.setHint(language.equals(KeyWord.BANGLA) ? getString(R.string.shop_address_bn) : getString(R.string.shop_address));
        binding.signUp.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.sign_up_bn) : getString(R.string.sign_up));
    }

    private void initView() {
        //Select Spinner Item
        binding.signUpShopType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    shopType = "";
                } else {
                    binding.signUpShopCategoryError.setVisibility(View.GONE);
                    shopType = String.valueOf(shopCategoryList.get(position).getShopCategoryId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.signUpDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    districtList.clear();
                    districtList.add(new District("0",
                            getString(language.equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.district : R.string.district_bn)));
                    initDistrictAdapter(districtList);
                } else {
                    String divisionId = divisionList.get(position).getDivisionId();
                    Division division = new Division(divisionId);
                    observeDistrictData(division);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.signUpDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    cityList.clear();
                    cityList.add(new City("0",
                            getString(language.equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.city : R.string.city_bn)));
                    initCityAdapter(cityList);
                } else {
                    String districtId = districtList.get(position).getDistrictId();
                    District district = new District();
                    district.setDistrictId(districtId);
                    observeCityData(district);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.signUpCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    areaList.clear();
                    areaList.add(new Area(getString(language.equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.area : R.string.area_bn)));
                    initAreaAdapter(areaList);
                } else {
                    String cityId = cityList.get(position).getCityId();
                    City city = new City();
                    city.setCityId(cityId);
                    observeAreaData(city);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.signUpArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    areaId = "";
                } else {
                    binding.signUpAreaError.setVisibility(View.GONE);
                    areaId = areaList.get(position).getAreaId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.signUpBackBtn.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_nav_sign_up_to_nav_sign_in)
        );
        binding.signUp.setOnClickListener(this::validation);
        binding.textTermsOfService.setOnClickListener(v -> {
            navigateToWebView(getString(R.string.url_terms_and_conditions));
        });
        binding.textPrivacyPolicy.setOnClickListener(v -> {
            navigateToWebView(getString(R.string.url_privacy_policy));
        });
    }

    private void validation(View v) {
        email = binding.signUpEmail.getText().toString().trim();
        if (TextUtils.isEmpty(binding.signUpFullName.getText())) {
            binding.signUpFullNameLayout.setError("Enter your full name");
            binding.signUpFullName.requestFocus();

        } else if (TextUtils.isEmpty(binding.signUpPhone.getText()) ||
                !utility.validateMsisdn(binding.signUpPhone.getText().toString())) {

            binding.signUpPhoneLayout.setError("Enter valid phone number");
            binding.signUpPhone.requestFocus();
        } else if (TextUtils.isEmpty(binding.signUpShopName.getText())) {
            binding.signUpShopNameLayout.setError("Enter your shop name");
            binding.signUpShopName.requestFocus();
        } else if (TextUtils.isEmpty(binding.signUpShopAddress.getText())) {
            binding.signUpShopAddressLayout.setError("Enter your shop address");
            binding.signUpShopAddress.requestFocus();

        } else if (TextUtils.isEmpty(shopType)) {
            binding.signUpShopCategoryError.setVisibility(View.VISIBLE);
            binding.signUpShopCategoryError.setText("Select shop type");
        } else if (TextUtils.isEmpty(areaId)) {
            binding.signUpAreaError.setVisibility(View.VISIBLE);
            binding.signUpAreaError.setText("Select Area");
        } else {
            if (utility.isNetworkAvailable()) {
                Registration registration = new Registration(binding.signUpFullName.getText()
                        .toString().trim(), binding.signUpPhone.getText().toString().trim(),
                        email, shopType, binding.signUpShopName.getText().toString().trim(), areaId,
                        binding.signUpShopAddress.getText().toString().trim());

                Log.d("registration", "validation: " + registration.toString());
                sendRegistration(registration, v);
            } else {
                utility.showDialog(getString(R.string.check_internet_connection));
            }
        }
    }

    private void sendRegistration(Registration registration, View v) {
        utility.showProgress(true, "");
        signUpViewModel.sendRegistration(registration);
    }

    // Getting shop category list & Division list
    private void observeViewModel() {
        signUpViewModel = new ViewModelProvider(requireActivity()).get(SignUpViewModel.class);
        utility = new Utility(getActivity());
        language = utility.getLangauge();
        // Shop Category
        signUpViewModel.getShopCategoryList().observe(getViewLifecycleOwner(), shopCategories -> {
            if (shopCategories.size() > 0) {
                shopCategoryList.clear();
                shopCategoryList.add(new ShopCategory(getString(language.equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.shop_type : R.string.shop_type_bn)));
                shopCategoryList.addAll(shopCategories);
                binding.signUpShopType.setAdapter(new GenericAdapter<ShopCategory>(getActivity(), shopCategoryList) {
                    @Override
                    public View getCustomView(int position, View view, ViewGroup parent) {

                        ShopCategory model = getItem(position);
                        View spinnerRow = LayoutInflater.from(parent.getContext())
                                .inflate(com.bikroybaba.seller.R.layout.custom_spinner_item, parent, false);
                        TextView label = spinnerRow.findViewById(com.bikroybaba.seller.R.id.custom_spinner);
                        assert model != null;
                        label.setText(model.getShopCategoryName());
                        if (position == 0) {
                            label.setTextColor(requireActivity().getResources().getColor(R.color.app_black2));
                            label.setTypeface(label.getTypeface(), Typeface.BOLD);
                        }
                        return spinnerRow;
                    }
                });
            }
        });
        // Division List
        signUpViewModel.getDivisionList().observe(getViewLifecycleOwner(), divisions -> {

            if (divisions.size() > 0) {
                divisionList.clear();
                Division division = new Division();
                division.setDivisionName(getString(language.equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.division : R.string.division_bn));
                divisionList.add(division);
                divisionList.addAll(divisions);
                binding.signUpDivision.setAdapter(new GenericAdapter<Division>(getActivity(), divisionList) {
                    @Override
                    public View getCustomView(int position, View view, ViewGroup parent) {
                        Division model = getItem(position);
                        View spinnerRow = LayoutInflater.from(parent.getContext())
                                .inflate(com.bikroybaba.seller.R.layout.custom_spinner_item, parent, false);
                        TextView label = spinnerRow.findViewById(com.bikroybaba.seller.R.id.custom_spinner);
                        assert model != null;
                        label.setText(model.getDivisionName());
                        if (position == 0) {
                            label.setTextColor(requireActivity().getResources().getColor(R.color.app_black2));
                            label.setTypeface(label.getTypeface(), Typeface.BOLD);
                        }
                        return spinnerRow;
                    }
                });

            }

        });
        signUpViewModel.getRegistrationResponse().observe(getViewLifecycleOwner(), api_response -> {
            utility.hideProgress();
            if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                if (api_response.getCode() == 200) {
                    showDialog(api_response.getData().getAsString());
                } else {
                    utility.showDialog(api_response.getData().getAsString());
                }
            }
        });
    }

    // Getting District List
    private void observeDistrictData(Division division) {
        signUpViewModel.getDistrictList(division).observe(getViewLifecycleOwner(), districts -> {
            districtList.clear();
            districtList.add(new District("0", "District"));
            districtList.addAll(districts);
            initDistrictAdapter(districtList);

        });
    }

    //Getting City List
    private void observeCityData(District district) {
        signUpViewModel.getCityList(district).observe(getViewLifecycleOwner(), cities -> {
            cityList.clear();
            cityList.add(new City("0", "City"));
            cityList.addAll(cities);
            initCityAdapter(cityList);
        });
    }

    //Getting Area List
    private void observeAreaData(City city) {
        signUpViewModel.getAreaList(city).observe(getViewLifecycleOwner(), areas -> {
            areaList.clear();
            areaList.add(new Area("Area"));
            areaList.addAll(areas);
            initAreaAdapter(areaList);
        });
    }

    //Initialize Adapter
    private void initAreaAdapter(List<Area> areaList) {
        binding.signUpArea.setAdapter(new GenericAdapter<Area>(getActivity(), areaList) {
            @Override
            public View getCustomView(int position, View view, ViewGroup parent) {
                Area model = getItem(position);
                View spinnerRow = LayoutInflater.from(parent.getContext())
                        .inflate(com.bikroybaba.seller.R.layout.custom_spinner_item, parent, false);
                TextView label = spinnerRow.findViewById(com.bikroybaba.seller.R.id.custom_spinner);
                assert model != null;
                label.setText(model.getAreaName());
                if (position == 0) {
                    label.setTextColor(requireActivity().getResources().getColor(R.color.app_black2));
                    label.setTypeface(label.getTypeface(), Typeface.BOLD);
                }
                return spinnerRow;
            }
        });

    }

    private void initCityAdapter(List<City> cityList) {
        binding.signUpCity.setAdapter(new GenericAdapter<City>(requireActivity(), cityList) {
            @Override
            public View getCustomView(int position, View view, ViewGroup parent) {
                City model = getItem(position);
                View spinnerRow = LayoutInflater.from(parent.getContext())
                        .inflate(com.bikroybaba.seller.R.layout.custom_spinner_item, parent, false);
                TextView label = spinnerRow.findViewById(com.bikroybaba.seller.R.id.custom_spinner);

                assert model != null;
                label.setText(model.getCityName());
                if (position == 0) {
                    label.setTextColor(requireActivity().getResources().getColor(R.color.app_black2));
                    label.setTypeface(label.getTypeface(), Typeface.BOLD);
                }
                return spinnerRow;
            }
        });

    }

    private void initDistrictAdapter(List<District> districts) {
        binding.signUpDistrict.setAdapter(new GenericAdapter<District>(getActivity(), districts) {
            @Override
            public View getCustomView(int position, View view, ViewGroup parent) {
                District model = getItem(position);
                View spinnerRow = LayoutInflater.from(parent.getContext())
                        .inflate(com.bikroybaba.seller.R.layout.custom_spinner_item, parent, false);
                TextView label = spinnerRow.findViewById(com.bikroybaba.seller.R.id.custom_spinner);
                assert model != null;
                label.setText(model.getDistrictName());
                if (position == 0) {
                    label.setTextColor(requireActivity().getResources().getColor(R.color.app_black2));
                    label.setTypeface(label.getTypeface(), Typeface.BOLD);
                }
                return spinnerRow;
            }
        });
    }

    public void showDialog(String msg) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_successfully_add_shop);
        MaterialTextView title = dialog.findViewById(R.id.dialog_title);
        title.setText(msg);
        Button yes = dialog.findViewById(R.id.dialog_yes);
        yes.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private void navigateToWebView(String url) {
        WebViewActivity.loadUrl(requireActivity(), url, getString(R.string.app_name));
    }
}