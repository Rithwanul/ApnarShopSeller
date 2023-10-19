package com.bikroybaba.seller.ui.offer;

import static com.bikroybaba.seller.ui.order.DateFragment.fromDateStartHour;
import static com.bikroybaba.seller.ui.order.DateFragment.toDateLastHour;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
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
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.adapter.GenericAdapter;
import com.bikroybaba.seller.adapter.IncludeExcludeAdapter;
import com.bikroybaba.seller.databinding.FragmentEditOfferBinding;
import com.bikroybaba.seller.dialog.ProductListBottomSheet;
import com.bikroybaba.seller.interfaces.EmptyListInterface;
import com.bikroybaba.seller.interfaces.ILoadDataAgain;
import com.bikroybaba.seller.model.remote.request.OfferCategoryRequest;
import com.bikroybaba.seller.model.remote.request.OfferDetailsRequest;
import com.bikroybaba.seller.model.remote.request.OfferListRequest;
import com.bikroybaba.seller.model.remote.request.OfferProductListRequest;
import com.bikroybaba.seller.model.remote.request.OfferStatus;
import com.bikroybaba.seller.model.remote.request.OfferUpdate;
import com.bikroybaba.seller.model.remote.response.OfferCategory;
import com.bikroybaba.seller.model.remote.response.OfferDetailsResponse;
import com.bikroybaba.seller.model.remote.response.OfferListResponse;
import com.bikroybaba.seller.model.remote.response.OfferProductListResponse;
import com.bikroybaba.seller.model.remote.response.OfferTypeResponse;
import com.bikroybaba.seller.util.CalendarView;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;
import com.bikroybaba.seller.viewmodel.OfferViewModel;
import com.moktar.zmvvm.base.base.BaseFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class EditOfferFragment extends BaseFragment<OfferViewModel, FragmentEditOfferBinding>
        implements EmptyListInterface {

    private final List<OfferTypeResponse> offerTypeList = new ArrayList<>();
    private LinearLayoutCompat switchLayout;
    private SimpleDateFormat monthFormat, dateFormat, yearFormat;
    private final List<OfferListResponse> offerList = new ArrayList<>();
    private final List<OfferCategory> offerCategoryList = new ArrayList<>();
    private Utility utility;
    private String offerId = "", offerCategoryId, offerTypeId;
    private String offerName = "", amountOfDiscount = "", minimumBuy = "", maxDiscount = "",
            generatedCode = "", offerPeriodFrom = "", offErPeriodTo = "";
    private OfferDetailsResponse offerDetailsResponse;
    private String shopId;

    private ProductListBottomSheet productListBottomSheet;

    @Override
    public int setContent() {
        return  R.layout.fragment_edit_offer;
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
            shopId = userProfile.getShopId();
        });
        loadProductListData();
        viewModel.getAPIResponse().observe(requireActivity(), api_response -> {
            if (api_response != null && api_response.getCode() == 200) {
                observeOfferProduct(offerId);
                utility.showDialog(api_response.getData().getAsString());
            } else if (api_response!=null && api_response.getCode()==300){
                utility.showDialog(api_response.getData().getAsString());
            } else {
                utility.showDialog(api_response.getData().getAsString());
            }
        });
        viewModel.getUpdateOfferResponse().observe(requireActivity(), api_response -> {
            if (api_response != null) {
                if (api_response.getCode() == 200) {
                    utility.showDialog(getString(R.string.offer_update_successfully));
                } else {
                    utility.showDialog(api_response.getData().getAsString());
                }
            } else {
                utility.showDialog(getString(R.string.duplicate_voucher_code));
            }
        });
        viewModel.getOfferTypeList().observe(requireActivity(), offerTypeResponseList1 -> {
            for (int i = 0; i < offerTypeResponseList1.size(); i++) {
                if (offerDetailsResponse.getOfferTypeResponse().getOfferTypeId()
                        .equals(offerTypeResponseList1.get(i).getOfferTypeId())) {
                    offerTypeList.add(offerTypeResponseList1.get(i));
                    showOfferType(offerTypeList);
                }
            }
        });
    }

    private void initView() {
        monthFormat = new SimpleDateFormat("MM");
        yearFormat = new SimpleDateFormat("yyyy");
        dateFormat = new SimpleDateFormat("dd");
        utility.showProgress(false, "");
        productListBottomSheet = new ProductListBottomSheet();
        offerDetailsResponse = EditOfferFragmentArgs.fromBundle(getArguments()).getOfferDetails();
        //    private FragmentEditOfferBinding binding;
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        TextView textToolHeader = toolbar.findViewById(R.id.title);
        switchLayout = toolbar.findViewById(R.id.switch_layout2);
        TextView switchTextView = toolbar.findViewById(R.id.switch_textView2);
        SwitchCompat offerSwitch = toolbar.findViewById(R.id.offer_switch);
        textToolHeader.setText(offerDetailsResponse.getOfferName());
        switchLayout.setVisibility(View.VISIBLE);
        switchTextView.setText(getString(R.string.start_stop));
        offerSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                if (isChecked) {
                    OfferStatus offerStatus = new OfferStatus(offerDetailsResponse.getId(), KeyWord.ACTIVE);
                    viewModel.updateOfferStatus(offerStatus);
                } else {
                    OfferStatus offerStatus = new OfferStatus(offerDetailsResponse.getId(), KeyWord.INACTIVE);
                    viewModel.updateOfferStatus(offerStatus);
                }
            }
        });

        String amount = offerDetailsResponse.getAmount();
        String offerCategoryName = offerDetailsResponse.getOfferCategory().getOfferCategoryTitle();
        offerId = offerDetailsResponse.getId();
        bindingView.editOfferAmountOfDiscount.setText(offerDetailsResponse.getAmount());
        bindingView.editOfferOfferName.setText(offerDetailsResponse.getOfferName());
        bindingView.editOfferMinimumBuy.setText(offerDetailsResponse.getMinAmount());
        bindingView.editOfferMaxDiscountEt.setText(offerDetailsResponse.getMaxAmount());
        bindingView.editOfferOfferNameMinimumBuy.setText(offerDetailsResponse.getOfferName());
        bindingView.editOfferGenerateOfferEditText.setText(offerDetailsResponse.getGeneratedCode());
        bindingView.editOfferBuy.setText(offerDetailsResponse.getMinAmount());
        bindingView.editOfferGet.setText(offerDetailsResponse.getAmount());
        bindingView.editOfferBuyGetOfferName.setText(offerDetailsResponse.getOfferName());
        bindingView.editOfferOfferPeriodFrom
                .setText(convertDate(offerDetailsResponse.getStartTime(), "dd/MM/yyyy"));
        bindingView.editOfferOfferPeriodTo
                .setText(convertDate(offerDetailsResponse.getEndTime(), "dd/MM/yyyy"));
        if (offerDetailsResponse.getStatus().equals(KeyWord.ACTIVE)) {
            offerSwitch.setChecked(true);
        } else if (offerDetailsResponse.getStatus().equals(KeyWord.INACTIVE)) {
            offerSwitch.setChecked(false);
        }
        onClickEvents();
    }

    private void loadProductListData() {
        OfferProductListRequest offerProductListRequest =
                new OfferProductListRequest(offerId, KeyWord.FILTERED_BY_INCLUDED, "");
        viewModel.getOfferProductList(offerProductListRequest, EditOfferFragment.this)
                .observe(requireActivity(), this::initAdapter);
    }

    private void initAdapter(PagedList<OfferProductListResponse> offerProductList) {
        //binding.srProducts.setRefreshing(false);
        IncludeExcludeAdapter adapter = new IncludeExcludeAdapter(getActivity(), offerId, viewModel);
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        bindingView.rvBottomSheet.setLayoutManager(mLayoutManager);
        bindingView.rvBottomSheet.setAdapter(adapter);
        adapter.submitList(offerProductList);
    }


    private void observeOfferProduct(String offerId) {
        OfferProductListRequest offerProductListRequest =
                new OfferProductListRequest(offerId, KeyWord.FILTERED_BY_INCLUDED,"");
//        binding.includeExcludeSwipeRefresh.setRefreshing(false);
        viewModel.getOfferProductList(offerProductListRequest, EditOfferFragment.this)
                .observe(requireActivity(), offerProductListResponses -> {
                    if (utility.isNetworkAvailable()){
                        bindingView.rvBottomSheet.setVisibility(View.VISIBLE);
                        //binding.noInternetLayout.setVisibility(View.GONE);
                        initAdapter(offerProductListResponses);
                    }else {
                        bindingView.rvBottomSheet.setVisibility(View.GONE);
                        //binding.noInternetLayout.setVisibility(View.VISIBLE);
                    }
                });
    }
    private void showOfferType(String offerTypeId, OfferCategoryRequest offerCategoryRequest,
                               int position) {
        if (offerTypeId.equals("1")) {
            viewModel.getOfferCategory(offerCategoryRequest).observe(requireActivity(),
                    offerCategories -> {
                showOfferCategory(offerCategories);
            });
            OfferListRequest offerListRequest =
                    new OfferListRequest(KeyWord.TIMELINE_ALL,
                            offerTypeList.get(position).getOfferTypeId(),
                            "", shopId);
            viewModel.getOfferList(offerListRequest)
                    .observe(requireActivity(), offerListResponses -> {
                offerList.clear();
                offerList.addAll(offerListResponses);
                showOfferList(offerList);
            });
            bindingView.editOfferSelectCreateOfferLayout.setVisibility(View.GONE);
            bindingView.editOfferMinimumBuyOfferAmountLayout.setVisibility(View.VISIBLE);
            bindingView.editOfferGenerateOfferCodeLayout.setVisibility(View.VISIBLE);
            bindingView.editOfferAmountOfDiscountLayout.setVisibility(View.VISIBLE);
            bindingView.editOfferOfferLayout.setVisibility(View.VISIBLE);
            bindingView.editOfferOfferList.setVisibility(View.VISIBLE);
            bindingView.editOfferIncludeExcludeProducts.setVisibility(View.GONE);
            bindingView.editOfferOfferAmountLayout.setVisibility(View.GONE);
            bindingView.editOfferMaxDiscountLayout.setVisibility(View.VISIBLE);
//            bindingView.editOfferOfferTv.setText(language.equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.offer : R.string.offer_bn);
        } else if (offerTypeId.equals("2")) {
            OfferListRequest offerListRequest =
                    new OfferListRequest(KeyWord.TIMELINE_ALL,
                            offerTypeList.get(position).getOfferTypeId(), "", shopId);
            viewModel.getOfferList(offerListRequest)
                    .observe(requireActivity(), offerListResponses -> {
                offerList.clear();
                offerList.addAll(offerListResponses);
                showOfferSelectCreate(offerList);
            });
            bindingView.editOfferSelectCreateOfferLayout.setVisibility(View.VISIBLE);
            bindingView.editOfferIncludeExcludeProducts.setVisibility(View.GONE);
            bindingView.editOfferMinimumBuyOfferAmountLayout.setVisibility(View.VISIBLE);
            bindingView.editOfferGenerateOfferCodeLayout.setVisibility(View.GONE);
            bindingView.editOfferAmountOfDiscountLayout.setVisibility(View.GONE);
            bindingView.editOfferOfferLayout.setVisibility(View.GONE);
            bindingView.editOfferOfferList.setVisibility(View.GONE);
            bindingView.editOfferMaxDiscountLayout.setVisibility(View.GONE);
            bindingView.editOfferOfferAmountLayout.setVisibility(View.VISIBLE);
        } else if (offerTypeId.equals("3")) {
            viewModel.getOfferCategory(offerCategoryRequest)
                    .observe(requireActivity(), offerCategories -> {
                showOfferCategory(offerCategories);
            });
            OfferListRequest offerListRequest =
                    new OfferListRequest(KeyWord.TIMELINE_ALL,
                            offerTypeList.get(position).getOfferTypeId(), "", shopId);
            viewModel.getOfferList(offerListRequest)
                    .observe(requireActivity(), offerListResponses -> {
                offerList.clear();
                offerList.addAll(offerListResponses);
                showOfferSelectCreate(offerList);
            });

            bindingView.editOfferSelectCreateOfferLayout.setVisibility(View.VISIBLE);
            bindingView.editOfferOfferLayout.setVisibility(View.VISIBLE);
            bindingView.editOfferOfferTv.setVisibility(View.VISIBLE);
            bindingView.editOfferOfferTv.setText("Percentage or Amount");
            bindingView.editOfferAmountOfDiscountLayout.setVisibility(View.VISIBLE);
            bindingView.editOfferMinimumBuyOfferAmountLayout.setVisibility(View.GONE);
            bindingView.editOfferGenerateOfferCodeLayout.setVisibility(View.GONE);
            bindingView.editOfferIncludeExcludeProducts.setVisibility(View.VISIBLE);
            bindingView.editOfferOfferList.setVisibility(View.GONE);
        }
    }

    //All kind of click events
    private void onClickEvents() {
        bindingView.editOfferOfferPeriodFrom
                .setOnClickListener(v -> openCalenderDialog(bindingView.editOfferOfferPeriodFrom, "from"));
        bindingView.editOfferOfferPeriodTo
                .setOnClickListener(v -> openCalenderDialog(bindingView.editOfferOfferPeriodTo, "to"));
        bindingView.editOfferSave.setOnClickListener(v -> {
            validation(offerTypeId);
        });
        bindingView.editOfferGenerateOfferIv.setOnClickListener(v -> {
            bindingView.editOfferGenerateOfferEditText.setText(getSaltString());
        });
        bindingView.editOfferIncludeExcludeProducts.setOnClickListener(v -> {
            if (offerId.equals("0") || offerId.equals("")) {
                utility.showDialog(getString(R.string.title_exclude_include_product));
            } else {
                /*EditOfferFragmentDirections.ActionNavEditOfferToNavIncludeExclude action =
                        EditOfferFragmentDirections.actionNavEditOfferToNavIncludeExclude();
                action.setOfferId(offerId);
                action.setMessage(offerCategoryName + " Discount " + amount + " % ");
                navController.navigate(action);*/
                Bundle bundle = new Bundle();
                bundle.putString(KeyWord.OFFER_ID, offerId);
                productListBottomSheet.setArguments(bundle);
                productListBottomSheet.setiLoadDataAgain(new ILoadDataAgain() {
                    @Override
                    public void loadAgain() {
                        loadProductListData();
                    }
                });
                productListBottomSheet.show(getChildFragmentManager(),
                        KeyWord.BOTTOM_SHEET_PRODUCT_LIST_TAG);
            }
        });
    }


    private void validation(String offerTypeId) {
        switch (offerTypeId) {
            case "1":
                offerName = bindingView.editOfferOfferName.getText().toString();
                amountOfDiscount = bindingView.editOfferAmountOfDiscount.getText().toString();
                minimumBuy = bindingView.editOfferMinimumBuy.getText().toString();
                if (bindingView.editOfferMaxDiscountEt.getText().toString().equals("")) {
                    maxDiscount = "0";
                } else {
                    maxDiscount = bindingView.editOfferMaxDiscountEt.getText().toString();
                }
                generatedCode = bindingView.editOfferGenerateOfferEditText.getText().toString();
                offerPeriodFrom = bindingView.editOfferOfferPeriodFrom.getText().toString();
                offErPeriodTo = bindingView.editOfferOfferPeriodTo.getText().toString();
                if (TextUtils.isEmpty(offerTypeId)) {
                    showErrorStyle(bindingView.editOfferOfferTypeTv);
                } else if (TextUtils.isEmpty(offerCategoryId)) {
                    showErrorStyle(bindingView.editOfferOfferTv);
                } else if (TextUtils.isEmpty(amountOfDiscount)) {
                    showErrorStyle(bindingView.editOfferAmountOfDiscountTv, bindingView.editOfferAmountOfDiscount);
                } else if (TextUtils.isEmpty(offerName)) {
                    showErrorStyle(bindingView.editOfferOfferNameTv, bindingView.editOfferOfferName);
                } else if (TextUtils.isEmpty(minimumBuy)) {
                    showErrorStyle(bindingView.editOfferMinimumBuyTv, bindingView.editOfferMinimumBuy);
                } else if (TextUtils.isEmpty(maxDiscount)) {
                    showErrorStyle(bindingView.editOfferMaxDiscountTv, bindingView.editOfferMaxDiscountEt);
                } else if (TextUtils.isEmpty(generatedCode)) {
                    showErrorStyle(bindingView.editOfferGenerateOfferCodeTv, bindingView.editOfferGenerateOfferEditText);
                } else if (TextUtils.isEmpty(offerPeriodFrom)) {
                    showErrorStyle(bindingView.editOfferOfferPeriodTv);
                } else if (TextUtils.isEmpty(offErPeriodTo)) {
                    showErrorStyle(bindingView.editOfferOfferPeriodTv);
                } else {
                    if (utility.isNetworkAvailable()) {
                        OfferUpdate offerUpdate =
                                new OfferUpdate(offerId, offerTypeId, offerCategoryId, offerName,
                                        amountOfDiscount, minimumBuy, maxDiscount, generatedCode,
                                        convertStringToMilliSecondForFromDate(offerPeriodFrom),
                                        convertStringToMilliSecondForToDate(offErPeriodTo));
                        updateOffer(offerUpdate, "Upon total bill ", amountOfDiscount);
                    } else {
                        utility.showDialog(getString(R.string.check_internet_connection));
                    }
                }
                break;
            case "2":
                offerCategoryId = "1";
                amountOfDiscount = "";
                maxDiscount = "";
                generatedCode = "";
                minimumBuy = bindingView.editOfferMinimumBuy.getText().toString();
                offerName = bindingView.editOfferOfferNameMinimumBuy.getText().toString();
                offerPeriodFrom = bindingView.editOfferOfferPeriodFrom.getText().toString();
                offErPeriodTo = bindingView.editOfferOfferPeriodTo.getText().toString();
                if (TextUtils.isEmpty(minimumBuy)) {
                    showErrorStyle(bindingView.editOfferMinimumBuyTv, bindingView.editOfferMinimumBuy);
                } else if (TextUtils.isEmpty(offerName)) {
                    showErrorStyle(bindingView.editOfferOfferNameTv, bindingView.editOfferOfferName);
                } else if (TextUtils.isEmpty(offerPeriodFrom)) {
                    showErrorStyle(bindingView.editOfferOfferPeriodTv);
                } else if (TextUtils.isEmpty(offErPeriodTo)) {
                    showErrorStyle(bindingView.editOfferOfferPeriodTv);
                } else {
                    if (utility.isNetworkAvailable()) {
                        OfferUpdate offerUpdate =
                                new OfferUpdate(offerId, offerTypeId, offerCategoryId, offerName,
                                        amountOfDiscount, minimumBuy, maxDiscount, generatedCode,
                                        convertStringToMilliSecondForFromDate(offerPeriodFrom),
                                        convertStringToMilliSecondForToDate(offErPeriodTo));
                        updateOffer(offerUpdate, "Free delivery", "");
                    } else {
                        utility.showDialog(getString(R.string.check_internet_connection));
                    }
                }
                break;
            case "3":
                minimumBuy = bindingView.editOfferBuy.getText().toString();
                amountOfDiscount = bindingView.editOfferGet.getText().toString();
                offerName = bindingView.editOfferBuyGetOfferName.getText().toString();
                offerPeriodFrom = bindingView.editOfferOfferPeriodFrom.getText().toString();
                offErPeriodTo = bindingView.editOfferOfferPeriodTo.getText().toString();
                if (offerCategoryId.equals("3")) {
                    if (TextUtils.isEmpty(minimumBuy)) {
                        showErrorStyle(bindingView.editOfferBuyTv, bindingView.editOfferBuy);
                    } else if (TextUtils.isEmpty(offerName)) {
                        showErrorStyle(bindingView.editOfferGetTv, bindingView.editOfferGet);
                    } else if (TextUtils.isEmpty(offerName)) {
                        showErrorStyle(bindingView.editOfferBuyGetOfferNameTv, bindingView.editOfferBuyGetOfferName);
                    } else if (TextUtils.isEmpty(offerPeriodFrom)) {
                        showErrorStyle(bindingView.editOfferOfferPeriodTv);
                    } else if (TextUtils.isEmpty(offErPeriodTo)) {
                        showErrorStyle(bindingView.editOfferOfferPeriodTv);
                    } else {
                        if (utility.isNetworkAvailable()) {
                            OfferUpdate offerUpdate =
                                    new OfferUpdate(offerId, offerTypeId, offerCategoryId, offerName,
                                            amountOfDiscount, minimumBuy, maxDiscount, generatedCode,
                                            convertStringToMilliSecondForFromDate(offerPeriodFrom),
                                            convertStringToMilliSecondForToDate(offErPeriodTo));
                            updateOffer(offerUpdate, "Product based offer", "");
                        } else {
                            utility.showDialog(getString(R.string.check_internet_connection));
                        }
                    }
                } else {
                    minimumBuy = "0";
                    amountOfDiscount = bindingView.editOfferAmountOfDiscount.getText().toString();
                    offerName = bindingView.editOfferOfferName.getText().toString();
                    offerPeriodFrom = bindingView.editOfferOfferPeriodFrom.getText().toString();
                    offErPeriodTo = bindingView.editOfferOfferPeriodTo.getText().toString();
                    if (TextUtils.isEmpty(amountOfDiscount)) {
                        showErrorStyle(bindingView.editOfferAmountOfDiscountTv, bindingView.editOfferAmountOfDiscount);
                    } else if (TextUtils.isEmpty(offerName)) {
                        showErrorStyle(bindingView.editOfferOfferNameTv, bindingView.editOfferOfferName);
                    } else if (TextUtils.isEmpty(offerPeriodFrom)) {
                        showErrorStyle(bindingView.editOfferOfferPeriodTv);
                    } else if (TextUtils.isEmpty(offErPeriodTo)) {
                        showErrorStyle(bindingView.editOfferOfferPeriodTv);
                    } else {
                        if (utility.isNetworkAvailable()) {
                            OfferUpdate offerUpdate =
                                    new OfferUpdate(offerId, offerTypeId, offerCategoryId, offerName,
                                            amountOfDiscount, minimumBuy, maxDiscount, generatedCode,
                                            convertStringToMilliSecondForFromDate(offerPeriodFrom),
                                            convertStringToMilliSecondForToDate(offErPeriodTo));
                            updateOffer(offerUpdate, "Product based offer", amountOfDiscount);
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
        bindingView.editOfferOfferTypeDropdown.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        bindingView.editOfferOfferTypeDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                offerTypeId = offerTypeResponses.get(position).getOfferTypeId();
                bindingView.editOfferOfferTypeDropdown.setAlpha(0.5f);
                OfferCategoryRequest offerCategoryRequest = new OfferCategoryRequest(offerTypeId);
                showOfferType(offerTypeId, offerCategoryRequest, position);
                utility.hideProgress();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void showOfferSelectCreate(List<OfferListResponse> offerList) {
        List<OfferListResponse> offerListResponse = new ArrayList<>();
        for (int i = 0; i < offerList.size(); i++) {
            if (offerDetailsResponse.getId().equals(offerList.get(i).getOfferListId())) {
                offerListResponse.add(offerList.get(i));
            }
        }
        GenericAdapter<OfferListResponse> adapter =
                new GenericAdapter<OfferListResponse>(getActivity(), offerListResponse) {
            @Override
            public View getCustomView(int position, View view, ViewGroup parent) {
                OfferListResponse model = getItem(position);
                View spinnerRow = LayoutInflater.from(parent.getContext())
                        .inflate(com.bikroybaba.seller.R.layout.custom_spinner_item, parent, false);
                TextView label = spinnerRow.findViewById(com.bikroybaba.seller.R.id.custom_spinner);
                if (model != null) {
                    label.setText(utility.firstTextCapitalize(model.getOfferListTitle()));
                }
                return spinnerRow;
            }
        };

        bindingView.editOfferSelectCreateOfferDropdown.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        bindingView.editOfferSelectCreateOfferDropdown
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectCreateOfferId = offerList.get(position).getOfferListId();
                bindingView.editOfferSelectCreateOfferDropdown.setAlpha(0.5f);
                offerName = "";
                amountOfDiscount = "";
                minimumBuy = "";
                maxDiscount = "";
                generatedCode = "";
                offerPeriodFrom = "";
                offErPeriodTo = "";
                OfferDetailsRequest offerDetailsRequest = new OfferDetailsRequest(selectCreateOfferId);
                viewModel.sendOfferDetailsRequest(offerDetailsRequest);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showOfferList(List<OfferListResponse> offerListResponses) {
        List<OfferListResponse> offerList = new ArrayList<>();
        for (int i = 0; i < offerListResponses.size(); i++) {
            if (offerDetailsResponse.getId().equals(offerListResponses.get(i).getOfferListId())) {
                offerList.add(offerListResponses.get(i));
            }
        }
        GenericAdapter<OfferListResponse> adapter =
                new GenericAdapter<OfferListResponse>(getActivity(), offerList) {
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

        bindingView.editOfferOfferListDropdown.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        bindingView.editOfferOfferListDropdown
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String offerListId = offerListResponses.get(position).getOfferListId();
                bindingView.editOfferOfferListDropdown.setAlpha(0.5f);
                if (!offerListResponses.get(position).getOfferListId().equals("0")) {
                    OfferDetailsRequest offerDetailsRequest =
                            new OfferDetailsRequest(offerListResponses.get(position).getOfferListId());
                    viewModel.sendOfferDetailsRequest(offerDetailsRequest);
                } else {
                    bindingView.editOfferAmountOfDiscount.setText("");
                    bindingView.editOfferOfferName.setText("");
                    bindingView.editOfferMinimumBuy.setText("");
                    bindingView.editOfferMaxDiscountEt.setText("");
                    bindingView.editOfferGenerateOfferEditText.setText("");
                    bindingView.editOfferOfferPeriodFrom.setText("");
                    bindingView.editOfferOfferPeriodTo.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showOfferCategory(List<OfferCategory> offerCategories) {
        offerCategoryList.clear();
        for (int i = 0; i < offerCategories.size(); i++) {
            if (offerDetailsResponse.getOfferCategory().getOfferCategoryId().equals(offerCategories.get(i).getOfferCategoryId())) {
                offerCategoryList.add(offerCategories.get(i));

            }
        }
        GenericAdapter<OfferCategory> adapter =
                new GenericAdapter<OfferCategory>(getActivity(), offerCategoryList) {
            @Override
            public View getCustomView(int position, View view, ViewGroup parent) {
                OfferCategory model = getItem(position);
                View spinnerRow = LayoutInflater.from(parent.getContext())
                        .inflate(com.bikroybaba.seller.R.layout.custom_spinner_item, parent, false);
                TextView label = spinnerRow.findViewById(com.bikroybaba.seller.R.id.custom_spinner);
                if (model != null) {
                    if (model.getOfferCategoryTitle().equalsIgnoreCase("Percentile")) {
                        label.setText("Percentage");
                    } else {
                        label.setText(utility.firstTextCapitalize(model.getOfferCategoryTitle()));
                    }
                }
                return spinnerRow;
            }
        };
        bindingView.editOfferOfferDropdown.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        bindingView.editOfferOfferDropdown
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                offerCategoryId = offerCategoryList.get(position).getOfferCategoryId();
                bindingView.editOfferOfferDropdown.setEnabled(false);
                bindingView.editOfferOfferDropdown.setAlpha(0.5f);
                // "3 category id for bundle"
                if (offerCategoryId.equals("3")) {
                    bindingView.editOfferBuyGetLayout.setVisibility(View.VISIBLE);
                    bindingView.editOfferBuyGetOfferNameLayout.setVisibility(View.VISIBLE);
                    bindingView.editOfferAmountOfDiscountLayout.setVisibility(View.GONE);
                } else if (offerCategoryId.equals("1")) {
                    bindingView.editOfferMaxDiscountLayout.setVisibility(View.GONE);
                    bindingView.editOfferAmountOfDiscountTv
                            .setText(getString(R.string.amount_of_discount));
                } else {
                    bindingView.editOfferBuyGetLayout.setVisibility(View.GONE);
                    bindingView.editOfferBuyGetOfferNameLayout.setVisibility(View.GONE);
                    bindingView.editOfferAmountOfDiscountLayout.setVisibility(View.VISIBLE);
                    bindingView.editOfferAmountOfDiscountTv.setText(getString(R.string.percentage_of_discount));
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
        // assign event handler
        cv.setEventHandler(date -> {
            dateTextView.setText(dateFormat.format(date) + "/" + monthFormat.format(date) + "/"
                    + yearFormat.format(date));
            dialog.dismiss();
        });
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
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf =
                new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = null;
        try {
            date1 = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(toDateLastHour(date1).getTime());
    }

    @Override
    public void order(int size) {
        if (size == 0){
            bindingView.rvBottomSheet.setVisibility(View.GONE);
            bindingView.cpIndicator.setVisibility(View.VISIBLE);
        }else {
            bindingView.rvBottomSheet.setVisibility(View.VISIBLE);
            bindingView.cpIndicator.setVisibility(View.GONE);
        }
    }
}