package com.bikroybaba.seller.ui.product;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.adapter.AllProductAdapter;
import com.bikroybaba.seller.adapter.CategoryAdapter;
import com.bikroybaba.seller.adapter.PopupWindowAdapter;
import com.bikroybaba.seller.databinding.FragmentAllProductBinding;
import com.bikroybaba.seller.interfaces.CategoryInterface;
import com.bikroybaba.seller.interfaces.EmptyListInterface;
import com.bikroybaba.seller.model.entity.AllProduct;
import com.bikroybaba.seller.model.remote.request.Category;
import com.bikroybaba.seller.model.remote.request.FilterProduct;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;
import com.bikroybaba.seller.viewmodel.ProductViewModel;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class AllProductFragment extends Fragment implements
        CategoryInterface, AllProductAdapter.UpdateUI, EmptyListInterface {

    Toolbar toolbar;
    private FragmentAllProductBinding binding;
    private final OnBackPressedCallback onBackPressedCallback =
            new OnBackPressedCallback(true /* Enabled by default */) {
                @Override
                public void handleOnBackPressed() {
                    // Handle the back button event
                    if (popMenuOpen) {
                        popMenuOpen = false;
                        listPopupWindow.dismiss();
                        Navigation.findNavController(binding.getRoot()).popBackStack();
                    } else {
                        Navigation.findNavController(binding.getRoot()).popBackStack();
                    }
           /* if (!isScroll) {
                mLayoutManager.smoothScrollToPosition(binding.allProduct, null, 0);
                isScroll = true;

            } else {
                Navigation.findNavController(binding.getRoot()).popBackStack();

            }*/

                    //closeFragment();

                }
            };
    private final List<Category> categoryList = new ArrayList<>();
    private MaterialTextView textToolHeader;
    private boolean isCategoryExpanded = false;
    private boolean popMenuOpen = false;
    private ListPopupWindow listPopupWindow;
    boolean isScroll = false;
    private String filter = "";
    private String language;
    private ProductViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_all_product, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Utility utility = new Utility(getActivity());
        language = utility.getLangauge();
        initViewModel();
        initView();
        FilterProduct filterProduct = new FilterProduct("new", "");
        binding.allProductSearch.setIconifiedByDefault(false);
        binding.allProductSearch.setQueryHint(language.equalsIgnoreCase(KeyWord.BANGLA) ? getString(R.string.search_by_product_name_bn) : getString(R.string.search_by_product_name));

        if (getArguments() != null &&
                !AllProductFragmentArgs.fromBundle(getArguments()).getFilter().equals("BLANK")) {
            filter = AllProductFragmentArgs.fromBundle(getArguments()).getFilter();
            FilterProduct filterProductLimited = new FilterProduct(filter, "");
            if (utility.isNetworkAvailable()) {
                binding.noInternetLayout.setVisibility(View.GONE);
                binding.allProductSwipeRefresh.setVisibility(View.VISIBLE);
                binding.allProductSwipeRefresh.setRefreshing(true);
                getAllProduct(filterProductLimited);
            } else {
                getAllProduct(filterProduct);
                binding.allProductSwipeRefresh.setVisibility(View.GONE);
                binding.noInternetLayout.setVisibility(View.VISIBLE);
            }
//            textToolHeader.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.limited_product_bn) : getString(R.string.limited_product));
            binding.allProductFilter.setVisibility(View.GONE);
        } else {
            if (utility.isNetworkAvailable()) {
                binding.noInternetLayout.setVisibility(View.GONE);
                binding.allProductSwipeRefresh.setVisibility(View.VISIBLE);
                binding.allProductSwipeRefresh.setRefreshing(true);
                getAllProduct(filterProduct);
            } else {
                binding.allProductSwipeRefresh.setVisibility(View.GONE);
                binding.noInternetLayout.setVisibility(View.VISIBLE);
            }
            binding.allProductFilter.setVisibility(View.VISIBLE);
        }

        binding.allProductSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                binding.allProductSwipeRefresh.setRefreshing(true);
                if (filter.equalsIgnoreCase("limited")) {
                    getAllProduct(new FilterProduct(filter, newText));
                } else {
                    getAllProduct(new FilterProduct("new", newText));
                }
                return false;
            }
        });
        binding.allProductSwipeRefresh.setOnRefreshListener(() -> {
            if (getArguments() != null &&
                    !AllProductFragmentArgs.fromBundle(getArguments()).getFilter().equals("BLANK")) {
                filter = AllProductFragmentArgs.fromBundle(getArguments()).getFilter();
                FilterProduct filterProductLimited = new FilterProduct(filter, "");
                if (utility.isNetworkAvailable()) {
                    binding.noInternetLayout.setVisibility(View.GONE);
                    binding.allProductSwipeRefresh.setVisibility(View.VISIBLE);
                    binding.allProductSwipeRefresh.setRefreshing(true);
                    getAllProduct(filterProductLimited);
                } else {
                    binding.allProductSwipeRefresh.setVisibility(View.GONE);
                    binding.noInternetLayout.setVisibility(View.VISIBLE);
                }
            } else {
                if (utility.isNetworkAvailable()) {
                    binding.noInternetLayout.setVisibility(View.GONE);
                    binding.allProductSwipeRefresh.setVisibility(View.VISIBLE);
                    binding.allProductSwipeRefresh.setRefreshing(true);
                    getAllProduct(filterProduct);
                } else {
                    binding.allProductSwipeRefresh.setVisibility(View.GONE);
                    binding.noInternetLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.tryAgain.setOnClickListener(v -> {
            if (getArguments() != null &&
                    !AllProductFragmentArgs.fromBundle(getArguments()).getFilter().equals("BLANK")) {
                filter = AllProductFragmentArgs.fromBundle(getArguments()).getFilter();
                FilterProduct filterProductLimited = new FilterProduct(filter, "");
                if (utility.isNetworkAvailable()) {
                    binding.noInternetLayout.setVisibility(View.GONE);
                    binding.allProductSwipeRefresh.setVisibility(View.VISIBLE);
                    binding.allProductSwipeRefresh.setRefreshing(true);
                    getAllProduct(filterProductLimited);
                } else {
                    binding.allProductSwipeRefresh.setVisibility(View.GONE);
                    binding.noInternetLayout.setVisibility(View.VISIBLE);
                }
            } else {
                if (utility.isNetworkAvailable()) {
                    binding.noInternetLayout.setVisibility(View.GONE);
                    binding.allProductSwipeRefresh.setVisibility(View.VISIBLE);
                    binding.allProductSwipeRefresh.setRefreshing(true);
                    getAllProduct(filterProduct);
                } else {
                    binding.allProductSwipeRefresh.setVisibility(View.GONE);
                    binding.noInternetLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        viewModel.getProductCategory().observe(requireActivity(), categories -> {
            categoryList.clear();
            categoryList.addAll(categories);
        });

        binding.allProductFilter.setOnClickListener(v -> {
            if (!popMenuOpen) {
                showListPopupWindow(v);
                popMenuOpen = true;
            } else {
                popMenuOpen = false;
                listPopupWindow.dismiss();
            }
        });

        requireActivity().getOnBackPressedDispatcher()
                .addCallback(getViewLifecycleOwner(), onBackPressedCallback);
        viewModel.getError().observe(requireActivity(), s -> {
            binding.allProductSwipeRefresh.setRefreshing(false);
            binding.allProductSwipeRefresh.setVisibility(View.GONE);
            utility.showDialog(s);
            binding.noProductFound.setVisibility(View.VISIBLE);
        });

    }

    private void initView() {
        toolbar = requireActivity().findViewById(R.id.toolbar);
        textToolHeader = toolbar.findViewById(R.id.title);
//        textToolHeader.setText(language.equals(KeyWord.BANGLA) ? R.string.your_product_bn : R.string.your_product);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);
    }

    private void getAllProduct(FilterProduct filterProduct) {
        viewModel.getProductList(filterProduct, AllProductFragment.this)
                .observe(requireActivity(), this::initAdapter);
    }

    private void initAdapter(PagedList<AllProduct> allProducts) {
        binding.allProductSwipeRefresh.setRefreshing(false);
        AllProductAdapter adapter =
                new AllProductAdapter(requireActivity(), viewModel, AllProductFragment.this);
        LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.allProduct.setLayoutManager(mLayoutManager);
        adapter.submitList(allProducts);
        binding.allProduct.setAdapter(adapter);
    }

    private void showListPopupWindow(View anchor) {
        List<String> listPopupItems = new ArrayList<>();
        listPopupItems.add("Limited Stock");
        listPopupItems.add("Category");
        listPopupItems.add("Ascending");
        listPopupItems.add("Descending");
        listPopupItems.add("New");
        listPopupItems.add("Old");
        listPopupItems.add("Other");
        listPopupWindow = createListPopupWindow(anchor, 400, listPopupItems);
        listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
            listPopupWindow.dismiss();
            Toast.makeText(getActivity(), "clicked at " +
                    listPopupItems.get(position), Toast.LENGTH_SHORT).show();
        });
        listPopupWindow.show();
    }

    private ListPopupWindow createListPopupWindow(View anchor, int width,
                                                  List<String> items) {
        final ListPopupWindow popup = new ListPopupWindow(requireContext());
        PopupWindowAdapter<String> adapter = new PopupWindowAdapter<String>(items) {

            @Override
            public View getCustomView(int position, View view, ViewGroup parent) {
                String model = getItem(position);
                View view1 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_list_popup, parent, false);
                TextView title = view1.findViewById(R.id.popup_menu_title);
                ImageView arrow = view1.findViewById(R.id.expand_arrow);
                RecyclerView categoryRecyclerView = view1.findViewById(R.id.popup_menu_rv);
                title.setText(model);
                if (model.equalsIgnoreCase("Category")) {
                    arrow.setVisibility(View.VISIBLE);
                }
                view1.setOnClickListener(v -> {
                    if (model.equalsIgnoreCase("Category")) {
                        if (!isCategoryExpanded) {
                            arrow.setImageResource(R.drawable.ic_baseline_expand_more_24);
                            categoryRecyclerView.setVisibility(View.VISIBLE);
                            CategoryAdapter adapter =
                                    new CategoryAdapter(categoryList, getActivity(),
                                            AllProductFragment.this, popup);
                            RecyclerView.LayoutManager mLayoutManager =
                                    new LinearLayoutManager(getActivity(),
                                            LinearLayoutManager.VERTICAL, false);
                            categoryRecyclerView.setLayoutManager(mLayoutManager);
                            categoryRecyclerView.setAdapter(adapter);
                            isCategoryExpanded = true;
                        } else {
                            arrow.setImageResource(R.drawable.ic_baseline_arrow_forward_ios_24);
                            categoryRecyclerView.setVisibility(View.GONE);
                            isCategoryExpanded = false;
                        }
                    } else if (model.equalsIgnoreCase("limited stock")) {
                        FilterProduct filterProduct = new FilterProduct("limited", "");
                        getAllProduct(filterProduct);
                        popup.dismiss();
                    } else {
                        FilterProduct filterProduct = new FilterProduct(model, "");
                        getAllProduct(filterProduct);
                        popup.dismiss();
                    }
                });
                return view1;
            }
        };
        popup.setAnchorView(anchor);
        popup.setWidth(width);
        popup.setAdapter(adapter);
        popup.setDropDownGravity(Gravity.END);
        return popup;
    }

    @Override
    public void categoryId(String categoryId) {
        FilterProduct filterProduct = new FilterProduct(categoryId, "");
        getAllProduct(filterProduct);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }

    private void closeFragment() {
        // Disable to close fragment
        requireActivity().getOnBackPressedDispatcher().onBackPressed();
    }


    @Override
    public void update() {
        getAllProduct(new FilterProduct("new", ""));
    }

    @Override
    public void order(int size) {
        if (size == 0) {
            binding.allProductSwipeRefresh.setVisibility(View.GONE);
            binding.noProductFound.setVisibility(View.VISIBLE);
        } else {
            binding.allProductSwipeRefresh.setVisibility(View.VISIBLE);
            binding.noProductFound.setVisibility(View.GONE);
        }
    }
/*
    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable
                                              Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toolbar = requireActivity().findViewById(R.id.toolbar);
        textToolHeader = toolbar.findViewById(R.id.title);
        textToolHeader.setText(language.equals(KeyWord.BANGLA) ? R.string.your_product_bn : R.string.your_product);
    }*/
}