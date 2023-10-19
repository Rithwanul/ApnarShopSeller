package com.bikroybaba.seller.ui.report;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.adapter.GenericAdapter;
import com.bikroybaba.seller.adapter.ReportViewPagerAdapter;
import com.bikroybaba.seller.databinding.FragmentReportBinding;
import com.bikroybaba.seller.model.remote.request.Category;
import com.bikroybaba.seller.model.remote.request.ProductType;
import com.bikroybaba.seller.model.remote.response.ProductName;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;
import com.bikroybaba.seller.viewmodel.ReportViewModel;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class ReportFragment extends Fragment {

    private Toolbar toolbar;
    private MaterialTextView textToolHeader;
    private ReportViewPagerAdapter adapter;
    private FragmentReportBinding binding;
    private Utility utility;
    private String language;

//    private ProductViewModel productViewModel;
    private ReportViewModel viewModel;
    private String categoryId, productTypeId, productId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_report, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
        utility = new Utility(getActivity());
        language = utility.getLangauge();
        adapter = new ReportViewPagerAdapter(getChildFragmentManager());
        changeLanguage();
        binding.viewPager.setAdapter(adapter);
        binding.tabs.setupWithViewPager(binding.viewPager);
        getCategory();
    }

    private void changeLanguage() {
        binding.reportCategoryTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.category_bn) : getString(R.string.category));
        binding.reportProductNameTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.product_type_bn) : getString(R.string.product_type));
        binding.reportProductTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.product_name_bn_) : getString(R.string.product_name));
        adapter.addFragment(new AllReportFragment(), getString(R.string.all));
        adapter.addFragment(new WeeklyReportFragment(), language.equals(KeyWord.BANGLA) ? getString(R.string.weekly_bn) : getString(R.string.weekly));
        adapter.addFragment(new MonthlyReportFragment(), language.equals(KeyWord.BANGLA) ? getString(R.string.monthly_bn) : getString(R.string.monthly));
        adapter.addFragment(new YearlyReportFragment(), language.equals(KeyWord.BANGLA) ? getString(R.string.yearly_bn) : getString(R.string.yearly));
        adapter.addFragment(new DateReportFragment(), language.equals(KeyWord.BANGLA) ? getString(R.string.date_bn) : getString(R.string.date));
    }

    private void initViewModel() {
//        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        viewModel = new ViewModelProvider(this).get(ReportViewModel.class);
    }

    private void loadProduct(List<ProductName> productNameList) {
        GenericAdapter<ProductName> adapter = new GenericAdapter<ProductName>(getActivity(), productNameList) {
            @Override
            public View getCustomView(int position, View view, ViewGroup parent) {
                ViewHolder holder;

                if (view == null) {

                    holder = new ViewHolder();

                    view = LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner_item,
                            parent, false);
                    holder.title = view.findViewById(R.id.custom_spinner);

                    holder.title.setText(productNameList.get(position).getProductName());


                    view.setTag(holder);
                }
                return view;
            }
        };
        binding.reportProductNameDropdown.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        binding.reportProductNameDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (productNameList.get(position).getProductId().equals("0")) {
                    productId = "ALL";
                    getActivity().sendBroadcast(new Intent("com.aapnarshop.seller.VIEW.FRAGMENT.report").putExtra("categoryId", categoryId)
                            .putExtra("productTypeId", productTypeId).putExtra("productId", productId));
                    utility.setProductId(productId);

                } else {
                    productId = productNameList.get(position).getProductId();
                    getActivity().sendBroadcast(new Intent("com.aapnarshop.seller.VIEW.FRAGMENT.report").putExtra("categoryId", categoryId)
                            .putExtra("productTypeId", productTypeId).putExtra("productId", productId));
                    utility.setProductId(productId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getCategory() {
        viewModel.getProductCategory().observe(requireActivity(), categories -> {
            List<Category> categoryList = new ArrayList<>();
            categoryList.clear();
            categoryList.add(new Category("All", "0"));
            categoryList.addAll(categories);
            GenericAdapter<Category> adapter =
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
            binding.reportCategoryDropdown.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            binding.reportCategoryDropdown
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (categoryList.get(position).getCategoryId().equals("0")) {
                        categoryId = "ALL";
                        utility.setCategoryId(categoryId);
                        List<ProductType> productTypeList = new ArrayList<>();
                        productTypeList.add(new ProductType("0", "All"));
                        loadProductType(productTypeList);
                    } else {
                        categoryId = categoryList.get(position).getCategoryId();
                        utility.setCategoryId(categoryId);
                        Category category = new Category();
                        category.setCategoryId(categoryId);
                        getProductType(category);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        });

    }

    private void loadProductType(List<ProductType> productTypeList) {
        GenericAdapter<ProductType> adapter =
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
        binding.reportProductTypeDropdown.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        binding.reportProductTypeDropdown
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (productTypeList.get(position).getTypeId().equals("0")) {
                    productTypeId = "ALL";
                    utility.setProductTypeId(productTypeId);
                    List<ProductName> productNameList = new ArrayList<>();
                    productNameList.add(new ProductName("0", "All"));
                    loadProduct(productNameList);
                } else {
                    productTypeId = productTypeList.get(position).getTypeId();
                    utility.setProductTypeId(productTypeId);
                    ProductType productType = new ProductType();
                    productType.setTypeId(productTypeId);
                    getProduct(productType);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getProduct(ProductType productType) {

        viewModel.getProduct(productType).observe(requireActivity(), productNames -> {
            List<ProductName> productNameList = new ArrayList<>();
            productNameList.clear();
            productNameList.add(new ProductName("0", "All"));
            productNameList.addAll(productNames);
            loadProduct(productNameList);
        });
    }

    private void getProductType(Category category) {

        viewModel.getProductTypes(category).observe(requireActivity(), productTypes -> {
            List<ProductType> productTypeList = new ArrayList<>();
            productTypeList.clear();
            productTypeList.add(new ProductType("0", "All"));
            productTypeList.addAll(productTypes);
            loadProductType(productTypeList);
        });
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable
                                              Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toolbar = requireActivity().findViewById(R.id.toolbar);
        textToolHeader = toolbar.findViewById(R.id.title);
//        textToolHeader.setText(language.equals(KeyWord.BANGLA) ? KeyWord.REPORT_BN : getActivity().getResources().getString(R.string.report));
    }
}