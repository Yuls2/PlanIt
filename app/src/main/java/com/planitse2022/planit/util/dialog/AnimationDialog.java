package com.planitse2022.planit.util.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.planitse2022.planit.R;

public class AnimationDialog extends PlanitDialog{
    TextView tvTitle, tvContent;
    Button btnBtnOk;
    ImageView imgAnimation;
    String strTitle, strContent, strBtn;
    int res;

    public AnimationDialog(Context context, int res, String strTitle, String strContent, String strBtn) {
        super(context);
        this.strTitle = strTitle;
        this.strContent = strContent;
        this.strBtn = strBtn;
        this.res = res;
    }

    public AnimationDialog(Context context, int res, String strTitle, String strContent) {
        this(context, res, strTitle, strContent, "확인");
    }

    @Override
    public void show() {
        dialogInit();
        dialog.setContentView(R.layout.lay_dialog_animation);

        tvTitle = dialog.findViewById(R.id.dialog_tv_title);
        tvContent = dialog.findViewById(R.id.dialog_tv_content);
        btnBtnOk = dialog.findViewById(R.id.dialog_btn_ok);
        imgAnimation = dialog.findViewById(R.id.dialog_img_animation);

        tvTitle.setText(strTitle);
        tvContent.setText(strContent);
        btnBtnOk.setText(strBtn);

        Glide.with(context)
                .load(res)
                .into(new DrawableImageViewTarget(imgAnimation) {
                    @Override public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        if (resource instanceof GifDrawable) {
                            ((GifDrawable)resource).setLoopCount(1);
                        }
                        super.onResourceReady(resource, transition);
                    }
                });

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
