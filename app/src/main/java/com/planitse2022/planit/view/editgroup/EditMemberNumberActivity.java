package com.planitse2022.planit.view.editgroup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.planitse2022.planit.R;
import com.planitse2022.planit.data.GroupData;
import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.data.UserData;
import com.planitse2022.planit.util.dialog.OneBtnDialog;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.ServiceGenerator;
import com.planitse2022.planit.util.toolbar.ToolbarSubpage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditMemberNumberActivity extends AppCompatActivity {
    private EditText edtMaxNum;
    private TextView tvMaxNum;
    private UserData userData;
    private ToolbarSubpage toolbar;
    private RetrofitAPI retrofitAPI;
    private Button btnCancel, btnComplete;
    private int groupID, groupNum, score, inputNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_member_number);

        toolbar = new ToolbarSubpage(this);
        toolbar.setTitle("최대 멤버 수 설정");

        Intent intent = getIntent();
        groupID = intent.getIntExtra("groupID", -1);
        userData = UserData.getInstance();
        retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, userData.getToken());

        edtMaxNum = findViewById(R.id.edt_max_membernum);
        tvMaxNum = findViewById(R.id.tv_max_membernum);
        btnCancel = findViewById(R.id.btn_cancel);
        btnComplete = findViewById(R.id.btn_complete);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    inputNum = Integer.parseInt(edtMaxNum.getText().toString());
                }catch (Exception e) {
                    inputNum = 0;
                }
                if(isUpdatableNum(inputNum)) {
                    retrofitAPI.updateGroupMaxNumber(inputNum, groupID).enqueue(new Callback<ResponseData<Integer>>() {
                        @Override
                        public void onResponse(Call<ResponseData<Integer>> call, Response<ResponseData<Integer>> response) {
                            ResponseData<Integer> responseData = response.body();
                            if(responseData.getResult() == 0) {
                                OneBtnDialog dialog = new OneBtnDialog(EditMemberNumberActivity.this, "설정 성공"
                                        , "최대 멤버 수를 " + inputNum + "명으로 설정하였습니다!");
                                dialog.show();
                            }
                            else if(responseData.getResult() == 4){
                                OneBtnDialog dialog = new OneBtnDialog(view.getContext(),"설정 실패"
                                        , "권한이 없습니다. 플래닛의 관리자가 아닙니다.");
                                dialog.show();
                            }
                            else if(responseData.getResult() == 10) {
                                OneBtnDialog dialog = new OneBtnDialog(EditMemberNumberActivity.this, "설정 실패"
                                        , "현재 플래닛 멤버 수보다 최대 멤버 수를 작게 설정할 수 없습니다!\n현재 멤버 수: " + inputNum);
                                dialog.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseData<Integer>> call, Throwable t) {
                            OneBtnDialog dialog = new OneBtnDialog(EditMemberNumberActivity.this, "오류", "알 수 없는 오류가 발생했습니다.");
                            dialog.show();
                        }
                    });
                }
                else {
                    OneBtnDialog dialog;
                    if(inputNum < groupNum) {
                        dialog = new OneBtnDialog(EditMemberNumberActivity.this, "설정 실패"
                                , "현재 플래닛 멤버 수보다 최대 멤버 수를 작게 설정할 수 없습니다!\n현재 멤버 수: " + groupNum);
                    }
                    else {
                        dialog = new OneBtnDialog(EditMemberNumberActivity.this, "설정 실패"
                                , "최대 설정 가능한 멤버 수를 초과했습니다!");
                    }
                    dialog.show();
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
                    groupNum = data.getMemberNum();
                    score = data.getGroupScore();

                    tvMaxNum.setText("설정 가능한 수: " + groupNum + " ~ " + getMaxNum(score));
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

    private boolean isUpdatableNum(int num) {
        int maxNum = getMaxNum(num);

        if(num >= groupNum && num <= maxNum) {
            return true;
        }
        return false;
    }

    private int getMaxNum(int num) {
        int maxNum = 0;
        if(score >= 1000000) {
            maxNum = 300;
        }
        else if(score >= 500000) {
            maxNum = 200;
        }
        else if(score >= 100000) {
            maxNum = 150;
        }
        else if(score >= 50000) {
            maxNum = 100;
        }
        else if(score >= 10000) {
            maxNum = 80;
        }
        else if(score >= 5000) {
            maxNum = 50;
        }
        else if(score >= 1000) {
            maxNum = 30;
        }
        else if(score >= 500) {
            maxNum = 20;
        }
        else {
            maxNum = 15;
        }
        return maxNum;
    }
}