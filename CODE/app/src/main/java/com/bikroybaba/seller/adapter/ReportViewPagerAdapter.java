package com.bikroybaba.seller.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class ReportViewPagerAdapter extends FragmentStatePagerAdapter {


    private final ArrayList<Fragment> fragmentList;
    private final ArrayList<String> fragmentTitleList;

    public ReportViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
        fragmentTitleList = new ArrayList<>();

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentTitleList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }


    public void addFragment(Fragment fragment, String title){
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }
}
