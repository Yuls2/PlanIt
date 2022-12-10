package com.planitse2022.planit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.planitse2022.planit.R;
import com.planitse2022.planit.Adapter.IntroPagerAdapter;
import com.planitse2022.planit.util.AutoLoginManager;
import com.google.android.material.tabs.TabLayout;

public class IntroductionActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private Button btnNext;
    private TabLayout tabLayout;
    private IntroPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        viewPager = findViewById(R.id.pager_intro);
        tabLayout = findViewById(R.id.tab_intro);
        btnNext = findViewById(R.id.btn_next);

        adapter = new IntroPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (viewPager.getCurrentItem() < adapter.getCount() - 1) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
                else {
                    Intent in = new Intent(IntroductionActivity.this, LoginActivity.class);
                    AutoLoginManager.setIsFirst(IntroductionActivity.this, 0);
                    startActivity(in);
                    finish();
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override public void onPageSelected(int position) {
                if (position == adapter.getCount() - 1) {
                    btnNext.setText("플래닛 시작하기");
                } else {
                    btnNext.setText("다음");
                }
            }
            @Override public void onPageScrollStateChanged(int state) {
            }
        });
    }
//    private void changeStatusBarColor() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }
//    }
}