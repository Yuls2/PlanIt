package com.planitse2022.planit.util.dialog;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.planitse2022.planit.R;
import com.planitse2022.planit.data.PlantData;
import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.data.UserData;
import com.planitse2022.planit.util.plant.PlantView;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.ServiceGenerator;
import com.planitse2022.planit.view.plantkeeper.SelectPlantFromKeeperActivity;
import com.google.gson.Gson;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPlantDialog extends PlanitDialog{
    int mode; // 0: 새로운 식물, 1: 보관함 식물
    Button btnBtnOk, btnBtnSelect, btnBtnCancel;
    TextView tvContent;
    EditText edtPlantName;
    PlantView plantView;
    ViewSwitcher viewSwitcher;
    PlantData newPlant;
    UserData userData;
    int groupID;
    private ActivityResultLauncher<Intent> resultLauncher;

    public NewPlantDialog(Context context, int groupID) {
        super(context);
        this.groupID = groupID;
        newPlant = new PlantData();
        mode = 0;

        resultLauncher = ((AppCompatActivity)context).registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == RESULT_OK){
                            Intent intent = result.getData();
                            PlantData selectedPlant =
                                    (new Gson()).fromJson(intent.getStringExtra("selectedPlant")
                                            , PlantData.class);
                            plantView.setData(selectedPlant);
                            switchMode(1);
                        }
                    }
                });
    }
    @Override
    public void show() {
        dialogInit();
        dialog.setContentView(R.layout.lay_dialog_newplant);

        dialog.setCancelable(false);

        userData = UserData.getInstance();
        btnBtnOk = dialog.findViewById(R.id.dialog_btn_ok);
        btnBtnSelect = dialog.findViewById(R.id.dialog_btn_select);
        btnBtnCancel = dialog.findViewById(R.id.dialog_btn_cancel);
        tvContent = dialog.findViewById(R.id.dialog_tv_content);
        edtPlantName = dialog.findViewById(R.id.dialog_edt_plantname);
        viewSwitcher = dialog.findViewById(R.id.dialog_switch_plant);
        plantView = new PlantView(dialog.findViewById(R.id.dialog_plant_lay));

        plantView.setData(newPlant);

        btnBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:식물 추가(보관함이냐, 아니냐에 따라)
                RetrofitAPI retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, userData.getToken());
                HashMap<String, Object> param = new HashMap<>();
                param.put("mode", mode);
                param.put("groupID", groupID);
                if(mode == 0) { // 새로 생성하는 경우의 파라미터
                    param.put("plantName", edtPlantName.getText().toString());
                }
                else { // 보관함에서 가져오는 경우의 파라미터
                    param.put("plantID", plantView.getData().getPlantID());
                }
                retrofitAPI.insertPlant(param).enqueue(new Callback<ResponseData<Integer>>() {
                    @Override
                    public void onResponse(Call<ResponseData<Integer>> call, Response<ResponseData<Integer>> response) {
                        ResponseData<Integer> responseData = response.body();
                        if(responseData.getResult() == 0) {
                            AnimationDialog animationDialog = new AnimationDialog(context,R.drawable.img_ani_plant,
                                    "식물 심기 성공!", "플래닛에 식물을 심었습니다.\n열정을 담아 길러주세요!", "야호!");
                            animationDialog.show();
                        }
                        dissmissDailog();
                    }

                    @Override
                    public void onFailure(Call<ResponseData<Integer>> call, Throwable t) {
                        OneBtnDialog dialog = new OneBtnDialog(context, "식물 심기 실패", "알 수 없는 오류가 발생하였습니다.");
                        dialog.show();
                    }
                });
            }
        });
        btnBtnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:보관함 액티비티로 이동하여 선택된 결과 가져오기

                Intent intent = new Intent(context, SelectPlantFromKeeperActivity.class);
                resultLauncher.launch(intent);
            }
        });
        btnBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchMode(0);
            }
        });

        dialog.show();
    }

    private void switchMode(int mode) {
        this.mode = mode;
        String strContent = "앗! 아직 이 플래닛에 심은 식물이 없군요!\n";
        if (mode == 0) {
            strContent += "새로 심을 식물의 이름을 입력하세요!";
            //식물 초기화
            plantView.setData(newPlant);
            
            if (R.id.dialog_edt_plantname == viewSwitcher.getNextView().getId()) {
                viewSwitcher.showNext();
            }
        } else {
            strContent += plantView.getData().getName() + " 식물을 심으실 건가요?";
            if (R.id.dialog_btn_cancel == viewSwitcher.getNextView().getId()) {
                viewSwitcher.showNext();
            }
        }
        tvContent.setText(strContent);
    }
}
