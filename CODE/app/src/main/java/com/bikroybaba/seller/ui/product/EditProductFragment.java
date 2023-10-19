package com.bikroybaba.seller.ui.product;

import static com.bikroybaba.seller.ui.HomeActivity.navController;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SwitchCompat;
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
import com.bikroybaba.seller.databinding.FragmentEditProductBinding;
import com.bikroybaba.seller.databinding.RecyclerviewDeliveryAreaBinding;
import com.bikroybaba.seller.interfaces.DialogListerner;
import com.bikroybaba.seller.model.entity.Attribute;
import com.bikroybaba.seller.model.entity.Brand;
import com.bikroybaba.seller.model.entity.Image;
import com.bikroybaba.seller.model.remote.request.Category;
import com.bikroybaba.seller.model.remote.request.Product;
import com.bikroybaba.seller.model.remote.request.ProductStatus;
import com.bikroybaba.seller.model.remote.request.ProductType;
import com.bikroybaba.seller.model.remote.response.AttributeItem;
import com.bikroybaba.seller.model.remote.response.AttributeProductEditResponse;
import com.bikroybaba.seller.model.remote.response.ProductDetails;
import com.bikroybaba.seller.model.remote.response.Supplier;
import com.bikroybaba.seller.model.remote.response.UnitType;
import com.bikroybaba.seller.ui.delivery.DeliveryAreaFragment;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.ReadExternalStoragePermission;
import com.bikroybaba.seller.util.Utility;
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


public class EditProductFragment extends BaseFragment<ProductViewModel, FragmentEditProductBinding>
        implements AddProductImageAdapter.ListInterface {

    private final List<String> imageList = new ArrayList<>();
    private final List<Category> categories = new ArrayList<>();
    private final List<Supplier> supplierList = new ArrayList<>();
    private final List<ProductType> productTypeList = new ArrayList<>();
    private final String type = KeyWord.BLANK;
    private final String supplier = KeyWord.BLANK;
    private final List<String> newImageList = new ArrayList<>();
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private FragmentEditProductBinding binding;
    private ProductViewModel viewModel;
    private Toolbar toolbar;
    private MaterialTextView textToolHeader, switchTextView, updatedDate;
    private SwitchCompat editProductSwitch;
    private final List<Brand> brandList = new ArrayList<>();
    private final List<UnitType> unitTypeList = new ArrayList<>();
    private final List<UnitType> unitTypeList2 = new ArrayList<>();
    private LinearLayoutCompat linearLayout;
    private ProductType productType;
    private Product product;
    private List<String> imageListForGalleryImage = new ArrayList<>();
    private final List<String> imageFromAPI = new ArrayList<>();
    private String url;
    private String currentPhotoPath;
    private String unitType;
    private String unitWeightType = KeyWord.BLANK;
    private String unitWeightQuantity = KeyWord.BLANK;
    private String productCategory = KeyWord.BLANK;
    private Utility utility;
    private String description = KeyWord.BLANK;
    private String brand = KeyWord.BLANK;
    private Category category;
    private final String size = KeyWord.BLANK;
    private File file;
    private Uri photoURI;
    boolean isUnitTypeSelected = false;
    private File photoFile = null;
    private ProductDetails productDetails;
    private final ArrayList<Attribute> addedRecyclerViewData = new ArrayList<Attribute>();
    private final ArrayList<String> attributeNames = new ArrayList<>();
    private final List<Attribute> attributes = new ArrayList<>();
    private final ArrayList<Attribute> attributeArrayList = new ArrayList<>();
    private final ArrayList<Attribute> attributeDataNameArrayList = new ArrayList<>();
    private final ArrayList<String> _attributeDataName = new ArrayList<>();
    private String retailPrice = "", totalQuantity = "";
    private DeliveryAreaAdapter<Attribute> attributeDataNameAdapter;
    private AttributeArrayListAdapter mAttributeArrayListAdapter;
    private AttributeDataNameArrayListAdapter mAttributeDataNameArrayListAdapter;
    private String attributeId = "";
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
    private String attributeDataNameId = "";
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
    private boolean mIsAttributeSpinnerOpen;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil
                .inflate(LayoutInflater.from(getContext()),
                        R.layout.fragment_edit_product, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hideInputKey();
        utility = new Utility(getActivity());
//        language = utility.getLangauge();
        initViewModel();
        category = new Category();
        productType = new ProductType();
        product = new Product(EditProductFragmentArgs.fromBundle(getArguments()).getProductId());
//        changeLanguage(language);
        utility.showProgress(false, "");
        setUpSpinners();
        observeFilteredData();
        onItemClickListener();
        onTextChangeListener();
        onClick();
        viewModel.getProductUpdateResponse().observe(getViewLifecycleOwner(), apiResponse -> {
            if (apiResponse.getCode() == 200) {
                utility.hideProgress();
                utility.showDialog("Product edit successful");
            } else {
                utility.showDialog(apiResponse.getData().getAsString());
                utility.hideProgress();
            }
        });
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);
    }

    @Override
    public int setContent() {
        return R.layout.fragment_edit_product;
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

   /* private void changeLanguage(String language) {
        binding.editProductCategoryTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.category_bn) : getString(R.string.category));
//        binding.editProductProductTypeTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.product_type_bn) : getString(R.string.product_type));
        binding.editProductBrandTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.brand_bn) : getString(R.string.brand));
//        binding.editProductSupplierTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.supplier_bn) : getString(R.string.supplier));
        binding.editProductProductNameTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.product_name_bn) : getString(R.string.product_name));
        // binding.editProductProductSizeTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.product_size_bn) : getString(R.string.product_size));
        binding.editProductUnitTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.unit_size_bn) : getString(R.string.unit_size));
        binding.editProductWeightOfUnitTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.weight_of_the_unit_bn) : getString(R.string.weight_of_the_unit));
        binding.editProductRetailSellPriceTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.retail_sell_price_per_unit_bn) : getString(R.string.retail_sell_price_per_unit));
        binding.editProductTkTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.tk_bn) : getString(R.string.tk));
        binding.addProductTkTv2.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.tk_bn) : getString(R.string.tk));
        binding.editProductTotalQuantityTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.total_quantity_bn) : getString(R.string.total_quantity));
        binding.editProductTotalPriceWholesellTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.total_price_wholesale_bn) : getString(R.string.total_price_wholesale));
        binding.editProductProductDetailsTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.product_details_bn) : getString(R.string.product_details));
//        binding.cancel.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.cancel_bn) : getString(R.string.cancel));
        binding.btnUpdate.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.update_bn) : getString(R.string.update));
        binding.editProductBrowse.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.browse_bn) : getString(R.string.browse));
        binding.editProductCategory.setHint(language.equalsIgnoreCase(KeyWord.BANGLA) ? getString(R.string.type_product_category_bn) : getString(R.string.type_product_category));
//        binding.editProductProductType.setHint(language.equalsIgnoreCase(KeyWord.BANGLA) ? getString(R.string.type_product_type_bn) : getString(R.string.type_product_type));
        binding.editProductBrand.setHint(language.equalsIgnoreCase(KeyWord.BANGLA) ? getString(R.string.type_brand_name_bn) : getString(R.string.type_brand_name));
//        binding.editProductSupplier.setHint(language.equalsIgnoreCase(KeyWord.BANGLA) ? getString(R.string.type_supplier_name_bn) : getString(R.string.type_supplier_name));
        binding.editProductProductName.setHint(language.equalsIgnoreCase(KeyWord.BANGLA) ? getString(R.string.write_product_name_bn) : getString(R.string.write_product_name));
        // binding.editProductProductSize.setHint(language.equalsIgnoreCase(KeyWord.BANGLA)?getString(R.string.write_product_size_bn): getString(R.string.write_product_size));
        binding.editProductDetails.setHint(language.equalsIgnoreCase(KeyWord.BANGLA) ? getString(R.string.hint_product_details_bn) : getString(R.string.hint_product_details));
    }*/

    private void onItemClickListener() {
        binding.editProductCategory.setOnItemClickListener((parent, view, position, id) -> {
            category.setCategoryId(categories.get(position).getCategoryId());
            binding.editProductCategory.setText(categories.get(position).getCategoryTitle());
            binding.editProductCategory.setSelection(categories.get(position).getCategoryTitle().length());

        });
      /*  binding.editProductSupplier.setOnItemClickListener((parent, view, position, id) -> {
            binding.editProductSupplier.setText(supplierList.get(position).getSupplierTitle());
            binding.editProductSupplier
                    .setSelection(supplierList.get(position).getSupplierTitle().length());

        });*/
     /*   binding.editProductProductType.setOnItemClickListener((parent, view, position, id) -> {
            productType.setTypeId(productTypeList.get(position).getTypeId());
            binding.editProductProductType.setText(productTypeList.get(position).getTypeTitle());
            binding.editProductProductType
                    .setSelection(productTypeList.get(position).getTypeTitle().length());

        });*/
        binding.editProductBrand.setOnItemClickListener((parent, view, position, id) -> {
            binding.editProductBrand.setText(brandList.get(position).getBrandTitle());
            binding.editProductBrand.setSelection(brandList.get(position).getBrandTitle().length());

        });
        binding.editProductUnitDropdown
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unitType = unitTypeList.get(position).getUnitTypeId();
                if (unitTypeList.get(position).getUnitType().equalsIgnoreCase("piece")) {
                    binding.editProductWeightOfUnitLayout.setVisibility(View.VISIBLE);
                    isUnitTypeSelected = true;
                    for (int i = 0; i < unitTypeList2.size(); i++) {
                        if (productDetails.getUnitWeight().getUnitId()
                                .equals(unitTypeList2.get(i).getUnitTypeId())) {
                            binding.editProductWeightUnitDropdown.setSelection(i);
                        }
                    }
                } else {
                    binding.editProductWeightOfUnitLayout.setVisibility(View.GONE);
                    isUnitTypeSelected = false;
                    unitWeightQuantity = KeyWord.BLANK;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.editProductWeightUnitDropdown
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isUnitTypeSelected) {
                    if (unitTypeList2.get(position).getUnitTypeId().equals("0")) {
                        unitWeightType = KeyWord.BLANK;
                    } else {
                        unitWeightType = unitTypeList2.get(position).getUnitTypeId();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void onTextChangeListener() {
        binding.editProductCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    viewModel.searchCategory(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
/*
        binding.editProductSupplier.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    viewModel.searchSupplier(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
*/
/*
        binding.editProductProductType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    viewModel.searchProductType(s.toString(), category);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
*/
        binding.editProductBrand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    viewModel.searchBrand(s.toString(), productType);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.editProductRetailSellPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (binding.editProductRetailSellPrice.getText().toString().equals("")) {
                } else {
                    if (s.length() > 0) {
                        retailPrice = s.toString();
                    } else {
                        retailPrice = "";
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.editTotalQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.editTotalQuantity.getText().toString().equals("")) {
                    if (s.length() > 0) {
                        totalQuantity = s.toString();
                        Toast.makeText(getActivity(), totalQuantity
                                + "" + retailPrice, Toast.LENGTH_SHORT).show();
                    } else {
                        totalQuantity = "";
                    }
                } else {
                    if (s.length() > 0) {
                        totalQuantity = s.toString();
                        binding.editProductTotalPriceWholesell
                                .setText(String.valueOf(Integer.parseInt(totalQuantity)
                                        * Integer.parseInt(retailPrice)));
                    } else {
                        totalQuantity = "";
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void onClick() {
        binding.browserLayout.setOnClickListener(v -> {
            if (ReadExternalStoragePermission.isReadStoragePermissionGranted(getActivity())) {
                //startActivityForResult(new Intent(getActivity(), GalleryActivity.class), KeyWord.GALLERY_IMAGE);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 6);
            }
        });

        binding.editProductCamera.setOnClickListener(v -> {
            if (ReadExternalStoragePermission.isReadStoragePermissionGranted(getActivity())) {
                dispatchTakePictureIntent();
            }
        });

        binding.btnUpdate.setOnClickListener(v -> {
            if (TextUtils.isEmpty(binding.editProductCategory.getText())) {
                productCategory = KeyWord.BLANK;
            } else {
                productCategory = binding.editProductCategory.getText().toString();
            }
          /*  if (TextUtils.isEmpty(binding.editProductProductType.getText())) {
                type = KeyWord.BLANK;
            } else {
                type = binding.editProductProductType.getText().toString();
            }*/
            if (TextUtils.isEmpty(binding.editProductBrand.getText())) {
                brand = KeyWord.BLANK;
            } else {
                brand = binding.editProductBrand.getText().toString();
            }
          /*  if (TextUtils.isEmpty(binding.editProductSupplier.getText())) {
                supplier = KeyWord.BLANK;
            } else {
                supplier = binding.editProductSupplier.getText().toString();
            }*/

            if (TextUtils.isEmpty(binding.editProductDetails.getText())) {
                description = KeyWord.BLANK;
            } else {
                description = binding.editProductDetails.getText().toString();
            }
            if (TextUtils.isEmpty(binding.editProductWeightUnit.getText())) {
                unitWeightQuantity = KeyWord.BLANK;
            } else {
                unitWeightQuantity = binding.editProductWeightUnit.getText().toString();
            }

            if (TextUtils.isEmpty(binding.editProductProductName.getText())) {
                binding.editProductProductName.setError("Enter product name");
                binding.editProductProductName.requestFocus();
            } else if (TextUtils.isEmpty(binding.editProductRetailSellPrice.getText())) {
                binding.editProductRetailSellPrice.setError("Enter retail sell price");
                binding.editProductRetailSellPrice.requestFocus();
            } else if (TextUtils.isEmpty(binding.editTotalQuantity.getText())) {
                binding.editTotalQuantity.setError("Enter product quantity");
                binding.editTotalQuantity.requestFocus();
            } else if (TextUtils.isEmpty(binding.editProductTotalPriceWholesell.getText())) {
                binding.editProductTotalPriceWholesell.setError("Enter total price");
                binding.editProductTotalPriceWholesell.requestFocus();
            } else if (TextUtils.isEmpty(binding.editProductUnit.getText())) {
                binding.editProductUnitError.setVisibility(View.VISIBLE);
                binding.editProductUnit.requestFocus();
            } else {

                if (utility.isNetworkAvailable()) {
                    utility.showProgress(false, "");

                    MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    builder.addFormDataPart("productId", product.getProductId())
                            .addFormDataPart("category", productCategory)
                            .addFormDataPart("type", type)
                            .addFormDataPart("brand", brand)
                            .addFormDataPart("supplier", supplier)
                            .addFormDataPart("title", binding.editProductProductName.getText().toString())
                            .addFormDataPart("size", size)
                            .addFormDataPart("unitType", unitType)
                            .addFormDataPart("unitQuantity", binding.editProductUnit.getText().toString())
                            .addFormDataPart("unitWeightType", unitWeightType)
                            .addFormDataPart("unitWeightQuantity", unitWeightQuantity)
                            .addFormDataPart("unitPrice", binding.editProductRetailSellPrice.getText().toString())
                            .addFormDataPart("totalQuantity", binding.editTotalQuantity.getText().toString())
                            .addFormDataPart("totalPrice", binding.editProductTotalPriceWholesell.getText().toString())
                            .addFormDataPart("description", description);

                    if (addedRecyclerViewData.size() > 0){
                        for (Attribute attribute: addedRecyclerViewData) {
                            builder.addFormDataPart("attributeData", String.valueOf(attribute.getId()));
                        }
                    }else {
                        builder.addFormDataPart("attributeData", "");
                    }



                    if (imageFromAPI.size() > 0) {
                        for (String image : imageFromAPI) {
                            builder.addFormDataPart("oldImages", image);
                        }
                    }
                    if (newImageList.size() > 0) {
                        for (String image : newImageList) {
                            file = new File(image);
                            builder.addFormDataPart("newImage", "image",
                                    RequestBody.create(MultipartBody.FORM, file));

                        }
                    }

                    RequestBody requestBody = builder.build();
                    viewModel.updateProduct(requestBody);
                } else {
                    utility.showDialog(getString(R.string.check_internet_connection));
                }
            }
        });

        binding.cancel.setOnClickListener(v -> {
            navController.navigate(R.id.action_nav_edit_product_to_nav_all_product);
        });

        binding.getRoot().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    if (!binding.spinnerAttribute.isInsideSearchEditText(motionEvent)) {
                        binding.spinnerAttribute.hideEdit();
                    }
                    if (!binding.spinnerAttributeDataName.isInsideSearchEditText(motionEvent)) {
                        binding.spinnerAttributeDataName.hideEdit();
                    }
                }
                return true;
            }
        });

    }


    private void observeFilteredData() {

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
                            holder.title.setText(categoryList.get(position).getCategoryTitle());
                            view.setTag(holder);
                        }
                        return view;
                    }
                };
                binding.editProductCategory.setAdapter(autocompleteAdapter);
                autocompleteAdapter.notifyDataSetChanged();
            }

        });
/*
        viewModel.getFilteredSupplierList().observe(requireActivity(), suppliers -> {
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
//                binding.editProductSupplier.setAdapter(autocompleteAdapter);
                autocompleteAdapter.notifyDataSetChanged();

            }

        });
*/
/*
        viewModel.getFilteredProductTypeList().observe(requireActivity(), productTypes -> {

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
//                binding.editProductProductType.setAdapter(autocompleteAdapter);
                autocompleteAdapter.notifyDataSetChanged();
            }

        });
*/
        viewModel.getFilteredBandList().observe(requireActivity(), brands -> {
            if (brands.size() > 0) {
                brandList.clear();
                brandList.addAll(brands);
                GenericAdapter<Brand> autocompleteAdapter =
                        new GenericAdapter<Brand>(getActivity(), brandList) {
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
                binding.editProductBrand.setAdapter(autocompleteAdapter);
                autocompleteAdapter.notifyDataSetChanged();
            }
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


    private void observeData() {
        viewModel.getUnitType().observe(requireActivity(), unitTypes -> {
            unitTypeList.clear();
            unitTypeList.addAll(unitTypes);
            unitTypeList2.add(new UnitType("Select", "0"));
            for (UnitType unitType : unitTypes) {
                if (!unitType.getUnitType().equalsIgnoreCase("piece")) {
                    unitTypeList2.add(unitType);
                }
            }

            GenericAdapter<UnitType> spinnerAdapter =
                    new GenericAdapter<UnitType>(getActivity(), unitTypes) {
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
            binding.editProductUnitDropdown.setAdapter(spinnerAdapter);
            if (unitTypeList2.size() > 0) {
                GenericAdapter<UnitType> spinnerAdapter1 =
                        new GenericAdapter<UnitType>(getActivity(), unitTypeList2) {
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
                binding.editProductWeightUnitDropdown.setAdapter(spinnerAdapter1);
            }
        });

        viewModel.sendProductDetailsRequest(product);
        viewModel.getProductDetails().observe(requireActivity(), productDetails -> {
            this.productDetails = productDetails;
            if (!productDetails.getCategory().getCategoryTitle().equalsIgnoreCase("BLANK")) {
                binding.editProductCategory.setText(productDetails.getCategory().getCategoryTitle());
            }
            if (!productDetails.getTitle().equalsIgnoreCase("BLANK")) {
                binding.editProductProductName.setText(productDetails.getTitle());
            }
           /* if (!productDetails.getType().getTypeTitle().equalsIgnoreCase("BLANK")) {
                binding.editProductProductType.setText(productDetails.getType().getTypeTitle());
            }*/
            if (!productDetails.getBrand().getBrandTitle().equalsIgnoreCase("BLANK")) {
                binding.editProductBrand.setText(productDetails.getBrand().getBrandTitle());
            }
           /* if (!productDetails.getSupplier().getSupplierTitle().equalsIgnoreCase("BLANK")) {
                binding.editProductSupplier.setText(productDetails.getSupplier().getSupplierTitle());
            }*/

            if (!productDetails.getUnit().getUnitQuantity().equalsIgnoreCase("BLANK")) {
                binding.editProductUnit.setText(productDetails.getUnit().getUnitQuantity());

            }
            if (!productDetails.getUnitPrice().equalsIgnoreCase("BLANK")) {
                binding.editProductRetailSellPrice
                        .setText(String.valueOf(Math.round(Float.parseFloat(productDetails.getUnitPrice()))));

            }
            if (!productDetails.getTotalQuantity().equalsIgnoreCase("BLANK")) {
                binding.editTotalQuantity.setText(productDetails.getTotalQuantity());

            }
            if (!productDetails.getTotalPrice().equalsIgnoreCase("BLANK")) {
                binding.editProductTotalPriceWholesell.setText(productDetails.getTotalPrice());

            }
            if (!productDetails.getDescription().equalsIgnoreCase("BLANK")) {
                binding.editProductDetails.setText(productDetails.getDescription());

            }
            if (!productDetails.getUnitWeight().getUnitQuantity().equalsIgnoreCase("BLANK")) {
                binding.editProductWeightUnit.setText(productDetails.getUnitWeight().getUnitQuantity());

            }
            for (int i = 0; i < unitTypeList.size(); i++) {
                if (productDetails.getUnit().getUnitId().equals(unitTypeList.get(i).getUnitTypeId())) {
                    binding.editProductUnitDropdown.setSelection(i);
                }
            }
            for (Image image : productDetails.getImages()) {
                url = image.getUrl();
                imageList.add(url);
                imageFromAPI.clear();
                imageFromAPI.addAll(imageList);
                initAdapterForProductImage(imageFromAPI);
            }

            if (productDetails.getStatus().equalsIgnoreCase("ACTIVE")) {
                editProductSwitch.setChecked(true);
                switchTextView.setText(getString(R.string.active_product));
            } else {
                editProductSwitch.setChecked(false);
                switchTextView.setText(getString(R.string.inactive_product));

            }
            updatedDate.setVisibility(View.VISIBLE);
            updatedDate.setText(getString(R.string.updated_date) + sdf.format(new Date(Long.parseLong(productDetails.getUpdatedAt()))));
            utility.hideProgress();

            /**
             *  Processing attribute data
             *  converting response to attribute
             */
            ArrayList<AttributeProductEditResponse> attributes = productDetails.getAttributes();
            convertAttributeResponseToAttribute(attributes);

            initRecyclerViewForAttributeData();
        });

        observeAttributeData();
    }

    private void initRecyclerViewForAttributeData() {
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
                    Utility.showDeleteDialog(requireContext(), getString(R.string.are_you_want),
                            getString(R.string.common_delete_message), new DialogListerner() {
                        @Override
                        public void yesOnClick(View v, Dialog dialog) {
                            Attribute removedAttribute = addedRecyclerViewData.get(position);
                            viewModel.deleteAttribute(removedAttribute.getId()).observe(requireActivity(),
                                    apiResponse -> {
                                if (apiResponse.getCode() == 200) {
                                    addedRecyclerViewData.remove(removedAttribute);
                                    attributeDataNameAdapter.notifyItemRemoved(position);
                                    dialog.dismiss();
                                } else if (apiResponse.getCode() == 403) {
                                    addedRecyclerViewData.remove(removedAttribute);
                                    attributeDataNameAdapter.notifyItemRemoved(position);
                                    dialog.dismiss();
                                }
                            });

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
        if (addedRecyclerViewData.size() > 0){
            binding.rvAddedAttributes.setVisibility(View.VISIBLE);
        }
    }

    private void convertAttributeResponseToAttribute(ArrayList<AttributeProductEditResponse> attributesResponse) {
        addedRecyclerViewData.clear();
        for (AttributeProductEditResponse item : attributesResponse) {
            List<AttributeItem> attributeDataNameResponse = item.getAttribute();
            for (AttributeItem attributeResponseItem : attributeDataNameResponse) {
                Attribute attribute = new Attribute();
                attribute.setAttributeName(item.getAttributeName());
                attribute.setAttributeDataName(attributeResponseItem.getAttributeData());
                attribute.setId(attributeResponseItem.getAttributeDataId());
                addedRecyclerViewData.add(attribute);
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initAdapterForProductImage(List<String> images) {
        AddProductImageAdapter addProductImageAdapter =
                new AddProductImageAdapter(getActivity(), images, EditProductFragment.this);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.addProductProductImageRecyclerview.setLayoutManager(layoutManager);
        binding.addProductProductImageRecyclerview.setAdapter(addProductImageAdapter);
        addProductImageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == KeyWord.GALLERY_IMAGE) {
            imageListForGalleryImage.clear();
            if (data != null) {
                imageListForGalleryImage = data.getStringArrayListExtra("image");
                if (imageListForGalleryImage != null) {
                    newImageList.addAll(imageListForGalleryImage);
                }
            }

        } else if (requestCode == KeyWord.CAMERA_IMAGE) {
            newImageList.add(currentPhotoPath);
        } else if (requestCode == 6) {
            try {
                if (data.getClipData() == null) {
                    newImageList.add(FilePath.getPath(requireActivity(), data.getData()));
                } else {
                    for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                        newImageList.add(FilePath.getPath(getActivity(), data.getClipData().getItemAt(i).getUri()));
                    }
                }
                imageList.addAll(newImageList);
                initAdapterForProductImage(imageList);
            } catch (Exception e) {
                Log.d("TAG", "onActivityResult: " + e);
            }
        }
    }

    // Create camera image file name

    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
                    photoURI = FileProvider.getUriForFile(requireActivity(),
                            "com.bikroybaba.seller",
                            photoFile);
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, KeyWord.CAMERA_IMAGE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        linearLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        linearLayout.setVisibility(View.GONE);
        updatedDate.setVisibility(View.GONE);
    }

    @Override
    public void listIndex(int index) {
        imageFromAPI.remove(index);

    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable
                                              Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toolbar = requireActivity().findViewById(R.id.toolbar);
        textToolHeader = toolbar.findViewById(R.id.title);
        updatedDate = toolbar.findViewById(R.id.updatedTime);
        switchTextView = toolbar.findViewById(R.id.switch_textView);
        linearLayout = toolbar.findViewById(R.id.switch_layout_for_edit_product);
        editProductSwitch = toolbar.findViewById(R.id.edit_product_switch);
        textToolHeader.setText(getString(R.string.edit_product));
        if (utility.isNetworkAvailable()) {
            observeData();
        } else {
            utility.showDialog(getString(R.string.check_internet_connection));
        }
        editProductSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                if (isChecked) {
                    ProductStatus productStatus = new ProductStatus(product.getProductId(), KeyWord.ACTIVE);
                    switchTextView.setText(getString(R.string.active_product));
                    viewModel.updateProductStatus(productStatus);
                } else {
                    ProductStatus productStatus = new ProductStatus(product.getProductId(), KeyWord.INACTIVE);
                    switchTextView.setText(getString(R.string.inactive_product));
                    viewModel.updateProductStatus(productStatus);
                }
            }
        });
    }

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

    /*@Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
            if (!binding.spinnerAttribute.isInsideSearchEditText(motionEvent)) {
                binding.spinnerAttribute.hideEdit();
            }
            if (!binding.spinnerAttributeDataName.isInsideSearchEditText(motionEvent)) {
                binding.spinnerAttributeDataName.hideEdit();
            }
        }
        return true;
    }*/
}