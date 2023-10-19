package com.bikroybaba.seller.ui.offer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.bikroybaba.seller.adapter.CategoryAdapter;
import com.bikroybaba.seller.adapter.IncludeExcludeAdapter;
import com.bikroybaba.seller.adapter.PopupWindowAdapter;
import com.bikroybaba.seller.databinding.FragmentIncludeExcludeBinding;
import com.bikroybaba.seller.interfaces.CategoryInterface;
import com.bikroybaba.seller.interfaces.EmptyListInterface;
import com.bikroybaba.seller.model.remote.request.AddOfferProduct;
import com.bikroybaba.seller.model.remote.request.Category;
import com.bikroybaba.seller.model.remote.request.OfferProductListRequest;
import com.bikroybaba.seller.model.remote.response.OfferProductListResponse;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;
import com.bikroybaba.seller.viewmodel.OfferViewModel;
import com.bikroybaba.seller.viewmodel.ProductViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class IncludeExcludeFragment extends Fragment implements CategoryInterface, EmptyListInterface {

    private FragmentIncludeExcludeBinding binding;
    private OfferViewModel viewModel;
    private ProductViewModel productViewModel;
    private Toolbar toolbar;
    private TextView textToolHeader;
    private String offerId, title,language;
    private Utility utility;
    private ListPopupWindow listPopupWindow;
    private boolean popMenuOpen = false;
    private boolean isCategoryExpanded =false;
    private final List<Category> categoryList = new ArrayList<>();
    private Disposable disposable;
    private String searchKey = "";
    private final OnBackPressedCallback onBackPressedCallback =
            new OnBackPressedCallback(true /* Enabled by default */) {
        @Override
        public void handleOnBackPressed() {
            // Handle the back button event
            if (popMenuOpen){
                popMenuOpen = false;
                listPopupWindow.dismiss();
                Navigation.findNavController(binding.getRoot()).popBackStack();
            }else {
                Navigation.findNavController(binding.getRoot()).popBackStack();
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_include_exclude, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
        initView();
        utility = new Utility(getActivity());
        language = utility.getLangauge();
//        changeLanguage();
        binding.includeExcludeSwipeRefresh.setRefreshing(true);
        requireActivity().getOnBackPressedDispatcher()
                .addCallback(getViewLifecycleOwner(), onBackPressedCallback);
        viewModel.getAPIResponse().observe(requireActivity(), api_response -> {
            if (api_response != null && api_response.getCode() == 200) {
                observeOfferProduct(offerId,KeyWord.FILTERED_BY_ALL);
                utility.showDialog(api_response.getData().getAsString());
            } else if (api_response!=null && api_response.getCode()==300){
                utility.showDialog(api_response.getData().getAsString());
            } else {
                utility.showDialog(api_response.getData().getAsString());
            }
        });
        binding.includeExcludeIncludeBtn.setOnClickListener(v -> {
            if (utility.isNetworkAvailable()){
                AddOfferProduct addOfferProduct =
                        new AddOfferProduct(offerId, KeyWord.TYPE_ALL, KeyWord.STATUS_INCLUDE);
                viewModel.addOfferProduct(addOfferProduct);
            }else {
                utility.showDialog(getString(R.string.check_internet_connection));
            }

        });
        binding.includeExcludeExcludeBtn.setOnClickListener(v -> {
            if (utility.isNetworkAvailable()){
                AddOfferProduct addOfferProduct =
                        new AddOfferProduct(offerId, KeyWord.TYPE_ALL, KeyWord.STATUS_EXCLUDE);
                viewModel.addOfferProduct(addOfferProduct);
            }else {
                utility.showDialog(getString(R.string.check_internet_connection));
            }
        });

        if (utility.isNetworkAvailable()){
            binding.includeExcludeSwipeRefresh.setVisibility(View.VISIBLE);
            binding.noInternetLayout.setVisibility(View.GONE);
            binding.includeExcludeSwipeRefresh.setRefreshing(true);
            observeOfferProduct(offerId,KeyWord.FILTERED_BY_ALL);
        }else {
            binding.includeExcludeSwipeRefresh.setVisibility(View.GONE);
            binding.noInternetLayout.setVisibility(View.VISIBLE);
        }
        binding.tryAgain.setOnClickListener(v -> {
            if (utility.isNetworkAvailable()){
                binding.includeExcludeSwipeRefresh.setVisibility(View.VISIBLE);
                binding.includeExcludeSwipeRefresh.setRefreshing(true);
                binding.noInternetLayout.setVisibility(View.GONE);
                binding.includeExcludeSwipeRefresh.setRefreshing(true);
                observeOfferProduct(offerId,KeyWord.FILTERED_BY_ALL);
            }else {
                binding.includeExcludeSwipeRefresh.setVisibility(View.GONE);
                binding.noInternetLayout.setVisibility(View.VISIBLE);
            }
        });
        binding.includeExcludeFilter.setOnClickListener(v -> {
            if (!popMenuOpen){
                showListPopupWindow(v);
                popMenuOpen = true;
            }else {
                popMenuOpen = false;
                listPopupWindow.dismiss();
            }
        });

        binding.includeExcludeSwipeRefresh.setOnRefreshListener(() -> {
            if (utility.isNetworkAvailable()){
                binding.includeExcludeSwipeRefresh.setVisibility(View.VISIBLE);
                binding.noInternetLayout.setVisibility(View.GONE);
                binding.includeExcludeSwipeRefresh.setRefreshing(true);
                observeOfferProduct(offerId,KeyWord.FILTERED_BY_ALL);
            }else {
                binding.includeExcludeSwipeRefresh.setVisibility(View.GONE);
                binding.noInternetLayout.setVisibility(View.VISIBLE);
            }
        });
        productViewModel.getProductCategory().observe(requireActivity(),categories -> {
            categoryList.clear();
            categoryList.addAll(categories);
        });
        Observable<String> observable = Observable.create((ObservableOnSubscribe<String>) emitter ->
                binding.searchProduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {

                if (newText.length() > 0) {
                    binding.includeExcludeSwipeRefresh.setRefreshing(true);
                    if (!emitter.isDisposed()) {
                        emitter.onNext(newText); // Pass the query to the emitter
                    }
                }else {
                    searchKey="";
                    observeOfferProduct(offerId,KeyWord.FILTERED_BY_ALL);
                    utility.hideKeyboard(binding.getRoot());
                    binding.includeExcludeSwipeRefresh.setRefreshing(false);
                }
                return false;
            }
        })).debounce(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull String s) {
                searchKey = s;
                observeOfferProduct(offerId,KeyWord.FILTERED_BY_ALL);
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void initView() {
        toolbar =  requireActivity().findViewById(R.id.toolbar);
        textToolHeader = toolbar.findViewById(R.id.title);
        binding.searchProduct.setIconifiedByDefault(false);

        offerId = IncludeExcludeFragmentArgs.fromBundle(getArguments()).getOfferId();
        title = IncludeExcludeFragmentArgs.fromBundle(getArguments()).getMessage();
        textToolHeader.setText(title.toLowerCase());
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("com.aapnarshop.seller.offerProductList");
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
    }
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int listSize = intent.getIntExtra("offerProductList", -1);
            if (listSize == 0) {
                binding.includeExcludeSwipeRefresh.setVisibility(View.GONE);
                binding.noProductFound.setVisibility(View.VISIBLE);
            } else {
                binding.includeExcludeSwipeRefresh.setVisibility(View.VISIBLE);
                binding.noProductFound.setVisibility(View.GONE);
            }
        }
    };

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(OfferViewModel.class);
        productViewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);
    }

/*    private void changeLanguage() {
        binding.includeExcludeIncludeBtn.setText(language.equalsIgnoreCase(KeyWord.BANGLA)?getString(R.string.include_all_bn):getString(R.string.include_all));
        binding.includeExcludeExcludeBtn.setText(language.equalsIgnoreCase(KeyWord.BANGLA)?getString(R.string.exclude_all_bn):getString(R.string.exclude_all));
        binding.searchProduct.setQueryHint(language.equalsIgnoreCase(KeyWord.BANGLA)?getString(R.string.search_bn):getString(R.string.search));
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().unregisterReceiver(broadcastReceiver);
        if (disposable !=null){
            disposable.dispose();
        }
    }

    // Get List Of Products
    private void observeOfferProduct(String offerId, String filteredByNew) {
        OfferProductListRequest offerProductListRequest =
                new OfferProductListRequest(offerId,filteredByNew,searchKey);
        binding.includeExcludeSwipeRefresh.setRefreshing(false);
        viewModel.getOfferProductList(offerProductListRequest, IncludeExcludeFragment.this)
                .observe(requireActivity(), offerProductListResponses -> {
            if (utility.isNetworkAvailable()){
                binding.rvIncludeExclude.setVisibility(View.VISIBLE);
                binding.noInternetLayout.setVisibility(View.GONE);
                initAdapter(offerProductListResponses);
            }else {
                binding.rvIncludeExclude.setVisibility(View.GONE);
                binding.noInternetLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initAdapter(PagedList<OfferProductListResponse> offerProductList) {
        binding.includeExcludeSwipeRefresh.setRefreshing(false);
        IncludeExcludeAdapter adapter = new IncludeExcludeAdapter(getActivity(), offerId, viewModel);
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.rvIncludeExclude.setLayoutManager(mLayoutManager);
        binding.rvIncludeExclude.setAdapter(adapter);
        adapter.submitList(offerProductList);
    }

    private void showListPopupWindow(View anchor) {
        List<String> listPopupItems = new ArrayList<>();
        listPopupItems.add("Included");
        listPopupItems.add("Excluded");
        listPopupItems.add("Category");
        listPopupItems.add("Ascending");
        listPopupItems.add("Descending");
        listPopupItems.add("New");
        listPopupItems.add("Old");
        listPopupItems.add("All");
        listPopupWindow =
                createListPopupWindow(anchor, 400, listPopupItems);
        listPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
            listPopupWindow.dismiss();
            Toast.makeText(getActivity(), "clicked at "
                    + listPopupItems.get(position), Toast.LENGTH_SHORT).show();
        });
        listPopupWindow.show();
    }

    private ListPopupWindow createListPopupWindow(View anchor, int width,
                                                  List<String> items) {
        final ListPopupWindow popup = new ListPopupWindow(requireActivity());
        PopupWindowAdapter<String> adapter = new PopupWindowAdapter<String>(items) {

            @Override
            public View getCustomView(int position, View view, ViewGroup parent) {
                String model = getItem(position);

                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_popup, parent, false);
                TextView title = view1.findViewById(R.id.popup_menu_title);
                ImageView arrow = view1.findViewById(R.id.expand_arrow);
                RecyclerView categoryRecyclerView = view1.findViewById(R.id.popup_menu_rv);

                title.setText(model);
                if (model.equalsIgnoreCase("Category")){
                    arrow.setVisibility(View.VISIBLE);
                }
                view1.setOnClickListener(v -> {
                    if(model.equalsIgnoreCase("Category")){
                        if (!isCategoryExpanded){
                            arrow.setImageResource(R.drawable.ic_baseline_expand_more_24);
                            categoryRecyclerView.setVisibility(View.VISIBLE);
                            CategoryAdapter adapter = new CategoryAdapter(categoryList,getActivity(), IncludeExcludeFragment.this, popup);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            categoryRecyclerView.setLayoutManager(mLayoutManager);
                            categoryRecyclerView.setAdapter(adapter);
                            isCategoryExpanded = true;
                        }
                        else {
                            arrow.setImageResource(R.drawable.ic_baseline_arrow_forward_ios_24);
                            categoryRecyclerView.setVisibility(View.GONE);
                            isCategoryExpanded =false;
                        }
                    }else if (model.equalsIgnoreCase("Included")){
                        observeOfferProduct(offerId,KeyWord.FILTERED_BY_INCLUDED);
                        popup.dismiss();
                    }
                    else if (model.equalsIgnoreCase("Excluded")){
                        observeOfferProduct(offerId,KeyWord.FILTERED_BY_EXCLUDED);
                        popup.dismiss();
                    }
                    else if (model.equalsIgnoreCase("Ascending")){
                        observeOfferProduct(offerId,KeyWord.FILTERED_BY_ASCENDING);
                        popup.dismiss();
                    }
                    else if (model.equalsIgnoreCase("Descending")){
                        observeOfferProduct(offerId,KeyWord.FILTERED_BY_DESCENDING);
                        popup.dismiss();
                    }
                    else if (model.equalsIgnoreCase("New")){
                        observeOfferProduct(offerId,KeyWord.FILTERED_BY_NEW);
                        popup.dismiss();
                    }
                    else if (model.equalsIgnoreCase("Old")){
                        observeOfferProduct(offerId,KeyWord.FILTERED_BY_OLD);
                        popup.dismiss();
                    }
                    else if (model.equalsIgnoreCase("All")){
                        observeOfferProduct(offerId,KeyWord.FILTERED_BY_ALL);
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
        observeOfferProduct(offerId,categoryId);
    }

    @Override
    public void order(int size) {

    }
}