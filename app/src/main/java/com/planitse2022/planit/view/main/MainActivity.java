package com.planitse2022.planit.view.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.planitse2022.planit.R;
import com.planitse2022.planit.util.toolbar.ToolbarMain;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private static final int FRAG_NUM = 3;
    private Fragment[] fragArr = new Fragment[FRAG_NUM];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ToolbarMain toolbar = new ToolbarMain(this);

        fragArr[0] = new MainFragment();
        fragArr[1] = new MyPlanetFragment();
        fragArr[2] = new MypageFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.lay_content, fragArr[0]).commitAllowingStateLoss();
        TabLayout layNavi = findViewById(R.id.lay_navi);
        layNavi.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment sel = fragArr[tab.getPosition()];
                getSupportFragmentManager().beginTransaction().replace(R.id.lay_content, sel).commitAllowingStateLoss();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}