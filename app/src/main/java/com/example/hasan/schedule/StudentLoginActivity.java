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
import com.example.hasan.schedule.models.TeacherDataModel;
import com.example.hasan.schedule.routine.RoutineActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentLoginActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference dbChildReference;

    String userId;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        editText_name = findViewById(R.id.edtTxt_std_login_name);
        edit_Text_password = findViewById(R.id.edtTxt_std_login_password);

        textView_signup = findViewById(R.id.txtVw_std_signup);

        button_login = findViewById(R.id.btn_std_login);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
/*

        FirebaseUser user = firebaseAuth.getCurrentUser();




            if (user != null) {

                Intent intent;

                if (user.getEmail().equals("post2enam@gmail.com")) {
                    intent = new Intent(StudentLoginActivity.this, TeacherActivity.class);
                    intent.putExtra("user_id", user.getUid());

                    Log.d("user_id", user.getUid());
                } else {
                    intent = new Intent(StudentLoginActivity.this, RoutineActivity.class);
                    intent.putExtra("user_name", user.getDisplayName());
                    intent.putExtra("user_id", user.getUid());

                    Log.d("user_name", user.getDisplayName());
                    Log.d("user_id", user.getUid());
                }

                startActivity(intent);
                finish();
            }
*/


        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = editText_name.getText().toString().trim();
                String password = edit_Text_password.getText().toString().trim();
                if (email.isEmpty()) {

                    editText_name.setError("Enter your email address.");

                } else if (password.isEmpty()) {

                    edit_Text_password.setError("Enter your password.");

                } else {

                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        userId = user.getUid();
                                        userName = user.getDisplayName();

                                        if (user != null) {


                                            studentVarification();

                                           /* if (email.equals("post2enam@gmail.com")) {

                                                Log.d("teacher", "teacherIntent");

                                                final Intent intent = new Intent(StudentLoginActivity.this, TeacherActivity.class);
                                                intent.putExtra("user_id", user.getUid());

                                                Log.d("user_id", user.getUid());

                                                startActivity(intent);
                                                finish();
                                            } else {

                                                Log.d("student", "studentIntent");

                                                final Intent intent = new Intent(StudentLoginActivity.this, RoutineActivity.class);
                                                intent.putExtra("user_name", user.getDisplayName());
                                                intent.putExtra("user_id", user.getUid());

                                                Log.d("user_name", user.getDisplayName());
                                                Log.d("user_id", user.getUid());

                                                startActivity(intent);
                                                finish();
                                            }*/


                                        }

                                    } else {
                                        Toast.makeText(StudentLoginActivity.this, "" + task.getException(),
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
                Intent intent = new Intent(StudentLoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }


    private void studentVarification() {

        FirebaseUser user = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbChildReference = firebaseDatabase.getReference();

        dbChildReference.child("teacherData").child(userId)
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Log.d("teacherData", dataSnapshot.toString());

                        TeacherDataModel teacherDataModel = dataSnapshot.getValue(TeacherDataModel.class);

                        if (teacherDataModel != null) {

                            Toast.makeText(StudentLoginActivity.this, "Invalid ID", Toast.LENGTH_SHORT).show();
                        } else {

                            //Toast.makeText(StudentLoginActivity.this, "Student Activity", Toast.LENGTH_SHORT).show();
                            final Intent intent = new Intent(StudentLoginActivity.this, RoutineActivity.class);

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

    private EditText editText_name;
    private EditText edit_Text_password;
    private TextView textView_signup;
    private Button button_login;

}
