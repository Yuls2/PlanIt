package com.planitse2022.planit.view.group;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.planitse2022.planit.R;
import com.planitse2022.planit.data.GroupData;
import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.data.UserData;
import com.planitse2022.planit.util.ProgressLayout;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.ServiceGenerator;
import com.planitse2022.planit.util.toolbar.ToolbarSubpage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupDetailActivity extends AppCompatActivity {
    private int groupID;
    private TextView tvGroupName, tvGroupComment, tvGroupRule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        ToolbarSubpage toolbar = new ToolbarSubpage(this);
        toolbar.setTitle("그룹 설명");

        Intent intent = getIntent();
        groupID = intent.getIntExtra("groupID", -1);

        UserData userData = UserData.getInstance();

        tvGroupName = findViewById(R.id.tv_group_name);
        tvGroupComment = findViewById(R.id.tv_group_comment);
        tvGroupRule = findViewById(R.id.tv_group_rule);
        ProgressLayout progressLayout = new ProgressLayout(getWindow().getDecorView());

        progressLayout.addLoading();
        RetrofitAPI retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, userData.getToken());
        retrofitAPI.getGroupInformation(groupID).enqueue(new Callback<ResponseData<GroupData>>() {
            @Override
            public void onResponse(Call<ResponseData<GroupData>> call, Response<ResponseData<GroupData>> response) {
                ResponseData<GroupData> responseData = response.body();
                if(responseData.getResult() == 0) {
                    GroupData data = responseData.getData();
                    tvGroupName.setText(data.getGroupName());
                    tvGroupComment.setText(data.getGroupComment());
                    tvGroupRule.setText(data.getGroupRule());
                }
                progressLayout.doneLoading();
            }

            @Override
            public void onFailure(Call<ResponseData<GroupData>> call, Throwable t) {
                progressLayout.doneLoading();
            }
        });
    }
}