package com.example.hasan.schedule;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.hasan.schedule.TeacherActivity.TeacherActivity;
import com.example.hasan.schedule.models.TeacherDataModel;
import com.example.hasan.schedule.routine.RoutineActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference dbChildReference;

    String userId;
    String userName;

    Button stdBtn, techBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        stdBtn = findViewById(R.id.btn_student);
        techBtn = findViewById(R.id.btn_teacher);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        FirebaseUser user = firebaseAuth.getCurrentUser();


        if (user != null) {

            userId = user.getUid();
            userName = user.getDisplayName();

            userVarification();

        }


        stdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, StudentLoginActivity.class);
                startActivity(intent);
            }
        });

        techBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, TeacherLoginActivity.class);
                startActivity(intent);
            }
        });


    }


    private void userVarification() {


        firebaseDatabase = FirebaseDatabase.getInstance();
        dbChildReference = firebaseDatabase.getReference();

        dbChildReference.child("teacherData").child(userId)
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Log.d("teacherData", dataSnapshot.toString());

                        TeacherDataModel teacherDataModel = dataSnapshot.getValue(TeacherDataModel.class);

                        if (teacherDataModel != null) {

                           // Toast.makeText(LoginActivity.this, "Teacher Activity", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, TeacherActivity.class);
                            intent.putExtra("user_id", userId);
                            startActivity(intent);
                            finish();
                        } else {

                            //Toast.makeText(LoginActivity.this, "Student Activity", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, RoutineActivity.class);
                            intent.putExtra("user_name", userName);
                            intent.putExtra("user_id", userId);
                            startActivity(intent);
                            finish();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("OnCancelled", databaseError.getMessage());
                    }
                });


    }

}
