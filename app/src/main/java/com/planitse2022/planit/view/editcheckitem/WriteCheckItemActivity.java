package com.planitse2022.planit.view.editcheckitem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.planitse2022.planit.R;
import com.planitse2022.planit.data.ResponseData;
import com.planitse2022.planit.data.UserData;
import com.planitse2022.planit.util.dialog.CalenderDialog;
import com.planitse2022.planit.util.dialog.OneBtnDialog;
import com.planitse2022.planit.util.dialog.TwoBtnDialog;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.ServiceGenerator;
import com.planitse2022.planit.util.toolbar.ToolbarSubpage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WriteCheckItemActivity extends AppCompatActivity {
    private CheckBox dayCheckBoxes[];
    private TextView dayCheckTvs[], tvSeldate, tvPriority, tvGoalName, tvGroupName;
    private SeekBar seekPriority;
    private ImageView imgCalender;
    private Calendar selectedDate;
    private EditText edtTitle;
    private Button btnCancel, btnComplete, btnDelete;
    private int type = 0, checkID = -1, groupID;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String strGoalName, strGroupName;
    private Boolean isEdit;
    private static final String strPriority[] = {"전혀 중요하지 않음", "별로 중요하지 않음", "보통", "중요함", "매우 중요함"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_check_item);

        viewInit();

        ToolbarSubpage toolbar = new ToolbarSubpage(this);
        toolbar.setTitle("할 일 추가");
        
        // 그룹 명, 목표 명 이전 intent에서 받아오기
        Intent intent = getIntent();
        isEdit = intent.getBooleanExtra("isEdit", false);
        groupID = intent.getIntExtra("groupID", -1);
        strGroupName = intent.getStringExtra("groupName");
        strGoalName = intent.getStringExtra("goalName");
        tvGroupName.setText(strGroupName);
        tvGoalName.setText(strGoalName);
        // 수정의 경우 입력 폼 기존 데이터로 채우기
        if(isEdit) {
            toolbar.setTitle("할 일 수정");
            
            checkID = intent.getIntExtra("checkID", -1);
            edtTitle.setText(intent.getStringExtra("title"));
            seekPriority.setProgress(intent.getIntExtra("priority", 3));
            btnDelete.setVisibility(View.VISIBLE);
            // 요일/날짜 선택
            type = intent.getIntExtra("type",0);
            if(type == 0) { //요일 세팅
                int day = intent.getIntExtra("day", 0);
                for(int i = 0; i < 7; i++) {
                    int bound = (int) Math.pow(2,i+1);
                    dayCheckBoxes[i].setChecked(day%bound >= (bound/2));
                }
            }
            else { //날짜 세팅
                //캘린더 버튼 색 변경
                imgCalender.setColorFilter(ContextCompat.getColor(this, R.color.pi_purple)
                        , PorterDuff.Mode.SRC_IN);
                String strDate = intent.getStringExtra("date");
                selectedDate = Calendar.getInstance();
                try {
                    selectedDate.setTime(dateFormat.parse(strDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            tvSeldate.setText(getSelectedText());
        }
    }

    private void viewInit() {
        tvGroupName = findViewById(R.id.tv_groupname);
        tvGoalName = findViewById(R.id.tv_goalname);
        btnCancel = findViewById(R.id.btn_cancel);
        btnComplete = findViewById(R.id.btn_complete);
        btnDelete = findViewById(R.id.btn_delete);
        edtTitle = findViewById(R.id.edt_title);

        dayCheckBoxes = new CheckBox[7];
        dayCheckTvs = new TextView[7];

        dayCheckBoxes[0] = findViewById(R.id.chk_day_sun);
        dayCheckBoxes[1] = findViewById(R.id.chk_day_mon);
        dayCheckBoxes[2] = findViewById(R.id.chk_day_tue);
        dayCheckBoxes[3] = findViewById(R.id.chk_day_wed);
        dayCheckBoxes[4] = findViewById(R.id.chk_day_thu);
        dayCheckBoxes[5] = findViewById(R.id.chk_day_fri);
        dayCheckBoxes[6] = findViewById(R.id.chk_day_sat);
        dayCheckTvs[0] = findViewById(R.id.tv_day_sun);
        dayCheckTvs[1] = findViewById(R.id.tv_day_mon);
        dayCheckTvs[2] = findViewById(R.id.tv_day_tue);
        dayCheckTvs[3] = findViewById(R.id.tv_day_wed);
        dayCheckTvs[4] = findViewById(R.id.tv_day_thu);
        dayCheckTvs[5] = findViewById(R.id.tv_day_fri);
        dayCheckTvs[6] = findViewById(R.id.tv_day_sat);

        //요일 버튼 클릭 이벤트 설정
        CompoundButton.OnCheckedChangeListener colorChanger = new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                type = 0;
                tvSeldate.setText(getSelectedText());

                TextView tvInside = (TextView) compoundButton.getTag();
                tvInside.setTextColor(ContextCompat.getColor(compoundButton.getContext(), b?R.color.white:R.color.pi_purple));
                imgCalender.clearColorFilter();
            }
        };

        for(int i = 0; i < 7; i++) {
            dayCheckBoxes[i].setTag(dayCheckTvs[i]);
            dayCheckBoxes[i].setOnCheckedChangeListener(colorChanger);
        }

        //캘린더 버튼 클릭
        imgCalender = findViewById(R.id.img_calender);
        imgCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalenderDialog calenderDialog = new CalenderDialog(view.getContext());
                calenderDialog.show();
                calenderDialog.setBtnOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //요일 선택 내용 초기화
                        for(int i = 0; i < 7; i++) {
                            dayCheckBoxes[i].setChecked(false);
                        }

                        selectedDate = calenderDialog.getSelectedDate();
                        type = 1;
                        //캘린더 버튼 색 변경
                        imgCalender.setColorFilter(ContextCompat.getColor(view.getContext(), R.color.pi_purple)
                                , PorterDuff.Mode.SRC_IN);
                        tvSeldate.setText(getSelectedText());
                        calenderDialog.dissmissDailog();
                    }
                });
            }
        });

        tvSeldate = findViewById(R.id.tv_seldate);
        tvSeldate.setText(getSelectedText());

        //중요도 설정
        tvPriority = findViewById(R.id.tv_priority);
        seekPriority = findViewById(R.id.seek_priority);
        seekPriority.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvPriority.setText(strPriority[i-1]);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // 완료/취소/삭제 버튼 클릭 이벤트 ==========================================
        UserData userData = UserData.getInstance();
        RetrofitAPI retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, userData.getToken());

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DB에 업데이트 REQUEST
                HashMap<String, Object> param = new HashMap<>();
                // 수정/삽입 공통 파라미터
                param.put("type", type);
                param.put("title", edtTitle.getText().toString());
                param.put("priority", seekPriority.getProgress());
                if(type == 0) {
                    // 선택된 요일로 day값 계산
                    int calDay = 0;
                    for(int i = 0; i < 7; i++) {
                        if(dayCheckBoxes[i].isChecked()) {
                            calDay += Math.pow(2,i);
                        }
                    }
                    param.put("day", calDay);
                }
                else {
                    param.put("date", dateFormat.format(selectedDate.getTime()));
                }
                // 수정/삽입에 따라 다른 페이지에 요청
                if(isEdit) {
                    param.put("checkID", checkID);
                    retrofitAPI.editCheckItem(param).enqueue(new Callback<ResponseData<Integer>>() {
                        @Override
                        public void onResponse(Call<ResponseData<Integer>> call, Response<ResponseData<Integer>> response) {
                            ResponseData<Integer> responseData = response.body();
                            if(responseData.getResult() == 0) {
                                finish();
                            }
                            else {
                                OneBtnDialog dialog = new OneBtnDialog(view.getContext(), "수정 실패"
                                        , "오류 코드 " + responseData.getResult()
                                        + "\n" + responseData.getMessage());
                                dialog.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseData<Integer>> call, Throwable t) {
                            OneBtnDialog dialog = new OneBtnDialog(view.getContext(), "수정 실패", t.toString());
                            dialog.show();
                        }
                    });
                }
                else {
                    param.put("groupID", groupID);
                    retrofitAPI.insertCheckItem(param).enqueue(new Callback<ResponseData<Integer>>() {
                        @Override
                        public void onResponse(Call<ResponseData<Integer>> call, Response<ResponseData<Integer>> response) {
                            ResponseData<Integer> responseData = response.body();
                            if(responseData.getResult() == 0) {
                                finish();
                            }
                            else {
                                OneBtnDialog dialog = new OneBtnDialog(view.getContext(), "수정 실패"
                                        , "오류 코드 " + responseData.getResult()
                                        + "\n" + responseData.getMessage());
                                dialog.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseData<Integer>> call, Throwable t) {
                            OneBtnDialog dialog = new OneBtnDialog(view.getContext(), "수정 실패", t.toString());
                            dialog.show();
                        }
                    });
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwoBtnDialog dialog = new TwoBtnDialog(view.getContext(), "정말 삭제하시겠습니까?", "할 일을 삭제하면 다시 복구할 수 없습니다!\n그래도 삭제하시겠습니까?", "취소", "삭제할게요");
                dialog.show();
                dialog.setBtnOnClickListener(2, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //TODO: DB에 삭제 REQUEST
                        retrofitAPI.deleteCheckItem(checkID).enqueue(new Callback<ResponseData<Integer>>() {
                            @Override
                            public void onResponse(Call<ResponseData<Integer>> call, Response<ResponseData<Integer>> response) {
                                ResponseData<Integer> responseData = response.body();
                                if(responseData.getResult() == 0) {
                                    finish();
                                }
                                else {
                                    OneBtnDialog dialog = new OneBtnDialog(view.getContext(), "수정 실패"
                                            , "오류 코드 " + responseData.getResult()
                                            + "\n" + responseData.getMessage());
                                    dialog.show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseData<Integer>> call, Throwable t) {
                                OneBtnDialog dialog = new OneBtnDialog(view.getContext(), "수정 실패", "알 수 없는 오류가 발생했습니다.");
                                dialog.show();
                            }
                        });
                        dialog.dissmissDailog();
                    }
                });
            }
        });
    }

    private String getSelectedText() {
        String str = "";
        if(type == 0) {
            str = "매주";
            int dayCount = 0;
            for(int i = 0; i < 7; i++) {
                if(dayCheckBoxes[i].isChecked()) {
                    if(dayCount > 0) str += ",";
                    str += " " + ((TextView) dayCheckBoxes[i].getTag()).getText();
                    dayCount += 1;
                }
            }
            if(dayCount == 0 || dayCount == 7) {
                str = "매일";
            }
        }
        else {
            str = selectedDate.get(Calendar.YEAR) + "년 " + (selectedDate.get(Calendar.MONTH) + 1) + "월 " + selectedDate.get(Calendar.DAY_OF_MONTH) + "일";
        }
        return str;
    }
}