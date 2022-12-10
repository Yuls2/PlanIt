package com.planitse2022.planit.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.planitse2022.planit.R;
import com.planitse2022.planit.data.CheckItemData;

import java.util.ArrayList;
import java.util.List;

public class PostChecklistAdapter extends BaseAdapter {
    private ArrayList<CheckItemData> list;

    public PostChecklistAdapter() {
        this.list = new ArrayList<CheckItemData>();
    }
    public PostChecklistAdapter(List<CheckItemData> list) {
        this.list = new ArrayList<CheckItemData>(list);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CheckItemData getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();
        ViewHolder holder = null;
        if(view == null || ((ViewHolder)view.getTag()).id != list.get(i).getCheckID()) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.lay_post_checkbox, viewGroup, false);
            holder = new ViewHolder();
            holder.tvTitle = view.findViewById(R.id.chkpost_tv_title);
            holder.checkBox = view.findViewById(R.id.chkpost_img_chk);
            holder.id = list.get(i).getCheckID();
            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tvTitle.setText(list.get(i).getTitle());
        if(list.get(i).isChecked()) {
            holder.checkBox.setImageResource(R.drawable.ic_icon_check_on);
        }

        return view;
    }

    public static class ViewHolder {
        public int id;
        public TextView tvTitle;
        public ImageView checkBox;
    }

    public void addItem(CheckItemData item) {
        list.add(item);
    }

    public void addItem(List<CheckItemData> list) {
        this.list.addAll(list);
    }

    public void clear() {
        this.list.clear();
    }

}
