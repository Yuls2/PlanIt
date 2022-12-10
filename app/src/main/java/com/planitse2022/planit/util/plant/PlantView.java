package com.planitse2022.planit.util.plant;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.planitse2022.planit.R;
import com.planitse2022.planit.data.PlantData;
import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.data.UserData;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.RetrofitClient;
import com.planitse2022.planit.util.retrofit.ServiceGenerator;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlantView {
    private PlantData data;
    private ConstraintLayout view, plant;
    private ImageView imgPlantOne, imgPlantTwo, imgPlantSingle, imgPot;
    private RetrofitAPI retrofitAPI;

    public PlantView(ConstraintLayout view) {
        this.view = view;
        plant = view.findViewById(R.id.plant);
        imgPlantOne = view.findViewById(R.id.plant_plant_one);
        imgPlantTwo = view.findViewById(R.id.plant_plant_two);
        imgPlantSingle = view.findViewById(R.id.plant_plant_single);
        imgPot = view.findViewById(R.id.plant_pot);

        UserData userData = UserData.getInstance();
        retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, userData.getToken());
    }

    public PlantData getData() {
        return data;
    }

    public void setData(PlantData data) {
        this.data = data;

        loadPotImage();
        loadPlantImage();
    }

    public void setPlantByPlantID(int plantID) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("plantID", plantID);
        requestPlantData(param);
    }

    public void setPlantByGroupAndUserID(String userID, int groupID) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("targetUserID", userID);
        param.put("groupID", groupID);
        requestPlantData(param);
    }

    public void requestPlantData(HashMap<String, Object> param) {
        retrofitAPI.getPlantInfo(param).enqueue(new Callback<ResponseData<PlantData>>() {
            @Override
            public void onResponse(Call<ResponseData<PlantData>> call, Response<ResponseData<PlantData>> response) {
                ResponseData<PlantData> responseData = response.body();
                if(responseData.getResult() == 0) {
                    setData(responseData.getData());
                }
                else {
                    setData(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseData<PlantData>> call, Throwable t) {

            }
        });
    }

    public void setPotVisibility(boolean visible) {
        int visibility = visible?View.VISIBLE:View.GONE;
        imgPot.setVisibility(visibility);
    }

    private void loadPlantImage() {
        //아직 데이터 없는 경우
        if(data == null) {
            Glide.with(view)
                    .load(R.drawable.img_plant_none)
                    .into(imgPlantSingle);
            setSingleVisibility(true);
            return;
        }
        //데이터 있는 경우 식물 이미지 로드
        if(data.getLevel() <= 2) {
            Glide.with(view)
                    .load(RetrofitClient.getImageurl("plant",  "img_plant_0_p" + data.getLevel(), "png"))
                    .into(imgPlantSingle);
            setSingleVisibility(true);
        }
        else {
            if (data.isSingle()) {
                Glide.with(view)
                        .load(RetrofitClient.getImageurl("plant", data.getRes1Name() + "_p" + data.getLevel(), "png"))
                        .into(imgPlantSingle);
            } else {
                Glide.with(view)
                        .load(RetrofitClient.getImageurl("plant", data.getRes1Name() + "_p" + data.getLevel(), "png"))
                        .into(imgPlantOne);
                Glide.with(view)
                        .load(RetrofitClient.getImageurl("plant", data.getRes2Name() + "_p" + data.getLevel(), "png"))
                        .into(imgPlantTwo);
            }
            setSingleVisibility(data.isSingle());
        }

        float[] col = {
                (float)data.getRedTint()/255, 0.0000F, 1.0f - (float)data.getRedTint()/255, 0, 0,
                0.0000F, 1.0000F, 0.0000F, 0, 0,
                (float)data.getBlueTint()/255, 0.0000F, 1.0f - (float)data.getBlueTint()/255, 0, 0, //Red To Blue
                0.0000F, 0.0000F, 0.0000F, 1, 0
        };

        ColorMatrix colorMatrix = new ColorMatrix(col);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
        imgPlantSingle.setColorFilter(filter);
        imgPlantOne.setColorFilter(filter);
        imgPlantTwo.setColorFilter(filter);

        // 식물 애니메이션
//        imgPlantSingle.setPivotY(imgPlantSingle.getHeight());
//        imgPlantOne.setPivotY(imgPlantOne.getHeight());
//        imgPlantTwo.setPivotY(imgPlantTwo.getHeight());
//        imgPlantTwo.setPivotX(imgPlantTwo.getWidth()/2);
//        final ValueAnimator bubbleAnim = ValueAnimator.ofFloat(-0.025f, 0.025f);
//        bubbleAnim.setDuration(1000);
//        bubbleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                imgPlantOne.setScaleY(imgPlantOne.getScaleX()-(float) animation.getAnimatedValue());
//                imgPlantTwo.setScaleY(-1*imgPlantTwo.getScaleX()-(float) animation.getAnimatedValue());
//            }
//        });
//        bubbleAnim.setRepeatCount(-1);
//        bubbleAnim.setRepeatMode(ValueAnimator.REVERSE);
//        bubbleAnim.start();
    }

    private void loadPotImage() {
        //아직 데이터 없는 경우
        if(data == null) {
            Glide.with(view)
                    .load(R.drawable.img_pot_none)
                    .into(imgPot);
            return;
        }
        //데이터 있는 경우 화분 이미지 로드
        Glide.with(view)
                .load(RetrofitClient.getImageurl("pot",  "img_pot_" + data.getPot(), "png"))
                .into(imgPot);
    }

    private void setSingleVisibility(boolean visible) {
        int singleVisibility = visible?View.VISIBLE:View.GONE;
        int elseVisibility = visible?View.GONE:View.VISIBLE;
        imgPlantOne.setVisibility(elseVisibility);
        imgPlantTwo.setVisibility(elseVisibility);
        imgPlantSingle.setVisibility(singleVisibility);
    }
}
