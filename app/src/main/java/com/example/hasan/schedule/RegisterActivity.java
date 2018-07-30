package com.example.hasan.schedule;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hasan.schedule.routine.RoutineActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editText_name = findViewById(R.id.edtTxt_name);
        editText_email = findViewById(R.id.edtTxt_email);
        editText_register_no = findViewById(R.id.edtTxt_register_no);
        editText_year = findViewById(R.id.edtTxt_year);
        editText_semester = findViewById(R.id.edtTxt_semester);
        editText_password = findViewById(R.id.edtTxt_password);
        editText_confirm_password = findViewById(R.id.edtTxt_confirm_passsword);

        button_save = findViewById(R.id.btn_save);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference();


        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = editText_name.getText().toString().trim();
                final String email = editText_email.getText().toString().trim();
                final String register = editText_register_no.getText().toString().trim();
                final String year = editText_year.getText().toString().trim();
                final String semester = editText_semester.getText().toString().trim();
                String password = editText_password.getText().toString().trim();

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    FirebaseUser user = firebaseAuth.getCurrentUser();

                                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest
                                            .Builder()
                                            .setDisplayName(name)
                                            .build();

                                    if (user != null) {

                                        user.updateProfile(profileChangeRequest);

                                        DatabaseReference studentChild = databaseReference.child("students")
                                                .child(user.getUid());

                                        Map<String, String> studentData = new HashMap<>();

                                        studentData.put("std_name", name);
                                        studentData.put("std_email", email);
                                        studentData.put("register_no", register);
                                        studentData.put("year", year);
                                        studentData.put("semester", semester);

                                        studentChild.setValue(studentData);

                                        Intent intent = new Intent(RegisterActivity.this, RoutineActivity.class);
                                        intent.putExtra("user_name", name);
                                        startActivity(intent);
                                    }

                                } else {
                                    Toast.makeText(RegisterActivity.this, "" + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private EditText editText_name;
    private EditText editText_email;
    private EditText editText_register_no;
    private EditText editText_year;
    private EditText editText_semester;
    private EditText editText_password;
    private EditText editText_confirm_password;

    private Button button_save;
}
