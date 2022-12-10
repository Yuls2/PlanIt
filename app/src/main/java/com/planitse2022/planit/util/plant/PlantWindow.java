package com.planitse2022.planit.util.plant;

import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.planitse2022.planit.Adapter.PlantWindowAdapter;
import com.planitse2022.planit.R;
import com.planitse2022.planit.data.UserData;
import com.planitse2022.planit.util.dpconverter.DPConverter;
import com.planitse2022.planit.util.rdecoration.PaddingDecoration;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.RetrofitClient;
import com.planitse2022.planit.util.retrofit.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

public class PlantWindow {
    private ConstraintLayout view;
    private RecyclerView plantList;
    private ImageView imgBackground;
    private RetrofitAPI retrofitAPI;
    private ArrayList<Integer> list;
    private PlantWindowAdapter adapter;

    public PlantWindow(ConstraintLayout view) {
        this.view = view;
        plantList = view.findViewById(R.id.list_top_plant);
        imgBackground = view.findViewById(R.id.img_background);

        adapter = new PlantWindowAdapter();
        plantList.setAdapter(adapter);
        plantList.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        plantList.setHasFixedSize(true);

        int paddingSize = -DPConverter.dpToInt(view.getContext(), 30);
        plantList.addItemDecoration(new PaddingDecoration(paddingSize, PaddingDecoration.LEFT));
        plantList.addItemDecoration(new PaddingDecoration(paddingSize, PaddingDecoration.RIGHT));

        UserData userData = UserData.getInstance();
        retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, userData.getToken());
    }

    public void setBackground(int background) {
        Glide.with(view.getContext())
                .load(RetrofitClient.getImageurl("background","groupBackground" + background))
                .transform(new CenterCrop(),new RoundedCorners(DPConverter.dpToInt(view.getContext(),10)))
                .into(imgBackground);
    }

    public void setList(List<Integer> list) {
        this.list = new ArrayList<>(list);
        adapter.clear();
        adapter.addItem(list);
        adapter.notifyDataSetChanged();
    }
}
