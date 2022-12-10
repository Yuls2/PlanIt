package com.planitse2022.planit.view.plantkeeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.planitse2022.planit.Adapter.PlantAdapter;
import com.planitse2022.planit.R;
import com.planitse2022.planit.data.PlantData;
import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.data.UserData;
import com.planitse2022.planit.util.ProgressLayout;
import com.planitse2022.planit.util.dpconverter.DPConverter;
import com.planitse2022.planit.util.rdecoration.PaddingDecoration;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.ServiceGenerator;
import com.planitse2022.planit.util.toolbar.ToolbarSubpage;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectPlantFromKeeperActivity extends AppCompatActivity {
    private RecyclerView plantList;

    private ProgressLayout progressLayout;
    private RetrofitAPI retrofitAPI;
    private PlantAdapter plantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_keeper);

        ToolbarSubpage toolbar = new ToolbarSubpage(this);
        toolbar.setTitle("보관함에서 식물 가져오기");

        progressLayout = new ProgressLayout(getWindow().getDecorView());
        progressLayout.setBackgroundColor(R.color.pi_back);
        UserData userData = UserData.getInstance();

        plantList = findViewById(R.id.list_plant);

        // 리스트에 사용할 어댑터 생성
        plantAdapter = new PlantAdapter();
        plantAdapter.setMode(1);
        // 멤버 리스트 레이아웃 매니저 및 간격 등 설정
        plantList.setLayoutManager(new GridLayoutManager(this, 2));
        plantList.setHasFixedSize(true);
        plantList.addItemDecoration(new PaddingDecoration(DPConverter.dpToInt(this,20), PaddingDecoration.RIGHT));
        plantList.addItemDecoration(new PaddingDecoration(DPConverter.dpToInt(this,20), PaddingDecoration.BOTTOM));
        plantList.setAdapter(plantAdapter);

        retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, userData.getToken());

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