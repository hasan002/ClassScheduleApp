package com.example.hasan.schedule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class LoginActivity extends AppCompatActivity {

    Button stdBtn,techBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        stdBtn = findViewById(R.id.btn_student);
        techBtn = findViewById(R.id.btn_teacher);

        stdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,StudentLoginActivity.class);
                startActivity(intent);
            }
        });

        techBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this,StudentLoginActivity.class);
//                startActivity(intent);
            }
        });


    }
}
