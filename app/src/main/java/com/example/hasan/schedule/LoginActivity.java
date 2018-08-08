package com.example.hasan.schedule;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hasan.schedule.TeacherActivity.TeacherActivity;
import com.example.hasan.schedule.routine.RoutineActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private int radioId = -1;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editText_name = findViewById(R.id.edtTxt_login_name);
        edit_Text_password = findViewById(R.id.edtTxt_login_password);

        textView_signup = findViewById(R.id.txtVw_signup);

        button_login = findViewById(R.id.btn_login);

        radioGroup = findViewById(R.id.radioGroup);
        radioButton = findViewById(R.id.radioBtn_student);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {

            Intent intent;

            if(user.getEmail().equals("post2enam@gmail.com")){
                intent = new Intent(LoginActivity.this, TeacherActivity.class);
                intent.putExtra("user_id",user.getUid());

                Log.d("user_id",user.getUid());
            }else{
                intent = new Intent(LoginActivity.this, RoutineActivity.class);
                intent.putExtra("user_name", user.getDisplayName());
                intent.putExtra("user_id",user.getUid());

                Log.d("user_name",user.getDisplayName());
                Log.d("user_id",user.getUid());
            }

            startActivity(intent);
            finish();
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioBtn_teacher:
                        radioId = 0;
                        break;
                    case R.id.radioBtn_student:
                        radioId = 1;
                        break;
                }
            }
        });


        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = editText_name.getText().toString().trim();
                String password = edit_Text_password.getText().toString().trim();
                if (email.isEmpty()) {

                    editText_name.setError("Enter your email address.");

                } else if (password.isEmpty()) {
                    edit_Text_password.setError("Enter your password.");
                } else if (radioId == -1) {
                    radioButton.setError("Choose one from teacher/Student");
                } else {

                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        FirebaseUser user = firebaseAuth.getCurrentUser();


                                        if (user != null) {

                                            if(email.equals("post2enam@gmail.com")){

                                                Log.d("teacher","teacherIntent");

                                                final Intent intent = new Intent(LoginActivity.this, TeacherActivity.class);
                                                intent.putExtra("user_id",user.getUid());

                                                Log.d("user_id",user.getUid());

                                                startActivity(intent);
                                            }else {

                                                Log.d("student","studentIntent");

                                                final Intent intent = new Intent(LoginActivity.this, RoutineActivity.class);
                                                intent.putExtra("user_name", user.getDisplayName());
                                                intent.putExtra("user_id",user.getUid());

                                                Log.d("user_name",user.getDisplayName());
                                                Log.d("user_id",user.getUid());

                                                startActivity(intent);
                                            }


                                        }

                                    } else {
                                        Toast.makeText(LoginActivity.this, "" + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
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
    private RadioButton radioButton;

}
