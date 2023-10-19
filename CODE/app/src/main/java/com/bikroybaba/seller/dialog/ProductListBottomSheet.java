package com.bikroybaba.seller.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.adapter.CategoryAdapter;
import com.bikroybaba.seller.adapter.IncludeExcludeAdapter;
import com.bikroybaba.seller.adapter.PopupWindowAdapter;
import com.bikroybaba.seller.databinding.LauoutProductListBottomSheetBinding;
import com.bikroybaba.seller.interfaces.CategoryInterface;
import com.bikroybaba.seller.interfaces.EmptyListInterface;
import com.bikroybaba.seller.interfaces.ILoadDataAgain;
import com.bikroybaba.seller.model.remote.request.Category;
import com.bikroybaba.seller.model.remote.request.OfferProductListRequest;
import com.bikroybaba.seller.model.remote.response.OfferProductListResponse;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;
import com.bikroybaba.seller.viewmodel.OfferViewModel;
import com.bikroybaba.seller.viewmodel.ProductViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProductListBottomSheet extends BottomSheetDialogFragment implements EmptyListInterface, CategoryInterface {

    private final List<Category> categoryList = new ArrayList<>();
    private OfferViewModel viewModel;
    private Utility utility;
    private String searchKey = "";
    private String offerId, title,language;
    private RelativeLayout toolBar;
    private AppCompatImageView search;
    private CoordinatorLayout rootLayout;
    private RecyclerView rv;
    private LauoutProductListBottomSheetBinding binding;
    private ILoadDataAgain iLoadDataAgain;
    private ListPopupWindow listPopupWindow;
    private boolean popMenuOpen = false;
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
    private Disposable disposable;
    private boolean isCategoryExpanded =false;
    private ProductViewModel productViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //View v = inflater.inflate(R.layout.lauout_product_list_bottom_sheet, container, false);
        binding = LauoutProductListBottomSheetBinding.inflate(inflater,container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
        utility = new Utility(requireActivity());

        initData();
        initAction();
        loadProductListData();
        initObserver();
        initSearchBox();
    }

    private void initSearchBox() {
        binding.searchProduct.setIconifiedByDefault(false);
        binding.searchProduct.setQueryHint(getString(R.string.search));
        binding.srProducts.setOnRefreshListener(() -> {
            if (utility.isNetworkAvailable()){
                binding.srProducts.setVisibility(View.VISIBLE);
                binding.noInternetLayout.setVisibility(View.GONE);
                binding.srProducts.setRefreshing(true);
                observeOfferProduct(offerId,KeyWord.FILTERED_BY_ALL);
            }else {
                binding.srProducts.setVisibility(View.GONE);
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
                            binding.srProducts.setRefreshing(true);
                            if (!emitter.isDisposed()) {
                                emitter.onNext(newText); // Pass the query to the emitter
                            }
                        }else {
                            searchKey="";
                            observeOfferProduct(offerId,KeyWord.FILTERED_BY_ALL);
                            utility.hideKeyboard(binding.getRoot());
                            binding.srProducts.setRefreshing(false);
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

    private void initObserver() {
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
                            CategoryAdapter adapter = new CategoryAdapter(categoryList,getActivity(), ProductListBottomSheet.this, popup);
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

    private void initAction() {
        binding.close.setOnClickListener(l -> {
            dismiss();
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
    }

    private void initData() {
        Bundle bundle = this.getArguments();
        if (bundle != null){
            offerId = bundle.getString(KeyWord.OFFER_ID);
        }
    }

    private void loadProductListData() {
        binding.cpIndicator.show();
        OfferProductListRequest offerProductListRequest =
                new OfferProductListRequest(offerId, KeyWord.FILTERED_BY_ALL, "");
        viewModel.getOfferProductList(offerProductListRequest, ProductListBottomSheet.this)
                .observe(requireActivity(), products -> {
                    initAdapter(products);
                });
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialog;
    }

    private void initViewModel(){
        viewModel = new ViewModelProvider(this).get(OfferViewModel.class);
        productViewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);
    }

    public void setiLoadDataAgain(ILoadDataAgain iLoadDataAgain) {
        this.iLoadDataAgain = iLoadDataAgain;
    }

    private void observeOfferProduct(String offerId, String filteredByNew) {
        OfferProductListRequest offerProductListRequest =
                new OfferProductListRequest(offerId,filteredByNew,searchKey);
//        binding.includeExcludeSwipeRefresh.setRefreshing(false);
        viewModel.getOfferProductList(offerProductListRequest, ProductListBottomSheet.this)
                .observe(requireActivity(), offerProductListResponses -> {
                    if (utility.isNetworkAvailable()){
                        binding.rvBottomSheet.setVisibility(View.VISIBLE);
                        binding.noInternetLayout.setVisibility(View.GONE);
                        initAdapter(offerProductListResponses);
                    }else {
                        binding.rvBottomSheet.setVisibility(View.GONE);
                        binding.noInternetLayout.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void initAdapter(PagedList<OfferProductListResponse> offerProductList) {
        binding.srProducts.setRefreshing(false);
        IncludeExcludeAdapter adapter = new IncludeExcludeAdapter(getActivity(), offerId, viewModel);
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.rvBottomSheet.setLayoutManager(mLayoutManager);
        binding.rvBottomSheet.setAdapter(adapter);
        adapter.submitList(offerProductList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        iLoadDataAgain.loadAgain();

    }

    @Override
    public void order(int size) {
        if (size == 0) {
            binding.llNoDataFound.setVisibility(View.VISIBLE);
            binding.rvBottomSheet.setVisibility(View.GONE);
        } else {
            binding.llNoDataFound.setVisibility(View.GONE);
            binding.rvBottomSheet.setVisibility(View.VISIBLE);
        }
        binding.cpIndicator.hide();
    }

    @Override
    public void categoryId(String categoryId) {
        observeOfferProduct(offerId,categoryId);
    }
}
