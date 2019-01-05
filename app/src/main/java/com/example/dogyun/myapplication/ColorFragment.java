package com.example.dogyun.myapplication;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dogyun.myapplication.Adapter.CustomAdapter;
import com.example.dogyun.myapplication.Adapter.CustomAdapter_list;
import com.example.dogyun.myapplication.Adapter.DocExpandableRecyclerAdapter;
import com.example.dogyun.myapplication.Adapter.MyPagerAdapter;
import com.example.dogyun.myapplication.Models.ChildList;
import com.example.dogyun.myapplication.Models.ParentList;
import com.example.dogyun.myapplication.Models.itemList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


//------------------
//포스기 메인 화면
//------------------
public class ColorFragment extends Fragment{

    ArrayList<itemList> purchase; //구매목록
    ArrayList<itemList> menu; //구매목록

    public ColorFragment() {
        this.menu = MainActivity.menu;
        purchase = new ArrayList<>();
        // Required empty public constructor(
    }


    GridView gv; //메뉴목록
    ListView lv; //구매물품
    TextView tx; //구매가격

    static CustomAdapter adapter; //메뉴 어뎁터
    CustomAdapter_list adapter2;//구매 어뎁터

    ArrayList<Integer> chartCash ,chartCard, chartTotal, chartCount;

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

        adapter2 = new CustomAdapter_list(getActivity(), purchase);
        lv.setAdapter(adapter2);

        adapter = new CustomAdapter(getActivity(), menu);
        gv.setAdapter(adapter);

        chartCash = ChartFragment.cash;
        chartCard = ChartFragment.card;
        chartTotal = ChartFragment.total;
        chartCount = ChartFragment.count;

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

                Toast.makeText(getActivity(), menu.get(position).name + " 추가 하였습니다.", Toast.LENGTH_SHORT).show();

                int itemPosition = findArrayList_item(menu.get(position).name);

                if(itemPosition == -1){
                    itemList il = new itemList();
                    il.name = menu.get(position).name;
                    il.price = menu.get(position).price;
                    il.count = 1;
                    purchase.add(il);
                } else {
                    purchase.get(itemPosition).count++;
            }

                adapter2.notifyDataSetChanged();

                tx.setText(Integer.toString(Integer.parseInt(new StringBuffer(tx.getText()).toString()) + menu.get(position).price));

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getActivity(), purchase.get(position).name + " 제거 하였습니다.", Toast.LENGTH_SHORT).show();

                tx.setText(Integer.toString(Integer.parseInt(new StringBuffer(tx.getText()).toString()) - purchase.get(position).price));
                purchase.get(position).count--;

                if(purchase.get(position).count==0){
                    purchase.remove(position);
                }

                adapter2.notifyDataSetChanged();

            }
        });

        bt1 = (Button) rootView.findViewById(R.id.button);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!tx.getText().equals("0") && !tx.getText().equals("마감") ) {
                    calculate("현금");
                }
            }
        });

        bt2 = (Button) rootView.findViewById(R.id.button2);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tx.getText().equals("0") && !tx.getText().equals("마감") ) {
                    calculate("카드");
                }
            }
        });

        timeChecker();

        return rootView;
    }

    public void calculate(String type){

        Toast.makeText(getActivity(), tx.getText() + "원 " + type + " 계산이 완료되었습니다.", Toast.LENGTH_SHORT).show();

        final Date date = new Date(System.currentTimeMillis());

        String content = "";
        ArrayList<String> iln= new ArrayList<String>(); //구매하는 이름 나열해주는 배열
        ArrayList<String> ilc = new ArrayList<String>(); //구매하는 갯수 나열해주는 배열
        ArrayList<String> ilp = new ArrayList<String>(); //구매하는 가격 나열해주는 배열

        for (itemList il : purchase) {
            String name = il.name;
            String count = String.valueOf(il.count);
            String price = String.valueOf(il.price * il.count);
            iln.add(name);
            ilc.add(count);
            ilp.add(price);

            int pos = -1;
            for(itemList il2 : menu){
                pos++;
                if(il2.name.equals(il.name)) break;
            }
            chartCount.set(pos, chartCount.get(pos) + il.count);
            content += "\n" + name + "\tX" + count + "\t" + price + "\n";
        }

        int hour = Integer.parseInt(new SimpleDateFormat("HH").format(date)) - 10;
        if(type.equals("현금")) chartCash.set(hour, chartCash.get(hour) + Integer.parseInt(tx.getText().toString()));
        else  chartCard.set(hour, chartCard.get(hour) + Integer.parseInt(tx.getText().toString()));
        chartTotal.set(hour, chartTotal.get(hour) + Integer.parseInt(tx.getText().toString()));

        MainActivity.dbHelper.insertBill(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date), iln.toString(), ilc.toString(),
                ilp.toString(), Integer.parseInt(tx.getText().toString()), type);

        final List<ChildList> Child = new ArrayList<>();
        String title = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E").format(date) +
                " \t " + purchase.get(0).name + " " + purchase.get(0).count + "개\t 외 " + (purchase.size() - 1) + "개 \t "
                + tx.getText().toString() + "(" + type + ")";

        Child.add(new ChildList(content));
        HistoryFragment.Parent.add(0, new ParentList(title, Child));
        HistoryFragment.adapter = new DocExpandableRecyclerAdapter(HistoryFragment.Parent);
        HistoryFragment.recycler_view.setAdapter(HistoryFragment.adapter);

        MainActivity.getBill();
        //영수증 추가랑 불러오는거에서 에러가 뜨는데;; 해결좀 해보자

        purchase.clear();
        adapter2.notifyDataSetChanged();
        MyPagerAdapter.chartFragment.chartDraw();
        tx.setText("0");
    }

    public int findArrayList_item(String name){

        int i = -1;
        int count = -1;

        for(itemList il : purchase){
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
            purchase.clear();
            adapter2.notifyDataSetChanged();
            tx.setText("마감");
        } else{
            bt1.setEnabled(true);
            bt2.setEnabled(true);
            gv.setEnabled(true);
            lv.setEnabled(true);
            purchase.clear();
            adapter2.notifyDataSetChanged();
            tx.setText("0");
        }

    }

    public void refresh(){

        GregorianCalendar calendar = new GregorianCalendar();

        MyPagerAdapter.chartFragment.dateS = calendar;
        MyPagerAdapter.chartFragment.dateE = calendar;
        MyPagerAdapter.chartFragment.chartFirstDraw();

        MyPagerAdapter.historyFragment.dateS = calendar;
        MyPagerAdapter.historyFragment.dateE = calendar;
        MyPagerAdapter.historyFragment.historyFirstDraw();
    }

}
