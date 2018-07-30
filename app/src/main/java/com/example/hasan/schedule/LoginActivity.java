package com.example.hasan.schedule;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hasan.schedule.routine.RoutineActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private int radioId;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editText_name = findViewById(R.id.edtTxt_login_name);
        edit_Text_password = findViewById(R.id.edtTxt_login_password);

        textView_signup = findViewById(R.id.txtVw_signup);

        button_login = findViewById(R.id.btn_login);

        radioGroup = findViewById(R.id.radioGroup);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference();


        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, RoutineActivity.class);
            intent.putExtra("user_name", user.getDisplayName());
            startActivity(intent);
            finish();
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioBtn_teacher:
                        radioId = 0;
                        break;
                    case R.id.radioBtn_student:
                        radioId= 1;
                        break;
                }
            }
        });


        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editText_name.getText().toString().trim();
                String password = edit_Text_password.getText().toString().trim();


                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    FirebaseUser user = firebaseAuth.getCurrentUser();

                                    if (user != null) {

                                        Intent intent = new Intent(LoginActivity.this, RoutineActivity.class);
                                        intent.putExtra("user_name", user.getDisplayName());
                                        startActivity(intent);
                                    }

                                } else {
                                    Toast.makeText(LoginActivity.this, "" + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        textView_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    private EditText editText_name;
    private EditText edit_Text_password;
    private TextView textView_signup;
    private Button button_login;
    private RadioGroup radioGroup;

}
