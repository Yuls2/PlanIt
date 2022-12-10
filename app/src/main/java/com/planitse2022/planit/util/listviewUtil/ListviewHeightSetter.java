package com.planitse2022.planit.util.listviewUtil;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class ListviewHeightSetter {
    public static void setListHeight(ListView listView, BaseAdapter adapter) {
        int listHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            listHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = listHeight + (listView.getDividerHeight() * (listView.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
