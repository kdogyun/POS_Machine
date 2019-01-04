package com.example.dogyun.myapplication;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by dogyun on 2018-08-23.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mData;
    public static HistoryFragment historyFragment = new HistoryFragment();
    public static ChartFragment ChartFragment = new ChartFragment();
    public static ColorFragment ColorFragment = new ColorFragment();

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);

        mData = new ArrayList<>();
        mData.add(new ThirdFragment());
        mData.add(historyFragment);
        mData.add(ColorFragment);
        mData.add(ChartFragment);
    }

    @Override
    public Fragment getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "관리";
            case 1:
                return "영수증";
            case 2:
                return "주문";
            case 3:
                return "통계";
            default:
                return position + "번째";
        }
    }

}
