package com.planitse2022.planit.view;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.planitse2022.planit.R;
import com.planitse2022.planit.data.UserData;
import com.planitse2022.planit.util.ProgressLayout;
import com.planitse2022.planit.util.retrofit.RetrofitAPI;
import com.planitse2022.planit.util.retrofit.ServiceGenerator;
import com.planitse2022.planit.util.toolbar.ToolbarSubpage;

abstract public class OneListActivity extends AppCompatActivity {
    protected RecyclerView listList;
    protected TextView tvTitle;
    protected ImageView imgIcon;
    protected ProgressLayout progressLayout;
    protected RetrofitAPI retrofitAPI;
    protected UserData userData;
    protected ToolbarSubpage toolbar;
    protected int groupID;
    protected int contentView = R.layout.activity_one_list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(contentView);

        toolbar = new ToolbarSubpage(this);
        userData = UserData.getInstance();

        groupID = getIntent().getIntExtra("groupID",-1);

        listList = findViewById(R.id.list_list);
        tvTitle = findViewById(R.id.tv_title);
        imgIcon = findViewById(R.id.img_icon);

        listList.setHasFixedSize(true);

        retrofitAPI = ServiceGenerator.createService(RetrofitAPI.class, userData.getToken());
        progressLayout = new ProgressLayout(getWindow().getDecorView());
    }

    protected void setContent(String title, int icon, String toolbarString) {
        tvTitle.setText(title);
        Glide.with(this).load(icon).into(imgIcon);

        toolbar.setTitle(toolbarString);
    }
}
