package com.planitse2022.planit;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.data.UserData;
import com.planitse2022.planit.util.dialog.OneBtnDialog;
import com.planitse2022.planit.util.dialog.TwoBtnDialog;
import com.planitse2022.planit.util.hash.SHA256;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.RetrofitClient;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    Button btnRegister, btnCancel;
    EditText edtID, edtPass, edtPassCon, edtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        btnRegister = findViewById(R.id.btn_register);
        btnCancel = findViewById(R.id.btn_cancel);
        edtID = findViewById(R.id.edt_id);
        edtPass = findViewById(R.id.edt_password);
        edtPassCon = findViewById(R.id.edt_password_confirm);
        edtName = findViewById(R.id.edt_name);

        UserData userData = UserData.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int check = isAvailable();
                try {
                    if(check == 0) {
                        RetrofitAPI retrofitAPI = RetrofitClient.getInstance().getRetrofitAPI();
                        HashMap<String, Object> param = new HashMap<>();
                        SHA256 sha256=new SHA256();
                        param.put("userID",edtID.getText().toString());
                        param.put("password",sha256.encrypt(edtPass.getText().toString()).toUpperCase());
                        param.put("nickname",edtName.getText().toString());
                        Log.e("param",param.toString());
                        retrofitAPI.requestRegister(param).enqueue(new Callback<ResponseData<Integer>>() {
                            @Override
                            public void onResponse(Call<ResponseData<Integer>> call, Response<ResponseData<Integer>> response) {
                                ResponseData<Integer> responseData = response.body();
                                OneBtnDialog dialog;
                                if(responseData.getResult() == 0) {
                                    dialog = new OneBtnDialog(RegisterActivity.this, "?????? ??????", "???????????? ?????? ?????? ??????????????? " + edtName.getText().toString() + "???!");
                                    dialog.show();
                                    dialog.setBtnOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            finish();
                                            dialog.dissmissDailog();
                                        }
                                    });
                                }
                                else if(responseData.getResult() == 1) {
                                    dialog = new OneBtnDialog(RegisterActivity.this, "?????? ??????", "????????? ???????????? ????????????! ?????? ???????????? ??????????????????.");
                                    dialog.show();
                                }
                                else if(responseData.getResult() == 2) {
                                    dialog = new OneBtnDialog(RegisterActivity.this, "?????? ??????", "????????? ???????????? ????????????! ?????? ??????????????? ??????????????????.");
                                    dialog.show();
                                }
                                else {
                                    dialog = new OneBtnDialog(RegisterActivity.this, "?????? ??????", "??? ??? ?????? ????????? ??????????????????.");
                                    dialog.show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseData<Integer>> call, Throwable t) {
                                OneBtnDialog dialog = new OneBtnDialog(RegisterActivity.this, "?????? ??????", "??? ??? ?????? ????????? ??????????????????.");
                                dialog.show();
                            }
                        });
                    }
                    else {
                        OneBtnDialog dialog = null;
                        if(check == 1)
                            dialog = new OneBtnDialog(RegisterActivity.this, "?????? ??????", "???????????? 6~20???????????? ?????????!");
                        else if(check == 2)
                            dialog = new OneBtnDialog(RegisterActivity.this, "?????? ??????", "??????????????? 7~20???????????? ?????????!");
                        else if(check == 3)
                            dialog = new OneBtnDialog(RegisterActivity.this, "?????? ??????", "??????????????? ???????????? ????????? ???????????? ????????????");
                        else if(check == 4)
                            dialog = new OneBtnDialog(RegisterActivity.this, "?????? ??????", "???????????? 2~10???????????? ?????????!");
                        dialog.show();
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwoBtnDialog dialog = new TwoBtnDialog(view.getContext(), "????????? ????????????????"
                        , "??????????????? ????????? ???????????? ????????????!", "?????????", "???");
                dialog.show();
                dialog.setBtnOnClickListener(2, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        dialog.dissmissDailog();
                    }
                });
            }
        });
    }

    private int isAvailable() {
        int res = 0;
        int lenID = edtID.getText().toString().trim().length();
        int lenPass = edtPass.getText().toString().trim().length();
        int lenName = edtName.getText().toString().trim().length();
        if(lenID < 6 || lenID > 20) {
            res = 1;
        }
        else if(lenPass < 7 || lenPass > 20) {
            res = 2;
        }
        else if(!edtPass.getText().toString().equals(edtPassCon.getText().toString())) {
            res = 3;
        }
        else if(lenName < 2 || lenName > 10) {
            res = 4;
        }
        return res;
    }
}