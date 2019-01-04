package com.example.dogyun.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.dogyun.myapplication.Bill.BillType;
import com.example.dogyun.myapplication.Bill.GetBill;
import com.example.dogyun.myapplication.Models.ChildList;
import com.example.dogyun.myapplication.Models.ParentList;
import com.example.dogyun.myapplication.ViewHolders.MyChildViewHolder;
import com.example.dogyun.myapplication.ViewHolders.MyParentViewHolder;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class HistoryFragment extends Fragment {

    ArrayList<BillType> arraBill;
    public static List<ParentList> Parent = new ArrayList<ParentList>();
    public static GetBill GetBill;
    int flag;

    public HistoryFragment() {
        // Required empty public constructor
        arraBill = MainActivity.arraBillHistory;
        GetBill = MainActivity.GetBillHistory;
        flag = 0;

        GregorianCalendar calendar = new GregorianCalendar();
        yearS = calendar.get(Calendar.YEAR);
        monthS = calendar.get(Calendar.MONTH);
        dayS= calendar.get(Calendar.DAY_OF_MONTH);
        yearE = yearS;
        monthE = monthS;
        dayE= dayS;
    }

    public static RecyclerView recycler_view;
    //Handler mHandler = new Handler();
    public static int yearS, monthS, dayS, yearE, monthE, dayE;
    TextView tv1, tv2;
    String date;
    public static DocExpandableRecyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        recycler_view = (RecyclerView) rootView.findViewById(R.id.recycler_Expand);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_view.setAdapter(adapter);

        tv1 = (TextView) rootView.findViewById(R.id.historyTV1);
        tv2 = (TextView) rootView.findViewById(R.id.historyTV3);

        tv1.setText(yearS+" / "+(monthS+1)+" / "+dayS);
        tv2.setText(yearS+" / "+(monthS+1)+" / "+dayS);

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateSetListenerS, yearS, monthS, dayS).show();
            }
        });

        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateSetListenerE, yearE, monthE, dayE).show();
            }
        });

        if(flag == 0) {
            firstBill();
            flag = 1;
        }

        return rootView;
    }

    private DatePickerDialog.OnDateSetListener dateSetListenerS = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            yearS = year;
            monthS = monthOfYear;
            dayS = dayOfMonth;
            yearE = year;
            monthE = monthOfYear;
            dayE = dayOfMonth;

            tv1.setText(yearS+" / "+(monthS+1)+" / "+dayS);
            tv2.setText(yearE+" / "+(monthE+1)+" / "+dayE);


            if((monthS+1)<10) {
                if(dayS<10) date = yearS + "-0" + (monthS+1) + "-0" + dayS;
                else date = yearS + "-0" + (monthS+1) + "-" + dayS;
            }
            else {
                if(dayS<10) date = yearS + "-" + (monthS+1) + "-0" + dayS;
                else date = yearS + "-" + (monthS+1) + "-" + dayS;
            }
            historyFirstDraw(date);
        }
    };

    public void historyFirstDraw(String str){
        arraBill.clear();
        GetBill.setDate(str); GetBill.getJson();
        firstBill();
    }

    public void firstBill(){

        Parent.clear();

        for(BillType bt : arraBill) {
            final List<ChildList> Child = new ArrayList<>();
            String title = "";
            String content = "";

            if(bt.type.equals("현금_환불")){

                title = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E").format(bt.date) +
                        " \t " + bt.name.get(0) + " " + bt.count.get(0) + "개\t 외 " + (bt.name.size() - 1) + "개 \t 0원" + "(" + bt.type + ")";
                content = "";
                int i = 0;
                for (String name : bt.name) {
                    int count = Integer.parseInt(bt.count.get(i));
                    content += "\n" + name + "\tX" + count + "\t 0원\n";
                    i++;
                }

            } else if(bt.type.equals("카드_환불")){

                title = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E").format(bt.date) +
                        " \t " + bt.name.get(0) + " " + bt.count.get(0) + "개\t 외 " + (bt.name.size() - 1) + "개 \t 0원" + "(" + bt.type + ")";
                content = "";
                int i = 0;
                for (String name : bt.name) {
                    int count = Integer.parseInt(bt.count.get(i));
                    content += "\n" + name + "\tX" + count + "\t 0원\n";
                    i++;
                }

            } else {

                title = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E").format(bt.date) +
                        " \t " + bt.name.get(0) + " " + bt.count.get(0) + "개\t 외 " + (bt.name.size() - 1) + "개 \t " + bt.total + "(" + bt.type + ")";
                content = "";
                int i = 0;
                for (String name : bt.name) {
                    int count = Integer.parseInt(bt.count.get(i));
                    content += "\n" + name + "\tX" + count + "\t" + (Integer.parseInt(MainActivity.items.get(name)) * count) + "\n";
                    i++;
                }
            }
            Child.add(new ChildList(content));
            Parent.add(0,new ParentList(title, Child));
        }

        adapter = new DocExpandableRecyclerAdapter(Parent);
        recycler_view.setAdapter(adapter);
    }

    private DatePickerDialog.OnDateSetListener dateSetListenerE = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
            // TODO Auto-generated method stub
            yearE = year;
            monthE = monthOfYear;
            dayE = dayOfMonth;

            tv2.setText(yearE+" / "+(monthE+1)+" / "+dayE);
        }
    };


}
