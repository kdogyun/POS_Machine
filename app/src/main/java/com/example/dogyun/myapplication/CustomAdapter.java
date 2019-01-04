package com.example.dogyun.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dogyun on 2018-08-24.
 */

public class CustomAdapter extends BaseAdapter{

    private Context c;
    private ArrayList<String> names;
    /*
    private String[] names;
    private String[] teams;
    int[] images;

    public CustomAdapter(Context c, String[] names, String[] teams, int[] images) {
        this.c = c;
        this.names = names;
        this.images = images;
        this.teams = teams;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return names[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_grid, null);
        }

        TextView nameTv = (TextView) convertView.findViewById(R.id.textView3);
        //ImageView img = (ImageView) convertView.findViewById(R.id.imageView2);

        nameTv.setText(names[position]);
        //img.setImageResource(images[position]);

        return convertView;
    }

    */
    public CustomAdapter(Context c, ArrayList<String> names) {
        this.names = names;
        this.c = c;
    }

    @Override
    public int getCount() { return names.size(); }

    @Override
    public Object getItem(int position) {return names.get(position); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_grid, null);
        }

        TextView nameTv = (TextView) convertView.findViewById(R.id.textView3);
        //ImageView img = (ImageView) convertView.findViewById(R.id.imageView2);

        nameTv.setText(names.get(position));
        //img.setImageResource(images[position]);

        return convertView;
    }
}
