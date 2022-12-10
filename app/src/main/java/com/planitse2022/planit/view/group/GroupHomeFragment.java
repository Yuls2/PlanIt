package com.planitse2022.planit.view.group;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.planitse2022.planit.Adapter.ChecklistAdapter;
import com.planitse2022.planit.R;
import com.planitse2022.planit.data.CheckItemData;
import com.planitse2022.planit.data.ChecklistData;
import com.planitse2022.planit.data.GroupData;
import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.data.UserData;
import com.planitse2022.planit.util.ProgressLayout;
import com.planitse2022.planit.util.dialog.OneBtnDialog;
import com.planitse2022.planit.util.dialog.TwoBtnDialog;
import com.planitse2022.planit.util.listviewUtil.ListviewHeightSetter;
import com.planitse2022.planit.util.plant.PlantWindow;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.ServiceGenerator;
import com.planitse2022.planit.view.editgroup.GroupManageActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupHomeFragment extends Fragment {
    private Context context;
    private TextView tvGroupName, tvGroupComment, tvGoalName, tvGroupScore, tvMemberNum;
    private ListView listChecklist;
    private Button btnMoreDetail, btnManageGroup;
    private int groupID;
    private RetrofitAPI retrofitAPI;
    private ChecklistAdapter checklistAdapter;
    private PlantWindow plantWindow;

    private ProgressLayout progressLayout;
    private UserData userData;
    private View.OnClickListener listenerForManage, listenerForLeave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_home, container, false);

        groupID = getArguments().getInt("groupID");
        progressLayout = new ProgressLayout(view);
        userData = UserData.getInstance();

        tvGroupName = view.findViewById(R.id.tv_groupname);
        tvGroupComment = view.findViewById(R.id.tv_comment);
        tvGroupScore = view.findViewById(R.id.tv_score);
        tvMemberNum = view.findViewById(R.id.tv_membernum);
        tvGoalName = view.findViewById(R.id.tv_goalname);
        listChecklist = view.findViewById(R.id.list_checklist);
        btnMoreDetail = view.findViewById(R.id.btn_moredetail);
        btnManageGroup = view.findViewById(R.id.btn_manage_group);
        plantWindow = new PlantWindow(view.findViewById(R.id.lay_plantwindow));

        listenerForManage = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(view.getContext(), GroupManageActivity.class);
                in.putExtra("groupID", groupID);
                startActivity(in);
            }
        };

        listenerForLeave = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwoBtnDialog dialog = new TwoBtnDialog(view.getContext(), "정말로 플래닛을 떠나시겠습니까?", "다시 들어오려면 다시 가입을 신청해야 합니다!", "떠나기 싫어요!", "안녕!");
                dialog.show();
                dialog.setBtnOnClickListener(2, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.updateText("식물 보관", "기르던 식물은 보관할까요? 보관할 경우 다른 그룹에서 이어서 기를 수 있습니다!", "미련 없어요", "보관할래요!");
                        dialog.setBtnOnClickListener(1, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                requestLeaveGroup(1);
                                dialog.dissmissDailog();
                            }
                        });
                        dialog.setBtnOnClickListener(2, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                requestLeaveGroup(0);
                                dialog.dissmissDailog();
                            }
                        });
                    }
                });
            }
        };

        btnMoreDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(view.getContext(), GroupDetailActivity.class);
                in.putExtra("groupID", groupID);
                startActivity(in);
            }
        });


        retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, userData.getToken());
        //유저의 그룹 내 정보 가져오기
        checklistAdapter = new ChecklistAdapter();
        listChecklist.setAdapter(checklistAdapter);
        requestCheckList();
        //그룹 정보 가져오기
        requestGroupInfo();

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestCheckList();
        requestGroupInfo();
    }

    private void requestCheckList() {
        progressLayout.addLoading();
        retrofitAPI.getUserGroupCheckList(groupID).enqueue(new Callback<ResponseData<ChecklistData>>() {
            @Override
            public void onResponse(Call<ResponseData<ChecklistData>> call, Response<ResponseData<ChecklistData>> response) {
                ResponseData<ChecklistData> responseData = response.body();
                if(responseData.getResult() == 0) {
                    ChecklistData data = responseData.getData();
                    tvGoalName.setText(data.getUserGoal());
                    //체크리스트 채우기
                    //오늘 날짜에 해당하는 체크리스트만 골라 담기
                    ArrayList<CheckItemData> checkItemList = new ArrayList<CheckItemData>();
                    for(CheckItemData item: data.getCheckItemList()) {
                        if(item.isToday()) {
                            checkItemList.add(item);
                        }
                    }
                    checklistAdapter.clear();
                    checklistAdapter.addItem(checkItemList);
                    checklistAdapter.notifyDataSetChanged();
                    ListviewHeightSetter.setListHeight(listChecklist, checklistAdapter);
                }
                if(responseData.getResult() == 3) {
                    //체크리스트 비어있는 경우
                    // TODO: 체크리스트 추가 권유
                    ChecklistData data = responseData.getData();
                    tvGoalName.setText(data.getUserGoal());
                }
                progressLayout.doneLoading();
            }

            @Override
            public void onFailure(Call<ResponseData<ChecklistData>> call, Throwable t) {
                progressLayout.doneLoading();
            }
        });
    }

    private void requestGroupInfo() {
        progressLayout.addLoading();
        //그룹 정보 가져오기
        retrofitAPI.getGroupInformation(groupID).enqueue(new Callback<ResponseData<GroupData>>() {
            @Override
            public void onResponse(Call<ResponseData<GroupData>> call, Response<ResponseData<GroupData>> response) {
                ResponseData<GroupData> responseData = response.body();
                if(responseData.getResult() == 0) {
                    GroupData data = responseData.getData();

                    //텍스트뷰 텍스트 지정
                    tvGroupName.setText(data.getGroupName());
                    tvGroupComment.setText(data.getGroupComment());
                    tvGroupScore.setText("" + data.getGroupScore());
                    tvMemberNum.setText(data.getMemberNum() + "/" + data.getMaxMemberNum());
                    //배경 이미지 가져오기기
                    plantWindow.setBackground(data.getBackground());
                    plantWindow.setList(data.getTopMemberPlant());
                    //매니저인 경우 관리 버튼 나오게
                    if(userData.isManager(groupID)) {
//                        btnManageGroup.setVisibility(View.VISIBLE);
                        btnManageGroup.setOnClickListener(listenerForManage);
                    }
                    else {
                        btnManageGroup.setText("플래닛 떠나기");
                        btnManageGroup.setOnClickListener(listenerForLeave);
                    }

                    progressLayout.doneLoading();
                }
            }

            @Override
            public void onFailure(Call<ResponseData<GroupData>> call, Throwable t) {
                progressLayout.doneLoading();
            }
        });
    }

    private void requestLeaveGroup(int plantDelete) {
        retrofitAPI.leaveGroup(groupID, plantDelete).enqueue(new Callback<ResponseData<Integer>>() {
            @Override
            public void onResponse(Call<ResponseData<Integer>> call, Response<ResponseData<Integer>> response) {
                ResponseData<Integer> responseData = response.body();
                if(responseData.getResult() == 0) {
                    OneBtnDialog dialog = new OneBtnDialog(context,"떠나기 성공", "성공적으로 플래닛을 떠났습니다!", "안녕!");
                    dialog.show();
                    dialog.setBtnOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ((Activity)context).finish();
                            dialog.dissmissDailog();
                        }
                    });
                }
                else {
                    OneBtnDialog dialog = new OneBtnDialog(context,"떠나기 실패", "오류가 발생했습니다.");
                    dialog.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData<Integer>> call, Throwable t) {
                OneBtnDialog dialog = new OneBtnDialog(context,"떠나기 실패", "알 수 없는 오류가 발생했습니다.");
                dialog.show();
            }
        });
    }
}