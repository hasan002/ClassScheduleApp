package com.example.hasan.schedule.TeacherActivity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.hasan.schedule.LoginActivity;
import com.example.hasan.schedule.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
            Log.d("userId", userId);

        }


        bundle = new Bundle();
        bundle.putString("user_id", userId);

        // set Fragmentclass Arguments

        AllRoutineFragment allRoutineFragment = new AllRoutineFragment();
        allRoutineFragment.setArguments(bundle);

        OwnRoutineFragment ownRoutineFragment = new OwnRoutineFragment();
        ownRoutineFragment.setArguments(bundle);


        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(allRoutineFragment, "All Routine");
        viewPagerAdapter.addFragments(ownRoutineFragment, "Own Routine");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.id_profile) {

            return true;

        } else if (id == R.id.id_log_out) {

            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();

            return true;
        }

        return true;
    }
}
