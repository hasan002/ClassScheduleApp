package com.example.hasan.schedule.TeacherActivity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter{

    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String > tabTitles = new ArrayList<>();

    public void addFragments(Fragment fragments,String  titles){

        this.fragments.add(fragments);
        this.tabTitles.add(titles);
    }



    ViewPagerAdapter(FragmentManager fm){
        super(fm);

    }
    @Override
    public Fragment getItem(int position) {

        return fragments.get(position);
    }

    @Override
    public int getCount() {

        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return tabTitles.get(position);
    }

}
