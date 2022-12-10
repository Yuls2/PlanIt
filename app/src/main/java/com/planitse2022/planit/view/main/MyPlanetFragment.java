package com.planitse2022.planit.view.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.planitse2022.planit.Adapter.GroupAdapter;
import com.planitse2022.planit.R;
import com.planitse2022.planit.data.GroupData;
import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.data.UserData;
import com.planitse2022.planit.util.ProgressLayout;
import com.planitse2022.planit.util.dialog.OneBtnDialog;
import com.planitse2022.planit.util.dpconverter.DPConverter;
import com.planitse2022.planit.util.rdecoration.PaddingDecoration;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.ServiceGenerator;
import com.planitse2022.planit.view.editgroup.GroupEditActivity;
import com.planitse2022.planit.view.editgroup.GroupSearchActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyPlanetFragment extends Fragment {
    private Context context;
    private RecyclerView myPlanetList;
    private Button btnNewPlanet, btnSearchPlanet;
    private RetrofitAPI retrofitAPI;
    private GroupAdapter groupAdapter;

    private ProgressLayout progressLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_planet, container, false);

        UserData userData = UserData.getInstance();
        progressLayout = new ProgressLayout(view);

        myPlanetList = view.findViewById(R.id.list_myplanet);
        btnNewPlanet = view.findViewById(R.id.btn_new_planet);
        btnSearchPlanet = view.findViewById(R.id.btn_search_planet);
        //내 플래닛 리스트 채우기
        groupAdapter = new GroupAdapter(0);
        
        myPlanetList.setLayoutManager(new LinearLayoutManager(context));
        myPlanetList.setHasFixedSize(true);
        myPlanetList.addItemDecoration(new PaddingDecoration(DPConverter.dpToInt(context,10), PaddingDecoration.BOTTOM));
        myPlanetList.setAdapter(groupAdapter);

        retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, userData.getToken());
        requestGroup();

        //새로 만들기 버튼 클릭 시
        btnNewPlanet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 그룹 생성 페이지로 이동
                Intent in = new Intent(getActivity(), GroupEditActivity.class);
                startActivity(in);
            }
        });
        //검색하기 버튼 클릭 시
        btnSearchPlanet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), GroupSearchActivity.class);
                startActivity(in);
            }
        });
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
        requestGroup();
    }

    private void requestGroup() {
        progressLayout.addLoading();
        retrofitAPI.getMyGroupList().enqueue(new Callback<ResponseData<List<GroupData>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<GroupData>>> call, Response<ResponseData<List<GroupData>>> response) {
                Log.e("ee", response.toString());
                ResponseData<List<GroupData>> data = response.body();
                groupAdapter.clear();
                groupAdapter.addItem(data.getData());
                groupAdapter.notifyDataSetChanged();
                progressLayout.doneLoading();
            }

            @Override
            public void onFailure(Call<ResponseData<List<GroupData>>> call, Throwable t) {
                progressLayout.doneLoading();
                OneBtnDialog dialog = new OneBtnDialog(context, "오류", "알 수 없는 오류가 발생했습니다.");
                dialog.show();
            }
        });
    }
}