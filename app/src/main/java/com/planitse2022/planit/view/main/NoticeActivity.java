package com.planitse2022.planit.view.main;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.planitse2022.planit.Adapter.NoticeAdapter;
import com.planitse2022.planit.R;
import com.planitse2022.planit.data.NoticeData;
import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.util.dpconverter.DPConverter;
import com.planitse2022.planit.util.rdecoration.PaddingDecoration;
import com.planitse2022.planit.view.OneListActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticeActivity extends OneListActivity {
    private NoticeAdapter noticeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContent("나의 알림", R.drawable.ic_icon_notice, "나의 알림");

        noticeAdapter = new NoticeAdapter();
        listList.setLayoutManager(new LinearLayoutManager(this));
        listList.setHasFixedSize(true);
        listList.addItemDecoration(new PaddingDecoration(DPConverter.dpToInt(this,10), PaddingDecoration.BOTTOM));
        listList.setAdapter(noticeAdapter);

        progressLayout.addLoading();
        retrofitAPI.getUserNoticeList().enqueue(new Callback<ResponseData<List<NoticeData>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<NoticeData>>> call, Response<ResponseData<List<NoticeData>>> response) {
                ResponseData<List<NoticeData>> responseData = response.body();
                if(responseData.getResult() == 0) {
                    noticeAdapter.clear();
                    noticeAdapter.addItem(responseData.getData());
                    noticeAdapter.notifyDataSetChanged();
                }
                progressLayout.doneLoading();
            }

            @Override
            public void onFailure(Call<ResponseData<List<NoticeData>>> call, Throwable t) {
                progressLayout.doneLoading();
            }
        });
    }
}