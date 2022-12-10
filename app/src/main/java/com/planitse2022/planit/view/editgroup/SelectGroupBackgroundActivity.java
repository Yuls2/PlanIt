package com.planitse2022.planit.view.editgroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.planitse2022.planit.Adapter.BackgroundAdapter;
import com.planitse2022.planit.R;
import com.planitse2022.planit.data.BackgroundData;
import com.planitse2022.planit.data.GroupData;
import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.util.dpconverter.DPConverter;
import com.planitse2022.planit.util.rdecoration.PaddingDecoration;
import com.planitse2022.planit.util.retrofit.RetrofitClient;
import com.planitse2022.planit.view.OneListActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectGroupBackgroundActivity extends OneListActivity {
    private BackgroundAdapter backgroundAdapter;
    private ImageView imgBackground;
    private TextView tvScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        contentView = R.layout.activity_select_group_background;
        super.onCreate(savedInstanceState);
        
        setContent("배경 목록", R.drawable.ic_icon_image, "플래닛 배경 변경");

        tvScore = findViewById(R.id.tv_score);
        imgBackground = findViewById(R.id.img_background);

        backgroundAdapter = new BackgroundAdapter();
        backgroundAdapter.setGroupID(groupID);
        listList.setLayoutManager(new LinearLayoutManager(this));
        listList.setHasFixedSize(true);
        listList.addItemDecoration(new PaddingDecoration(DPConverter.dpToInt(this,20), PaddingDecoration.BOTTOM));
        listList.setAdapter(backgroundAdapter);

        progressLayout.addLoading();
        requestGroupInfo();
        retrofitAPI.getBackgroundList().enqueue(new Callback<List<BackgroundData>>() {
            @Override
            public void onResponse(Call<List<BackgroundData>> call, Response<List<BackgroundData>> response) {
                List<BackgroundData> data = response.body();
                backgroundAdapter.clear();
                backgroundAdapter.addItem(data);
                backgroundAdapter.notifyDataSetChanged();
                progressLayout.doneLoading();
            }

            @Override
            public void onFailure(Call<List<BackgroundData>> call, Throwable t) {
                Log.d("test", t.toString());
                progressLayout.doneLoading();
            }
        });
    }

    public void requestGroupInfo() {
        progressLayout.addLoading();
        retrofitAPI.getGroupInformation(groupID).enqueue(new Callback<ResponseData<GroupData>>() {
            @Override
            public void onResponse(Call<ResponseData<GroupData>> call, Response<ResponseData<GroupData>> response) {
                ResponseData<GroupData> responseData = response.body();
                if(responseData.getResult() == 0) {
                    GroupData data = responseData.getData();

                    Glide.with(SelectGroupBackgroundActivity.this)
                            .load(RetrofitClient.getImageurl("background","groupBackground" + data.getBackground()))
                            .transform(new CenterCrop(),new RoundedCorners(DPConverter.dpToInt(SelectGroupBackgroundActivity.this,10)))
                            .into(imgBackground);
                    tvScore.setText(data.getGroupScore()+"");

                    progressLayout.doneLoading();
                }
            }

            @Override
            public void onFailure(Call<ResponseData<GroupData>> call, Throwable t) {
                progressLayout.doneLoading();
            }
        });
    }
}