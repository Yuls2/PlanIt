package com.planitse2022.planit.view.editgroup;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.planitse2022.planit.Adapter.GroupRequestAdapter;
import com.planitse2022.planit.R;
import com.planitse2022.planit.data.GroupRequestData;
import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.util.dpconverter.DPConverter;
import com.planitse2022.planit.util.rdecoration.PaddingDecoration;
import com.planitse2022.planit.view.OneListActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupRequestListActivity extends OneListActivity {
    private GroupRequestAdapter requestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContent("플래닛 가입 신청 목록", R.drawable.ic_icon_user_sel, "가입 수락");

        requestAdapter = new GroupRequestAdapter();
        listList.setAdapter(requestAdapter);
        listList.setLayoutManager(new LinearLayoutManager(this));
        listList.addItemDecoration(new PaddingDecoration(DPConverter.dpToInt(this,15), PaddingDecoration.BOTTOM));

        progressLayout.addLoading();
        retrofitAPI.getGroupRequestList(groupID).enqueue(new Callback<ResponseData<List<GroupRequestData>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<GroupRequestData>>> call, Response<ResponseData<List<GroupRequestData>>> response) {
                ResponseData<List<GroupRequestData>> responseData = response.body();
                if(responseData.getResult() == 0) {
                    requestAdapter.clear();
                    requestAdapter.addItem(responseData.getData());
                    requestAdapter.notifyDataSetChanged();
                }
                progressLayout.doneLoading();
            }

            @Override
            public void onFailure(Call<ResponseData<List<GroupRequestData>>> call, Throwable t) {
                progressLayout.doneLoading();
            }
        });
    }
}