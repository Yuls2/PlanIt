package com.planitse2022.planit.util.dialog;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.planitse2022.planit.R;
import com.planitse2022.planit.data.GroupData;
import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.data.UserData;
import com.planitse2022.planit.util.plant.PlantWindow;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.ServiceGenerator;
import com.planitse2022.planit.view.group.GroupActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupDialog extends PlanitDialog{
    TextView tvTitle, tvContent, tvRule, tvGroupScore, tvMemberNum;
    Button btnBtnClose, btnBtnJoin;
    GroupData groupData;
    UserData userData;
    PlantWindow plantWindow;

    public GroupDialog(Context context, GroupData groupData) {
        super(context);
        this.groupData = groupData;
    }

    @Override
    public void show() {
        dialogInit();
        dialog.setContentView(R.layout.lay_dialog_group);

        userData = UserData.getInstance();

        tvTitle = dialog.findViewById(R.id.dialog_tv_title);
        tvContent = dialog.findViewById(R.id.dialog_tv_content);
        tvRule = dialog.findViewById(R.id.dialog_tv_group_rule);
        tvGroupScore = dialog.findViewById(R.id.dialog_tv_score);
        tvMemberNum = dialog.findViewById(R.id.dialog_tv_membernum);
        plantWindow = new PlantWindow(dialog.findViewById(R.id.lay_plantwindow));


        btnBtnClose = dialog.findViewById(R.id.dialog_btn_close);
        btnBtnJoin = dialog.findViewById(R.id.dialog_btn_join);

        tvTitle.setText(groupData.getGroupName());
        tvContent.setText(groupData.getGroupComment());
//        tvRule.setText(groupData.getGroupRule());
        if(groupData.getGroupRule() == null) {
            tvRule.setText("아직 등록된 규칙이 없습니다.");
        }else {
            tvRule.setText(groupData.getGroupRule());
        }
        if(!groupData.isJoined()) {
            if(groupData.isAutoAccept()) {
                btnBtnJoin.setText("가입하기");
            }
        }
        else {
            btnBtnJoin.setText("이미 가입했습니다");
            btnBtnJoin.setEnabled(false);
        }

        tvGroupScore.setText(""+groupData.getGroupScore());
        tvMemberNum.setText(groupData.getMemberNum() + "/" + groupData.getMaxMemberNum());


        //배경 이미지 가져오기기
        plantWindow.setBackground(groupData.getBackground());
        plantWindow.setList(groupData.getTopMemberPlant());

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
                if(groupData.isAutoAccept()) { //자동 가입인 경우
                    RetrofitAPI retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, userData.getToken());
                    retrofitAPI.insertGroupRequest(groupData.getGroupID()).enqueue(new Callback<ResponseData<Integer>>() {
                        @Override
                        public void onResponse(Call<ResponseData<Integer>> call, Response<ResponseData<Integer>> response) {
                            ResponseData<Integer> responseData = response.body();
                            if(responseData.getResult() == 0) {
                                if(responseData.getData() == 1) {
                                    //실제로 자동 가입이었던 경우, 성공한 경우
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
                                            dialog.dissmissDailog();
                                        }
                                    });
                                }
                                else if(responseData.getData() == 0) {
                                    //유저가 검색한 사이 자동 가입이 아니게 된 경우
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
                }
                else { // 자동 가입이 아닌 경우
                    GroupRequestDialog dialog = new GroupRequestDialog(view.getContext(), groupData);
                    dialog.show();
                }
                dissmissDailog();
            }
        });

        dialog.show();
    }
}
