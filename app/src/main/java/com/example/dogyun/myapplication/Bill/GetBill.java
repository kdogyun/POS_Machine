package com.example.dogyun.myapplication.Bill;

import android.content.ContentValues;
import android.content.Context;

import com.example.dogyun.myapplication.MainActivity;
import com.example.dogyun.myapplication.Models.BillType;
import com.example.dogyun.myapplication.Networks.NetworkTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dogyun on 2018-09-27.
 */

public class GetBill{

    String date;
    ArrayList<BillType> ab;
    Context context;

    public GetBill() {
    }

    public GetBill(String date){
        this.date = date;
    }

    public GetBill(ArrayList<BillType> ab,String date){
        this.date = date;
        this.ab = ab;
    }

    public GetBill(Context context, ArrayList<BillType> ab, String date){
        this.context = context;
        this.date = date;
        this.ab = ab;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void getJson(){
        String url =  MainActivity.siteURL + "POS_billg.php";
        ContentValues cv = new ContentValues();
        cv.put("cago","PLAN_bill");
        cv.put("date", date);

        // AsyncTask를 통해 HttpURLConnection 수행.
        NetworkTask networkTask = new NetworkTask(context, url, cv);

        try {
            doJSONParser(networkTask.execute().get());
            //doJSONParser(networkTask.execute().get());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void doJSONParser(String str){
        try{
            JSONObject order = new JSONObject(str);
            JSONArray index = order.getJSONArray("bill");
            for (int i = 0; i < index.length(); i++) {
                JSONObject tt = index.getJSONObject(i);
                BillType bt = new BillType();
                /*
                try {
                    //bt.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E").parse(tt.getString("date") + " 월"); //배포용
                } catch(ParseException e){
                    try{
                    //bt.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E").parse(tt.getString("date") + " Thu"); //에뮬용
                    } catch(ParseException e1){}
                    //e.printStackTrace();
                }
                */
                bt.name = parserString(tt.getString("name"));
                bt.count = parserString(tt.getString("count"));
                bt.total = tt.getInt("total");
                bt.type = tt.getString("type");
                ab.add(bt);
            }
        }
        catch (JSONException e){
        }

    }

    public ArrayList<String> parserString(String str){
        ArrayList<String> as = new ArrayList<String>();
        String[] array = str.substring(1, str.length()-1).split(", ");
        for(int i=0;i<array.length; i++) {
            as.add(array[i]);
        }
        return as;
    }

}
