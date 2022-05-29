package com.grpprj.submit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import adapters.ChaptersViewPageAdapter;

public class ChaptersActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    TabLayout tabLayout;
    ChaptersViewPageAdapter chaptersViewPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        viewPager2 = findViewById(R.id.vp2);
        tabLayout = findViewById(R.id.chaptersTabs);

        FragmentManager fragmentManager = getSupportFragmentManager();

        chaptersViewPageAdapter = new ChaptersViewPageAdapter(fragmentManager, getLifecycle());

        viewPager2.setAdapter(chaptersViewPageAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });




    }
}