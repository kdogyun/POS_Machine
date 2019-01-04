package com.example.dogyun.myapplication;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dogyun.myapplication.Bill.BillType;
import com.example.dogyun.myapplication.Bill.GetBill;
import com.example.dogyun.myapplication.dummy.DummyContent;
import com.github.mikephil.charting.charts.LineChart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static DBHelper dbHelper;
    SharedPreferences prefs;
    public static Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //권한이 부여되어 있는지 확인
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED){
            //권한있음
        }else{
            //권한없음
/*
            //권한설정 dialog에서 거부를 누르면
            //ActivityCompat.shouldShowRequestPermissionRationale 메소드의 반환값이 true가 된다.
            //단, 사용자가 "Don't ask again"을 체크한 경우
            //거부하더라도 false를 반환하여, 직접 사용자가 권한을 부여하지 않는 이상, 권한을 요청할 수 없게 된다.
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)){
                //이곳에 권한이 왜 필요한지 설명하는 Toast나 dialog를 띄워준 후, 다시 권한을 요청한다.
                Toast.makeText(getApplicationContext(), "SMS권한이 필요합니다", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.INTERNET}, INTERNET_PERMISSON);
            }else{
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.INTERNET}, SMS_RECEIVE_PERMISSON);
            }*/
        }

        dbHelper = new DBHelper(this, "MENU.db", null, 1);
        //첫실행 체크
        prefs = this.getSharedPreferences("Pref", MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean("isFirstRun",true);
        if(isFirstRun)
        {
            dbHelper.doFirst();
            prefs.edit().putBoolean("isFirstRun",false).apply();
        }


        //------------------------------------------------------------
        // 필요한 목록 셋팅
        names = new ArrayList<>();
        items = MainActivity.dbHelper.getResult();

        date = new Date(System.currentTimeMillis());
        String dateF = new SimpleDateFormat("yyyy-MM-dd").format(date);
        arraBillChart = new ArrayList<>();
        arraBillHistory = new ArrayList<>();
        GetBillChart = new GetBill(this,arraBillChart,dateF);
        GetBillHistory = new GetBill(this,arraBillHistory,dateF);
        GetBillChart.getJson();
        GetBillHistory.getJson();

        //------------------------------------------------------------
        //페이저 실행
        ViewPager viewPager = findViewById(R.id.pager);
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(2);

        TabLayout tabLayout = findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);
/*
        //주기적 업데이트 확인
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    MyPagerAdapter.ColorFragment.timeChecker();
                }
            }
        });

        th.start();*/
    }

    public static HashMap<String, String> items; //메뉴판 - 이름,가격
    public static ArrayList<String> names; //메뉴목록 - 이름
    public static GetBill GetBillHistory;
    public static GetBill GetBillChart;
    public static ArrayList<BillType> arraBillChart;
    public static ArrayList<BillType> arraBillHistory;
}
