package com.planitse2022.planit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.planitse2022.planit.R;
import com.planitse2022.planit.data.CheckItemData;
import com.planitse2022.planit.data.ChecklistData;
import com.planitse2022.planit.view.group.GroupActivity;

import java.util.ArrayList;
import java.util.List;

public class ChecklistSummaryAdapter extends RecyclerView.Adapter<ChecklistSummaryAdapter.CheckSummaryHolder> {
    ArrayList<ChecklistData> list;

    public ChecklistSummaryAdapter(List<ChecklistData> list) {
        this.list = new ArrayList<ChecklistData>(list);
    }

    public ChecklistSummaryAdapter() {
        this.list = new ArrayList<ChecklistData>();
    }

    public void addItem(List<ChecklistData> list) {
        this.list.addAll(list);
    }

    public void addItem(ChecklistData data) {
        this.list.add(data);
    }

    @NonNull
    @Override
    public ChecklistSummaryAdapter.CheckSummaryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.lay_checklist_summary, parent, false);
        return new ChecklistSummaryAdapter.CheckSummaryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChecklistSummaryAdapter.CheckSummaryHolder holder, int position) {
        holder.tvGoalName.setText(list.get(position).getUserGoal());
        holder.tvGroupName.setText(list.get(position).getGroupName());
        //오늘 날짜에 해당하는 체크리스트만 골라 담기
        holder.checkItemList = new ArrayList<CheckItemData>();
        for(CheckItemData item: list.get(position).getCheckItemList()) {
            if(item.isToday()) {
                holder.checkItemList.add(item);
            }
        }
        holder.listChecklist.setAdapter(new ChecklistAdapter(holder.checkItemList));

        Log.d("holder"+position, ""+list.get(position).getCheckItemList().toString());
        final int gid = list.get(position).getGroupID();

        if (holder.checkItemList.size() > 0) {
            if (R.id.checksum_list_checklist == holder.listSwitcher.getNextView().getId()) {
                holder.listSwitcher.showNext();
            }
        } else if (R.id.checksum_tv_empty == holder.listSwitcher.getNextView().getId()) {
            holder.listSwitcher.showNext();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void clear() {
        this.list.clear();
    }

    class CheckSummaryHolder extends RecyclerView.ViewHolder{
        ArrayList<CheckItemData> checkItemList;
        TextView tvGoalName, tvGroupName;
        ListView listChecklist;
        ViewSwitcher listSwitcher;
        Button btnMoreDetail;
        public CheckSummaryHolder(@NonNull View itemView) {
            super(itemView);
            tvGoalName = itemView.findViewById(R.id.checksum_tv_goalname);
            tvGroupName = itemView.findViewById(R.id.checksum_tv_groupname);
            listChecklist = itemView.findViewById(R.id.checksum_list_checklist);
            btnMoreDetail = itemView.findViewById(R.id.checksum_btn_moredetail);
            listSwitcher = itemView.findViewById(R.id.checksum_switch_list);

            btnMoreDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    Log.e("test",pos + "");
                    if (pos != RecyclerView.NO_POSITION) {
                        ChecklistData item = list.get(pos);
                        Intent in = new Intent(view.getContext(), GroupActivity.class);
                        in.putExtra("groupName", item.getGroupName());
                        in.putExtra("groupID", item.getGroupID());
                        in.putExtra("page", 2);
                        view.getContext().startActivity(in);
                    }
                }
            });
        }
    }
}