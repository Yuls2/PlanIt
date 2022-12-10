package com.planitse2022.planit.view.editgroup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.planitse2022.planit.R;
import com.planitse2022.planit.data.GroupData;
import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.data.UserData;
import com.planitse2022.planit.util.dialog.OneBtnDialog;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.ServiceGenerator;
import com.planitse2022.planit.util.toolbar.ToolbarSubpage;
import com.planitse2022.planit.view.group.GroupActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupDeleteActivity extends AppCompatActivity {
    private UserData userData;
    private ToolbarSubpage toolbar;
    private RetrofitAPI retrofitAPI;
    private Button btnCancel, btnDelete;
    private CheckBox chAgree;
    private int groupID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_delete);

        toolbar = new ToolbarSubpage(this);
        toolbar.setTitle("플래닛 삭제");

        Intent intent = getIntent();
        groupID = intent.getIntExtra("groupID", -1);
        userData = UserData.getInstance();
        retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, userData.getToken());

        btnCancel = findViewById(R.id.btn_cancel);
        btnDelete = findViewById(R.id.btn_delete_planet);
        chAgree = findViewById(R.id.ch_agree);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        chAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if((Boolean) btnDelete.getTag()) {
                    if(b) {
                        btnDelete.setEnabled(true);
                    }
                    else {
                        btnDelete.setEnabled(false);
                    }
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chAgree.isChecked()) {
                    retrofitAPI.deleteGroup(groupID).enqueue(new Callback<ResponseData<Integer>>() {
                        @Override
                        public void onResponse(Call<ResponseData<Integer>> call, Response<ResponseData<Integer>> response) {
                            ResponseData<Integer> responseData = response.body();
                            if (responseData.getResult() == 0) {
                                OneBtnDialog dialog = new OneBtnDialog(view.getContext(), "삭제 성공"
                                        , "플래닛 삭제에 성공하였습니다", "안녕...!");
                                dialog.show();
                                dialog.setBtnOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //이전 액티비티 포함해서 제거
                                        GroupActivity.GROUP_ACTIVIRY.finish();
                                        GroupManageActivity.GROUP_MANAGE_ACTIVITY.finish();
                                        finish();
                                    }
                                });
                            } else if (responseData.getResult() == 4) {
                                OneBtnDialog dialog = new OneBtnDialog(view.getContext(), "삭제 실패"
                                        , "권한이 없습니다. 플래닛의 관리자가 아닙니다.");
                                dialog.show();
                            } else if (responseData.getResult() == 10) {
                                OneBtnDialog dialog = new OneBtnDialog(view.getContext(), "삭제 실패"
                                        , "아직 플래닛에 다른 멤버가 남아있습니다! 모든 멤버를 추방하고 다시 시도해주세요.");
                                dialog.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseData<Integer>> call, Throwable t) {
                            OneBtnDialog dialog = new OneBtnDialog(view.getContext(), "삭제 실패"
                                    , "알 수 없는 오류가 발생했습니다.");
                            dialog.show();
                        }
                    });
                }

            }
        });

        requestGroupInfo();
    }

    private void requestGroupInfo() {
        //그룹 정보 가져오기
        retrofitAPI.getGroupInformation(groupID).enqueue(new Callback<ResponseData<GroupData>>() {
            @Override
            public void onResponse(Call<ResponseData<GroupData>> call, Response<ResponseData<GroupData>> response) {
                ResponseData<GroupData> responseData = response.body();
                if(responseData.getResult() == 0) {
                    GroupData data = responseData.getData();
                    if(data.getMemberNum() > 1) {
                        btnDelete.setTag(false);
                        btnDelete.setEnabled(false);
                        btnDelete.setText("아직 남아있는 멤버가 있습니다");
                    }
                    else {
                        btnDelete.setTag(true);
                    }

                }
                else {
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseData<GroupData>> call, Throwable t) {
                finish();
            }
        });
    }
}