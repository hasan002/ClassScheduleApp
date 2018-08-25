package com.example.hasan.schedule;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hasan.schedule.routine.RoutineActivity;
import com.example.hasan.schedule.routine.RoutineListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


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
        editText_password = findViewById(R.id.edtTxt_password);
        editText_confirm_password = findViewById(R.id.edtTxt_confirm_passsword);


        button_save = findViewById(R.id.btn_save);


        ArrayList<String> yearList = new ArrayList<>();

        yearList.add("year");
        yearList.add("1");
        yearList.add("2");
        yearList.add("3");
        yearList.add("4");

        //for dayofweek
        spinnerYearId = findViewById(R.id.year);
        List<String> listYearId = new LinkedList<>(yearList);
        spinnerYearId.attachDataSource(listYearId);
        spinnerYearId.setOnItemSelectedListener(this);
        ArrayList<String> semesterList = new ArrayList<>();

        semesterList.add("semester");
        semesterList.add("1");
        semesterList.add("2");

        //for dayofweek
        spinnerSemesterId = findViewById(R.id.semester);
        List<String> listSemesterId = new LinkedList<>(semesterList);
        spinnerSemesterId.attachDataSource(listSemesterId);
        spinnerSemesterId.setOnItemSelectedListener(this);


        ArrayList<String> sactionList = new ArrayList<>();

        sactionList.add("saction");
        sactionList.add("A");
        sactionList.add("B");
        sactionList.add("Null");


        //for dayofweek
        spinnerSactionId = findViewById(R.id.saction);
        List<String> listSactionId = new LinkedList<>(sactionList);
        spinnerSactionId.attachDataSource(listSactionId);
        spinnerSactionId.setOnItemSelectedListener(this);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference();


        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = editText_name.getText().toString().trim();
                final String email = editText_email.getText().toString().trim();
                final String register = editText_register_no.getText().toString().trim();

                final String year = String.valueOf(yearId);
                final String semester = String.valueOf(semesterId);
                final String section = String.valueOf(sactionId);

                String password = editText_password.getText().toString().trim();
                String confirm_password = editText_confirm_password.getText().toString();


                if (name.isEmpty()) {

                    editText_name.setError("Please enter your username");

                } else if (email.isEmpty()) {

                    editText_email.setError("Please enter a email address.");

                } else if (register.isEmpty()) {

                    editText_register_no.setError("Please enter register no.");

                } else if (yearId == 0) {

                    spinnerYearId.setError("Please enter your year.");

                }  else if (semesterId == 0) {

                    spinnerSemesterId.setError("Please enter your semester.");

                } else if (sactionId == 0) {

                    spinnerSactionId.setError("Enter value 1 or 2.");

                } else if (password.isEmpty()) {

                    editText_password.setError("Please enter a password");

                } else if (confirm_password.isEmpty()) {

                    editText_confirm_password.setError("Please confirm your password");

                } else if (!password.equals(confirm_password)) {

                    editText_confirm_password.setError("Your passwords do not match.");

                } else if (password.length() < 6) {

                    editText_password.setError("Please ensure your password is at least 8 digits");

                } else if (password.equals(confirm_password)) {

                    int x = 0;

                    if (x == 0) {

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

                                                if (year.equals("2") || year.equals("1")) {

                                                    studentData.put("section", section);

                                                } else {

                                                    studentData.put("section", "C");
                                                }

                                                studentChild.setValue(studentData);

                                                Intent intent = new Intent(RegisterActivity.this, RoutineActivity.class);
                                                intent.putExtra("user_name", name);
                                                intent.putExtra("user_id", user.getUid());

                                                Log.d("user_name", name);
                                                Log.d("user_id", user.getUid());

                                                startActivity(intent);
                                            }

                                        } else {
                                            Toast.makeText(RegisterActivity.this, "" + task.getException(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }
                }
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.year:
                yearId = position;
                break;

            case R.id.semester:
                semesterId = position;
                break;
            case R.id.saction:
                sactionId = position;
                break;
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private EditText editText_name;
    private EditText editText_email;
    private EditText editText_register_no;
    private EditText editText_password;
    private EditText editText_confirm_password;

    private NiceSpinner spinnerYearId;
    private NiceSpinner spinnerSemesterId;
    private NiceSpinner spinnerSactionId;

    int yearId = 0;
    int semesterId = 0;
    int sactionId = 0;

    private Button button_save;
}
