package com.example.hasan.schedule;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hasan.schedule.TeacherActivity.TeacherActivity;
import com.example.hasan.schedule.models.StudentModel;
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

public class TeacherLoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference dbChildReference;


    String userId;
    String tc_email;
    String tc_password;
    String tc_varificationCode;
    String varificationCode = "l";
    String tchrId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);


        editText_name = findViewById(R.id.edtTxt_tc_login_name);
        edit_Text_password = findViewById(R.id.edtTxt_tc_login_password);
        edit_Text_varification_code = findViewById(R.id.edtTxt_tc_varification_code);


        button_login = findViewById(R.id.btn_tc_login);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


       /* FirebaseUser user = firebaseAuth.getCurrentUser();


        if (user != null) {

            Intent intent;
            intent = new Intent(TeacherLoginActivity.this, TeacherActivity.class);
            intent.putExtra("user_id", user.getUid());
            startActivity(intent);
            finish();
        }
*/
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tc_email = editText_name.getText().toString().trim();
                tc_password = edit_Text_password.getText().toString().trim();
                tc_varificationCode = edit_Text_varification_code.getText().toString().trim();

                Log.d("tc_VCode", tc_varificationCode);

                if (tc_email.isEmpty()) {

                    editText_name.setError("Enter your email address.");

                } else if (tc_password.isEmpty()) {

                    edit_Text_password.setError("Enter your password.");

                } else if (tc_varificationCode.isEmpty()) {

                    edit_Text_varification_code.setError("Enter your varification code.");

                } else {

                    firebaseAuth.signInWithEmailAndPassword(tc_email, tc_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        userId = user.getUid();

                                        if (user != null) {

                                            getUserVarificationCode();

                                        }

                                    } else {

                                        Toast.makeText(TeacherLoginActivity.this, "" + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }


    private void getUserVarificationCode() {


        firebaseDatabase = FirebaseDatabase.getInstance();
        dbChildReference = firebaseDatabase.getReference();

        dbChildReference.child("teacherData").child(userId)
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Log.d("teacherData", dataSnapshot.toString());

                        TeacherDataModel teacherDataModel = dataSnapshot.getValue(TeacherDataModel.class);

                        if (teacherDataModel != null) {
                            tchrId = teacherDataModel.getTchrId();
                            varificationCode = teacherDataModel.getVarificationCode();
                            Log.d("varificaion Code", varificationCode);

                            if (varificationCode.equals(tc_varificationCode)) {


                                Intent intent = new Intent(TeacherLoginActivity.this, TeacherActivity.class);
                                intent.putExtra("user_id", userId);
                                startActivity(intent);
                                finish();
                            } else {

                                Toast.makeText(TeacherLoginActivity.this, "varification code is not correct",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("OnCancelled", " teacherData :" + databaseError.getMessage());
                    }
                });


    }


    private EditText editText_name;
    private EditText edit_Text_password;
    private EditText edit_Text_varification_code;
    private Button button_login;

}
