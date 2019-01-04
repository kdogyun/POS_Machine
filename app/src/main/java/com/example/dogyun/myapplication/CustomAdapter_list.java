package com.example.dogyun.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dogyun on 2018-08-24.
 */

public class CustomAdapter_list extends BaseAdapter {

    private Context c;
    private ArrayList<itemList> item;
    /*
    private ArrayList<String> names;
    private ArrayList<String> price;

    public CustomAdapter_list(Context c, ArrayList<String> names, ArrayList<String> price) {
        this.c = c;
        this.names = names;
        this.price = price;
    }
*/

    public CustomAdapter_list(Context c, ArrayList<itemList> item) {
        this.c = c;
        this.item = item;
        /*
        this.names = names;
        this.price = price;
        */
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_list, null);
        }

        TextView nameTv = (TextView) convertView.findViewById(R.id.textView4);
        TextView countTv = (TextView) convertView.findViewById(R.id.textView4_0);

        nameTv.setText(item.get(position).name);
        countTv.setText("X" + item.get(position).count);
        return convertView;
    }
}
