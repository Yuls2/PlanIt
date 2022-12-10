package com.planitse2022.planit.util.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.planitse2022.planit.R;

import java.util.Calendar;

public class CalenderDialog extends PlanitDialog{
    TextView tvTitle;
    Button btnBtnOk, btnBtnCancel;
    String strTitle;
    DatePicker datePicker;

    public CalenderDialog(Context context, String strTitle) {
        super(context);
        this.strTitle = strTitle;
    }

    public CalenderDialog(Context context) {
        this(context, "날짜 선택");
    }

    @Override
    public void show() {
        dialogInit();
        dialog.setContentView(R.layout.lay_dialog_calender);

        tvTitle = dialog.findViewById(R.id.dialog_tv_title);
        datePicker = dialog.findViewById(R.id.dialog_datepicker);
        btnBtnOk = dialog.findViewById(R.id.dialog_btn_ok);
        btnBtnCancel = dialog.findViewById(R.id.dialog_btn_cancel);

        tvTitle.setText(strTitle);
        datePicker.setMinDate(System.currentTimeMillis() - 1000);

        btnBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dissmissDailog();
            }
        });
        btnBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dissmissDailog();
            }
        });

        dialog.show();
    }

    public void setBtnOnClickListener(View.OnClickListener onClickListener) {
        btnBtnOk.setOnClickListener(onClickListener);
    }

    public Calendar getSelectedDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        return calendar;
    }
}
