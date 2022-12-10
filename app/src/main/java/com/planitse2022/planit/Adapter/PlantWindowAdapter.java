package com.planitse2022.planit.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.planitse2022.planit.R;
import com.planitse2022.planit.util.plant.PlantView;

import java.util.ArrayList;
import java.util.List;

public class PlantWindowAdapter extends RecyclerView.Adapter<PlantWindowAdapter.PlantHolder> {
    private ArrayList<Integer> list;
    private View view;

    public PlantWindowAdapter(List<Integer> list) {
        this.list = new ArrayList<Integer>(list);
    }
    public PlantWindowAdapter() {
        this.list = new ArrayList<Integer>();
    }

    public void addItem(List<Integer> list) {
        this.list.addAll(list);
    }

    public void addItem(Integer data) {
        this.list.add(data);
    }

    public void clear() { this.list.clear(); }


    @NonNull
    @Override
    public PlantWindowAdapter.PlantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.lay_plantitem, parent, false);
        return new PlantWindowAdapter.PlantHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantWindowAdapter.PlantHolder holder, int position) {
        holder.plantView.setPlantByPlantID(list.get(position));
        holder.plantView.setPotVisibility(false);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PlantHolder extends RecyclerView.ViewHolder{
        PlantView plantView;
        public PlantHolder(@NonNull View itemView) {
            super(itemView);
            plantView = new PlantView(itemView.findViewById(R.id.plant_plant_lay));
        }
    }

}

