package com.planitse2022.planit.util.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.planitse2022.planit.R;
import com.planitse2022.planit.data.GroupData;
import com.planitse2022.planit.data.UserData;

public class OneEdtDialog extends TwoBtnDialog{
    TextView tvTitle, tvContent;
    Button btnBtnClose, btnBtnComplete;
    EditText edtEdt;
    GroupData groupData;
    UserData userData;

    public OneEdtDialog(Context context, String strTitle, String strContent, String strBtnOne, String strBtnTwo) {
        super(context, strTitle, strContent, strBtnOne, strBtnTwo);
        this.groupData = groupData;
    }

    public OneEdtDialog(Context context, String strTitle, String strContent) {
        this(context, strTitle, strContent, "취소","작성 완료");
    }

    @Override
    public void show() {
        dialogInit();
        dialog.setContentView(R.layout.lay_dialog_one_edt);

        userData = UserData.getInstance();

        tvTitle = dialog.findViewById(R.id.dialog_tv_title);
        tvContent = dialog.findViewById(R.id.dialog_tv_content);
        btnBtnClose = dialog.findViewById(R.id.dialog_btn_close);
        btnBtnComplete = dialog.findViewById(R.id.dialog_btn_complete);
        edtEdt = dialog.findViewById(R.id.dialog_edt);

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

    public String getEdtText() {
        return edtEdt.getText().toString();
    }
}
