package com.planitse2022.planit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.planitse2022.planit.R;
import com.planitse2022.planit.data.AccountData;
import com.planitse2022.planit.data.UserData;
import com.planitse2022.planit.util.AutoLoginManager;
import com.planitse2022.planit.util.LoginInfo;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.RetrofitClient;
import com.planitse2022.planit.view.main.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        UserData userData = UserData.getInstance();
        LoginInfo loginInfo = AutoLoginManager.getLoginInfo(this);
        if(loginInfo.isFirst) {
            ActivityChange(IntroductionActivity.class);
        }
        else {
            RetrofitAPI retrofitAPI = RetrofitClient.getInstance().getRetrofitAPI();
            retrofitAPI.requestLogin(loginInfo.id, loginInfo.password).enqueue(new Callback<AccountData>() {
                @Override
                public void onResponse(Call<AccountData> call, Response<AccountData> response) {
                    AccountData data = response.body();
                    if (data.getResult() == 0) {
                        String token = response.headers().get("Authenticate");
                        userData.setToken(token);
                        userData.setUserID(data.getUserID());
                        userData.setUserNickName(data.getNickName());
                        ActivityChange(MainActivity.class);
                    } else {
                        ActivityChange(LoginActivity.class);
                    }
                }

                @Override
                public void onFailure(Call<AccountData> call, Throwable t) {
                    ActivityChange(LoginActivity.class);
                }
            });
        }
    }

    private void ActivityChange(Class act) {
        Intent in = new Intent(InitActivity.this, act);
        startActivity(in);
        finish();
    }
}