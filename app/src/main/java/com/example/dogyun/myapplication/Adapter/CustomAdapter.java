package com.example.dogyun.myapplication.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dogyun.myapplication.Models.itemList;
import com.example.dogyun.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dogyun on 2018-08-24.
 */

public class CustomAdapter extends BaseAdapter{

    private Context c;
    private ArrayList<itemList> menu;
    private int type;

    public CustomAdapter(Context c, ArrayList<itemList> menu) {
        this.menu = menu;
        this.c = c;
        this.type = 0;
    }

    @Override
    public int getItemViewType(int position) {
        return menu.get(position).category;
    }

    @Override
    public int getCount() { return menu.size(); }

    @Override
    public Object getItem(int position) {return menu.get(position); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(getItemViewType(position) % 2 == 1){
            convertView = inflater.inflate(R.layout.item_grid, null);
        } else {
            convertView = inflater.inflate(R.layout.item_grid2, null);
        }

        TextView nameTv = (TextView) convertView.findViewById(R.id.textView3);

        nameTv.setText(menu.get(position).name);

        return convertView;
    }
}
