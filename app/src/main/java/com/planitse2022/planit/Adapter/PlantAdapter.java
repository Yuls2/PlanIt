package com.planitse2022.planit.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.planitse2022.planit.R;
import com.planitse2022.planit.data.PlantData;
import com.planitse2022.planit.util.plant.PlantView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantHolder> {
    private ArrayList<PlantData> list;
    private View view;
    private int mode;

    public PlantAdapter(List<PlantData> list) {
        this.list = new ArrayList<PlantData>(list);
        mode = 0;
    }
    public PlantAdapter() {
        this.list = new ArrayList<PlantData>();
        mode = 0;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void addItem(List<PlantData> list) {
        this.list.addAll(list);
    }

    public void addItem(PlantData data) {
        this.list.add(data);
    }

    public void clear() { this.list.clear(); }


    @NonNull
    @Override
    public PlantAdapter.PlantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.lay_memberitem, parent, false);
        return new PlantAdapter.PlantHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantAdapter.PlantHolder holder, int position) {
        holder.tvUserName.setText(list.get(position).getName());
        holder.plantView.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PlantHolder extends RecyclerView.ViewHolder{
        TextView tvUserName;
        PlantView plantView;
        public PlantHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.member_tv_name);
            plantView = new PlantView(itemView.findViewById(R.id.member_plant_lay));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        PlantData item = list.get(pos) ;

                        //보관함 선택
                        if(mode == 1) {
                            Activity activity = (Activity)itemView.getContext();
                            Intent in = new Intent();
                            in.putExtra("selectedPlant", (new Gson().toJson(item)));
                            activity.setResult(Activity.RESULT_OK, in);
                            activity.finish();
                        }
                    }
                }
            });
        }
    }

}

