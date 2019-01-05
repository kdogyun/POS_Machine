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

import com.example.dogyun.myapplication.Adapter.DocExpandableRecyclerAdapter;
import com.example.dogyun.myapplication.Models.BillType;
import com.example.dogyun.myapplication.Models.ChildList;
import com.example.dogyun.myapplication.Models.ParentList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

//--------------------
//영수증 페이지
//--------------------
public class HistoryFragment extends Fragment {

    ArrayList<BillType> wantBill;
    public static List<ParentList> Parent = new ArrayList<ParentList>();
    int flag;

    public HistoryFragment() {
        GregorianCalendar calendar = new GregorianCalendar();
        dateS = calendar;
        dateE = calendar;
        wantBill = new ArrayList<>();
        flag = 0;
    }

    public static RecyclerView recycler_view;
    //Handler mHandler = new Handler();
    TextView tv1, tv2;
    GregorianCalendar dateS, dateE;
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

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateSetListenerS, dateS.get(Calendar.YEAR), dateS.get(Calendar.MONTH), dateS.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateSetListenerE, dateE.get(Calendar.YEAR), dateE.get(Calendar.MONTH), dateE.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        if(flag==0) historyFirstDraw();
        else firstBill();

        return rootView;
    }

    private DatePickerDialog.OnDateSetListener dateSetListenerS = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub

            GregorianCalendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
            dateS = calendar;

            historyFirstDraw();
        }
    };

    public void historyFirstDraw(){

        tv1.setText(DateCalculate.date(dateS));
        tv2.setText(DateCalculate.date(dateE));

        wantBill.clear();
        int term = DateCalculate.between(dateS, dateE);
        for(int i=0; i<=term; i++) {
            for (BillType bt : MainActivity.bill) {
                if (bt.date.contains(DateCalculate.dateAdd(dateS, i))) {
                    wantBill.add(bt);
                }
            }
        }
        firstBill();
    }

    public void firstBill(){

        Parent.clear();

        for(BillType bt : wantBill) {
            final List<ChildList> Child = new ArrayList<>();
            String title = "";
            String content = "";

            if(bt.type.contains("_환불")){
                title = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E").format(DateCalculate.stringDate(bt.date)) +
                        " \t " + bt.name.get(0) + " " + bt.count.get(0) + "개\t 외 " + (bt.name.size() - 1) + "개 \t 0원" + "(" + bt.type + ")";
                content = "";
                int i = 0;
                for (String name : bt.name) {
                    int count = Integer.parseInt(bt.count.get(i));
                    content += "\n" + name + "\tX" + count + "\t 0원\n";
                    i++;
                }
            } else {
                title = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E").format(DateCalculate.stringDate(bt.date)) +
                            " \t " + bt.name.get(0) + " " + bt.count.get(0) + "개\t 외 " + (bt.name.size() - 1) + "개 \t " + bt.total + "(" + bt.type + ")";
                content = "";
                int i = 0;
                for (String name : bt.name) {
                    int count = Integer.parseInt(bt.count.get(i));
                    int price = Integer.parseInt(bt.price.get(i));
                    content += "\n" + name + "\tX" + count + "\t" + price + "\n";
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

            GregorianCalendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
            dateE = calendar;

            historyFirstDraw();
        }
    };


}
