package com.example.hasan.schedule.TeacherActivity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.hasan.schedule.R;

public class TeacherActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    private String userId;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        if (getIntent() != null) {

            userId = getIntent().getStringExtra("user_id");
            Log.d("userId",userId);

        }


         bundle = new Bundle();
        bundle.putString("user_id", userId);

        // set Fragmentclass Arguments

        AllRoutineFragment allRoutineFragment = new AllRoutineFragment();
        allRoutineFragment.setArguments(bundle);

        OwnRoutineFragment ownRoutineFragment =new OwnRoutineFragment();
        ownRoutineFragment.setArguments(bundle);



        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(allRoutineFragment,"All Routine");
        viewPagerAdapter.addFragments(ownRoutineFragment,"Own Routine");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}
