package com.planitse2022.planit.view.group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.planitse2022.planit.R;
import com.planitse2022.planit.data.PlantData;
import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.data.UserData;
import com.planitse2022.planit.util.dialog.NewPlantDialog;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.ServiceGenerator;
import com.planitse2022.planit.util.toolbar.ToolbarSubpage;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupActivity extends AppCompatActivity {
    private static final int FRAG_NUM = 4;
    private Fragment[] fragArr = new Fragment[FRAG_NUM];
    private String groupName;
    private int groupID;

    public static Activity GROUP_ACTIVIRY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        GROUP_ACTIVIRY = GroupActivity.this;

        Intent intent = getIntent();
        groupName = intent.getStringExtra("groupName");
        groupID = intent.getIntExtra("groupID", -1);
        int initPage = intent.getIntExtra("page", 0);

        ToolbarSubpage toolbar = new ToolbarSubpage(this);
        toolbar.setTitle(groupName);

        UserData userData = UserData.getInstance();
        RetrofitAPI retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, userData.getToken());
        //매니저인지 정보 가져오기
        retrofitAPI.getIsManager(groupID).enqueue(new Callback<ResponseData<Boolean>>() {
            @Override
            public void onResponse(Call<ResponseData<Boolean>> call, Response<ResponseData<Boolean>> response) {
                ResponseData<Boolean> responseData = response.body();
                userData.setManager(groupID, responseData.getData());
            }

            @Override
            public void onFailure(Call<ResponseData<Boolean>> call, Throwable t) {

            }
        });
        //식물 존재 여부 체크
        HashMap<String, Object> param = new HashMap<>();
        param.put("targetUserID", userData.getUserID());
        param.put("groupID", groupID);
        NewPlantDialog plantCreateDialog = new NewPlantDialog(GroupActivity.this, groupID);
        retrofitAPI.getPlantInfo(param).enqueue(new Callback<ResponseData<PlantData>>() {
            @Override
            public void onResponse(Call<ResponseData<PlantData>> call, Response<ResponseData<PlantData>> response) {
                ResponseData<PlantData> responseData = response.body();
                if(responseData.getResult() != 0) {
                    // 그룹에 식물이 등록되지 않은 경우
                    plantCreateDialog.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData<PlantData>> call, Throwable t) {

            }
        });

        fragArr[0] = new GroupHomeFragment();
        fragArr[1] = new GroupPostFragment();
        fragArr[2] = new ChecklistFragment();
        fragArr[3] = new GroupMemberFragment();
        //각 페이지에 그룹 아이디, 그룹명 넘겨주기
        Bundle args = new Bundle();
        args.putInt("groupID", groupID);
        args.putString("groupName", groupName);
        for(int i = 0; i < FRAG_NUM; i++) {
            fragArr[i].setArguments(args);
        }
        //디폴트 페이지 홈으로 설정
        getSupportFragmentManager().beginTransaction().replace(R.id.lay_content, fragArr[initPage]).commitAllowingStateLoss();

        TabLayout layNavi = findViewById(R.id.lay_navi);
        layNavi.selectTab(layNavi.getTabAt(initPage));
        layNavi.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment sel = fragArr[tab.getPosition()];
                getSupportFragmentManager().beginTransaction().replace(R.id.lay_content, sel).commitAllowingStateLoss();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}