package com.example.dogyun.myapplication.Models;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by dogyun on 2018-09-26.
 */

public class ParentList extends ExpandableGroup<ChildList> {

    String title;

    public ParentList(String title, List<ChildList> items) {
        super(title, items);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}