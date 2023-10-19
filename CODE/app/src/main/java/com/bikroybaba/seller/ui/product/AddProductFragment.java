package com.bikroybaba.seller.ui.product;

import static com.bikroybaba.seller.ui.HomeActivity.navController;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.adapter.AddProductImageAdapter;
import com.bikroybaba.seller.adapter.AttributeArrayListAdapter;
import com.bikroybaba.seller.adapter.AttributeDataNameArrayListAdapter;
import com.bikroybaba.seller.adapter.DeliveryAreaAdapter;
import com.bikroybaba.seller.adapter.GenericAdapter;
import com.bikroybaba.seller.data.FilePath;
import com.bikroybaba.seller.databinding.FragmentAddProductBinding;
import com.bikroybaba.seller.databinding.FragmentDashboardBinding;
import com.bikroybaba.seller.databinding.RecyclerviewDeliveryAreaBinding;
import com.bikroybaba.seller.interfaces.DialogListerner;
import com.bikroybaba.seller.model.entity.Attribute;
import com.bikroybaba.seller.model.entity.Brand;
import com.bikroybaba.seller.model.remote.request.Category;
import com.bikroybaba.seller.model.remote.request.ProductType;
import com.bikroybaba.seller.model.remote.response.Supplier;
import com.bikroybaba.seller.model.remote.response.UnitType;
import com.bikroybaba.seller.ui.delivery.DeliveryAreaFragment;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.ReadExternalStoragePermission;
import com.bikroybaba.seller.util.Utility;
import com.bikroybaba.seller.viewmodel.DashboardViewModel;
import com.bikroybaba.seller.viewmodel.ProductViewModel;
import com.google.android.material.textview.MaterialTextView;
import com.moktar.searchablespinner.interfaces.IStatusListener;
import com.moktar.searchablespinner.interfaces.OnItemSelectedListener;
import com.moktar.zmvvm.base.base.BaseFragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddProductFragment extends BaseFragment<DashboardViewModel, FragmentDashboardBinding> {

    private FragmentAddProductBinding binding;
    private final String type = KeyWord.BLANK;
    private List<String> imageList;
    private List<String> imageListAll;
    private final String supplier = KeyWord.BLANK;
    private String currentPhotoPath;
    private AddProductImageAdapter addProductImageAdapter;
    private LinearLayoutManager layoutManager;
    private Category category;
    private ProductType productType;
    private final List<Category> categories = new ArrayList<>();
    private final List<Attribute> attributes = new ArrayList<>();
    private final List<Supplier> supplierList = new ArrayList<>();
    private final List<ProductType> productTypeList = new ArrayList<>();
    private final List<Brand> brandList = new ArrayList<>();
    private final List<UnitType> unitTypeList = new ArrayList<>();
    private final List<UnitType> weightUnitTypeList = new ArrayList<>();
    private final ArrayList<Attribute> addedRecyclerViewData = new ArrayList<Attribute>();
    private final String size = KeyWord.BLANK;
    private String unitType;
    private String unitWeightType = KeyWord.BLANK;
    private String unitWeightQuantity = KeyWord.BLANK;
    private String productCategory = KeyWord.BLANK;
    private final int countProductTypeClick = 0;
    private String description = KeyWord.BLANK;
    private String brand = KeyWord.BLANK;
    private final int countSupplierClick = 0;
    private Utility utility;
    private boolean isUnitTypeSelected = false;
    private ProductViewModel viewModel;
    private File photoFile = null;
    private Uri photoURI;
    private MaterialTextView textToolHeader;
    private String language, retailPrice = "", totalQuantity = "";
    private int countCategory = 0;
    private int countBrandClick = 0;
    private String imageEncoded;
    private final ArrayList<String> attributeNames = new ArrayList<>();
    private final ArrayList<Attribute> attributeArrayList = new ArrayList<>();
    private final ArrayList<Attribute> attributeDataNameArrayList = new ArrayList<>();
    private final ArrayList<String> _attributeDataName = new ArrayList<>();
    private boolean mIsSwipeRefresh;
    private GenericAdapter<Attribute> attributeAdapter;
    private AttributeArrayListAdapter mAttributeArrayListAdapter;
    private boolean mIsAttributeSpinnerOpen;
    private String attributeId = "";
    private String attributeDataNameId = "";
    private AttributeDataNameArrayListAdapter mAttributeDataNameArrayListAdapter;
    private final OnItemSelectedListener mOnAttributeItemSelectedListener =
            new OnItemSelectedListener() {
                @Override
                public void onItemSelected(View view, int position, long id) {
                    if (binding.spinnerAttributeDataName.getSelectedPosition() > 0) {
                        binding.spinnerAttributeDataName.setSelectedItem(0);
                    }
                    /*attributeDataNameId = "";
                    Attribute selectedAttribute = attributes.get(position);
                    observedAttributeDataName(selectedAttribute);*/

                    //viewModel.getAttributeDataNameLiveData()

                    if (position == 0) {
                        attributeId = "";
                        attributeArrayList.clear();
                        //binding.spinnerAttributeDataName.setAdapter(attributeData);
                    } else {
                        /*attributeId = getCityId(cityList, mCityListAdapter.getItem(position));
                        City city = new City();
                        city.setCityId(attributeId);
                        observeAreaData(city);
                        Log.d(TAG, "city id: " + attributeId);*/

                        Attribute selectedAttribute = attributes.get(position - 1);
                        observedAttributeDataName(selectedAttribute);
                    }

                }

                @Override
                public void onNothingSelected() {
                    Toast.makeText(requireActivity(), "Nothing Selected", Toast.LENGTH_SHORT).show();
                }
            };
    private DeliveryAreaAdapter<Attribute> attributeDataNameAdapter;
    private final OnItemSelectedListener mAttributeDataNameSelectionListener =
            new OnItemSelectedListener() {
                @Override
                public void onItemSelected(View view, int position, long id) {
                    if (position == 0) {
                        attributeDataNameId = "";
                    } else {
                        Attribute selectedAttribute = attributeDataNameArrayList.get(position - 1);
                        if (addedRecyclerViewData.contains(selectedAttribute)){
                            Toast.makeText(requireContext(), "Attribute already added", Toast.LENGTH_SHORT).show();
                        }else {
                            addedRecyclerViewData.add(selectedAttribute);
                        }

                        if (addedRecyclerViewData.size() > 0){
                            binding.rvAddedAttributes.setVisibility(View.VISIBLE);
                            binding.addAttributeError.setVisibility(View.GONE);
                        }

                        attributeDataNameAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onNothingSelected() {
                    Toast.makeText(requireActivity(), "Nothing Selected", Toast.LENGTH_SHORT).show();
                }
            };

    private void observedAttributeDataName(Attribute selectedAttribute) {
        viewModel.getAttributeDataNameLiveData("",
                selectedAttribute.getId()).observe(requireActivity(), attributeResponse -> {
                attributeDataNameArrayList.clear();
                attributeDataNameArrayList.addAll(attributeResponse.getAttributes());
                _attributeDataName.clear();

            for (Attribute item: attributeDataNameArrayList) {
                _attributeDataName.add(item.getAttributeDataName());
            }

            mAttributeDataNameArrayListAdapter = new AttributeDataNameArrayListAdapter(requireActivity(), _attributeDataName);
            binding.spinnerAttributeDataName.setAdapter(mAttributeDataNameArrayListAdapter);

        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil
                .inflate(LayoutInflater.from(getContext()),
                        R.layout.fragment_add_product, container, false);
        return binding.getRoot();
    }

    private String getAreaId(ArrayList<Attribute> attributeDataNameArrayList, String areaName) {
        for (Attribute item : attributeDataNameArrayList) {
            if (item.getAttributeName().contains(areaName)) {
                return String.valueOf(item.getId());
            }
        }
        return "";
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hideInputKey();
        utility = new Utility(getActivity());
        language = utility.getLangauge();
        initView();
        initViewModel();
        loadData();

//        changeLanguage(language);

        imageList = new ArrayList<>();
        imageListAll = new ArrayList<>();
        category = new Category();
        productType = new ProductType();
        ReadExternalStoragePermission.isReadStoragePermissionGranted(getActivity());
        onclickEvent();
        observeFilteredDataForCategory();
        textWatcherListener();
        itemClickEvent();
        viewModel.getHandleError().observe(requireActivity(), aBoolean -> {
            if (aBoolean) {
                utility.hideProgress();
                utility.showDialog("Something is wrong.Please try again");
            }
        });

        initRecyclerView();

    }

    private void initRecyclerView(){
        attributeDataNameAdapter = new DeliveryAreaAdapter<Attribute>(requireContext(), addedRecyclerViewData) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                RecyclerviewDeliveryAreaBinding binding =
                        DataBindingUtil.inflate(LayoutInflater.from(getActivity()),
                                R.layout.recyclerview_delivery_area, parent, false);
                return new DeliveryAreaFragment.ViewHolder(binding);
            }

            @Override
            public void onBindData(RecyclerView.ViewHolder holder, Attribute val, int position) {
                DeliveryAreaFragment.ViewHolder viewHolder = (DeliveryAreaFragment.ViewHolder) holder;
                viewHolder.binding.recyclerviewDeliveryAreaTitle.setText(val.getAttributeDataName());
                viewHolder.binding.recyclerviewDeliveryAreaClear.setOnClickListener(v -> {
                    Utility.showDeleteDialog(requireContext(), getString(R.string.are_you_want), getString(R.string.common_delete_message), new DialogListerner() {
                        @Override
                        public void yesOnClick(View v, Dialog dialog) {
                            Attribute removedAttribute = addedRecyclerViewData.get(position);
                            addedRecyclerViewData.remove(removedAttribute);
                            attributeDataNameAdapter.notifyItemRemoved(position);
                            dialog.dismiss();
                        }

                        @Override
                        public void noOnClick(View v, Dialog dialog) {
                            dialog.dismiss();
                        }
                    });
                });
            }
        };
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        binding.rvAddedAttributes.setLayoutManager(staggeredGridLayoutManager);
        binding.rvAddedAttributes.setAdapter(attributeDataNameAdapter);
    }

    @Override
    protected void loadData() {
        loadUiData();
    }



    private void loadUiData() {
        observeAttributeData();
    }


    private void initView() {
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        textToolHeader = toolbar.findViewById(R.id.title);
        textToolHeader.setText(language.equals(KeyWord.BANGLA) ? KeyWord.ADD_PRODUCT_BN : KeyWord.ADD_PRODUCT);
        setUpSpinners();
    }

    private void setUpSpinners() {
        mAttributeArrayListAdapter = new AttributeArrayListAdapter(requireContext(), attributeNames);
        binding.spinnerAttribute.setOnItemSelectedListener(mOnAttributeItemSelectedListener);
        binding.spinnerAttribute.setStatusListener(new IStatusListener() {
            @Override
            public void spinnerIsOpening() {
                binding.spinnerAttributeDataName.hideEdit();
                mIsAttributeSpinnerOpen = true;
            }

            @Override
            public void spinnerIsClosing() {
                mIsAttributeSpinnerOpen = false;
            }
        });

        mAttributeDataNameArrayListAdapter = new AttributeDataNameArrayListAdapter(requireActivity(), _attributeDataName);
        binding.spinnerAttributeDataName.setOnItemSelectedListener(mAttributeDataNameSelectionListener);
        binding.spinnerAttributeDataName.setStatusListener(new IStatusListener() {
            @Override
            public void spinnerIsOpening() {
                binding.spinnerAttribute.hideEdit();
                binding.spinnerAttributeDataName.hideEdit();
            }

            @Override
            public void spinnerIsClosing() {

            }
        });
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);
    }

    @Override
    public int setContent() {
        return R.layout.fragment_add_product;
    }

    private void hideInputKey() {
        requireActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    private void changeTextColor(String s, int startColor, int endColor, TextView tv) {

        SpannableString ss = new SpannableString(s);
        ss.setSpan(new ForegroundColorSpan(Color.RED), startColor, endColor, 0);
        tv.setText(ss);
    }

/*
    private void changeLanguage(String language) {
        binding.addProductCategoryTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.category_bn) : getString(R.string.category));
//        binding.addProductProductTypeTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.product_type_bn) : getString(R.string.product_type));
        binding.addProductBrandTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.brand_bn) : getString(R.string.brand));
//        binding.addProductSupplierTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.supplier_bn) : getString(R.string.supplier));
        binding.addProductProductNameTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.product_name_bn) : getString(R.string.product_name));
        binding.addProductUnitTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.unit_size_bn) : getString(R.string.unit_size));
        binding.addProductWeightOfUnitTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.weight_of_the_unit_bn) : getString(R.string.weight_of_the_unit));
        binding.addProductRetailSellPriceTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.retail_sell_price_per_unit_bn) : getString(R.string.retail_sell_price_per_unit));
        binding.addProductTkTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.tk_bn) : getString(R.string.tk));
        binding.addProductTkTv2.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.tk_bn) : getString(R.string.tk));
        binding.addProductTotalQuantityTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.total_quantity_bn) : getString(R.string.total_quantity));
        binding.addProductTotalPriceWholesellTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.total_price_wholesale_bn) : getString(R.string.total_price_wholesale));
        binding.addProductProductDetailsTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.product_details_bn) : getString(R.string.product_details));
        binding.cancel.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.cancel_bn) : getString(R.string.cancel));
        binding.btnAdd.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.add) : getString(R.string.add));
        binding.addProductBrowse.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.browse_bn) : getString(R.string.browse));
        binding.addProductCategoryDropdown.setHint(language.equalsIgnoreCase(KeyWord.BANGLA) ? getString(R.string.type_product_category_bn) : getString(R.string.type_product_category));
//        binding.addProductProductTypeDropdown.setHint(language.equalsIgnoreCase(KeyWord.BANGLA) ? getString(R.string.type_product_type_bn) : getString(R.string.type_product_type));
        binding.addProductBrandDropdown.setHint(language.equalsIgnoreCase(KeyWord.BANGLA) ? getString(R.string.type_brand_name_bn) : getString(R.string.type_brand_name));
//        binding.addProductSupplierDropdown.setHint(language.equalsIgnoreCase(KeyWord.BANGLA) ? getString(R.string.type_supplier_name_bn) : getString(R.string.type_supplier_name));
        binding.addProductProductName.setHint(language.equalsIgnoreCase(KeyWord.BANGLA) ? getString(R.string.write_product_name_bn) : getString(R.string.write_product_name));
        binding.addProductProductDetails.setHint(language.equalsIgnoreCase(KeyWord.BANGLA) ? getString(R.string.hint_product_details_bn) : getString(R.string.hint_product_details));
    }
*/

    private void textWatcherListener() {
        binding.addProductCategoryDropdown.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    viewModel.searchCategory(s.toString());
                } else {
                    countCategory = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


/*
        binding.addProductSupplierDropdown.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    viewModel.searchSupplier(s.toString());
                } else {
                    countSupplierClick = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
*/
/*
        binding.addProductProductTypeDropdown.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    viewModel.searchProductType(s.toString(), category);
                } else {
                    countProductTypeClick = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
*/
        binding.addProductBrandDropdown.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    viewModel.searchBrand(s.toString(), productType);
                } else {
                    countBrandClick = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.addProductUnitEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    binding.addProductUnitError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.addProductRetailSellPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    retailPrice = binding.addProductRetailSellPrice.getText().toString();
                    if (!totalQuantity.equals("")) {
                        try {
                            binding.addProductTotalPriceWholesell
                                    .setText(String.valueOf(Integer.parseInt(retailPrice)
                                            * Integer.parseInt(totalQuantity)));

                        } catch (Exception e) {
                            Log.d("TAG", "onTextChanged: " + e.getMessage());
                        }

                    }
                } else {
                    retailPrice = "";
                    binding.addProductTotalPriceWholesell.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.totalQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    totalQuantity = binding.totalQuantity.getText().toString();
                    if (!retailPrice.equals("")) {
                        try {
                            binding.addProductTotalPriceWholesell
                                    .setText(String.valueOf(Integer.parseInt(retailPrice)
                                            * Integer.parseInt(totalQuantity)));
                        } catch (Exception e) {
                            Log.d("TAG", "onTextChanged: " + e.getMessage());
                        }
                    }
                } else {
                    totalQuantity = "";
                    binding.addProductTotalPriceWholesell.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void itemClickEvent() {
        binding.addProductCategoryDropdown.setOnItemClickListener((parent, view, position, id) -> {
            category.setCategoryId(categories.get(position).getCategoryId());
            binding.addProductCategoryDropdown
                    .setText(categories.get(position).getCategoryTitle());
            binding.addProductCategoryDropdown
                    .setSelection(categories.get(position).getCategoryTitle().length());
            countCategory = 1;
        });


/*        binding.addProductSupplierDropdown.setOnItemClickListener((parent, view, position, id) -> {
            binding.addProductSupplierDropdown
                    .setText(supplierList.get(position).getSupplierTitle());
            binding.addProductSupplierDropdown
                    .setSelection(supplierList.get(position).getSupplierTitle().length());
            countSupplierClick = 1;
        });*/
/*        binding.addProductProductTypeDropdown.setOnItemClickListener((parent, view, position, id) -> {
            productType.setTypeId(productTypeList.get(position).getTypeId());
            binding.addProductProductTypeDropdown
                    .setText(productTypeList.get(position).getTypeTitle());
            binding.addProductProductTypeDropdown
                    .setSelection(productTypeList.get(position).getTypeTitle().length());
            countProductTypeClick = 1;
        });*/
        binding.addProductBrandDropdown.setOnItemClickListener((parent, view, position, id) -> {
            binding.addProductBrandDropdown.setText(brandList.get(position).getBrandTitle());
            binding.addProductBrandDropdown
                    .setSelection(brandList.get(position).getBrandTitle().length());
            countBrandClick = 1;
        });
    }

    private void observeAttributeData() {
        viewModel.getAttributeLivedata("").observe(requireActivity(),
                attributeResponse -> {
                    attributes.clear();
                    attributeNames.clear();
                    ArrayList<Attribute> attributeResult = attributeResponse.getAttributes();
                    attributes.addAll(attributeResult);
                    for (Attribute item : attributeResult) {
                        attributeNames.add(item.getAttributeName());
                    }
                    binding.spinnerAttribute.setAdapter(mAttributeArrayListAdapter);
                });
    }

    private void observeFilteredDataForCategory() {

        //Get filtered category
        viewModel.getFilteredCategory().observe(requireActivity(), categoryList -> {
            if (categoryList.size() > 0) {
                categories.clear();
                categories.addAll(categoryList);
                GenericAdapter<Category> autocompleteAdapter =
                        new GenericAdapter<Category>(getActivity(), categoryList) {
                            @Override
                            public View getCustomView(int position, View view, ViewGroup parent) {
                                ViewHolder holder;
                                if (view == null) {
                                    holder = new ViewHolder();
                                    view = LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner_item,
                                            parent, false);
                                    holder.title = view.findViewById(R.id.custom_spinner);
                                    holder.title.setText(categories.get(position).getCategoryTitle());
                                    view.setTag(holder);
                                }
                                return view;
                            }
                        };

                if (countCategory == 0) {
                    binding.addProductCategoryDropdown.setAdapter(autocompleteAdapter);
                    binding.addProductCategoryDropdown.setThreshold(1);
                    binding.addProductCategoryDropdown.showDropDown();
                    autocompleteAdapter.notifyDataSetChanged();
                }
            }

        });
        //Get filtered supplier
/*        viewModel.getFilteredSupplierList().observe(requireActivity(), suppliers -> {

            if (suppliers.size() > 0) {
                supplierList.clear();
                supplierList.addAll(suppliers);
                GenericAdapter<Supplier> autocompleteAdapter =
                        new GenericAdapter<Supplier>(getActivity(), supplierList) {
                            @Override
                            public View getCustomView(int position, View view, ViewGroup parent) {
                                ViewHolder holder;
                                if (view == null) {
                                    holder = new ViewHolder();
                                    view = LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner_item,
                                            parent, false);
                                    holder.title = view.findViewById(R.id.custom_spinner);
                                    holder.title.setText(supplierList.get(position).getSupplierTitle());
                                    view.setTag(holder);
                                }
                                return view;
                            }
                        };
                if (countSupplierClick == 0) {
//                    binding.addProductSupplierDropdown.setAdapter(autocompleteAdapter);
//                    binding.addProductSupplierDropdown.setThreshold(1);
//                    binding.addProductSupplierDropdown.showDropDown();
                    autocompleteAdapter.notifyDataSetChanged();
                }
            }

        });*/

        //Get filtered product type
/*        viewModel.getFilteredProductTypeList().observe(requireActivity(), productTypes -> {

            if (productTypes.size() > 0) {
                productTypeList.clear();
                productTypeList.addAll(productTypes);
                GenericAdapter<ProductType> autocompleteAdapter =
                        new GenericAdapter<ProductType>(getActivity(), productTypeList) {
                            @Override
                            public View getCustomView(int position, View view, ViewGroup parent) {
                                ViewHolder holder;

                                if (view == null) {

                                    holder = new ViewHolder();

                                    view = LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner_item,
                                            parent, false);
                                    holder.title = view.findViewById(R.id.custom_spinner);

                                    holder.title.setText(productTypeList.get(position).getTypeTitle());


                                    view.setTag(holder);
                                }
                                return view;
                            }
                        };
                if (countProductTypeClick == 0) {
//                    binding.addProductProductTypeDropdown.setAdapter(autocompleteAdapter);
//                    binding.addProductProductTypeDropdown.setThreshold(1);
//                    binding.addProductProductTypeDropdown.showDropDown();
                    autocompleteAdapter.notifyDataSetChanged();
                }
            }
        });*/

        //Get filtered Band
        viewModel.getFilteredBandList().observe(requireActivity(), brands -> {
            if (brands.size() > 0) {
                brandList.clear();
                brandList.addAll(brands);
                GenericAdapter<Brand> autocompleteAdapter = new GenericAdapter<Brand>(getActivity(), brandList) {
                    @Override
                    public View getCustomView(int position, View view, ViewGroup parent) {
                        ViewHolder holder;
                        if (view == null) {
                            holder = new ViewHolder();
                            view = LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner_item,
                                    parent, false);
                            holder.title = view.findViewById(R.id.custom_spinner);
                            holder.title.setText(brandList.get(position).getBrandTitle());
                            view.setTag(holder);
                        }
                        return view;
                    }
                };
                if (countBrandClick == 0) {
                    binding.addProductBrandDropdown.setAdapter(autocompleteAdapter);
                    binding.addProductBrandDropdown.setThreshold(1);
                    binding.addProductBrandDropdown.showDropDown();
                    autocompleteAdapter.notifyDataSetChanged();
                }
            }
        });

        //Get Unit type
        viewModel.getUnitType().observe(requireActivity(), unitTypes -> {
            unitTypeList.clear();
            unitTypeList.addAll(unitTypes);
            weightUnitTypeList.add(new UnitType("Select", "0"));
            for (UnitType unitType : unitTypes) {
                if (!unitType.getUnitType().equalsIgnoreCase("piece")) {
                    weightUnitTypeList.add(unitType);
                }
            }
            GenericAdapter<UnitType> spinnerAdapter
                    = new GenericAdapter<UnitType>(getActivity(), unitTypes) {
                @Override
                public View getCustomView(int position, View view, ViewGroup parent) {
                    UnitType model = getItem(position);
                    View spinnerRow = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.unit_spinner, parent, false);
                    TextView label = spinnerRow.findViewById(com.bikroybaba.seller.R.id.custom_spinner);
                    if (model != null) {
                        label.setText(utility.firstTextCapitalize(model.getUnitType()));
                    }
                    return spinnerRow;
                }
            };
            binding.addProductUnitDropdown.setAdapter(spinnerAdapter);
            if (weightUnitTypeList.size() > 0) {
                GenericAdapter<UnitType> spinnerAdapter1 =
                        new GenericAdapter<UnitType>(getActivity(), weightUnitTypeList) {
                            @Override
                            public View getCustomView(int position, View view, ViewGroup parent) {
                                UnitType model = getItem(position);
                                View spinnerRow = LayoutInflater.from(parent.getContext())
                                        .inflate(R.layout.unit_spinner, parent, false);

                                TextView label =
                                        spinnerRow.findViewById(com.bikroybaba.seller.R.id.custom_spinner);
                                if (model != null) {
                                    label.setText(utility.firstTextCapitalize(model.getUnitType()));
                                }
                                return spinnerRow;
                            }
                        };
                binding.addProductWeightUnitDropdown.setAdapter(spinnerAdapter1);
            }
        });
    }

    //Handle all kinds of click events
    private void onclickEvent() {
        binding.browserLayout.setOnClickListener(v -> {
            if (ReadExternalStoragePermission.isReadStoragePermissionGranted(getActivity())) {
                // startActivityForResult(new Intent(getActivity(), GalleryActivity.class), KeyWord.GALLERY_IMAGE);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 6);


            }

        });
        binding.addProductCamera.setOnClickListener(v -> {
            if (ReadExternalStoragePermission.isReadStoragePermissionGranted(getActivity())) {
                dispatchTakePictureIntent();
            }
        });
        binding.addProductForwardArrow.setOnClickListener(v -> {
            if (layoutManager != null && addProductImageAdapter != null) {
                if (layoutManager.findLastCompletelyVisibleItemPosition() < (addProductImageAdapter.getItemCount() - 1)) {
                    layoutManager.scrollToPosition(layoutManager.findLastCompletelyVisibleItemPosition() + 1);
                }
            } else {
                Toast.makeText(getActivity(), "Choose product image", Toast.LENGTH_SHORT).show();
            }

        });
        binding.addProductBackArrow.setOnClickListener(v -> {
            if (layoutManager != null && addProductImageAdapter != null) {
                if (layoutManager.findLastCompletelyVisibleItemPosition() < (addProductImageAdapter.getItemCount() + 1)) {
                    layoutManager.scrollToPosition(layoutManager.findLastCompletelyVisibleItemPosition() - 1);
                } else {
                    Toast.makeText(getActivity(), "Choose product image", Toast.LENGTH_SHORT).show();

                }
            }

        });
        binding.addProductUnitDropdown
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        unitType = unitTypeList.get(position).getUnitTypeId();
                        if (unitTypeList.get(position).getUnitType().equalsIgnoreCase("piece")) {
                            binding.addProductWeightOfUnitLayout.setVisibility(View.VISIBLE);
                            isUnitTypeSelected = true;
                        } else {
                            binding.addProductWeightOfUnitLayout.setVisibility(View.GONE);
                            isUnitTypeSelected = false;
                            unitWeightQuantity = KeyWord.BLANK;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        binding.addProductWeightUnitDropdown
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (isUnitTypeSelected) {
                            if (weightUnitTypeList.get(position).getUnitTypeId().equals("0")) {
                                unitWeightType = KeyWord.BLANK;
                            } else {
                                unitWeightType = weightUnitTypeList.get(position).getUnitTypeId();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
        binding.btnAdd.setOnClickListener(v -> {
            if (TextUtils.isEmpty(binding.addProductCategoryDropdown.getText())) {
                productCategory = KeyWord.BLANK;
            } else {
                productCategory = binding.addProductCategoryDropdown.getText().toString();
            }
           /* if (TextUtils.isEmpty(binding.addProductProductTypeDropdown.getText())) {
                type = KeyWord.BLANK;
            } else {
                type = binding.addProductProductTypeDropdown.getText().toString();
            }*/
            if (TextUtils.isEmpty(binding.addProductBrandDropdown.getText())) {
                brand = KeyWord.BLANK;
            } else {
                brand = binding.addProductBrandDropdown.getText().toString();
            }
          /*  if (TextUtils.isEmpty(binding.addProductSupplierDropdown.getText())) {
                supplier = KeyWord.BLANK;
            } else {
                supplier = binding.addProductSupplierDropdown.getText().toString();
            }*/
            if (TextUtils.isEmpty(binding.addProductProductDetails.getText())) {
                description = KeyWord.BLANK;
            } else {
                description = binding.addProductProductDetails.getText().toString();
            }
            if (TextUtils.isEmpty(binding.addProductWeightUnit.getText())) {
                unitWeightQuantity = KeyWord.BLANK;
            } else {
                unitWeightQuantity = binding.addProductWeightUnit.getText().toString();
            }

            if (TextUtils.isEmpty(binding.addProductProductName.getText())) {
                binding.addProductProductName.setError("Enter product name");
                binding.addProductProductName.requestFocus();
            } else if (TextUtils.isEmpty(binding.addProductRetailSellPrice.getText())) {
                binding.addProductRetailSellPrice.setError("Enter retail sell price");
                binding.addProductRetailSellPrice.requestFocus();
            } else if (TextUtils.isEmpty(binding.totalQuantity.getText())) {
                binding.totalQuantity.setError("Enter product quantity");
                binding.totalQuantity.requestFocus();
            } else if (TextUtils.isEmpty(binding.addProductTotalPriceWholesell.getText())) {
                binding.addProductTotalPriceWholesell.setError("Enter total price");
                binding.addProductTotalPriceWholesell.requestFocus();
            } else if (TextUtils.isEmpty(binding.addProductUnitEt.getText())) {
                binding.addProductUnitError.setVisibility(View.VISIBLE);
                binding.addProductUnitEt.requestFocus();
            }  else {
                if (utility.isNetworkAvailable()) {
                    utility.showProgress(false, "");
                    MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    builder.addFormDataPart("category", productCategory)
                            .addFormDataPart("type", type)
                            .addFormDataPart("brand", brand)
                            .addFormDataPart("supplier", supplier)
                            .addFormDataPart("title", binding.addProductProductName.getText().toString())
                            .addFormDataPart("size", size)
                            .addFormDataPart("unitType", unitType)
                            .addFormDataPart("unitQuantity", binding.addProductUnitEt.getText().toString())
                            .addFormDataPart("unitWeightType", unitWeightType)
                            .addFormDataPart("unitWeightQuantity", unitWeightQuantity)
                            .addFormDataPart("unitPrice", binding.addProductRetailSellPrice.getText().toString())
                            .addFormDataPart("totalQuantity", binding.totalQuantity.getText().toString())
                            .addFormDataPart("totalPrice", binding.addProductTotalPriceWholesell.getText().toString())
                            .addFormDataPart("description", description);


                    if (addedRecyclerViewData.size() > 0){
                        for (Attribute attribute: addedRecyclerViewData) {
                            builder.addFormDataPart("attributeData", String.valueOf(attribute.getId()));
                        }
                    } else {
                        builder.addFormDataPart("attributeData", "");
                    }



                    for (String image : imageListAll) {
                        Log.d("image", "onclickEvent: " + image);
                        File file = new File(image);
                        builder.addFormDataPart("image", "image",
                                RequestBody.create(MultipartBody.FORM, file));

                    }
                    RequestBody requestBody = builder.build();
                    viewModel.createProduct(requestBody)
                            .observe(getViewLifecycleOwner(), api_response -> {
                                if (api_response.getCode() == 200) {
                                    utility.hideProgress();
                                    navController.navigate(R.id.action_nav_add_product_to_nav_product_added_successfully);
                                } else {
                                    utility.showDialog(api_response.getData().getAsString());
                                    utility.hideProgress();
                                }
                            });
                } else {
                    utility.showDialog(getString(R.string.check_internet_connection));
                }
            }
        });
        binding.cancel.setOnClickListener(v -> {
            navController.navigate(R.id.action_nav_add_product_to_nav_dashboard);

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == KeyWord.GALLERY_IMAGE) {
            imageList.clear();
            if (data != null) {
                imageList = data.getStringArrayListExtra("image");
                if (imageList != null) {
                    imageListAll.addAll(imageList);
                }
            }
        } else if (requestCode == KeyWord.CAMERA_IMAGE) {
            imageListAll.add(currentPhotoPath);
        } else if (requestCode == 6) {
            try {
                if (data.getClipData() == null) {
                    imageListAll.add(FilePath.getPath(requireActivity(), data.getData()));
                } else {
                    for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                        imageListAll.add(FilePath.getPath(getActivity(), data.getClipData().getItemAt(i).getUri()));
                    }
                }
            } catch (Exception e) {
                Log.d("TAG", "onActivityResult: " + e);
            }
            Log.d("Image", "onActivityResult: " + imageListAll);
        }
        addProductImageAdapter = new AddProductImageAdapter(getActivity(), imageListAll);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.addProductProductImageRecyclerview.setLayoutManager(layoutManager);
        binding.addProductProductImageRecyclerview.setAdapter(addProductImageAdapter);
        addProductImageAdapter.notifyDataSetChanged();
    }

    // Create camera image file name
    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //Capture Image using camera
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            try {
                photoFile = createImageFile();
            } catch (Exception e) {
                Log.d("Error Line Number", Log.getStackTraceString(e));
            }
            if (photoFile != null) {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                    photoURI = Uri.fromFile(photoFile);
                } else {
                    photoURI = FileProvider.getUriForFile(getActivity(),
                            "com.bikroybaba.seller",
                            photoFile);
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, KeyWord.CAMERA_IMAGE);
            }
        }
    }

    protected void hideKeyboard(View view) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}