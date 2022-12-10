package com.planitse2022.planit.util.dialog;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.planitse2022.planit.R;
import com.planitse2022.planit.data.GroupData;
import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.data.UserData;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.ServiceGenerator;
import com.planitse2022.planit.view.group.GroupActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupRequestDialog extends PlanitDialog{
    TextView tvTitle, tvContent;
    Button btnBtnClose, btnBtnJoin;
    EditText edtIntroduction;
    GroupData groupData;
    UserData userData;

    public GroupRequestDialog(Context context, GroupData groupData) {
        super(context);
        this.groupData = groupData;
    }

    @Override
    public void show() {
        dialogInit();
        dialog.setContentView(R.layout.lay_dialog_one_edt);

        userData = UserData.getInstance();

        tvTitle = dialog.findViewById(R.id.dialog_tv_title);
        tvContent = dialog.findViewById(R.id.dialog_tv_content);
        btnBtnClose = dialog.findViewById(R.id.dialog_btn_close);
        btnBtnJoin = dialog.findViewById(R.id.dialog_btn_complete);
        edtIntroduction = dialog.findViewById(R.id.dialog_edt);

        tvTitle.setText("플래닛 가입 신청");
        tvContent.setText("가입 신청 메세지를 적어주세요.\n플래닛 관리자가 참고하는 내용입니다!");
        btnBtnJoin.setText("신청하기");

        btnBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dissmissDailog();
            }
        });

        btnBtnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 가입 신청 요청
                    RetrofitAPI retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, userData.getToken());
                    retrofitAPI.insertGroupRequest(groupData.getGroupID(), edtIntroduction.getText().toString()).enqueue(new Callback<ResponseData<Integer>>() {
                        @Override
                        public void onResponse(Call<ResponseData<Integer>> call, Response<ResponseData<Integer>> response) {
                            ResponseData<Integer> responseData = response.body();
                            if(responseData.getResult() == 0) {
                                if(responseData.getData() == 1) {
                                    //자동 가입이었던 경우, 성공한 경우
                                    OneBtnDialog dialog = new OneBtnDialog(view.getContext(),"가입 성공"
                                            , groupData.getGroupName() + " 플래닛에 가입을 성공했습니다!", "야호!");
                                    dialog.show();
                                    dialog.setBtnOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            groupData.setJoined(true);
                                            Intent in = new Intent(view.getContext(), GroupActivity.class);
                                            in.putExtra("groupName", groupData.getGroupName());
                                            in.putExtra("groupID", groupData.getGroupID());
                                            view.getContext().startActivity(in);
                                            dialog.dissmissDailog();;
                                        }
                                    });
                                }
                                else if(responseData.getData() == 0) {
                                    //자동 가입이 아닌 경우
                                    OneBtnDialog dialog = new OneBtnDialog(view.getContext(),"가입 신청 성공"
                                            , groupData.getGroupName() + " 플래닛에 가입 신청 메세지를 보냈습니다!", "두근두근!");
                                    dialog.show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseData<Integer>> call, Throwable t) {
                            OneBtnDialog dialog = new OneBtnDialog(view.getContext(),"가입 신청 실패"
                                    , "알 수 없는 오류가 발생했습니다.");
                            dialog.show();
                        }
                    });
                dissmissDailog();
            }
        });

        dialog.show();
    }
}
