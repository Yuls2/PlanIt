package com.planitse2022.planit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.planitse2022.planit.R;
import com.planitse2022.planit.data.ManageData;

import java.util.ArrayList;
import java.util.List;

public class ManageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ManageData> list;
    private View view;

    public ManageAdapter(List<ManageData> list) {
        this.list = new ArrayList<ManageData>(list);
    }

    public void addItem(List<ManageData> list) {
        this.list.addAll(list);
    }

    public void addItem(ManageData data) {
        this.list.add(data);
    }

    public void clear() { this.list.clear(); }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getViewType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(viewType == 0) {
            view = inflater.inflate(R.layout.lay_group_manageitem, parent, false);
            return new ManageHolder(view);
        }
        else {
            view = inflater.inflate(R.layout.lay_group_manage_category, parent, false);
            return new CategoryHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ManageHolder) {
            ManageHolder manageHolder = (ManageHolder) holder;
            manageHolder.tvTitle.setText(list.get(position).getTitle());
            manageHolder.tvComment.setText(list.get(position).getComment());
            manageHolder.imgIcon.setImageResource(list.get(position).getIcon());
//            Glide.with(view).load(list.get(position).getIcon()).into(manageHolder.imgIcon);
        }
        else if(holder instanceof CategoryHolder) {
            CategoryHolder categoryHolder = (CategoryHolder) holder;
            categoryHolder.tvTitle.setText(list.get(position).getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ManageHolder extends RecyclerView.ViewHolder{
        TextView tvTitle, tvComment;
        ImageView imgIcon;
        public ManageHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.manage_tv_title);
            tvComment = itemView.findViewById(R.id.manage_tv_comment);
            imgIcon = itemView.findViewById(R.id.manage_img_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Intent in = list.get(pos).getIntent(itemView.getContext());
                        in.putExtra("groupID", list.get(pos).getGroupID());
                        in.putExtra("isEdit", true);
                        view.getContext().startActivity(in);
                    }
                }
            });
        }
    }

    class CategoryHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.manage_tv_title);
        }
    }

}

