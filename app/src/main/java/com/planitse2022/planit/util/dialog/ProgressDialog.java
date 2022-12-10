package com.planitse2022.planit.util.dialog;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.planitse2022.planit.R;

public class ProgressDialog {
    private Dialog dialog;
    private TextView tvTip;
    public ProgressDialog(Context context) {
        dialog = new Dialog(context);
        //배경 제거 및 타이틀바 제거
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //애니메이션 제거
        dialog.getWindow().setWindowAnimations(R.style.dialogFadeout);
        //취소 불가능 하도록
        dialog.setCancelable(false);

        dialog.setContentView(R.layout.lay_dialog_progress);

        //로딩 이미지
        Glide.with(context)
                .load(R.drawable.img_ani_progress)
                .into((ImageView)dialog.findViewById(R.id.prg_img));

        //화면에 가득 채우기
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
    }

    public void show() {
        dialog.show();
    }

    public void loadingComplete() {

        dialog.dismiss();
    }
}
