package com.bikroybaba.seller.ui.delivery;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bikroybaba.seller.R;
import com.bikroybaba.seller.adapter.DeliveryAreaAdapter;
import com.bikroybaba.seller.adapter.PopupWindowAdapter;
import com.bikroybaba.seller.databinding.FragmentDeliveryAreaBinding;
import com.bikroybaba.seller.databinding.RecyclerviewDeliveryAreaBinding;
import com.bikroybaba.seller.model.entity.Area;
import com.bikroybaba.seller.model.entity.DeliveryArea;
import com.bikroybaba.seller.model.entity.DeliveryCity;
import com.bikroybaba.seller.model.remote.request.City;
import com.bikroybaba.seller.model.remote.request.District;
import com.bikroybaba.seller.model.remote.request.Division;
import com.bikroybaba.seller.model.remote.request.UpdateDeliveryArea;
import com.bikroybaba.seller.model.remote.response.DeliveryDistrict;
import com.bikroybaba.seller.model.remote.response.DeliveryDivision;
import com.bikroybaba.seller.util.KeyWord;
import com.bikroybaba.seller.util.Utility;
import com.bikroybaba.seller.viewmodel.DeliveryAreaViewModel;
import com.moktar.zmvvm.base.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;


public class DeliveryAreaFragment extends BaseFragment<DeliveryAreaViewModel, FragmentDeliveryAreaBinding> {

    private FragmentDeliveryAreaBinding binding;
    private Utility utility;
    private DeliveryAreaViewModel viewModel;
    private List<Division> divisionList;
    private List<District> districtList;
    private List<City> cityList;
    private List<Area> areaList;
    private int divisionSelected = -1, districtSelected = -1, citySelected = -1, areaSelected = -1;
    private String divisionId = "", districtId = "", cityId = "", areaId = "";
    private String language;


    public DeliveryAreaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_delivery_area, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        utility = new Utility(getActivity());
        initViewModel();
        changeLanguage();
        observeDeliveryAreaData();
        viewModel.error().observe(requireActivity(), aBoolean -> {
            if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                if (aBoolean) {
                    utility.showDialog(getString(R.string.already_added));
                }
            }
        });
        // Click event for popup menu
        binding.deliveryAreaDivision.setOnClickListener(v -> {
            showDivisionDialog(getString(language.equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.select_division : R.string.select_division_bn), divisionList);
        });
        binding.deliveryAreaDistrict.setOnClickListener(v -> {
            if (districtList.size() > 0) {
                showDistrictDialog(getString(language.equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.select_district : R.string.select_district_bn), districtList);

            } else {
                utility.showToast(getString(R.string.no_district_found));
            }
        });
        binding.deliveryAreaCity.setOnClickListener(v -> {
            if (cityList.size() > 0) {
                showCityDialog(getString(language.equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.select_city : R.string.select_city_bn), cityList);
            } else {
                utility.showToast(getString(R.string.no_city_found));
            }
        });
        binding.deliveryAreaArea.setOnClickListener(v -> {
            if (areaList.size() > 0) {
                showAreaDialog(getString(language.equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.select_area : R.string.select_area_bn), areaList);
            } else {
                utility.showToast(getString(R.string.no_area_found));
            }
        });
        binding.deliveryAreaSave.setOnClickListener(v -> {
            if (!divisionId.equals("") && districtId.equals("") && cityId.equals("") && areaId.equals("")) {
                UpdateDeliveryArea updateDeliveryArea = new UpdateDeliveryArea(divisionId, "", "", "", "");
                viewModel.updateDeliveryArea(updateDeliveryArea);
            } else if (!districtId.equals("")
                    && !divisionId.equals("")
                    && cityId.equals("")
                    && areaId.equals("")) {
                UpdateDeliveryArea updateDeliveryArea =
                        new UpdateDeliveryArea("", "", "", districtId, "");
                viewModel.updateDeliveryArea(updateDeliveryArea);
            } else if (!cityId.equals("")
                    && !districtId.equals("")
                    && !divisionId.equals("")
                    && areaId.equals("")) {
                UpdateDeliveryArea updateDeliveryArea =
                        new UpdateDeliveryArea("", "", cityId, "", "");
                viewModel.updateDeliveryArea(updateDeliveryArea);
            } else if (!areaId.equals("")
                    && !cityId.equals("")
                    && !districtId.equals("")
                    && !divisionId.equals("")) {
                UpdateDeliveryArea updateDeliveryArea =
                        new UpdateDeliveryArea("", areaId, "", "", "");
                viewModel.updateDeliveryArea(updateDeliveryArea);
            } else {
                utility.showDialog(getString(language.equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.select_deliveryArea : R.string.select_deliveryArea_bn));
            }
        });
    }

    private void changeLanguage() {
        language = utility.getLangauge();
        binding.deliveryAreaDivisionTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.division_bn) : getString(R.string.division));
        binding.deliveryAreaDistrictTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.district_bn) : getString(R.string.district));
        binding.deliveryAreaCityTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.city_bn) : getString(R.string.city));
        binding.deliveryAreaAreaTv.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.area_bn) : getString(R.string.area));
        binding.deliveryAreaSelectDivision.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.select_division_bn) : getString(R.string.select_division));
        binding.deliveryAreaSelectDistrict.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.select_district_bn) : getString(R.string.select_district));
        binding.deliveryAreaSelectCity.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.select_city_bn) : getString(R.string.select_city));
        binding.deliveryAreaSelectArea.setText(language.equals(KeyWord.BANGLA) ? getString(R.string.select_area_bn) : getString(R.string.select_area));
//        binding.deliveryAreaSave.setText(language.equalsIgnoreCase(KeyWord.BANGLA) ? getString(R.string.save_bn) : getString(R.string.save));
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(DeliveryAreaViewModel.class);
        divisionList = new ArrayList<>();
        districtList = new ArrayList<>();
        cityList = new ArrayList<>();
        areaList = new ArrayList<>();
    }

    @Override
    public int setContent() {
        return R.layout.fragment_delivery_area;
    }

    // Get delivery area & division list for popup window
    private void observeDeliveryAreaData() {
        getDeliveryArea();
        viewModel.getDivisionList().observe(getViewLifecycleOwner(), divisions -> {
            if (divisions.size() > 0) {
                divisionList.clear();
                divisionList.addAll(divisions);
            }
        });
        viewModel.getUpdateDeliveryAreaResponse().observe(requireActivity(),
                apiResponse -> {
                    if (apiResponse.getCode() == 200) {
                        getDeliveryArea();
                        divisionId = "";
                        districtId = "";
                        cityId = "";
                        areaId = "";
                        binding.selectedDivisionLayout.setVisibility(View.GONE);
                        binding.selectedDistrictLayout.setVisibility(View.GONE);
                        binding.selectedCityLayout.setVisibility(View.GONE);
                        binding.selectedAreaLayout.setVisibility(View.GONE);
                        utility.showToast(apiResponse.getData().getAsString());
                    } else if ((apiResponse.getCode() == 333)) {
                        utility.showDialog(apiResponse.getData().getAsString());
                    }
                });
    }

    private void getDeliveryArea() {
        viewModel.getDeliveryArea().observe(requireActivity(), deliveryArea -> {
            initDivisionAdapter(deliveryArea.getDivisions(), "");
            initDistrictAdapter(deliveryArea.getDistricts());
            initCityAdapter(deliveryArea.getCities());
            initAreaAdapter(deliveryArea.getAreas());
        });
    }

    // Getting District List
    private void observeDistrictData(Division division) {
        viewModel.getDistrictList(division).observe(getViewLifecycleOwner(), districts -> {
            districtList.clear();
            districtList.addAll(districts);
        });
    }

    //Getting City List
    private void observeCityData(District district) {
        viewModel.getCityList(district).observe(getViewLifecycleOwner(), cities -> {
            cityList.clear();
            cityList.addAll(cities);
        });
    }

    //Getting Area List for popup window
    private void observeAreaData(City city) {
        viewModel.getAreaList(city).observe(getViewLifecycleOwner(), areas -> {
            areaList.addAll(areas);
        });
    }

    // Initialize adapter to show division,district,city,area
    @SuppressLint("NotifyDataSetChanged")
    private void initDivisionAdapter(List<DeliveryDivision> divisions, String from) {
        DeliveryAreaAdapter<DeliveryDivision> divisionAdapter =
                new DeliveryAreaAdapter<DeliveryDivision>(getActivity(), divisions) {
                    @Override
                    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                        RecyclerviewDeliveryAreaBinding binding =
                                DataBindingUtil.inflate(LayoutInflater.from(getActivity()),
                                        R.layout.recyclerview_delivery_area, parent, false);
                        return new ViewHolder(binding);
                    }

                    @Override
                    public void onBindData(RecyclerView.ViewHolder holder,
                                           DeliveryDivision division, int position) {
                        ViewHolder divisionViewHolder = (ViewHolder) holder;
                        divisionViewHolder.binding.recyclerviewDeliveryAreaTitle
                                .setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? division.getDivision() : division.getDivBl());
                        divisionViewHolder.binding.recyclerviewDeliveryAreaClear.setOnClickListener(v -> {
                            showDeleteDialog(division.getId(), division.getDivision());
                        });
                    }
                };
        if (divisions.size() == 0) {
            binding.deliveryAreaSelectDivision.setVisibility(View.VISIBLE);
            binding.deliveryAreaDivisionRecyclerView.setVisibility(View.GONE);
        } else {
            binding.deliveryAreaSelectDivision.setVisibility(View.GONE);
            binding.deliveryAreaDivisionRecyclerView.setVisibility(View.VISIBLE);
            StaggeredGridLayoutManager staggeredGridLayoutManager =
                    new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
            binding.deliveryAreaDivisionRecyclerView.setLayoutManager(staggeredGridLayoutManager);
            binding.deliveryAreaDivisionRecyclerView.setAdapter(divisionAdapter);
        }
        divisionAdapter.notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    private void showDeleteDialog(String id, String name) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_yes_no);
        Button yes = dialog.findViewById(R.id.dialog_yes);
        Button no = dialog.findViewById(R.id.dialog_no);
        TextView title = dialog.findViewById(R.id.dialog_title);
        TextView subTitle = dialog.findViewById(R.id.dialog_subTitle);
        title.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? getString(R.string.are_you_want) : getString(R.string.are_you_want_bn) + name + "?");
        yes.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.yes : R.string.yes_bn);
        no.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.no : R.string.no_bn);
        subTitle.setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? R.string.delete_message : R.string.delete_message_bn);
        yes.setOnClickListener(v -> {
            UpdateDeliveryArea updateDeliveryArea =
                    new UpdateDeliveryArea("", "", "", "", id);
            viewModel.updateDeliveryArea(updateDeliveryArea);
            dialog.dismiss();

        });
        no.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();

    }

    @SuppressLint("NotifyDataSetChanged")
    private void initDistrictAdapter(List<DeliveryDistrict> districts) {
        DeliveryAreaAdapter<DeliveryDistrict> districtAdapter =
                new DeliveryAreaAdapter<DeliveryDistrict>(getActivity(), districts) {
                    @Override
                    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                        RecyclerviewDeliveryAreaBinding binding =
                                DataBindingUtil.inflate(LayoutInflater.from(getActivity()),
                                        R.layout.recyclerview_delivery_area, parent, false);
                        return new ViewHolder(binding);
                    }

                    @Override
                    public void onBindData(RecyclerView.ViewHolder holder,
                                           DeliveryDistrict district, int position) {
                        ViewHolder viewHolder = (ViewHolder) holder;
                        viewHolder.binding.recyclerviewDeliveryAreaTitle
                                .setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? district.getDistrict() : district.getDisBl());
                        viewHolder.binding.recyclerviewDeliveryAreaClear.setOnClickListener(v -> {
                            showDeleteDialog(district.getId(), district.getDistrict());
                        });
                    }
                };

        if (districts.size() == 0) {
            binding.deliveryAreaSelectDistrict.setVisibility(View.VISIBLE);
            binding.deliveryAreaDistrictRecyclerView.setVisibility(View.GONE);
            districtAdapter.notifyDataSetChanged();
        } else {
            binding.deliveryAreaSelectDistrict.setVisibility(View.GONE);
            binding.deliveryAreaDistrictRecyclerView.setVisibility(View.VISIBLE);
            StaggeredGridLayoutManager staggeredGridLayoutManager =
                    new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
            binding.deliveryAreaDistrictRecyclerView.setLayoutManager(staggeredGridLayoutManager);
            binding.deliveryAreaDistrictRecyclerView.setAdapter(districtAdapter);
            districtAdapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initCityAdapter(List<DeliveryCity> cities) {
        DeliveryAreaAdapter<DeliveryCity> cityAdapter =
                new DeliveryAreaAdapter<DeliveryCity>(getActivity(), cities) {
                    @Override
                    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                        RecyclerviewDeliveryAreaBinding binding =
                                DataBindingUtil.inflate(LayoutInflater.from(getActivity()),
                                        R.layout.recyclerview_delivery_area, parent, false);
                        return new ViewHolder(binding);
                    }

                    @Override
                    public void onBindData(RecyclerView.ViewHolder holder,
                                           DeliveryCity city, int position) {
                        ViewHolder viewHolder = (ViewHolder) holder;
                        viewHolder.binding.recyclerviewDeliveryAreaTitle
                                .setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? city.getCity() : city.getCityBl());
                        viewHolder.binding.recyclerviewDeliveryAreaClear.setOnClickListener(v -> {
                            showDeleteDialog(city.getId(), city.getCity());
                        });
                    }
                };
        if (cities.size() == 0) {
            binding.deliveryAreaSelectCity.setVisibility(View.VISIBLE);
            binding.deliveryAreaCityRecyclerview.setVisibility(View.GONE);
            cityAdapter.notifyDataSetChanged();

        } else {
            binding.deliveryAreaSelectCity.setVisibility(View.GONE);
            binding.deliveryAreaCityRecyclerview.setVisibility(View.VISIBLE);
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
            binding.deliveryAreaCityRecyclerview.setLayoutManager(staggeredGridLayoutManager);
            binding.deliveryAreaCityRecyclerview.setAdapter(cityAdapter);
            cityAdapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initAreaAdapter(List<DeliveryArea> areas) {
        DeliveryAreaAdapter<DeliveryArea> areaAdapter =
                new DeliveryAreaAdapter<DeliveryArea>(getActivity(), areas) {
                    @Override
                    public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                        RecyclerviewDeliveryAreaBinding binding =
                                DataBindingUtil.inflate(LayoutInflater.from(getActivity()),
                                        R.layout.recyclerview_delivery_area, parent, false);
                        return new ViewHolder(binding);
                    }

                    @Override
                    public void onBindData(RecyclerView.ViewHolder holder,
                                           DeliveryArea area, int position) {
                        ViewHolder viewHolder = (ViewHolder) holder;
                        viewHolder.binding.recyclerviewDeliveryAreaTitle
                                .setText(utility.getLangauge().equalsIgnoreCase(KeyWord.ENGLISH) ? area.getArea() : area.getAreaBl());
                        viewHolder.binding.recyclerviewDeliveryAreaClear.setOnClickListener(v -> {
                            showDeleteDialog(area.getId(), area.getArea());
                        });
                    }
                };
        if (areas.size() == 0) {
            binding.deliveryAreaSelectArea.setVisibility(View.VISIBLE);
            binding.deliveryAreaAreaRecyclerview.setVisibility(View.GONE);
        } else {
            binding.deliveryAreaSelectArea.setVisibility(View.GONE);
            binding.deliveryAreaAreaRecyclerview.setVisibility(View.VISIBLE);
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
            binding.deliveryAreaAreaRecyclerview.setLayoutManager(staggeredGridLayoutManager);
            binding.deliveryAreaAreaRecyclerview.setAdapter(areaAdapter);
            areaAdapter.notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RecyclerviewDeliveryAreaBinding binding;

        public ViewHolder(RecyclerviewDeliveryAreaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void showDivisionDialog(String selectText, List<Division> divisions) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom_spinner);
        ListView listView = dialog.findViewById(R.id.listItem);
        EditText search = dialog.findViewById(R.id.search);
        TextView select = dialog.findViewById(R.id.select);
        select.setText(selectText);
        dialogDivisionAdapter(divisions, listView, dialog);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textLength = s.length();
                divisionSelected = -1;
                ArrayList<Division> tempArrayList = new ArrayList<>();
                for (Division c : divisions) {
                    if (textLength <= c.getDivisionName().length()) {
                        if (c.getDivisionName().toLowerCase()
                                .contains(s.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }
                if (tempArrayList.size() > 0) {
                    dialogDivisionAdapter(tempArrayList, listView, dialog);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dialog.show();
    }

    private void dialogDivisionAdapter(List<Division> divisions, ListView listView, Dialog dialog) {
        PopupWindowAdapter<Division> adapter = new PopupWindowAdapter<Division>(divisions) {

            @Override
            public View getCustomView(int position, View view, ViewGroup parent) {
                Division model = getItem(position);
                View view1 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_list_popup, parent, false);
                TextView label = view1.findViewById(R.id.popup_menu_title);
                label.setText(model.getDivisionName());
                if (position == divisionSelected) {
                    label.setTextColor(requireActivity().getResources().getColor(R.color.green));
                    label.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_24, 0, 0, 0);
                }
                return view1;
            }
        };
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Division item = adapter.getItem(position);
            divisionId = item.getDivisionId();
            districtId = "";
            cityId = "";
            areaId = "";
            divisionSelected = position;
            districtSelected = -1;
            citySelected = -1;
            areaSelected = -1;
            binding.selectedDivisionLayout.setVisibility(View.VISIBLE);
            binding.deliveryAreaSelectedDivision.setText(item.getDivisionName());
            observeDistrictData(new Division(item.getDivisionId()));
            adapter.notifyDataSetChanged();
            dialog.dismiss();
        });
        listView.setAdapter(adapter);
    }

    public void showDistrictDialog(String selectText, List<District> districts) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom_spinner);
        ListView listView = dialog.findViewById(R.id.listItem);
        EditText search = dialog.findViewById(R.id.search);
        TextView select = dialog.findViewById(R.id.select);
        select.setText(selectText);
        dialogDistrictAdapter(districts, listView, dialog);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textLength = s.length();
                districtSelected = -1;
                ArrayList<District> tempArrayList = new ArrayList<>();
                for (District c : districts) {
                    if (textLength <= c.getDistrictName().length()) {
                        if (c.getDistrictName().toLowerCase().contains(s.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }

                if (tempArrayList.size() > 0) {

                    dialogDistrictAdapter(tempArrayList, listView, dialog);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dialog.show();
    }

    private void dialogDistrictAdapter(List<District> districts, ListView listView, Dialog dialog) {
        PopupWindowAdapter<District> adapter = new PopupWindowAdapter<District>(districts) {

            @Override
            public View getCustomView(int position, View view, ViewGroup parent) {
                District model = getItem(position);
                View view1 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_list_popup, parent, false);
                TextView label = view1.findViewById(R.id.popup_menu_title);
                label.setText(model.getDistrictName());
                if (position == districtSelected) {
                    label.setTextColor(requireActivity().getResources().getColor(R.color.green));
                    label.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_24, 0, 0, 0);
                }
                return view1;
            }
        };
        listView.setOnItemClickListener((parent, view, position, id) -> {
            District item = adapter.getItem(position);
            districtSelected = position;
            citySelected = -1;
            areaSelected = -1;

            districtId = item.getDistrictId();
            cityId = "";
            areaId = "";
            adapter.notifyDataSetChanged();
            dialog.dismiss();
            binding.selectedDistrictLayout.setVisibility(View.VISIBLE);
            binding.deliveryAreaSelectedDistrict.setText(item.getDistrictName());
            observeCityData(new District(item.getDistrictId()));
        });
        listView.setAdapter(adapter);
    }

    public void showCityDialog(String selectText, List<City> cities) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom_spinner);
        ListView listView = dialog.findViewById(R.id.listItem);
        EditText search = dialog.findViewById(R.id.search);
        TextView select = dialog.findViewById(R.id.select);
        select.setText(selectText);
        dialogCityAdapter(cities, listView, dialog);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textLength = s.length();
                citySelected = -1;
                ArrayList<City> tempArrayList = new ArrayList<>();
                for (City c : cities) {
                    if (textLength <= c.getCityName().length()) {
                        if (c.getCityName().toLowerCase()
                                .contains(s.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }
                if (tempArrayList.size() > 0) {
                    dialogCityAdapter(tempArrayList, listView, dialog);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dialog.show();
    }

    private void dialogCityAdapter(List<City> cities, ListView listView, Dialog dialog) {
        PopupWindowAdapter<City> adapter = new PopupWindowAdapter<City>(cities) {

            @Override
            public View getCustomView(int position, View view, ViewGroup parent) {
                City model = getItem(position);
                View view1 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_list_popup, parent, false);
                TextView label = view1.findViewById(R.id.popup_menu_title);
                label.setText(model.getCityName());
                if (position == citySelected) {
                    label.setTextColor(requireActivity().getResources().getColor(R.color.green));
                    label.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_24, 0, 0, 0);
                }
                return view1;
            }
        };
        listView.setOnItemClickListener((parent, view, position, id) -> {
            City item = adapter.getItem(position);
            citySelected = position;
            areaSelected = -1;
            cityId = item.getCityId();
            areaId = "";
            observeAreaData(new City(item.getCityId()));
            binding.selectedCityLayout.setVisibility(View.VISIBLE);
            binding.deliveryAreaSelectedCity.setText(item.getCityName());
            dialog.dismiss();
        });
        listView.setAdapter(adapter);
    }

    public void showAreaDialog(String selectText, List<Area> areas) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom_spinner);
        ListView listView = dialog.findViewById(R.id.listItem);
        EditText search = dialog.findViewById(R.id.search);
        TextView select = dialog.findViewById(R.id.select);
        select.setText(selectText);
        dialogAreaAdapter(areas, listView, dialog);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textLength = s.length();
                areaSelected = -1;
                ArrayList<Area> tempArrayList = new ArrayList<>();
                for (Area c : areas) {
                    if (textLength <= c.getAreaName().length()) {
                        if (c.getAreaName().toLowerCase().contains(s.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }

                if (tempArrayList.size() > 0) {

                    dialogAreaAdapter(tempArrayList, listView, dialog);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dialog.show();
    }

    private void dialogAreaAdapter(List<Area> areas, ListView listView, Dialog dialog) {
        PopupWindowAdapter<Area> adapter = new PopupWindowAdapter<Area>(areas) {

            @Override
            public View getCustomView(int position, View view, ViewGroup parent) {
                Area model = getItem(position);
                View view1 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_list_popup, parent, false);
                TextView label = view1.findViewById(R.id.popup_menu_title);
                label.setText(model.getAreaName());
                if (position == areaSelected) {
                    label.setTextColor(requireActivity().getResources().getColor(R.color.green));
                    label.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_24, 0, 0, 0);
                }
                return view1;
            }
        };
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Area item = adapter.getItem(position);
            areaSelected = position;
            areaId = item.getAreaId();
            binding.selectedAreaLayout.setVisibility(View.VISIBLE);
            binding.deliveryAreaSelectedArea.setText(item.getAreaName());
            dialog.dismiss();
        });
        listView.setAdapter(adapter);

    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        TextView textToolHeader = toolbar.findViewById(R.id.title);
        textToolHeader.setText(utility.getLangauge().equals(KeyWord.BANGLA) ? KeyWord.DELIVERY_AREA_BN : KeyWord.DELIVERY_AREA);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}