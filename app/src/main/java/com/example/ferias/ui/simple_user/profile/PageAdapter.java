package com.example.ferias.ui.simple_user.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;


public class PageAdapter extends FragmentStatePagerAdapter {

    private ArrayList<String> list_Titles = new ArrayList<>();
    private ArrayList<Fragment> list_Fragments = new ArrayList<>();

    public PageAdapter(FragmentManager  fa) {
        super(fa);
    }

    public void addFragment(Fragment fragment,String title) {
        list_Fragments.add(fragment);
        list_Titles.add(title);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return list_Fragments.get(position);
    }

    @Override
    public int getCount() {
        return list_Fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return list_Titles.get(position);
    }
}