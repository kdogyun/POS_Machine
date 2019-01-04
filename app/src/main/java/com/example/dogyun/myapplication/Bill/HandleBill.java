package com.example.dogyun.myapplication.Bill;

import android.util.Log;

import com.example.dogyun.myapplication.ColorFragment;
import com.example.dogyun.myapplication.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dogyun on 2018-09-27.
 */

public class HandleBill {

    ArrayList<String> name;
    ArrayList<Integer> count;
    ArrayList<Integer> cash;
    ArrayList<Integer> card;
    ArrayList<Integer> total;
    ArrayList<BillType> ab;

    public HandleBill(ArrayList<BillType> ab, ArrayList<Integer> count, ArrayList<Integer> cash, ArrayList<Integer> card, ArrayList<Integer> total) {
        this.count = count;
        this.cash = cash;
        this.card = card;
        this.total = total;
        this.name = MainActivity.names;
        this.ab = ab;
    }

    public void HandleBill_count(){
        for(int i = 0; i<name.size(); i++){
            count.add(0);
        }

        for(BillType bt : ab){
            ArrayList<String> as = bt.count;
            int i=0;

            for(String s : bt.name){
                int pos = name.indexOf(s);

                if(bt.type.equals("현금_환불")) {
                }
                else if (bt.type.equals("카드_환불")){
                }
                else {
                    count.set(pos,(count.get(pos)+Integer.parseInt(as.get(i))));
                }
                i++;
            }
        }
    }

    public void HandleBill_Hour(){

        for(int i=0;i<11;i++){
            cash.add(0);total.add(0);card.add(0);
        }

        for(BillType bt : ab){
            int hour = Integer.parseInt(new SimpleDateFormat("HH").format(bt.date))-10;
            if(bt.type.equals("현금")){
                cash.set(hour, cash.get(hour) + bt.total);
                total.set(hour, total.get(hour) + bt.total);
            }
            else if(bt.type.equals("카드")) {
                card.set(hour, card.get(hour) + bt.total);
                total.set(hour, total.get(hour) + bt.total);
            }
            else if(bt.type.equals("현금_환불")) {
            }
            else {
            }
        }
    }
}
