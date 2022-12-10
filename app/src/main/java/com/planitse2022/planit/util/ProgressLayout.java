package com.planitse2022.planit.util;

import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.planitse2022.planit.R;

public class ProgressLayout {
    private View view;
    LinearLayout layout;
    ImageView img;
    int loadingNum;

    public ProgressLayout(View view) {
        this.view = view;
        layout = view.findViewById(R.id.lay_progress);
        img = layout.findViewById(R.id.prg_img);
        //로딩 이미지
        Glide.with(view.getContext())
                .load(R.drawable.img_ani_progress)
                .into(img);

        loadingNum = 0;
    }

    public void addLoading() {
        if(loadingNum == 0) {
            layout.setVisibility(View.VISIBLE);
        }
        loadingNum++;
    }

    public void doneLoading() {
        loadingNum--;
        if(loadingNum == 0) {
            layout.setVisibility(View.GONE);
        }
    }

    public void clearLoading() {
        loadingNum = 0;
        layout.setVisibility(View.GONE);
    }

    public void setBackgroundColor(int color) {
        img.setColorFilter(ContextCompat.getColor(view.getContext(), color), PorterDuff.Mode.MULTIPLY);
        layout.setBackgroundColor(ContextCompat.getColor(view.getContext(), color));
    }

}
