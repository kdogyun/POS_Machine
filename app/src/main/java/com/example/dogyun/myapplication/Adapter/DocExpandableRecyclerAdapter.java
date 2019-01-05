package com.example.dogyun.myapplication.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dogyun.myapplication.Models.ChildList;
import com.example.dogyun.myapplication.Models.ParentList;
import com.example.dogyun.myapplication.R;
import com.example.dogyun.myapplication.ViewHolders.MyChildViewHolder;
import com.example.dogyun.myapplication.ViewHolders.MyParentViewHolder;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by dogyun on 2018-09-28.
 */

public class DocExpandableRecyclerAdapter extends ExpandableRecyclerViewAdapter<MyParentViewHolder,MyChildViewHolder> {

    public DocExpandableRecyclerAdapter(List<ParentList> groups) {
        super(groups);
    }

    @Override
    public MyParentViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_parent, parent, false);
        return new MyParentViewHolder(view);
    }

    @Override
    public MyChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_child, parent, false);
        return new MyChildViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(MyChildViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {

        final ChildList childItem = ((ParentList) group).getItems().get(childIndex);
        final String str = ((ParentList) group).getTitle();
        holder.onBind(childItem.getTitle(), str);
        final String TitleChild = group.getTitle();
        holder.listChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d("이거 클릭했어용",str);
                //Toast toast = Toast.makeText(getApplicationContext(), TitleChild, Toast.LENGTH_SHORT);
                //toast.show();
            }

        });

    }

    @Override
    public void onBindGroupViewHolder(MyParentViewHolder holder, int flatPosition, final ExpandableGroup group) {
        holder.setParentTitle(group);

        if (group.getItems() == null) {
            holder.listGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast toast = Toast.makeText(getApplicationContext(), group.toString(), Toast.LENGTH_SHORT);
                    //toast.show();
                }
            });

        }
    }
}