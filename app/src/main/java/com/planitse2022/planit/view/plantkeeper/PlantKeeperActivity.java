package com.planitse2022.planit.view.plantkeeper;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;

import com.planitse2022.planit.Adapter.PlantAdapter;
import com.planitse2022.planit.R;
import com.planitse2022.planit.data.PlantData;
import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.util.dpconverter.DPConverter;
import com.planitse2022.planit.util.rdecoration.PaddingDecoration;
import com.planitse2022.planit.view.OneListActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlantKeeperActivity  extends OneListActivity {
    private PlantAdapter plantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContent("보관 중인 식물", R.drawable.ic_icon_planit, "식물 보관함");


        plantAdapter = new PlantAdapter();
        plantAdapter.setMode(0);
        listList.setLayoutManager(new GridLayoutManager(this, 2));
        listList.setHasFixedSize(true);
        listList.addItemDecoration(new PaddingDecoration(DPConverter.dpToInt(this,20), PaddingDecoration.RIGHT));
        listList.addItemDecoration(new PaddingDecoration(DPConverter.dpToInt(this,20), PaddingDecoration.BOTTOM));
        listList.setAdapter(plantAdapter);

        requestPlant();
    }

    private void requestPlant() {
        progressLayout.addLoading();
        retrofitAPI.getUserPlantList(1).enqueue(new Callback<ResponseData<List<PlantData>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<PlantData>>> call, Response<ResponseData<List<PlantData>>> response) {
                ResponseData<List<PlantData>> responseData = response.body();
                if(responseData.getResult() == 0) {
                    plantAdapter.clear();
                    plantAdapter.addItem(responseData.getData());
                    plantAdapter.notifyDataSetChanged();
                }
                progressLayout.doneLoading();
            }

            @Override
            public void onFailure(Call<ResponseData<List<PlantData>>> call, Throwable t) {
                progressLayout.doneLoading();
            }
        });
    }
}
