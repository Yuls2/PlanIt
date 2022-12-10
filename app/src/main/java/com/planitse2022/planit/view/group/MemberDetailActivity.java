package com.planitse2022.planit.view.group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.planitse2022.planit.Adapter.PostAdapter;
import com.planitse2022.planit.R;
import com.planitse2022.planit.data.MemberData;
import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.data.UserData;
import com.planitse2022.planit.util.ProgressLayout;
import com.planitse2022.planit.util.dpconverter.DPConverter;
import com.planitse2022.planit.util.plant.PlantView;
import com.planitse2022.planit.util.rdecoration.PaddingDecoration;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.ServiceGenerator;
import com.planitse2022.planit.util.toolbar.ToolbarSubpage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberDetailActivity extends AppCompatActivity {

    private RecyclerView postList;
    private PostAdapter postAdapter;
    private RetrofitAPI retrofitAPI;
    private TextView tvMemberName, tvMemberGoal, tvMemberDate, tvHistoryTitle;
    private EditText edtMemberStatus;
    private ImageView imgMemberManager;

    private ProgressLayout progressLayout;
    private ViewSwitcher listSwitcher;
    private PlantView plantView;
    private int groupID;
    private String memberUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_detail);

        ToolbarSubpage toolbar = new ToolbarSubpage(this);
        toolbar.setTitle("멤버 상세 정보");
        
        Intent intent = getIntent();
        memberUserID = intent.getStringExtra("memberUserID");
        groupID = intent.getIntExtra("groupID", -1);
        progressLayout = new ProgressLayout(getWindow().getDecorView());
        UserData userData = UserData.getInstance();

        postList = findViewById(R.id.list_post);
        listSwitcher = findViewById(R.id.switch_list);
        plantView = new PlantView(findViewById(R.id.plant_lay));
        tvMemberName  = findViewById(R.id.tv_member_name);
        tvMemberGoal = findViewById(R.id.tv_member_goal);
        tvMemberDate = findViewById(R.id.tv_member_date);
        edtMemberStatus = findViewById(R.id.edt_member_status);
        tvHistoryTitle = findViewById(R.id.tv_history_title);
        imgMemberManager = findViewById(R.id.img_member_manager);

        // 리스트에 사용할 어댑터 생성
        postAdapter = new PostAdapter(2);
        // 포스트 리스트 레이아웃 매니저 및 간격 등 설정
        postList.setLayoutManager(new LinearLayoutManager(MemberDetailActivity.this));
        postList.addItemDecoration(new PaddingDecoration(DPConverter.dpToInt(MemberDetailActivity.this,15), PaddingDecoration.BOTTOM));
        postList.setAdapter(postAdapter);
        
        // 유저의 정보 가져오기
        retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, userData.getToken());

        progressLayout.addLoading();
        plantView.setPlantByGroupAndUserID(memberUserID, groupID);
        retrofitAPI.getGroupMember(memberUserID, groupID).enqueue(new Callback<ResponseData<MemberData>>() {
            @Override
            public void onResponse(Call<ResponseData<MemberData>> call, Response<ResponseData<MemberData>> response) {
                ResponseData<MemberData> responseData = response.body();
                if(responseData.getResult() == 0) {
                    MemberData data = responseData.getData();
                    tvMemberName.setText(data.getUserName());
                    tvMemberGoal.setText(data.getUserGoal());
                    tvMemberDate.setText(data.getDate());
                    edtMemberStatus.setText(data.getStatus());
                    tvHistoryTitle.setText(data.getUserName() + "님의 인증 역사");
                    if(data.isManager()) {
                        imgMemberManager.setVisibility(View.VISIBLE);
                    }
                    else {
                        imgMemberManager.setVisibility(View.GONE);
                    }

                    if(data.getUserID().equals(userData.getUserID())) {
                        edtMemberStatus.setEnabled(true);
                        edtMemberStatus.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(TextView view, int i, KeyEvent keyEvent) {
                                view.clearFocus();
                                //키보드 내리기
                                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                //상태 메세지 DB에 업데이트
                                retrofitAPI.updateUserGroupStatus(groupID, view.getText().toString()).enqueue(new Callback<ResponseData<Integer>>() {
                                    @Override
                                    public void onResponse(Call<ResponseData<Integer>> call, Response<ResponseData<Integer>> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<ResponseData<Integer>> call, Throwable t) {

                                    }
                                });
                                return false;
                            }
                        });
                    }
                    
                    postAdapter.clear();
                    postAdapter.addItem(data.getPostList());
                    postAdapter.notifyDataSetChanged();

                    if (postAdapter.getItemCount() > 0) {
                        if (R.id.lay_post == listSwitcher.getNextView().getId()) {
                            listSwitcher.showNext();
                        }
                    } else if (R.id.lay_empty == listSwitcher.getNextView().getId()) {
                        listSwitcher.showNext();
                    }
                }
                progressLayout.doneLoading();
            }

            @Override
            public void onFailure(Call<ResponseData<MemberData>> call, Throwable t) {

            }
        });

    }
}