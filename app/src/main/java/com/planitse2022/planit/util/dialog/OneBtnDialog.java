package com.planitse2022.planit.util.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.planitse2022.planit.R;

public class OneBtnDialog extends PlanitDialog{
    TextView tvTitle, tvContent;
    Button btnBtnOk;
    String strTitle, strContent, strBtn;

    public OneBtnDialog(Context context, String strTitle, String strContent, String strBtn) {
        super(context);
        this.strTitle = strTitle;
        this.strContent = strContent;
        this.strBtn = strBtn;
    }

    public OneBtnDialog(Context context, String strTitle, String strContent) {
        this(context, strTitle, strContent, "확인");
    }

    @Override
    public void show() {
        dialogInit();
        dialog.setContentView(R.layout.lay_dialog_onebtn);

        tvTitle = dialog.findViewById(R.id.dialog_tv_title);
        tvContent = dialog.findViewById(R.id.dialog_tv_content);
        btnBtnOk = dialog.findViewById(R.id.dialog_btn_ok);

        tvTitle.setText(strTitle);
        tvContent.setText(strContent);
        btnBtnOk.setText(strBtn);

        btnBtnOk.setOnClickListener(new View.OnClickListener() {
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
}
