package com.planitse2022.planit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.planitse2022.planit.R;
import com.planitse2022.planit.data.CheckItemData;
import com.planitse2022.planit.view.editcheckitem.WriteCheckItemActivity;

import java.util.ArrayList;
import java.util.List;

public class EditChecklistAdapter extends BaseAdapter {
    private ArrayList<CheckItemData> list;
    private String strGoalName, strGroupName;

    public EditChecklistAdapter() {
        this.list = new ArrayList<CheckItemData>();
    }
    public EditChecklistAdapter(List<CheckItemData> list) {
        this.list = new ArrayList<CheckItemData>(list);
    }

    public boolean isEmpty() {
        return list.isEmpty();
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

    public ArrayList<CheckItemData> getList() {
        return list;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();
        ViewHolder holder = null;
        if(view == null || ((ViewHolder)view.getTag()).id != list.get(i).getCheckID()) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.lay_edit_checkitem, viewGroup, false);
            holder = new ViewHolder();
            holder.tvTitle = view.findViewById(R.id.chkedt_tv_title);
            holder.tvSelectedDate = view.findViewById(R.id.chkedt_tv_selected_date);
            holder.imgEdit = view.findViewById(R.id.chkedt_img_edit);
            holder.id = list.get(i).getCheckID();
            view.setTag(holder);

        }
        else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tvTitle.setText(list.get(i).getTitle());
        holder.tvSelectedDate.setText(list.get(i).getSelectedDateString());

        //오늘에 해당되지 않는 아이템은 연하게
        if(!list.get(i).isToday()) {
            holder.tvTitle.setTextColor(ContextCompat.getColor(view.getContext(), R.color.pi_disabled));
            holder.tvSelectedDate.setTextColor(ContextCompat.getColor(view.getContext(), R.color.pi_disabled));
        }
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(view.getContext(), WriteCheckItemActivity.class);
                //체크 아이템 정보 파라미터로 보내기
                in.putExtra("isEdit", true);
                in.putExtra("groupName", strGroupName);
                in.putExtra("goalName", strGoalName);

                in.putExtra("checkID", list.get(i).getCheckID());
                in.putExtra("title", list.get(i).getTitle());
                in.putExtra("priority", list.get(i).getPriority());
                in.putExtra("type", list.get(i).getType());
                if(list.get(i).getType() == 0) {
                    in.putExtra("day", list.get(i).getDay());
                }
                else {
                    in.putExtra("date", list.get(i).getDate());
                }
                view.getContext().startActivity(in);
            }
        });
        return view;
    }

    public static class ViewHolder {
        public int id;
        public TextView tvTitle, tvSelectedDate;
        public ImageView imgEdit;
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

    public void setGoalName(String strGoalName) {
        this.strGoalName = strGoalName;
    }

    public void setGroupName(String strGroupName) {
        this.strGroupName = strGroupName;
    }
}
