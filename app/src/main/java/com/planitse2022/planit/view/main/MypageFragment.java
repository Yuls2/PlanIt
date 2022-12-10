package com.planitse2022.planit.view.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.planitse2022.planit.LoginActivity;
import com.planitse2022.planit.R;
import com.planitse2022.planit.data.UserData;
import com.planitse2022.planit.util.AutoLoginManager;
import com.planitse2022.planit.util.dialog.TwoBtnDialog;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.ServiceGenerator;
import com.planitse2022.planit.view.plantkeeper.PlantKeeperActivity;

public class MypageFragment extends Fragment {
    private Context context;
    private UserData userData;
    private RetrofitAPI retrofitAPI;
    private TextView tvUserName;
    private Button btnLogout, btnPlantKeeper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);

        userData = UserData.getInstance();

        btnLogout = view.findViewById(R.id.btn_logout);
        btnPlantKeeper = view.findViewById(R.id.btn_plant_keeper);
        tvUserName = view.findViewById(R.id.tv_username);

        btnPlantKeeper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, PlantKeeperActivity.class);
                startActivity(in);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwoBtnDialog dialog = new TwoBtnDialog(context,"로그아웃 할까요?", "정말 로그아웃 하시겠습니까?", "아니요", "네");
                dialog.show();
                dialog.setBtnOnClickListener(2, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        userData.setToken("");
                        userData.setUserID("");
                        userData.setUserNickName("");
                        userData.managerInfoClear();
                        AutoLoginManager.clearPreferences(context);


                        Intent in = new Intent(context, LoginActivity.class);
                        startActivity(in);
                        ((Activity)context).finish();

                        dialog.dissmissDailog();
                    }
                });
            }
        });
        tvUserName.setText(userData.getUserNickName());

        retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, userData.getToken());

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
    }

}