package com.planitse2022.planit.view.editgroup;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;

import com.planitse2022.planit.Adapter.MemberAdapter;
import com.planitse2022.planit.R;
import com.planitse2022.planit.data.MemberData;
import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.util.dpconverter.DPConverter;
import com.planitse2022.planit.util.rdecoration.PaddingDecoration;
import com.planitse2022.planit.view.OneListActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberManageActivity extends OneListActivity {
    private MemberAdapter memberAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContent("멤버 목록", R.drawable.ic_icon_member_sel, "플래닛 멤버 추방");

        memberAdapter = new MemberAdapter(1);
        listList.setLayoutManager(new GridLayoutManager(this, 2));
        listList.setHasFixedSize(true);
        listList.addItemDecoration(new PaddingDecoration(DPConverter.dpToInt(this,20), PaddingDecoration.RIGHT));
        listList.addItemDecoration(new PaddingDecoration(DPConverter.dpToInt(this,20), PaddingDecoration.BOTTOM));
        listList.setAdapter(memberAdapter);

        progressLayout.addLoading();
        retrofitAPI.getGroupMemberList(groupID).enqueue(new Callback<ResponseData<List<MemberData>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<MemberData>>> call, Response<ResponseData<List<MemberData>>> response) {
                ResponseData<List<MemberData>> responseData = response.body();
                if(responseData.getResult() == 0) {
                    memberAdapter.clear();
                    memberAdapter.addItem(responseData.getData());
                    memberAdapter.notifyDataSetChanged();
                }
                progressLayout.doneLoading();
            }

            @Override
            public void onFailure(Call<ResponseData<List<MemberData>>> call, Throwable t) {
                progressLayout.doneLoading();
            }
        });
    }
}