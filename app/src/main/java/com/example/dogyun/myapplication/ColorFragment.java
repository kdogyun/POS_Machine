package com.example.dogyun.myapplication;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dogyun.myapplication.Models.ChildList;
import com.example.dogyun.myapplication.Models.ParentList;
import com.example.dogyun.myapplication.ViewHolders.NetworkTask;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ColorFragment extends Fragment{

    //int[] images = {R.drawable.ic_launcher_background};
    ArrayList<itemList> item = new ArrayList<>(); //구매목록
    HashMap<String, String> items; //메뉴판 - 이름,가격
    ArrayList<String> names; //메뉴목록 - 이름

    public ColorFragment() {
        // Required empty public constructor(
    }

    GridView gv;
    ListView lv;
    TextView tx;

    CustomAdapter adapter;
    CustomAdapter_list adapter2;

    ArrayList<Integer> cash;
    ArrayList<Integer> card;
    ArrayList<Integer> total;
    ArrayList<Integer> count;
    ArrayList<String> name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    Button bt2, bt1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_color, null);

        gv = (GridView) rootView.findViewById(R.id.gridView1);
        lv = (ListView) rootView.findViewById(R.id.ListView);
        tx = (TextView) rootView.findViewById(R.id.textView);
/*
        arrayList = new ArrayList<>();
        arrayList2 = new ArrayList<>();

        adapter2 = new CustomAdapter_list(getActivity(), arrayList, arrayList2);
*/
        items = MainActivity.items;
        names = MainActivity.names;

        adapter2 = new CustomAdapter_list(getActivity(), item);
        lv.setAdapter(adapter2);

        adapter = new CustomAdapter(getActivity(), names);
        gv.setAdapter(adapter);

        cash = ChartFragment.cash;
        card = ChartFragment.card;
        total = ChartFragment.total;
        count = ChartFragment.count;
        name = MainActivity.names;

        tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeChecker();
                refresh();
            }
        });

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getActivity(), names.get(position) + " 추가 하였습니다.", Toast.LENGTH_SHORT).show();

                int itemPosition = findArrayList_item(names.get(position));

                if(itemPosition == -1){
                    itemList il = new itemList();
                    il.name = names.get(position);
                    il.price = items.get(names.get(position));
                    il.count = 1;
                    item.add(il);
                } else {
                    item.get(itemPosition).count++;
                }
/*
                arrayList.add(names[position]);
                //그냥 에드하는게 아니라 해당 메뉴의 갯수를 반환해야함
                arrayList2.add(price[position]);
                */
                adapter2.notifyDataSetChanged();

                tx.setText(Integer.toString(Integer.parseInt(new StringBuffer(tx.getText()).toString()) + Integer.parseInt(items.get(names.get(position)))));

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getActivity(), item.get(position).name + " 제거 하였습니다.", Toast.LENGTH_SHORT).show();

                tx.setText(Integer.toString(Integer.parseInt(new StringBuffer(tx.getText()).toString())-Integer.parseInt(item.get(position).price)));
                item.get(position).count--;


                if(item.get(position).count==0){
                    item.remove(position);
                }
                /*
                arrayList.remove(position);
                //그냥 삭제하는게 아니라 해당 메뉴의 갯수를 반환해야함
                arrayList2.remove(position);
                */
                adapter2.notifyDataSetChanged();

            }
        });

        bt1 = (Button) rootView.findViewById(R.id.button);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!tx.getText().equals("0") && !tx.getText().equals("마감") ) {
                    Toast.makeText(getActivity(), tx.getText() + "원 현금 계산이 완료되었습니다.", Toast.LENGTH_SHORT).show();

                    final Date date = new Date(System.currentTimeMillis());

                    String url = "http://andomira.com/POS_billp.php";
                    ContentValues cv = new ContentValues();
                    cv.put("cago", "PLAN_bill");
                    cv.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));

                    String content = "";

                    ArrayList<String> iln = new ArrayList<String>();
                    ArrayList<String> ilc = new ArrayList<String>();
                    //ArrayList<String> ili = new ArrayList<String>();
                    for (itemList il : item) {
                        String name = il.name;
                        String counts = String.valueOf(il.count);
                        //public String image = il.image;
                        iln.add(name);
                        ilc.add(counts);
                        //ili.add(image);
                        count.set(names.indexOf(il.name), count.get(names.indexOf(il.name)) + il.count);
                        content += "\n" + name + "\tX" + counts + "\t" + (Integer.parseInt(MainActivity.items.get(name)) * il.count) + "\n";
                    }
                    cv.put("name", iln.toString());
                    cv.put("count", ilc.toString());
                    cv.put("total", Integer.parseInt(tx.getText().toString()));
                    cv.put("type", "현금");

                    int hour = Integer.parseInt(new SimpleDateFormat("HH").format(date)) - 10;
                    cash.set(hour, cash.get(hour) + Integer.parseInt(tx.getText().toString()));
                    total.set(hour, total.get(hour) + Integer.parseInt(tx.getText().toString()));

                    // AsyncTask를 통해 HttpURLConnection 수행.
                    NetworkTask networkTask = new NetworkTask(getActivity(), url, cv);
                    networkTask.execute();

                    final List<ChildList> Child = new ArrayList<>();
                    String title = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E").format(date) +
                            " \t " + item.get(0).name + " " + item.get(0).count + "개\t 외 " + (item.size() - 1) + "개 \t " + tx.getText().toString() + "(현금)";


                    Child.add(new ChildList(content));
                    HistoryFragment.Parent.add(0, new ParentList(title, Child));
                    HistoryFragment.adapter = new DocExpandableRecyclerAdapter(HistoryFragment.Parent);
                    HistoryFragment.recycler_view.setAdapter(HistoryFragment.adapter);

                    item.clear();
                    //arrayList2.clear();
                    adapter2.notifyDataSetChanged();
                    MyPagerAdapter.ChartFragment.chartDraw();
                    //MyPagerAdapter.ChartFragment.chart1.notifyDataSetChanged();
                    //MyPagerAdapter.ChartFragment.chart1.invalidate();
                    //MyPagerAdapter.ChartFragment.chart2.notifyDataSetChanged();
                    //MyPagerAdapter.ChartFragment.chart2.invalidate();

                    //MyPagerAdapter.historyFragment.firstBill();

                    tx.setText("0");
                }
            }
        });

        bt2 = (Button) rootView.findViewById(R.id.button2);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tx.getText().equals("0") && !tx.getText().equals("마감") ) {
                    Toast.makeText(getActivity(), tx.getText() + "원 카드 계산이 완료되었습니다.", Toast.LENGTH_SHORT).show();

                    final Date date = new Date(System.currentTimeMillis());

                    String url = "http://andomira.com/POS_billp.php";
                    ContentValues cv = new ContentValues();
                    cv.put("cago", "PLAN_bill");
                    cv.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));

                    String content = "";

                    ArrayList<String> iln = new ArrayList<String>();
                    ArrayList<String> ilc = new ArrayList<String>();
                    //ArrayList<String> ili = new ArrayList<String>();
                    for (itemList il : item) {
                        String name = il.name;
                        String counts = String.valueOf(il.count);
                        //public String image = il.image;
                        iln.add(name);
                        ilc.add(counts);
                        //ili.add(image);
                        count.set(names.indexOf(il.name), count.get(names.indexOf(il.name)) + il.count);
                        content += "\n" + name + "\tX" + counts + "\t" + (Integer.parseInt(MainActivity.items.get(name)) * il.count) + "\n";
                    }
                    cv.put("name", iln.toString());
                    cv.put("count", ilc.toString());
                    cv.put("total", Integer.parseInt(tx.getText().toString()));
                    cv.put("type", "카드");

                    int hour = Integer.parseInt(new SimpleDateFormat("HH").format(date)) - 10;
                    card.set(hour, card.get(hour) + Integer.parseInt(tx.getText().toString()));
                    total.set(hour, total.get(hour) + Integer.parseInt(tx.getText().toString()));

                    // AsyncTask를 통해 HttpURLConnection 수행.
                    NetworkTask networkTask = new NetworkTask(getActivity(), url, cv);
                    networkTask.execute();

                    final List<ChildList> Child = new ArrayList<>();
                    String title = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E").format(date) +
                            " \t " + item.get(0).name + " " + item.get(0).count + "개\t 외 " + (item.size() - 1) + "개 \t " + tx.getText().toString() + "(카드)";


                    Child.add(new ChildList(content));
                    HistoryFragment.Parent.add(0, new ParentList(title, Child));
                    HistoryFragment.adapter = new DocExpandableRecyclerAdapter(HistoryFragment.Parent);
                    HistoryFragment.recycler_view.setAdapter(HistoryFragment.adapter);

                    item.clear();
                    //arrayList2.clear();
                    adapter2.notifyDataSetChanged();
                    MyPagerAdapter.ChartFragment.chartDraw();
                    //MyPagerAdapter.ChartFragment.chart1.notifyDataSetChanged();
                    //MyPagerAdapter.ChartFragment.chart1.invalidate();
                    //MyPagerAdapter.ChartFragment.chart2.notifyDataSetChanged();
                    //MyPagerAdapter.ChartFragment.chart2.invalidate();

                    //MyPagerAdapter.historyFragment.firstBill();

                    tx.setText("0");
                }
            }
        });

        timeChecker();

        return rootView;
    }

    public int findArrayList_item(String name){

        int i = -1;
        int count = -1;

        for(itemList il : item){
            count++;
            if(il.name.equals(name)){
                i = count;
                break;
            }
        }

        return i;
    }

    public void timeChecker(){

        Date date = new Date(System.currentTimeMillis());
        int time = Integer.parseInt(new SimpleDateFormat("HH").format(date));
        if(time>21 || time<10){
            bt1.setEnabled(false);
            bt2.setEnabled(false);
            gv.setEnabled(false);
            lv.setEnabled(false);
            item.clear();
            adapter2.notifyDataSetChanged();
            tx.setText("마감");
        } else{
            bt1.setEnabled(true);
            bt2.setEnabled(true);
            gv.setEnabled(true);
            lv.setEnabled(true);
            item.clear();
            adapter2.notifyDataSetChanged();
            tx.setText("0");
        }

    }

    public void refresh(){

        Date date = new Date(System.currentTimeMillis());
        int time = Integer.parseInt(new SimpleDateFormat("HH").format(date));

        String strD = new SimpleDateFormat("yyyy-MM-dd").format(date);
        MainActivity.date = date;
        MyPagerAdapter.ChartFragment.chartFirstDraw(strD);
        MyPagerAdapter.ChartFragment.yearS = Integer.parseInt(new SimpleDateFormat("yyyy").format(date));
        MyPagerAdapter.ChartFragment.monthS = Integer.parseInt(new SimpleDateFormat("MM").format(date))-1;
        MyPagerAdapter.ChartFragment.dayS = Integer.parseInt(new SimpleDateFormat("dd").format(date));
        MyPagerAdapter.ChartFragment.yearE = Integer.parseInt(new SimpleDateFormat("yyyy").format(date));
        MyPagerAdapter.ChartFragment.monthE = Integer.parseInt(new SimpleDateFormat("MM").format(date))-1;
        MyPagerAdapter.ChartFragment.dayE = Integer.parseInt(new SimpleDateFormat("dd").format(date));
        MyPagerAdapter.ChartFragment.tv6.setText(MyPagerAdapter.ChartFragment.yearS+" / "+(MyPagerAdapter.ChartFragment.monthS+1)+" / "+MyPagerAdapter.ChartFragment.dayS);
        MyPagerAdapter.ChartFragment.tv8.setText(MyPagerAdapter.ChartFragment.yearE+" / "+(MyPagerAdapter.ChartFragment.monthE+1)+" / "+MyPagerAdapter.ChartFragment.dayE);

        MyPagerAdapter.historyFragment.historyFirstDraw(strD);
        MyPagerAdapter.historyFragment.yearS = Integer.parseInt(new SimpleDateFormat("yyyy").format(date));
        MyPagerAdapter.historyFragment.monthS = Integer.parseInt(new SimpleDateFormat("MM").format(date))-1;
        MyPagerAdapter.historyFragment.dayS = Integer.parseInt(new SimpleDateFormat("dd").format(date));
        MyPagerAdapter.historyFragment.yearE = Integer.parseInt(new SimpleDateFormat("yyyy").format(date));
        MyPagerAdapter.historyFragment.monthE = Integer.parseInt(new SimpleDateFormat("MM").format(date))-1;
        MyPagerAdapter.historyFragment.dayE = Integer.parseInt(new SimpleDateFormat("dd").format(date));
        MyPagerAdapter.historyFragment.tv1.setText(MyPagerAdapter.historyFragment.yearS+" / "+(MyPagerAdapter.historyFragment.monthS+1)+" / "+MyPagerAdapter.historyFragment.dayS);
        MyPagerAdapter.historyFragment.tv2.setText(MyPagerAdapter.historyFragment.yearE+" / "+(MyPagerAdapter.historyFragment.monthE+1)+" / "+MyPagerAdapter.historyFragment.dayE);
    }

}
