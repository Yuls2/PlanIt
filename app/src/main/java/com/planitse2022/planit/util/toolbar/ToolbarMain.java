package com.planitse2022.planit.util.toolbar;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.planitse2022.planit.R;
import com.planitse2022.planit.view.main.NoticeActivity;

public class ToolbarMain implements PlanitToolbar{
    private AppCompatActivity parantActivity;
    private Toolbar toolbar;
//    private TextView tvTitle;
    private ImageView imgNotice;
    private String title;

    public ToolbarMain(AppCompatActivity parantActivity) {
        this.parantActivity = parantActivity;
        toolbar = parantActivity.findViewById(R.id.toolbar);
        imgNotice = toolbar.findViewById(R.id.toolbar_img_Notice);
        imgNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(parantActivity, NoticeActivity.class);
                parantActivity.startActivity(in);
            }
        });
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
