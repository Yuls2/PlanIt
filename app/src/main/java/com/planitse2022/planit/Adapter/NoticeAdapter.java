package com.planitse2022.planit.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.planitse2022.planit.R;
import com.planitse2022.planit.data.NoticeData;

import java.util.ArrayList;
import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeHolder> {
    private ArrayList<NoticeData> list;
    private View view;

    public NoticeAdapter(List<NoticeData> list) {
        this.list = new ArrayList<NoticeData>(list);
    }
    public NoticeAdapter() {
        this.list = new ArrayList<NoticeData>();
    }

    public void addItem(List<NoticeData> list) {
        this.list.addAll(list);
    }

    public void addItem(NoticeData data) {
        this.list.add(data);
    }

    public void clear() { this.list.clear(); }


    @NonNull
    @Override
    public NoticeAdapter.NoticeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.lay_noticeitem, parent, false);
        return new NoticeAdapter.NoticeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeAdapter.NoticeHolder holder, int position) {
        holder.tvName.setText(list.get(position).getSenderName());
        holder.tvTitle.setText(list.get(position).getMessage());
        holder.tvDate.setText(list.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class NoticeHolder extends RecyclerView.ViewHolder{
        TextView tvTitle, tvName, tvDate;
        public NoticeHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.notice_tv_name);
            tvTitle = itemView.findViewById(R.id.notice_tv_title);
            tvDate = itemView.findViewById(R.id.notice_tv_date);
        }
    }

}

