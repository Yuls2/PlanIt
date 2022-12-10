package com.planitse2022.planit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.planitse2022.planit.data.AccountData;
import com.planitse2022.planit.data.UserData;
import com.planitse2022.planit.util.AutoLoginManager;
import com.planitse2022.planit.util.dialog.OneBtnDialog;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.RetrofitClient;
import com.planitse2022.planit.view.main.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin, btnRegister;
        EditText edtID, edtPass;

        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        edtID = findViewById(R.id.edt_id);
        edtPass = findViewById(R.id.edt_password);

        UserData userData = UserData.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetrofitAPI retrofitAPI = RetrofitClient.getInstance().getRetrofitAPI();
                retrofitAPI.requestLogin(edtID.getText().toString(), edtPass.getText().toString()).enqueue(new Callback<AccountData>() {
                    @Override
                    public void onResponse(Call<AccountData> call, Response<AccountData> response) {
                        AccountData data = response.body();
                        if(data.getResult() == 0) {
                            String token = response.headers().get("Authenticate");
                            userData.setToken(token);
                            userData.setUserID(data.getUserID());
                            userData.setUserNickName(data.getNickName());

                            AutoLoginManager.setLoginInfo(LoginActivity.this, data.getUserID(), edtPass.getText().toString());

                            Intent in = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(in);
                            finish();
                        }
                        else if(data.getResult() == 1) {
                            //id 존재 X
                            OneBtnDialog dialog = new OneBtnDialog(LoginActivity.this, "로그인 실패", "존재하지 않는 아이디입니다!");
                            dialog.show();
                        }
                        else if(data.getResult() == 2) {
                            //비번 플림
                            OneBtnDialog dialog = new OneBtnDialog(LoginActivity.this, "로그인 실패", "잘못된 비밀번호 입니다.");
                            dialog.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AccountData> call, Throwable t) {
                    }
                });
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(in);
            }
        });
    }
}