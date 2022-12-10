package com.planitse2022.planit.util.toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.planitse2022.planit.R;

public class ToolbarSubpage implements PlanitToolbar{
    private AppCompatActivity parantActivity;
    private Toolbar toolbar;
    private TextView tvTitle;
    private ImageView imgBack;
    private String title;

    public ToolbarSubpage(AppCompatActivity parantActivity) {
        this.parantActivity = parantActivity;
        toolbar = parantActivity.findViewById(R.id.toolbar);
        tvTitle = toolbar.findViewById(R.id.toolbar_tv_title);
        imgBack = toolbar.findViewById(R.id.toolbar_img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parantActivity.finish();
            }
        });
    }

    public void setTitle(String title) {
        this.title = title;
        tvTitle.setText(title);
    }
}
