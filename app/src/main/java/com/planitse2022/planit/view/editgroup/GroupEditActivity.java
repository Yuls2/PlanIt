package com.planitse2022.planit.view.editgroup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.planitse2022.planit.R;
import com.planitse2022.planit.data.GroupData;
import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.data.UserData;
import com.planitse2022.planit.util.dialog.AnimationDialog;
import com.planitse2022.planit.util.dialog.OneBtnDialog;
import com.planitse2022.planit.util.dialog.TwoBtnDialog;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.ServiceGenerator;
import com.planitse2022.planit.util.toolbar.ToolbarSubpage;
import com.planitse2022.planit.view.group.GroupActivity;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupEditActivity extends AppCompatActivity {
    private boolean isEdit;
    private int groupID;
    private EditText edtName, edtComment, edtRule;
    private CheckBox chAutoAccept;
    private TextView tvAutoAccept;
    private RetrofitAPI retrofitAPI;
    private Button btnCancel, btnUpload;

    private UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_edit);

        userData = UserData.getInstance();
        ToolbarSubpage toolbar = new ToolbarSubpage(this);
        toolbar.setTitle("플래닛 생성");

        retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, userData.getToken());

        edtName = findViewById(R.id.edt_name);
        edtComment = findViewById(R.id.edt_comment);
        edtRule = findViewById(R.id.edt_rule);
        chAutoAccept = findViewById(R.id.ch_auto_accept);
        tvAutoAccept = findViewById(R.id.tv_auto_accept);
        btnCancel = findViewById(R.id.btn_cancel);
        btnUpload = findViewById(R.id.btn_upload);

        Intent intent = getIntent();
        isEdit = intent.getBooleanExtra("isEdit", false);
        groupID = intent.getIntExtra("groupID", -1);
        if(isEdit) {
            toolbar.setTitle("플래닛 정보 수정");
            requestGroupInfo();
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwoBtnDialog dialog = new TwoBtnDialog(view.getContext(), "생성을 취소할까요?", "작성중이던 내용은 저장되지 않습니다!", "이어서 쓸래요", "상관없어요");
                dialog.show();
                dialog.setBtnOnClickListener(2, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        dialog.dissmissDailog();
                    }
                });
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int check = isAvailable();
                if(check == 0) {
                    HashMap<String, Object> param = new HashMap<>();
                    param.put("mode", isEdit ? 1 : 0);
                    param.put("groupName", edtName.getText().toString());
                    param.put("groupComment", edtComment.getText().toString());
                    param.put("groupRule", edtRule.getText().toString());
                    param.put("autoAccept", chAutoAccept.isChecked() ? 1 : 0);
                    if (isEdit) {
                        param.put("groupID", groupID);
                    }
                    retrofitAPI.insertGroup(param).enqueue(new Callback<ResponseData<Integer>>() {
                        @Override
                        public void onResponse(Call<ResponseData<Integer>> call, Response<ResponseData<Integer>> response) {
                            ResponseData<Integer> responseData = response.body();
                            if (responseData.getResult() == 0) {
                                if (isEdit) {
                                    OneBtnDialog dialog = new OneBtnDialog(GroupEditActivity.this, "수정 완료", "플래닛 정보를 수정했습니다!");
                                    dialog.show();
                                } else {
                                    AnimationDialog dialog = new AnimationDialog(GroupEditActivity.this, R.drawable.img_ani_new_planet
                                            , "생성 성공", "플래닛 생성을 성공했습니다!", "야호!");
                                    dialog.show();
                                    dialog.setBtnOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent in = new Intent(view.getContext(), GroupActivity.class);
                                            in.putExtra("groupName", edtName.getText().toString());
                                            in.putExtra("groupID", responseData.getData());
                                            startActivity(in);
                                            finish();
                                            dialog.dissmissDailog();
                                        }
                                    });
                                }
                            } else if (responseData.getResult() == 4) {
                                OneBtnDialog dialog = new OneBtnDialog(view.getContext(), "수정 실패"
                                        , "권한이 없습니다. 플래닛의 관리자가 아닙니다.");
                                dialog.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseData<Integer>> call, Throwable t) {
                            OneBtnDialog dialog = new OneBtnDialog(view.getContext(), "실패", "알 수 없는 오류가 발생했습니다.");
                            dialog.show();
                        }
                    });
                }
                else {
                    OneBtnDialog dialog = null;
                    if(check == 1) {
                        dialog = new OneBtnDialog(view.getContext(), "실패", "그룹의 이름은 2글자 이상 작성해주세요");
                    }
                    else if(check == 2) {
                        dialog = new OneBtnDialog(view.getContext(), "실패", "그룹의 소개는 5글자 이상 작성해주세요");
                    }
                    dialog.show();
                }
            }
        });
        chAutoAccept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                tvAutoAccept.setText(b?"네":"아니요");
            }
        });
    }
    private void requestGroupInfo() {
        //그룹 정보 가져오기
        retrofitAPI.getGroupInformation(groupID).enqueue(new Callback<ResponseData<GroupData>>() {
            @Override
            public void onResponse(Call<ResponseData<GroupData>> call, Response<ResponseData<GroupData>> response) {
                ResponseData<GroupData> responseData = response.body();
                if(responseData.getResult() == 0) {
                    GroupData data = responseData.getData();

                    //텍스트뷰 텍스트 지정
                    edtName.setText(data.getGroupName());
                    edtComment.setText(data.getGroupComment());
                    edtRule.setText(data.getGroupRule());
                    chAutoAccept.setChecked(data.isAutoAccept());
                }
                else {
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseData<GroupData>> call, Throwable t) {
            }
        });
    }
    private int isAvailable() {
        String strName, strComment;
        strName = edtName.getText().toString();
        strComment = edtComment.getText().toString();
        if(strName.length() < 2)
            return 1;
        else if (strComment.length() < 5)
            return 2;
        return 0;
    }
}