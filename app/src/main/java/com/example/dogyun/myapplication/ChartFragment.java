package com.example.dogyun.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.dogyun.myapplication.Bill.GetBill;
import com.example.dogyun.myapplication.Bill.HandleBill;
import com.example.dogyun.myapplication.Models.BillType;
import com.example.dogyun.myapplication.Models.itemList;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;

/**
 * Created by dogyun on 2018-09-05.
 */

//----------------------
//판매량 차트 화면
//----------------------
public class ChartFragment extends Fragment {

    ArrayList<itemList> menu = MainActivity.menu;
    ArrayList<BillType> wantBill;
    public static ArrayList<Integer> count;
    public static ArrayList<Integer> cash;
    public static ArrayList<Integer> card;
    public static ArrayList<Integer> total;
    HandleBill hb;
    int flag;


    public ChartFragment() {
        count = new ArrayList<Integer>();
        cash = new ArrayList<Integer>();
        card = new ArrayList<Integer>();
        total = new ArrayList<Integer>();
        wantBill = new ArrayList<>();
        hb = new HandleBill(wantBill, count, cash, card, total);
        flag = 0;

        GregorianCalendar calendar = new GregorianCalendar();
        dateS = calendar;
        dateE = calendar;
    }

    GregorianCalendar dateS, dateE;
    TextView tv6, tv8, chart11, chart12, chart13, chart21, chart22, chart23;
    public static LineChart chart1, chart2;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_chart, null);

        tv6 = (TextView) rootView.findViewById(R.id.textView6);
        tv8 = (TextView) rootView.findViewById(R.id.textView8);
        chart11 = (TextView) rootView.findViewById(R.id.textView11);
        chart12 = (TextView) rootView.findViewById(R.id.textView13);
        chart13 = (TextView) rootView.findViewById(R.id.textView15);
        chart21 = (TextView) rootView.findViewById(R.id.textView12);
        chart22 = (TextView) rootView.findViewById(R.id.textView14);
        chart23 = (TextView) rootView.findViewById(R.id.textView16);

        chart1 = (LineChart) rootView.findViewById(R.id.chart1);
        chart2 = (LineChart) rootView.findViewById(R.id.chart2);

        tv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateSetListenerS, dateS.get(Calendar.YEAR), dateS.get(Calendar.MONTH), dateS.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        tv8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateSetListenerE, dateE.get(Calendar.YEAR), dateE.get(Calendar.MONTH), dateE.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        if(flag == 0) {
            chartFirstDraw();
            flag = 1;
        }else{
            chartDraw();
        }
        //chart1.refreshDrawableState();
        //chart2.refreshDrawableState();

        return rootView;
    }

    public void init(){
        count.clear(); cash.clear(); card.clear(); total.clear();
    }

    public void chartSetting(){
    }

    public void chartDraw(){
        chartHour(chart1, chart2);
    }

    public void chartFirstHour(LineChart lc1, LineChart lc2){

        init();
        hb.HandleBill_count();
        hb.HandleBill_Hour();
        chartHour(lc1, lc2);
    }

    public void chartHour(LineChart lc1, LineChart lc2){

        tv6.setText(DateCalculate.date(dateS));
        tv8.setText(DateCalculate.date(dateE));

        //------------------------------------------------------------------------------------------
        //차트1 - 현금, 카드 계산 //시간별..
        //------------------------------------------------------------------------------------------
        LineChart lineChart1 = lc1;

        ArrayList<Entry> entries1 = new ArrayList<>();
        for(int i = 0; i<11; i++) entries1.add(new Entry(i, total.get(i)));

        LineDataSet dataset1 = new LineDataSet(entries1, "매출");

        ArrayList<String> labels1 = new ArrayList<String>();
        labels1.add("10시");
        labels1.add("11시");
        labels1.add("12시");
        labels1.add("1시");
        labels1.add("2시");
        labels1.add("3시");
        labels1.add("4시");
        labels1.add("5시");
        labels1.add("6시");
        labels1.add("7시");
        labels1.add("8시");

        LineData data1 = new LineData(dataset1);
        dataset1.setColors(ColorTemplate.rgb("#256A9C")); //
        //dataset.setDrawCubic(true); //선 둥글게 만들기
        //dataset.setDrawFilled(true); //그래프 밑부분 색칠

        lineChart1.setData(data1);
        lineChart1.animateY(2500);
        lineChart1.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels1));
        lineChart1.getXAxis().setTextSize(20);
        //lineChart1.setExtraOffsets(0,0,20,12);
        lineChart1.invalidate();

        XAxis xval1 = lineChart1.getXAxis();
        xval1.setDrawLabels(true);
        xval1.setPosition(XAxis.XAxisPosition.BOTTOM);
        xval1.setDrawGridLines(false);
        xval1.setYOffset(15f);
        xval1.setGranularityEnabled(true);
        xval1.setLabelCount(labels1.size());

        lineChart1.getAxisRight().setEnabled(false);

        YAxis yval1 = lineChart1.getAxisLeft();
        yval1.setDrawGridLines(false);
        yval1.setStartAtZero(true);
        yval1.setXOffset(15f);


        //------------------------------------------------------------------------------------------
        //차트2 - 메뉴별 판매량 //메뉴별..
        //이거 왜 최신화가 안되지? 메뉴 2개만 팔리면,,, 나머지 하나가 1등메뉴로 다 된다;; 계산해야지 바뀜;;
        //------------------------------------------------------------------------------------------
        LineChart lineChart2 = lc2;

        int i = 0;
        int c = 0;
        ArrayList<Entry> entries2 = new ArrayList<>();
        ArrayList<String> labels2 = new ArrayList<String>();

        for(Integer x : count){
            if(x != 0){
                entries2.add(new Entry(i++, (float) x));
                labels2.add(menu.get(c).name);
            }
            c++;
        }

        if(i==0){
            for(int e = 0; e<5; e++){
                try {
                    labels2.add(menu.get(e).name);
                    entries2.add(new Entry(e, 0));
                }catch (IndexOutOfBoundsException e1){
                    labels2.add("자료없음");
                    entries2.add(new Entry(e, 0));
                }
            }
        }

        LineDataSet dataset2 = new LineDataSet(entries2, "판매량");
        dataset2.setColors(ColorTemplate.rgb("#256A9C")); //
        //dataset.setDrawCubic(true); //선 둥글게 만들기
        //dataset.setDrawFilled(true); //그래프 밑부분 색칠

        LineData data2 = new LineData(dataset2);
        lineChart2.setData(data2);
        lineChart2.animateY(2500);
        lineChart2.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels2));
        lineChart2.getXAxis().setTextSize(20);
        //lineChart2.setExtraOffsets(0,0,0,0);
        lineChart2.getXAxis().setLabelRotationAngle(-45);
        lineChart2.invalidate();

        XAxis xval2 = lineChart2.getXAxis();
        xval2.setDrawLabels(true);
        xval2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xval2.setDrawGridLines(false);
        xval2.setYOffset(15f);
        xval2.setGranularityEnabled(true);
        xval2.setLabelCount(labels2.size());

        lineChart2.getAxisRight().setEnabled(false);

        YAxis yval2 = lineChart2.getAxisLeft();
        yval2.setDrawGridLines(false);
        yval2.setStartAtZero(true);
        yval2.setXOffset(15f);

        //카드 현금
        chart11.setText("현금 : " + cal(cash));//카드
        chart12.setText("카드 : " + cal(card));//현금
        chart13.setText("총합 : " + cal(total));//총합

        ArrayList<String> rank = rank(count);

        //판매순위
        chart21.setText(rank.get(0));//1등
        chart22.setText(rank.get(1));//2등
        chart23.setText(rank.get(2));//3등

    }

    public String cal(ArrayList<Integer> al){
        int i = 0;
        for(int a : al){
            i += a;
        }
        String str = String.valueOf(i);
        if(i!=0){
            str = str.substring(0,str.length()-3) + "," + str.substring(str.length()-3,str.length());
        }
        return str;
    }

    public ArrayList<String> rank(ArrayList<Integer> al){
        ArrayList<String> s = new ArrayList<>();
        ArrayList<Integer> a = new ArrayList<>();
        ArrayList<Integer> a2 = new ArrayList<>();
        int[] i = new int[3];
        int[] p = new int[3];
        for(int x : al){
            a.add(x);
            a2.add(x);
        }

        Collections.sort(a);

        for(int x=1;x<4;x++){
            try {
                i[x - 1] = a.get(a.size() - x);
            }catch (ArrayIndexOutOfBoundsException e){
                i[x - 1] = 0;
            }
        }

        if(i[0]==0){
            for(int x=1;x<4;x++){
                String str = "자료가 없어요..ㅠ";
                s.add(str);
            }
        } else{
            p[0] = a2.indexOf(i[0]);
            a2.set(p[0],0);
            p[1] = a2.indexOf(i[1]);
            a2.set(p[1], 0);
            p[2] = a2.indexOf(i[2]);

            for(int x=1;x<4;x++){
                String str = "" + x + "등\t\t" + menu.get(p[x-1]).name + " " + al.get(p[x-1]) + "개";
                s.add(str);
            }
        }

        return s;
    }

    /*
    public void chartMonth(){

        LineChart lineChart = (LineChart) rootView.findViewById(R.id.chart1);


        //LineChart lineChart = (LineChart) rootView.findViewById(R.id.chart);

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 5f));
        entries.add(new Entry(1, 0f));
        entries.add(new Entry(2, 9f));
        entries.add(new Entry(3, 10f));
        entries.add(new Entry(4, 13f));
        entries.add(new Entry(5, 6f));
        entries.add(new Entry(6, 3f));
        entries.add(new Entry(7, 11f));
        entries.add(new Entry(8, 18f));
        entries.add(new Entry(9, 1f));
        entries.add(new Entry(10, 16f));
        entries.add(new Entry(10, 12f));

        LineDataSet dataset = new LineDataSet(entries, "# of Calls");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("1월");
        labels.add("2월");
        labels.add("3월");
        labels.add("4월");
        labels.add("5월");
        labels.add("6월");
        labels.add("7월");
        labels.add("8월");
        labels.add("9월");
        labels.add("10월");
        labels.add("11월");
        labels.add("12월");

        LineData data = new LineData(dataset);
        dataset.setColors(ColorTemplate.rgb("#256A9C")); //
        //dataset.setDrawCubic(true); //선 둥글게 만들기
        //dataset.setDrawFilled(true); //그래프 밑부분 색칠

        lineChart.setData(data);
        lineChart.animateY(2500);
        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        lineChart.invalidate();

        XAxis xval = lineChart.getXAxis();
        xval.setDrawLabels(true);
        xval.setPosition(XAxis.XAxisPosition.BOTTOM);
        xval.setDrawGridLines(false);
        xval.setYOffset(15f);
        xval.setGranularityEnabled(true);
        xval.setLabelCount(labels.size());

        lineChart.getAxisRight().setEnabled(false);

        YAxis yval = lineChart.getAxisLeft();
        yval.setDrawGridLines(false);
        yval.setStartAtZero(true);
        yval.setXOffset(15f);
    }*/

    private DatePickerDialog.OnDateSetListener dateSetListenerS = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
            // TODO Auto-generated method stub
            GregorianCalendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
            dateS = calendar;

            chartFirstDraw();
        }
    };

    public void chartFirstDraw(){
        wantBill.clear();
        int term = DateCalculate.between(dateS, dateE);
        for(int i=0; i<=term; i++) {
            for (BillType bt : MainActivity.bill) {
                if (bt.date.contains(DateCalculate.dateAdd(dateS, i))) {
                    wantBill.add(bt);
                }
            }
        }
        chartFirstHour(chart1, chart2);
    }

    private DatePickerDialog.OnDateSetListener dateSetListenerE = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
            // TODO Auto-generated method stub
            GregorianCalendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
            dateE = calendar;

            chartFirstDraw();
        }
    };
}