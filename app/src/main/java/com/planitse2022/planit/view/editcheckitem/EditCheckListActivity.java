package com.planitse2022.planit.view.editcheckitem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.planitse2022.planit.Adapter.EditChecklistAdapter;
import com.planitse2022.planit.R;
import com.planitse2022.planit.data.CheckItemData;
import com.planitse2022.planit.data.ChecklistData;
import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.data.UserData;
import com.planitse2022.planit.util.ProgressLayout;
import com.planitse2022.planit.util.plant.PlantTypeGenerator;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.ServiceGenerator;
import com.planitse2022.planit.util.toolbar.ToolbarSubpage;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCheckListActivity extends AppCompatActivity {
    private TextView tvGoalName;
    private ListView listChecklist;
    private Button btnAddCheckItem, btnComplete;
    private ImageView imgEditGoalName;
    private EditText edtGoalName;
    private int groupID, plantID, plantGrowth;
    private String groupName;
    private RetrofitAPI retrofitAPI;
    private boolean isLoaded;
    private EditChecklistAdapter checklistAdapter;
    private ViewSwitcher editSwitcher;

    private ProgressLayout progressLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_check_list);

        ToolbarSubpage toolbar = new ToolbarSubpage(this);
        toolbar.setTitle("목표 편집");

        Intent intent = getIntent();
        groupID = intent.getIntExtra("groupID", -1);
        groupName = intent.getStringExtra("groupName");
        if (groupID == -1) {
            Log.e("Error", "그룹 아이디가 오지 않음.");
            finish();
        }
        plantID = intent.getIntExtra("plantID", -1);
        plantGrowth = intent.getIntExtra("plantGrowth", -1);

        progressLayout = new ProgressLayout(getWindow().getDecorView());
        UserData userData = UserData.getInstance();

        tvGoalName = findViewById(R.id.tv_goalname);
        listChecklist = findViewById(R.id.list_checklist);
        btnComplete = findViewById(R.id.btn_complete);
        btnAddCheckItem = findViewById(R.id.btn_add_checkitem);
        imgEditGoalName = findViewById(R.id.img_goalname_edit);
        edtGoalName = findViewById(R.id.edt_goalname_edit);
        editSwitcher = findViewById(R.id.switch_edit);

        retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, userData.getToken());
        //유저의 그룹 내 정보 가져오기
        checklistAdapter = new EditChecklistAdapter();
        listChecklist.setAdapter(checklistAdapter);
        requestCheckList();

        btnAddCheckItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(view.getContext(), WriteCheckItemActivity.class);
                //체크리스트 정보 파라미터로 보내기
                in.putExtra("isEdit", false);
                in.putExtra("groupName", groupName);
                in.putExtra("goalName", tvGoalName.getText().toString());
                in.putExtra("groupID", groupID);
                startActivity(in);
            }
        });

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCheckList();
                requestUpdatePlantType();
                finish();
            }
        });

        imgEditGoalName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (R.id.edt_goalname_edit == editSwitcher.getNextView().getId()) {
                    edtGoalName.setText(tvGoalName.getText().toString());
                    editSwitcher.showNext();
                }
            }
        });

        edtGoalName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int i, KeyEvent keyEvent) {
                //TODO: 목표명 변경 요청
                retrofitAPI.updateGoalName(groupID, view.getText().toString()).enqueue(new Callback<ResponseData<Integer>>() {
                    @Override
                    public void onResponse(Call<ResponseData<Integer>> call, Response<ResponseData<Integer>> response) {
                        ResponseData<Integer> responseData = response.body();
                        if(responseData.getResult() == 0) {
                            tvGoalName.setText(view.getText().toString());
                            if (R.id.lay_goalname_edit == editSwitcher.getNextView().getId()) {
                                editSwitcher.showNext();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseData<Integer>> call, Throwable t) {

                    }
                });
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 편집 후 돌아왔을 때 체크리스트 업데이트
        requestCheckList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        requestCheckList();
        requestUpdatePlantType();
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
                    checklistAdapter.clear();
                    checklistAdapter.addItem(data.getCheckItemList());
                    checklistAdapter.setGroupName(groupName);
                    checklistAdapter.setGoalName(data.getUserGoal());
                    checklistAdapter.notifyDataSetChanged();
                }
                else if(responseData.getResult() == 3) {
                    //체크리스트 비어있는 경우
                    // TODO: 체크리스트 추가 권유
                    ChecklistData data = responseData.getData();
                    tvGoalName.setText(data.getUserGoal());
                    checklistAdapter.clear();
                    checklistAdapter.notifyDataSetChanged();
                }

                progressLayout.doneLoading();
            }

            @Override
            public void onFailure(Call<ResponseData<ChecklistData>> call, Throwable t) {
                progressLayout.doneLoading();
            }
        });
    }

    private void requestUpdatePlantType() {
        if(plantGrowth <= 500) {
            String stringCheck = "";
            stringCheck += tvGoalName.getText().toString();
            for(CheckItemData item: checklistAdapter.getList()) {
                stringCheck += item.getTitle();
            }
            PlantTypeGenerator typeGenerator = new PlantTypeGenerator();
            try {
                typeGenerator.readKeywordJson(getAssets().open("json/keyword.json"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            long seed = 3; // 시드 값
            Integer plantTypeNum = typeGenerator.determintPlantType(stringCheck, seed); // 식물 타입 숫자 반환
            retrofitAPI.updatePlantType(plantID, plantTypeNum).enqueue(new Callback<ResponseData<Integer>>() {
                @Override
                public void onResponse(Call<ResponseData<Integer>> call, Response<ResponseData<Integer>> response) {

                }

                @Override
                public void onFailure(Call<ResponseData<Integer>> call, Throwable t) {

                }
            });
        }
    }
}