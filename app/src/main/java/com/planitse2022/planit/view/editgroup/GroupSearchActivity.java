package com.planitse2022.planit.view.editgroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.planitse2022.planit.Adapter.GroupAdapter;
import com.planitse2022.planit.R;
import com.planitse2022.planit.data.GroupData;
import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.data.UserData;
import com.planitse2022.planit.util.ProgressLayout;
import com.planitse2022.planit.util.dpconverter.DPConverter;
import com.planitse2022.planit.util.rdecoration.PaddingDecoration;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupSearchActivity extends AppCompatActivity {
    private RecyclerView planetList;
    private Button btnNewPlanet;
    private EditText edtSearch;
    private ImageView imgBack;
    private RetrofitAPI retrofitAPI;
    private GroupAdapter groupAdapter;

    private ProgressLayout progressLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_search);

        progressLayout = new ProgressLayout(getWindow().getDecorView());
        progressLayout.setBackgroundColor(R.color.pi_back);
        UserData userData = UserData.getInstance();

        planetList = findViewById(R.id.list_planet);
        btnNewPlanet = findViewById(R.id.btn_new_planet);
        edtSearch = findViewById(R.id.edt_search);
        imgBack = findViewById(R.id.img_back);
        //내 플래닛 리스트 채우기
        groupAdapter = new GroupAdapter(1);

        planetList.setLayoutManager(new LinearLayoutManager(this));
        planetList.setHasFixedSize(true);
        planetList.addItemDecoration(new PaddingDecoration(DPConverter.dpToInt(this,10), PaddingDecoration.BOTTOM));
        planetList.setAdapter(groupAdapter);

        retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, userData.getToken());
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int i, KeyEvent keyEvent) {
                requestGroup(view.getText().toString(), -1);
                return false;
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnNewPlanet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(GroupSearchActivity.this, GroupEditActivity.class);
                startActivity(in);
            }
        });

        requestGroup("", 10);
    }
    private void requestGroup(String strSearch, int maxnum) {
        progressLayout.addLoading();
        retrofitAPI.getGroupList(strSearch, maxnum).enqueue(new Callback<ResponseData<List<GroupData>>>() {
            @Override
            public void onResponse(Call<ResponseData<List<GroupData>>> call, Response<ResponseData<List<GroupData>>> response) {
                ResponseData<List<GroupData>> responseData = response.body();
                if (responseData.getResult() == 0) {
                    groupAdapter.clear();
                    groupAdapter.addItem(responseData.getData());
                    groupAdapter.notifyDataSetChanged();
                }
                progressLayout.doneLoading();
            }

            @Override
            public void onFailure(Call<ResponseData<List<GroupData>>> call, Throwable t) {
                progressLayout.doneLoading();
            }
        });
    }
}