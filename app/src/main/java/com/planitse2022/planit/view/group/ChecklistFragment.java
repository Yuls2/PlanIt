package com.planitse2022.planit.view.group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
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
import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.data.UserData;
import com.planitse2022.planit.util.ProgressLayout;
import com.planitse2022.planit.util.dialog.OneBtnDialog;
import com.planitse2022.planit.util.plant.PlantView;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.ServiceGenerator;
import com.planitse2022.planit.view.editcheckitem.EditCheckListActivity;
import com.planitse2022.planit.view.editcheckitem.PostWriteActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChecklistFragment extends Fragment {
    private Context context;
    private TextView tvGoalName;
    private ListView listChecklist;
    private Button btnUploadPost, btnEditChecklist;
    private int groupID;
    private String groupName;

    private RetrofitAPI retrofitAPI;
    private ChecklistAdapter checklistAdapter = null;
    private ProgressLayout progressLayout;
    private PlantView plantView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_checklist, container, false);

        groupID = getArguments().getInt("groupID");
        groupName = getArguments().getString("groupName");
        progressLayout = new ProgressLayout(view);
        UserData userData = UserData.getInstance();

        tvGoalName = view.findViewById(R.id.tv_goalname);
        listChecklist = view.findViewById(R.id.list_checklist);
        btnUploadPost = view.findViewById(R.id.btn_upload_post);
        btnEditChecklist = view.findViewById(R.id.btn_edit_checklist);

        retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, userData.getToken());
        //????????? ?????? ??? ?????? ????????????
        checklistAdapter = new ChecklistAdapter();
        listChecklist.setAdapter(checklistAdapter);
        requestCheckList();


        //??????????????? ?????? ?????? ??????
        btnEditChecklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), EditCheckListActivity.class);
                in.putExtra("groupID", groupID);
                in.putExtra("groupName", groupName);
                in.putExtra("plantGrowth", plantView.getData().getGrowth());
                in.putExtra("plantID", plantView.getData().getPlantID());
                startActivity(in);
            }
        });

        //????????? ?????? ??????
        btnUploadPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: POST ????????? ???????????? ??????, ??????????????? ????????? + ?????? ?????? ?????? ????????? ??????
                if(checklistAdapter != null) {
                    if(checklistAdapter.isSomeChecked()) {
                        Intent in = new Intent(context, PostWriteActivity.class);
                        in.putExtra("chJson", checklistAdapter.getCheckListString());
                        in.putExtra("groupID", groupID);
                        startActivity(in);
                        Log.d("json", checklistAdapter.getCheckListString());
                    }
                    else {
                        OneBtnDialog dialog = new OneBtnDialog(context, "???????????????...?", "?????? ????????? ????????? ?????? ??????...? ???????????? ??????????");
                        dialog.show();
                    }
                }
                else {
                    OneBtnDialog dialog = new OneBtnDialog(context, "?????? ????????????!", "?????? ?????? ????????????! ?????? ??? ?????? ??????????????????.");
                    dialog.show();
                }
            }
        });

        plantView = new PlantView(view.findViewById(R.id.plant_myplant));
        plantView.setPlantByGroupAndUserID(userData.getUserID(), groupID);

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
        UserData userData = UserData.getInstance();
        requestCheckList();
        plantView.setPlantByGroupAndUserID(userData.getUserID(), groupID);
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
                    //??????????????? ?????????
                    //?????? ????????? ???????????? ?????????????????? ?????? ??????
                    ArrayList<CheckItemData> checkItemList = new ArrayList<CheckItemData>();
                    for(CheckItemData item: data.getCheckItemList()) {
                        if(item.isToday()) {
                            checkItemList.add(item);
                        }
                    }
                    checklistAdapter.clear();
                    checklistAdapter.addItem(checkItemList);
                    checklistAdapter.notifyDataSetChanged();
                }
                if(responseData.getResult() == 3) {
                    //??????????????? ???????????? ??????
                    // TODO: ??????????????? ?????? ??????
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
}