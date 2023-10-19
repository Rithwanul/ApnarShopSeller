package com.bikroybaba.seller.ui.offer;

import static com.bikroybaba.seller.ui.HomeActivity.navController;
import static com.bikroybaba.seller.ui.order.DateFragment.fromDateStartHour;
import static com.bikroybaba.seller.ui.order.DateFragment.toDateLastHour;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.adapter.GenericAdapter;
import com.bikroybaba.seller.databinding.FragmentCreateOfferBinding;
import com.bikroybaba.seller.model.remote.request.OfferCategoryRequest;
import com.bikroybaba.seller.model.remote.request.OfferCreate;
import com.bikroybaba.seller.model.remote.request.OfferDetailsRequest;
import com.bikroybaba.seller.model.remote.request.OfferListRequest;
import com.bikroybaba.seller.model.remote.request.OfferStatus;
import com.bikroybaba.seller.model.remote.request.OfferUpdate;
import com.bikroybaba.seller.model.remote.response.OfferCategory;
import com.bikroybaba.seller.model.remote.response.OfferListResponse;
import com.bikroybaba.seller.model.remote.response.OfferTypeResponse;
import com.bikroybaba.seller.util.CalendarView;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;
import com.bikroybaba.seller.viewmodel.OfferViewModel;
import com.google.android.material.textview.MaterialTextView;
import com.moktar.zmvvm.base.base.BaseFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class CreateOfferFragment extends
        BaseFragment<OfferViewModel, FragmentCreateOfferBinding> {


    private SwitchCompat offerSwitch;
    private LinearLayoutCompat switchLayout;
    private SimpleDateFormat yearFormat;
    private final List<OfferListResponse> offerList = new ArrayList<>();
    private final List<OfferCategory> offerCategoryList = new ArrayList<>();
    private Utility utility;
    private String offerId = "", offerListId, offerCategoryId, offerCategoryName, offerTypeId,
            selectCreateOfferId, amount, offerCategory = "";
    private String offerName = "", amountOfDiscount = "", minimumBuy = "", maxDiscount = "",
            generatedCode = "", offerPeriodFrom = "", offErPeriodTo = "";
    private String message, discount;
    private Date date, dateTo;
    private long fromDate, toDate;
    private String shopId;


    @Override
    public int setContent() {
        return  R.layout.fragment_create_offer;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showContentView();
        utility = new Utility(getActivity());
        initView();
        loadData();
    }

    @Override
    protected void loadData() {
        viewModel.getUserLivedata().observe(requireActivity(), userProfile -> {
            if (userProfile != null) {
                shopId = userProfile.getShopId();
            }
        });
        viewModel.getOfferTypeList().observe(requireActivity(), this::showOfferType);
        observeOfferDetails();
        onClickEvents();
        showAPIResponse();
    }

    private void initView() {
        yearFormat = new SimpleDateFormat("dd/MM/yyyy");
        utility.showProgress(false, "");
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        MaterialTextView textToolHeader = toolbar.findViewById(R.id.title);
        switchLayout = toolbar.findViewById(R.id.switch_layout2);
        MaterialTextView switchTextView = toolbar.findViewById(R.id.switch_textView2);
        offerSwitch = toolbar.findViewById(R.id.offer_switch);
        textToolHeader.setText(requireActivity().getResources().getString(R.string.create_offer));
        switchLayout.setVisibility(View.VISIBLE);
        switchTextView.setText(getString(R.string.start_stop));
        offerSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                OfferStatus offerStatus;
                if (isChecked) {
                    offerStatus = new OfferStatus(offerId, KeyWord.ACTIVE);
                } else {
                    offerStatus = new OfferStatus(offerId, KeyWord.INACTIVE);
                }
                viewModel.updateOfferStatus(offerStatus);
            }
        });
    }

    private void showAPIResponse() {
        viewModel.getUpdateOfferResponse().observe(requireActivity(), api_response -> {
            if (api_response != null) {
                if (api_response.getCode() == 200) {
                    CreateOfferFragmentDirections.ActionNavCreateOfferToNavOfferCreatedSuccessfully action =
                            CreateOfferFragmentDirections.actionNavCreateOfferToNavOfferCreatedSuccessfully();
                    action.setOfferDate(bindingView.createOfferOfferPeriodFrom.getText().toString());
                    if (discount.equals("")) {
                        action.setOfferDiscount(message);
                    } else {
                        if (offerCategory.equalsIgnoreCase(KeyWord.FIXED)) {
                            action.setOfferDiscount(message + " ৳" + discount);
                        } else if (offerCategory.equalsIgnoreCase(KeyWord.PERCENTILE)) {
                            action.setOfferDiscount(message + discount + " %");
                        } else if (offerCategory.equalsIgnoreCase(KeyWord.BUNDLE)) {
                            action.setOfferDiscount("Bundle discount");
                        }
                    }
                    action.setOfferToDate(bindingView.createOfferOfferPeriodTo.getText().toString());
                    action.setMessage("Offer Updated Successfully");
                    navController.navigate(action);

                } else {
                    utility.showDialog(api_response.getData().getAsString());
                }
            } else {
                utility.showDialog(getString(R.string.duplicate_voucher_code));
            }

        });
        viewModel.createOfferResponse().observe(requireActivity(), api_response -> {
            if (api_response != null) {
                if (api_response.getCode() == 200) {
                    CreateOfferFragmentDirections.ActionNavCreateOfferToNavOfferCreatedSuccessfully action =
                            CreateOfferFragmentDirections.actionNavCreateOfferToNavOfferCreatedSuccessfully();
                    action.setOfferDate(bindingView.createOfferOfferPeriodFrom.getText().toString());
                    if (amountOfDiscount.equals("")) {
                        action.setOfferDiscount(message);
                    } else {
                        if (offerCategory.equalsIgnoreCase(KeyWord.FIXED)) {
                            action.setOfferDiscount(message + " ৳" + discount);
                        } else if (offerCategory.equalsIgnoreCase(KeyWord.PERCENTILE)) {
                            action.setOfferDiscount(message + discount + " %");
                        } else if (offerCategory.equalsIgnoreCase(KeyWord.BUNDLE)) {
                            action.setOfferDiscount("Bundle discount");
                        }
                    }
                    action.setOfferToDate(bindingView.createOfferOfferPeriodTo.getText().toString());
                    action.setMessage(getString(R.string.offer_create_successfully));
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                    navController.navigate(action);

                } else {
                    utility.showDialog(api_response.getData().getAsString());
                }
            } else {
                utility.showDialog(getString(R.string.duplicate_voucher_code));

            }

        });
        viewModel.statusUpdateResponse().observe(requireActivity(), api_response -> {
            if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                if (api_response.getCode() == 200) {
                    if (api_response.getData().getAsString().equalsIgnoreCase(KeyWord.DELETE)) {
                        navController.navigate(R.id.nav_dashboard);
                        navController.popBackStack(R.id.nav_create_offer, true);
                        utility.showDialog(getString(R.string.offer_delete_successfully));
                    }
                }
            }
        });
    }

    private void observeOfferDetails() {
        viewModel.getOfferDetailsResponse().observe(requireActivity(), offerDetailsResponse -> {
            utility.hideProgress();
            fromDate = Long.parseLong(offerDetailsResponse.getStartTime());
            toDate = Long.parseLong(offerDetailsResponse.getEndTime());
            bindingView.createOfferAmountOfDiscount.setText(offerDetailsResponse.getAmount());
            bindingView.createOfferOfferName.setText(offerDetailsResponse.getOfferName());
            bindingView.createOfferMinimumBuy.setText(offerDetailsResponse.getMinAmount());
            bindingView.createOfferMaxDiscountEt.setText(offerDetailsResponse.getMaxAmount());
            bindingView.createOfferOfferNameMinimumBuy.setText(offerDetailsResponse.getOfferName());
            bindingView.createOfferGenerateOfferEditText.setText(offerDetailsResponse.getGeneratedCode());
            bindingView.createOfferBuy.setText(offerDetailsResponse.getMinAmount());
            bindingView.createOfferGet.setText(offerDetailsResponse.getAmount());
            bindingView.createOfferBuyGetOfferName.setText(offerDetailsResponse.getOfferName());
            bindingView.createOfferOfferPeriodFrom
                    .setText(convertDate(offerDetailsResponse.getStartTime(), "dd/MM/yyyy"));
            bindingView.createOfferOfferPeriodTo
                    .setText(convertDate(offerDetailsResponse.getEndTime(), "dd/MM/yyyy"));
            for (int i = 0; i < offerCategoryList.size(); i++) {
                if (offerDetailsResponse.getOfferCategory().getOfferCategoryId()
                        .equals(offerCategoryList.get(i).getOfferCategoryId())) {
                    bindingView.createOfferOfferDropdown.setSelection(i);
                }
            }
            if (offerDetailsResponse.getStatus().equals(KeyWord.ACTIVE)) {
                offerSwitch.setChecked(true);
            } else if (offerDetailsResponse.getStatus().equals(KeyWord.INACTIVE)) {
                offerSwitch.setChecked(false);
            }
            amount = offerDetailsResponse.getAmount();
            offerId = offerDetailsResponse.getId();
            offerCategoryName = offerDetailsResponse.getOfferCategory().getOfferCategoryTitle();
            offerCategory = offerDetailsResponse.getOfferCategory().getOfferCategoryTitle();
        });
    }

    //All kind of click events
    private void onClickEvents() {
        bindingView.createOfferOfferPeriodFrom.setOnClickListener(
                v -> openCalenderDialog(bindingView.createOfferOfferPeriodFrom, "from"));
        bindingView.createOfferOfferPeriodTo.setOnClickListener(
                v -> openCalenderDialog(bindingView.createOfferOfferPeriodTo, "to"));
        bindingView.createOfferSave.setOnClickListener(v -> validation(offerTypeId));
        bindingView.createOfferGenerateOfferIv.setOnClickListener(
                v -> bindingView.createOfferGenerateOfferEditText.setText(getSaltString()));
        bindingView.createOfferIncludeExcludeProducts.setOnClickListener(v -> {
            if (offerId.equals("0") || offerId.equals("")) {
                utility.showDialog(getString(R.string.title_exclude_include_product));
            } else {
                CreateOfferFragmentDirections.ActionNavCreateOfferToNavIncludeExclude action =
                        CreateOfferFragmentDirections.actionNavCreateOfferToNavIncludeExclude();
                action.setOfferId(offerId);
                if (offerCategory.equalsIgnoreCase(KeyWord.FIXED)) {
                    action.setMessage(offerCategoryName + " Discount " + " ৳" + amount);

                } else if (offerCategory.equalsIgnoreCase(KeyWord.PERCENTILE)) {
                    action.setMessage(offerCategoryName + " Discount " + amount + " % ");

                } else if (offerCategory.equalsIgnoreCase(KeyWord.BUNDLE)) {
                    action.setMessage(offerCategoryName + " Discount ");
                }
                navController.navigate(action);
            }
        });
        bindingView.createOfferDeleteTv.setOnClickListener(v -> {
            OfferStatus offerStatus = new OfferStatus(offerId, KeyWord.DELETE);
            viewModel.updateOfferStatus(offerStatus);
        });
    }

    private void validation(String offerTypeId) {
        switch (offerTypeId) {
            case "1":
                offerName = bindingView.createOfferOfferName.getText().toString();
                amountOfDiscount = bindingView.createOfferAmountOfDiscount.getText().toString();
                minimumBuy = bindingView.createOfferMinimumBuy.getText().toString();
                if (bindingView.createOfferMaxDiscountEt.getText().toString().equals("")) {
                    maxDiscount = "0";
                } else {
                    maxDiscount = bindingView.createOfferMaxDiscountEt.getText().toString();
                }
                generatedCode = bindingView.createOfferGenerateOfferEditText.getText().toString();
                offerPeriodFrom = bindingView.createOfferOfferPeriodFrom.getText().toString();
                offErPeriodTo = bindingView.createOfferOfferPeriodTo.getText().toString();
                if (TextUtils.isEmpty(offerTypeId)) {
                    showErrorStyle(bindingView.createOfferOfferTypeTv);
                } else if (TextUtils.isEmpty(offerCategoryId)) {
                    showErrorStyle(bindingView.createOfferOfferTv);
                } else if (TextUtils.isEmpty(amountOfDiscount)) {
                    showErrorStyle(bindingView.createOfferAmountOfDiscountTv, bindingView.createOfferAmountOfDiscount);
                } else if (TextUtils.isEmpty(offerName)) {
                    showErrorStyle(bindingView.createOfferOfferNameTv, bindingView.createOfferOfferName);
                } else if (TextUtils.isEmpty(minimumBuy)) {
                    showErrorStyle(bindingView.createOfferMinimumBuyTv, bindingView.createOfferMinimumBuy);
                } else if (TextUtils.isEmpty(maxDiscount)) {
                    showErrorStyle(bindingView.createOfferMaxDiscountTv, bindingView.createOfferMaxDiscountEt);
                } else if (TextUtils.isEmpty(generatedCode)) {
                    showErrorStyle(bindingView.createOfferGenerateOfferCodeTv,
                            bindingView.createOfferGenerateOfferEditText);
                } else if (TextUtils.isEmpty(offerPeriodFrom)) {
                    showErrorStyle(bindingView.createOfferOfferPeriodTv);
                } else if (TextUtils.isEmpty(offErPeriodTo)) {
                    showErrorStyle(bindingView.createOfferOfferPeriodTv);
                } else {
                    // Here "0" means create own offer
                    if (utility.isNetworkAvailable()) {
                        if (offerListId.equals("0")) {
                            OfferCreate offerCreate =
                                    new OfferCreate(offerTypeId, offerCategoryId, offerName,
                                            amountOfDiscount, minimumBuy, maxDiscount, generatedCode,
                                            convertStringToMilliSecondForFromDate(offerPeriodFrom),
                                            convertStringToMilliSecondForToDate(offErPeriodTo));
                            createOffer(offerCreate, "Upon total bill ", amountOfDiscount);
                        } else {
                            OfferUpdate offerUpdate =
                                    new OfferUpdate(offerId, offerTypeId, offerCategoryId, offerName,
                                            amountOfDiscount, minimumBuy, maxDiscount, generatedCode,
                                            convertStringToMilliSecondForFromDate(offerPeriodFrom),
                                            convertStringToMilliSecondForToDate(offErPeriodTo));
                            updateOffer(offerUpdate, "Upon total bill ", amountOfDiscount);
                        }
                    } else {
                        utility.showDialog(getString(R.string.check_internet_connection));
                    }
                }
                break;
            case "2":
                offerCategoryId = "";
                amountOfDiscount = "";
                maxDiscount = "";
                generatedCode = "";
                minimumBuy = bindingView.createOfferMinimumBuy.getText().toString();
                offerName = bindingView.createOfferOfferNameMinimumBuy.getText().toString();
                offerPeriodFrom = bindingView.createOfferOfferPeriodFrom.getText().toString();
                offErPeriodTo = bindingView.createOfferOfferPeriodTo.getText().toString();
                if (TextUtils.isEmpty(minimumBuy)) {
                    showErrorStyle(bindingView.createOfferMinimumBuyTv, bindingView.createOfferMinimumBuy);
                } else if (TextUtils.isEmpty(offerName)) {
                    showErrorStyle(bindingView.createOfferOfferNameTv, bindingView.createOfferOfferName);
                } else if (TextUtils.isEmpty(offerPeriodFrom)) {
                    showErrorStyle(bindingView.createOfferOfferPeriodTv);
                } else if (TextUtils.isEmpty(offErPeriodTo)) {
                    showErrorStyle(bindingView.createOfferOfferPeriodTv);
                } else {
                    // Here "0" means create own offer
                    if (utility.isNetworkAvailable()) {
                        if (selectCreateOfferId.equals("0")) {
                            OfferCreate offerCreate =
                                    new OfferCreate(offerTypeId, "1", offerName,
                                            amountOfDiscount, minimumBuy, maxDiscount, generatedCode,
                                            convertStringToMilliSecondForFromDate(offerPeriodFrom),
                                            convertStringToMilliSecondForToDate(offErPeriodTo));
                            createOffer(offerCreate, "Free delivery ", "");
                        } else {
                            OfferUpdate offerUpdate =
                                    new OfferUpdate(offerId, offerTypeId, "1", offerName,
                                            amountOfDiscount, minimumBuy, maxDiscount, generatedCode,
                                            convertStringToMilliSecondForFromDate(offerPeriodFrom),
                                            convertStringToMilliSecondForToDate(offErPeriodTo));
                            updateOffer(offerUpdate, "Free delivery ", "");
                        }
                    } else {
                        utility.showDialog(getString(R.string.check_internet_connection));
                    }
                }
                break;
            case "3":
                minimumBuy = bindingView.createOfferBuy.getText().toString();
                amountOfDiscount = bindingView.createOfferGet.getText().toString();
                offerName = bindingView.createOfferBuyGetOfferName.getText().toString();
                offerPeriodFrom = bindingView.createOfferOfferPeriodFrom.getText().toString();
                offErPeriodTo = bindingView.createOfferOfferPeriodTo.getText().toString();
                if (offerCategoryId.equals("3")) {
                    if (TextUtils.isEmpty(minimumBuy)) {
                        showErrorStyle(bindingView.createOfferBuyTv, bindingView.createOfferBuy);
                    } else if (TextUtils.isEmpty(offerName)) {
                        showErrorStyle(bindingView.createOfferGetTv, bindingView.createOfferGet);
                    } else if (TextUtils.isEmpty(offerName)) {
                        showErrorStyle(bindingView.createOfferBuyGetOfferNameTv, bindingView.createOfferBuyGetOfferName);
                    } else if (TextUtils.isEmpty(offerPeriodFrom)) {
                        showErrorStyle(bindingView.createOfferOfferPeriodTv);
                    } else if (TextUtils.isEmpty(offErPeriodTo)) {
                        showErrorStyle(bindingView.createOfferOfferPeriodTv);
                    } else {
                        // Here "0" means create own offer
                        if (utility.isNetworkAvailable()) {
                            if (selectCreateOfferId.equals("0")) {
                                OfferCreate offerCreate =
                                        new OfferCreate(offerTypeId, offerCategoryId, offerName,
                                                amountOfDiscount, minimumBuy, maxDiscount, generatedCode,
                                                convertStringToMilliSecondForFromDate(offerPeriodFrom),
                                                convertStringToMilliSecondForToDate(offErPeriodTo));
                                createOffer(offerCreate, "Product based offer ", "");
                            } else {
                                OfferUpdate offerUpdate =
                                        new OfferUpdate(offerId, offerTypeId, offerCategoryId,
                                                offerName, amountOfDiscount, minimumBuy,
                                                maxDiscount, generatedCode,
                                                convertStringToMilliSecondForFromDate(offerPeriodFrom),
                                                convertStringToMilliSecondForToDate(offErPeriodTo));
                                updateOffer(offerUpdate, "Product based offer ", "");
                            }
                        } else {
                            utility.showDialog(getString(R.string.check_internet_connection));
                        }
                    }
                } else {
                    minimumBuy = "0";
                    amountOfDiscount = bindingView.createOfferAmountOfDiscount.getText().toString();
                    offerName = bindingView.createOfferOfferName.getText().toString();
                    offerPeriodFrom = bindingView.createOfferOfferPeriodFrom.getText().toString();
                    offErPeriodTo = bindingView.createOfferOfferPeriodTo.getText().toString();
                    if (TextUtils.isEmpty(amountOfDiscount)) {
                        showErrorStyle(bindingView.createOfferAmountOfDiscountTv, bindingView.createOfferAmountOfDiscount);
                    } else if (TextUtils.isEmpty(offerName)) {
                        showErrorStyle(bindingView.createOfferOfferNameTv, bindingView.createOfferOfferName);
                    } else if (TextUtils.isEmpty(offerPeriodFrom)) {
                        showErrorStyle(bindingView.createOfferOfferPeriodTv);
                    } else if (TextUtils.isEmpty(offErPeriodTo)) {
                        showErrorStyle(bindingView.createOfferOfferPeriodTv);
                    } else {
                        // Here "0" means create own offer
                        if (utility.isNetworkAvailable()) {
                            if (selectCreateOfferId.equals("0")) {
                                OfferCreate offerCreate =
                                        new OfferCreate(offerTypeId, offerCategoryId, offerName,
                                                amountOfDiscount, minimumBuy, maxDiscount, generatedCode,
                                                convertStringToMilliSecondForFromDate(offerPeriodFrom),
                                                convertStringToMilliSecondForToDate(offErPeriodTo));
                                createOffer(offerCreate, "Product based offer", amountOfDiscount);
                            } else {
                                OfferUpdate offerUpdate =
                                        new OfferUpdate(offerId, offerTypeId, offerCategoryId,
                                                offerName, amountOfDiscount, minimumBuy, maxDiscount,
                                                generatedCode, convertStringToMilliSecondForFromDate(offerPeriodFrom),
                                                convertStringToMilliSecondForToDate(offErPeriodTo));
                                updateOffer(offerUpdate, "Product based offer", amountOfDiscount);
                            }
                        } else {
                            utility.showDialog(getString(R.string.check_internet_connection));
                        }
                    }
                }
                break;
        }
    }

    private void updateOffer(OfferUpdate offerUpdate, String message, String discount) {
        viewModel.updateOffer(offerUpdate);
        this.message = message;
        this.discount = discount;
    }

    private void createOffer(OfferCreate offerCreate, String message, String amountOfDiscount) {
        viewModel.createOffer(offerCreate);
        this.message = message;
        discount = amountOfDiscount;
    }

    private void showErrorStyle(TextView textView) {
        textView.setTextColor(requireActivity().getResources().getColor(R.color.red_700));
    }

    private void showErrorStyle(TextView textView, EditText editText) {
        textView.setTextColor(requireActivity().getResources().getColor(R.color.red_700));
        editText.requestFocus();
    }

    // All kind of spinner operation
    private void showOfferType(List<OfferTypeResponse> offerTypeResponses) {
        GenericAdapter<OfferTypeResponse> adapter =
                new GenericAdapter<OfferTypeResponse>(getActivity(), offerTypeResponses) {
            @Override
            public View getCustomView(int position, View view, ViewGroup parent) {
                OfferTypeResponse model = getItem(position);
                View spinnerRow = LayoutInflater.from(parent.getContext())
                        .inflate(com.bikroybaba.seller.R.layout.custom_spinner_item, parent, false);
                TextView label = spinnerRow.findViewById(com.bikroybaba.seller.R.id.custom_spinner);
                if (model != null) {
                    label.setText(utility.firstTextCapitalize(model.getOfferTypeTitle()));
                }
                return spinnerRow;
            }
        };
        bindingView.createOfferOfferTypeDropdown.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        bindingView.createOfferOfferTypeDropdown
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                offerTypeId = offerTypeResponses.get(position).getOfferTypeId();
                OfferCategoryRequest offerCategoryRequest = new OfferCategoryRequest(offerTypeId);
                date = null;
                dateTo = null;
                if (position == 0) {
                    viewModel.getOfferCategory(offerCategoryRequest).observe(requireActivity(),
                            offerCategories -> {
                        showOfferCategory(offerCategories);
                    });
                    OfferListRequest offerListRequest =
                            new OfferListRequest(KeyWord.TIMELINE_ALL,
                                    offerTypeResponses.get(position).getOfferTypeId(),
                                    "", shopId);
                    viewModel.getOfferList(offerListRequest).observe(requireActivity(),
                            offerListResponses -> {
                        utility.hideProgress();
                        offerList.clear();
                        OfferListResponse offerListResponse =
                                new OfferListResponse("0", "Create your own offer");
                        offerList.add(offerListResponse);
                        offerList.addAll(offerListResponses);
                        showOfferList(offerList);
                    });
                    bindingView.createOfferSelectCreateOfferLayout.setVisibility(View.GONE);
                    bindingView.createOfferMinimumBuyOfferAmountLayout.setVisibility(View.VISIBLE);
                    bindingView.createOfferGenerateOfferCodeLayout.setVisibility(View.VISIBLE);
                    bindingView.createOfferAmountOfDiscountLayout.setVisibility(View.VISIBLE);
                    bindingView.createOfferOfferLayout.setVisibility(View.VISIBLE);
                    bindingView.createOfferOfferList.setVisibility(View.VISIBLE);
                    bindingView.createOfferIncludeExcludeProducts.setVisibility(View.GONE);
                    bindingView.createOfferOfferAmountLayout.setVisibility(View.GONE);
                    bindingView.createOfferMaxDiscountLayout.setVisibility(View.VISIBLE);
                    bindingView.createOfferBuyGetLayout.setVisibility(View.GONE);
                    bindingView.createOfferBuyGetOfferNameLayout.setVisibility(View.GONE);
                } else if (position == 1) {
                    OfferListRequest offerListRequest =
                            new OfferListRequest(KeyWord.TIMELINE_ALL,
                                    offerTypeResponses.get(position).getOfferTypeId(),
                                    "", shopId);
                    viewModel.getOfferList(offerListRequest).observe(requireActivity(),
                            offerListResponses -> {
                        utility.hideProgress();
                        offerList.clear();
                        OfferListResponse offerListResponse =
                                new OfferListResponse("0", "Create your own offer");
                        offerList.add(offerListResponse);
                        offerList.addAll(offerListResponses);
                        showOfferSelectCreate(offerList, "fromFreeDelivery");
                    });
                    bindingView.createOfferSelectCreateOfferLayout.setVisibility(View.VISIBLE);
                    bindingView.createOfferIncludeExcludeProducts.setVisibility(View.GONE);
                    bindingView.createOfferMinimumBuyOfferAmountLayout.setVisibility(View.VISIBLE);
                    bindingView.createOfferGenerateOfferCodeLayout.setVisibility(View.GONE);
                    bindingView.createOfferAmountOfDiscountLayout.setVisibility(View.GONE);
                    bindingView.createOfferOfferLayout.setVisibility(View.GONE);
                    bindingView.createOfferOfferList.setVisibility(View.GONE);
                    bindingView.createOfferMaxDiscountLayout.setVisibility(View.GONE);
                    bindingView.createOfferOfferAmountLayout.setVisibility(View.VISIBLE);
                    bindingView.createOfferBuyGetLayout.setVisibility(View.GONE);
                    bindingView.createOfferBuyGetOfferNameLayout.setVisibility(View.GONE);
                } else if (position == 2) {
                    viewModel.getOfferCategory(offerCategoryRequest).observe(requireActivity(),
                            offerCategories -> {
                        showOfferCategory(offerCategories);
                    });
                    OfferListRequest offerListRequest =
                            new OfferListRequest(KeyWord.TIMELINE_ALL,
                                    offerTypeResponses.get(position).getOfferTypeId(), "",
                                    shopId);
                    viewModel.getOfferList(offerListRequest).observe(requireActivity(),
                            offerListResponses -> {
                        offerList.clear();
                        OfferListResponse offerListResponse = new OfferListResponse("0", "Create your own offer");
                        offerList.add(offerListResponse);
                        offerList.addAll(offerListResponses);
                        showOfferSelectCreate(offerList, "fromProductBase");
                    });
                    bindingView.createOfferSelectCreateOfferLayout.setVisibility(View.VISIBLE);
                    bindingView.createOfferOfferLayout.setVisibility(View.VISIBLE);
                    bindingView.createOfferOfferTv.setVisibility(View.VISIBLE);
                    bindingView.createOfferOfferTv.setText(getString(R.string.percentage_or_amount));
                    bindingView.createOfferAmountOfDiscountLayout.setVisibility(View.VISIBLE);
                    bindingView.createOfferMinimumBuyOfferAmountLayout.setVisibility(View.GONE);
                    bindingView.createOfferGenerateOfferCodeLayout.setVisibility(View.GONE);
                    bindingView.createOfferIncludeExcludeProducts.setVisibility(View.VISIBLE);
                    bindingView.createOfferOfferList.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showOfferSelectCreate(List<OfferListResponse> offerList, String from) {
        GenericAdapter<OfferListResponse> adapter = new GenericAdapter<OfferListResponse>(getActivity(), offerList) {
            @Override
            public View getCustomView(int position, View view, ViewGroup parent) {
                OfferListResponse model = getItem(position);
                View spinnerRow = LayoutInflater.from(parent.getContext())
                        .inflate(com.bikroybaba.seller.R.layout.custom_spinner_item, parent, false);
                TextView label = spinnerRow.findViewById(com.bikroybaba.seller.R.id.custom_spinner);
                if (model != null) {
                    label.setText(model.getOfferListTitle());
                }
                return spinnerRow;
            }
        };
        bindingView.createOfferSelectCreateOfferDropdown.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        bindingView.createOfferSelectCreateOfferDropdown
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectCreateOfferId = offerList.get(position).getOfferListId();
                offerName = "";
                amountOfDiscount = "";
                minimumBuy = "";
                maxDiscount = "";
                generatedCode = "";
                offerPeriodFrom = "";
                offErPeriodTo = "";
                date = null;
                dateTo = null;
                if (from.equalsIgnoreCase("fromFreeDelivery")) {
                    bindingView.createOfferIncludeExcludeProducts.setVisibility(View.GONE);
                } else {
                    bindingView.createOfferIncludeExcludeProducts.setVisibility(View.VISIBLE);
                }

                if (offerList.get(position).getOfferListId().equals("0")) {
                    bindingView.createOfferIncludeExcludeProducts.setVisibility(View.GONE);
                    bindingView.createOfferOfferName.setText("");
                    bindingView.createOfferAmountOfDiscount.setText("");
                    bindingView.createOfferOfferNameMinimumBuy.setText("");
                    bindingView.createOfferMinimumBuy.setText("");
                    bindingView.createOfferOfferPeriodFrom.setText("");
                    bindingView.createOfferOfferPeriodTo.setText("");
                    bindingView.createOfferBuy.setText("");
                    bindingView.createOfferGet.setText("");
                    bindingView.createOfferBuyGetOfferName.setText("");
                    offerId = "0";
                    bindingView.createOfferOfferDropdown.setEnabled(true);
                    bindingView.createOfferOfferDropdown.setAlpha(1f); // used for enable spinner color
                } else {
                    OfferDetailsRequest offerDetailsRequest = new OfferDetailsRequest(selectCreateOfferId);
                    viewModel.sendOfferDetailsRequest(offerDetailsRequest);
                    bindingView.createOfferOfferDropdown.setEnabled(false);
                    bindingView.createOfferOfferDropdown.setAlpha(0.5f); // used for disable spinner color
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showOfferList(List<OfferListResponse> offerListResponses) {
        GenericAdapter<OfferListResponse> adapter =
                new GenericAdapter<OfferListResponse>(getActivity(), offerListResponses) {
            @Override
            public View getCustomView(int position, View view, ViewGroup parent) {
                OfferListResponse model = getItem(position);
                View spinnerRow = LayoutInflater.from(parent.getContext())
                        .inflate(com.bikroybaba.seller.R.layout.custom_spinner_item, parent, false);
                TextView label = spinnerRow.findViewById(com.bikroybaba.seller.R.id.custom_spinner);
                if (model != null) {
                    label.setText(model.getOfferListTitle());
                }
                return spinnerRow;
            }
        };
        bindingView.createOfferOfferListDropdown.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        bindingView.createOfferOfferListDropdown
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                offerListId = offerListResponses.get(position).getOfferListId();
                date = null;
                dateTo = null;
                fromDate = 0;
                toDate = 0;
                if (!offerListResponses.get(position).getOfferListId().equals("0")) {
                    OfferDetailsRequest offerDetailsRequest =
                            new OfferDetailsRequest(offerListResponses.get(position).getOfferListId());
                    viewModel.sendOfferDetailsRequest(offerDetailsRequest);
                    bindingView.createOfferOfferDropdown.setEnabled(false);
                    bindingView.createOfferOfferDropdown.setAlpha(0.5f);
                    bindingView.createOfferAmountOfDiscount.setText("");
                    bindingView.createOfferOfferName.setText("");
                    bindingView.createOfferMinimumBuy.setText("");
                    bindingView.createOfferMaxDiscountEt.setText("");
                    bindingView.createOfferGenerateOfferEditText.setText("");
                    bindingView.createOfferOfferPeriodFrom.setText("");
                    bindingView.createOfferOfferPeriodTo.setText("");
                } else {
                    bindingView.createOfferAmountOfDiscount.setText("");
                    bindingView.createOfferOfferName.setText("");
                    bindingView.createOfferMinimumBuy.setText("");
                    bindingView.createOfferMaxDiscountEt.setText("");
                    bindingView.createOfferGenerateOfferEditText.setText("");
                    bindingView.createOfferOfferPeriodFrom.setText("");
                    bindingView.createOfferOfferPeriodTo.setText("");
                    bindingView.createOfferOfferDropdown.setEnabled(true);
                    bindingView.createOfferOfferDropdown.setAlpha(1f);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showOfferCategory(List<OfferCategory> offerCategories) {
        offerCategoryList.clear();
        offerCategoryList.addAll(offerCategories);
        GenericAdapter<OfferCategory> adapter =
                new GenericAdapter<OfferCategory>(getActivity(), offerCategories) {
            @Override
            public View getCustomView(int position, View view, ViewGroup parent) {
                OfferCategory model = getItem(position);
                View spinnerRow = LayoutInflater.from(parent.getContext())
                        .inflate(com.bikroybaba.seller.R.layout.custom_spinner_item, parent, false);
                TextView label = spinnerRow.findViewById(com.bikroybaba.seller.R.id.custom_spinner);
                if (model != null) {
                    if (model.getOfferCategoryTitle().equalsIgnoreCase("Percentile")) {
                        label.setText(getString(R.string.percentage));
                    } else {
                        label.setText(utility.firstTextCapitalize(model.getOfferCategoryTitle()));
                    }
                }
                return spinnerRow;
            }
        };
        bindingView.createOfferOfferDropdown.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        bindingView.createOfferOfferDropdown
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                offerCategoryId = offerCategories.get(position).getOfferCategoryId();
                offerCategory = offerCategories.get(position).getOfferCategoryTitle();
                if (position == 2) {
                    bindingView.createOfferBuyGetLayout.setVisibility(View.VISIBLE);
                    bindingView.createOfferBuyGetOfferNameLayout.setVisibility(View.VISIBLE);
                    bindingView.createOfferAmountOfDiscountLayout.setVisibility(View.GONE);
                } else if (position == 0) {
                    bindingView.createOfferMaxDiscountLayout.setVisibility(View.GONE);
                    bindingView.createOfferBuyGetLayout.setVisibility(View.GONE);
                    bindingView.createOfferBuyGetOfferNameLayout.setVisibility(View.GONE);
                    bindingView.createOfferAmountOfDiscountLayout.setVisibility(View.VISIBLE);
                    bindingView.createOfferAmountOfDiscountTv.setText(R.string.amount_of_discount);
                } else {
                    bindingView.createOfferBuyGetLayout.setVisibility(View.GONE);
                    bindingView.createOfferBuyGetOfferNameLayout.setVisibility(View.GONE);
                    bindingView.createOfferAmountOfDiscountLayout.setVisibility(View.VISIBLE);
                    bindingView.createOfferMaxDiscountLayout.setVisibility(View.VISIBLE);
                    bindingView.createOfferAmountOfDiscountTv.setText(R.string.percentage_of_discount);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        switchLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        switchLayout.setVisibility(View.GONE);
    }

    //Open calender dialog
    @SuppressLint("SetTextI18n")
    private void openCalenderDialog(TextView dateTextView, String message) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.calendar_view);
        HashSet<Date> events = new HashSet<>();
        events.add(new Date());
        CalendarView cv = dialog.findViewById(R.id.calendar_view1);
        ImageView imageView = dialog.findViewById(R.id.control_calender_cancel);
        cv.updateCalendar(events);
        imageView.setOnClickListener(view -> {
            dialog.dismiss();
        });
        if (message.equalsIgnoreCase("from")) {
            cv.setEventHandler(date -> {
                if (dateTo != null) {
                    if (dateTo.before(date)) {
                        utility.showDialog(getString(R.string.please_select_date_before,
                                yearFormat.format(dateTo)));
                    } else {
                        this.date = date;
                        dateTextView.setText(yearFormat.format(date));
                    }
                } else if (toDate == 0) {
                    this.date = date;
                    dateTextView.setText(yearFormat.format(date));
                } else if (toDate < date.getTime()) {
                    utility.showDialog(getString(R.string.please_select_date_before,
                            convertDate(String.valueOf(toDate), "dd/MM/yyyy")));
                } else {
                    this.date = date;
                    dateTextView.setText(yearFormat.format(date));
                }
                dialog.dismiss();
            });
        } else if (message.equalsIgnoreCase("to")) {
            cv.setEventHandler(date -> {
                if (this.date != null) {
                    if (this.date.after(date)) {
                        utility.showDialog(getString(R.string.please_select_date_after,
                                yearFormat.format(this.date)));
                    } else {
                        dateTo = date;
                        dateTextView.setText(yearFormat.format(date));
                    }
                } else if (fromDate == 0) {
                    dateTo = date;
                    dateTextView.setText(yearFormat.format(date));

                } else if (fromDate > date.getTime()) {
                    utility.showDialog(getString(R.string.please_select_date_after,
                            convertDate(String.valueOf(fromDate), "dd/MM/yyyy")));
                } else {
                    dateTo = date;
                    dateTextView.setText(yearFormat.format(date));
                }
                dialog.dismiss();
            });
        }
        // assign event handler
        dialog.show();
    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 5) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }

    public static String convertDate(String dateInMilliseconds, String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }

    public String convertStringToMilliSecondForFromDate(String date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf =
                new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = null;
        try {
            date1 = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(fromDateStartHour(date1).getTime());
    }

    public String convertStringToMilliSecondForToDate(String date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = null;
        try {
            date1 = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(toDateLastHour(date1).getTime());
    }
}
