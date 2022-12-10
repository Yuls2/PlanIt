package com.planitse2022.planit.util.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.planitse2022.planit.R;

public class TwoBtnDialog extends PlanitDialog{
    TextView tvTitle, tvContent;
    Button btnBtnOne, btnBtnTwo;
    String strTitle, strContent, strBtnOne, strBtnTwo;

    public TwoBtnDialog(Context context, String strTitle, String strContent, String strBtnOne, String strBtnTwo) {
        super(context);
        this.strTitle = strTitle;
        this.strContent = strContent;
        this.strBtnOne = strBtnOne;
        this.strBtnTwo = strBtnTwo;
    }

    public TwoBtnDialog(Context context, String strTitle, String strContent) {
        this(context, strTitle, strContent, "취소","확인");
    }

    @Override
    public void show() {
        dialogInit();
        dialog.setContentView(R.layout.lay_dialog_twobtn);

        tvTitle = dialog.findViewById(R.id.dialog_tv_title);
        tvContent = dialog.findViewById(R.id.dialog_tv_content);
        btnBtnOne = dialog.findViewById(R.id.dialog_btn_one);
        btnBtnTwo = dialog.findViewById(R.id.dialog_btn_two);

        tvTitle.setText(strTitle);
        tvContent.setText(strContent);
        btnBtnOne.setText(strBtnOne);
        btnBtnTwo.setText(strBtnTwo);

        View.OnClickListener dismissOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dissmissDailog();
            }
        };

        btnBtnOne.setOnClickListener(dismissOnClickListener);
        btnBtnTwo.setOnClickListener(dismissOnClickListener);

        dialog.show();
    }

    public void updateText(String strTitle, String strContent, String strBtnOne, String strBtnTwo) {
        this.strTitle = strTitle;
        this.strContent = strContent;
        this.strBtnOne = strBtnOne;
        this.strBtnTwo = strBtnTwo;

        tvTitle.setText(strTitle);
        tvContent.setText(strContent);
        btnBtnOne.setText(strBtnOne);
        btnBtnTwo.setText(strBtnTwo);
    }

    public void setBtnOnClickListener(int btnNum, View.OnClickListener onClickListener) {
        if (btnNum == 1) {
            btnBtnOne.setOnClickListener(onClickListener);
        }
        else if (btnNum == 2) {
            btnBtnTwo.setOnClickListener(onClickListener);
        }
    }
}
