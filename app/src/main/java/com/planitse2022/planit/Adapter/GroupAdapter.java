package com.planitse2022.planit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.planitse2022.planit.R;
import com.planitse2022.planit.data.GroupData;
import com.planitse2022.planit.util.dialog.GroupDialog;
import com.planitse2022.planit.view.group.GroupActivity;

import java.util.ArrayList;
import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupHolder> {
    private ArrayList<GroupData> list;
    private View view;
    private int type; //0: 내 플래닛, 1: 검색 플래닛

    public GroupAdapter(List<GroupData> list, int type) {
        this.list = new ArrayList<GroupData>(list);
        this.type = type;
    }

    public GroupAdapter(int type) {
        this.list = new ArrayList<GroupData>();
        this.type = type;
    }

    public void addItem(List<GroupData> list) {
        this.list.addAll(list);
    }

    public void addItem(GroupData data) {
        this.list.add(data);
    }

    public void clear() {this.list.clear(); }


    @NonNull
    @Override
    public GroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.lay_groupitem, parent, false);
        //holder 생성
        GroupHolder holder = new GroupHolder(view);
        holder.setType(type);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupHolder holder, int position) {
        holder.tvGroupName.setText(list.get(position).getGroupName());
        holder.tvGroupComment.setText(list.get(position).getGroupComment());
        holder.tvGroupScore.setText(""+list.get(position).getGroupScore());
        holder.tvMemberNum.setText(list.get(position).getMemberNum() + "/" + list.get(position).getMaxMemberNum());
        if(type == 0) {
            holder.tvMyGoal.setText(list.get(position).getUserGoal());
        }
        else {
            holder.layMyGoal.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class GroupHolder extends RecyclerView.ViewHolder{
        int type;
        TextView tvGroupName, tvGroupComment, tvMyGoal, tvGroupScore, tvMemberNum;
        LinearLayout layMyGoal;
        public GroupHolder(@NonNull View itemView) {
            super(itemView);
            tvGroupName = itemView.findViewById(R.id.group_tv_name);
            tvGroupComment = itemView.findViewById(R.id.group_tv_comment);
            tvMyGoal = itemView.findViewById(R.id.group_tv_mygoal);
            tvGroupScore = itemView.findViewById(R.id.group_tv_score);
            tvMemberNum = itemView.findViewById(R.id.group_tv_membernum);
            layMyGoal = itemView.findViewById(R.id.group_lay_mygoal);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(type == 0) { //내 플래닛
                        int pos = getAdapterPosition() ;
                        if (pos != RecyclerView.NO_POSITION) {
                            GroupData item = list.get(pos) ;
                            Intent in = new Intent(view.getContext(), GroupActivity.class);
                            in.putExtra("groupName", item.getGroupName());
                            in.putExtra("groupID", item.getGroupID());
                            view.getContext().startActivity(in);
                        }

                    }
                    else if(type == 1) { //검색 플래닛
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            GroupDialog dialog = new GroupDialog(view.getContext(), list.get(pos));
                            dialog.show();
                        }
                    }
                }
            });
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}


