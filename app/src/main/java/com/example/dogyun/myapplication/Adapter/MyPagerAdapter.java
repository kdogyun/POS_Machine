package com.example.dogyun.myapplication.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.dogyun.myapplication.ChartFragment;
import com.example.dogyun.myapplication.ColorFragment;
import com.example.dogyun.myapplication.HistoryFragment;
import com.example.dogyun.myapplication.ThirdFragment;

import java.util.ArrayList;

/**
 * Created by dogyun on 2018-08-23.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mData;
    public static HistoryFragment historyFragment = new HistoryFragment();
    public static ChartFragment chartFragment = new ChartFragment();
    public static ColorFragment colorFragment = new ColorFragment();
    public static ThirdFragment thirdFragment = new ThirdFragment();

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);

        mData = new ArrayList<>();
        mData.add(thirdFragment);
        mData.add(historyFragment);
        mData.add(colorFragment);
        mData.add(chartFragment);
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
