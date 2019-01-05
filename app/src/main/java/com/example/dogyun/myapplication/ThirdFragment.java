package com.example.dogyun.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dogyun.myapplication.Adapter.MyPagerAdapter;

/**
 * Created by dogyun on 2018-09-05.
 */

public class ThirdFragment extends Fragment{
    public ThirdFragment() {
    }

    Button bt2, bt1, bt3;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_third, null);

//        bt1 = (Button) rootView.findViewById(R.id.addMenu);
//        bt1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MainActivity.dbHelper.insertMenu("쁠랑", 5000, 1);
//                MainActivity.getMenu();
//                ColorFragment.adapter.notifyDataSetChanged();
//                MyPagerAdapter.chartFragment.chartFirstDraw();
//            }
//        });
//
//        bt2 = (Button) rootView.findViewById(R.id.updateMenu);
//        bt2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MainActivity.dbHelper.insertMenu("전복죽", 11000, 2);
//                MainActivity.getMenu();
//                ColorFragment.adapter.notifyDataSetChanged();
//                MyPagerAdapter.chartFragment.chartFirstDraw();
//            }
//        });
//
//        bt3 = (Button) rootView.findViewById(R.id.addCategory);
//        bt3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MainActivity.dbHelper.insertMenu("샌드위치", 8000, 2);
//                MainActivity.getMenu();
//                ColorFragment.adapter.notifyDataSetChanged();
//                MyPagerAdapter.chartFragment.chartFirstDraw();
//            }
//        });

        return rootView;
    }

}
