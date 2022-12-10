package com.planitse2022.planit.view.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.planitse2022.planit.R;
import com.planitse2022.planit.data.TestData;
import com.planitse2022.planit.util.dialog.OneBtnDialog;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.RetrofitClient;
import com.planitse2022.planit.util.toolbar.ToolbarSubpage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ToolbarSubpage toolbar = new ToolbarSubpage(this);
        toolbar.setTitle("툴바 테스트");

        Button btnTest = findViewById(R.id.btn_test);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetrofitClient client = RetrofitClient.getInstance();
                RetrofitAPI retrofitAPI = client.getRetrofitAPI();
                retrofitAPI.getSampleData(1).enqueue(new Callback<TestData>() {
                    @Override
                    public void onResponse(Call<TestData> call, Response<TestData> response) {
                        TestData data = response.body();

                        OneBtnDialog dialog = new OneBtnDialog(TestActivity.this, "테스트", "테스트용 다이얼로그\n"+data.getName());
                        dialog.show();
                    }

                    @Override
                    public void onFailure(Call<TestData> call, Throwable t) {
                        OneBtnDialog dialog = new OneBtnDialog(TestActivity.this, "오류", t.toString());
                        dialog.show();
                    }
                });
            }
        });

//        retrofitAPI.getTest().enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                Log.d("test", response.body().toString());
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//
//            }
//        });
    }
}