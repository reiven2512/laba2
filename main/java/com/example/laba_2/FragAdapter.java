package com.example.laba_2;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class FragAdapter extends FragmentPagerAdapter {
    List<Info> data;
    public FragAdapter(FragmentManager fm, List<Info> data){
        super(fm);
        this.data = data;
    }
    public int getCount(){
        return 87;
    }
    public Fragment getItem(int position){
        return FragItem.newInstance(data.get(position));
    }
}