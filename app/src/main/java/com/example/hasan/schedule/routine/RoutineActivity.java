package com.example.hasan.schedule.routine;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.hasan.schedule.LoginActivity;
import com.example.hasan.schedule.R;
import com.example.hasan.schedule.models.Routine;
import com.example.hasan.schedule.models.CourseModel;
import com.example.hasan.schedule.models.RoutineModel;
import com.example.hasan.schedule.models.StudentModel;
import com.example.hasan.schedule.models.TeacherModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class RoutineActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Toolbar toolbar;

    private List<Routine> routineList;

    private RecyclerView recyclerView;
    private RoutineListAdapter routineListAdapter;
    private LinearLayoutManager linearLayoutManager;

    private NiceSpinner spinnerDayId;
    private NiceSpinner spinnerBatchId;

    private int batchId = 12;
    private int dayOfWeek;
    private int dayId;

    private String userId;
    private String userName;
    private String registerNo;
    private String year;
    private String semester;
    private String section;
    private int userBatchId;


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference dbChildReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);

        if (getIntent() != null) {
            userName = getIntent().getStringExtra("user_name");
            userId = getIntent().getStringExtra("user_id");

            Log.d("user_name", userName);
            Log.d("user_id", userId);

        }


        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);


        dashNameTextView = findViewById(R.id.dashboard_name);
        dashRegTextView = findViewById(R.id.dashboard_reg);

        recyclerView = findViewById(R.id.routine_recyclerview);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        routineList = new ArrayList<>();

        dashNameTextView.setText(userName);


        Date date = new Date();
        //Log.d("date", String.valueOf(date));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);


        if (dayOfWeek < 6) {
            dayId = dayOfWeek + 1;
        } else {
            dayId = dayOfWeek % 6;
        }


        Log.d("batchId", String.valueOf(batchId));

        ArrayList<String> dayList = new ArrayList<>();

        dayList.add("Friday");
        dayList.add("Saturday");
        dayList.add("Sunday");
        dayList.add("Monday");
        dayList.add("Tuesday");
        dayList.add("Wednesday");
        dayList.add("Thusday");

        //for dayofweek
        spinnerDayId = findViewById(R.id.dayOfWeek);
        List<String> listDayId = new LinkedList<>(dayList);
        spinnerDayId.attachDataSource(listDayId);
        spinnerDayId.setSelectedIndex(dayId);
        spinnerDayId.setOnItemSelectedListener(this);

        //for batch
        spinnerBatchId = findViewById(R.id.batch);
        List<String> listBatchName = new LinkedList<>(Arrays.asList("1/1A", "1/1B", "1/2A", "1/2B",
                "2/1A", "2/1B", "2/2A", "2/2B", "3/1", "3/2", "4/1", "4/2"));
        spinnerBatchId.attachDataSource(listBatchName);
        spinnerBatchId.setOnItemSelectedListener(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(RoutineActivity.this));


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        getUserData();
    }

    private int getUserBatchId() {

        if (section.equals("C")) {

            return 2 * Integer.parseInt(year) + Integer.parseInt(semester) + 2;

        } else {

            int x = 0;

            if (section.equals("A")) {

                x = 2;
            } else if (section.equals("B")) {

                x = 1;
            }

            return 4 * Integer.parseInt(year) + 2 * Integer.parseInt(semester) - 3 - x;
        }
    }

    private void getUserData() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbChildReference = firebaseDatabase.getReference();
        dbChildReference.child("students").child(userId)
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Log.d("Student", dataSnapshot.toString());

                        StudentModel studentModel = dataSnapshot.getValue(StudentModel.class);

                        if (studentModel != null) {

                            registerNo = studentModel.getRegisterNo();
                            year = studentModel.getYear();
                            semester = studentModel.getSemester();
                            section = studentModel.getSection();

                            Log.d("reg ", registerNo);
                            Log.d("year", year);
                            Log.d("semester", semester);
                            Log.d("section", section);

                            userBatchId = getUserBatchId();
                            batchId = userBatchId;
                            spinnerBatchId.setSelectedIndex(batchId - 1);

                            Log.d("userBatchId", String.valueOf(userBatchId));

                            dashRegTextView.setText(registerNo);
                            getListFromFirebase(String.valueOf(batchId), String.valueOf(dayId));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("OnCancelled", databaseError.getMessage());
                    }
                });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.dayOfWeek:
                dayId = position;
                break;

            case R.id.batch:
                batchId = position + 1;
                break;
        }

        Log.d("DayID", String.valueOf(dayId));
        Log.d("BatchId", String.valueOf(batchId));

        routineList.clear();
        routineListAdapter = new RoutineListAdapter(RoutineActivity.this, routineList);
        recyclerView.setAdapter(routineListAdapter);

        getListFromFirebase(String.valueOf(batchId), String.valueOf(dayId));


    }


    private void getListFromFirebase(String batchId, String dayId) {

        routineList = new ArrayList<>();
        databaseReference = firebaseDatabase.getReference("routine");
        Query queryReference = databaseReference
                .orderByChild("batch_day_id")
                .equalTo(String.valueOf(batchId + "_" + dayId));

        queryReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                final Routine routine = new Routine();
                final RoutineModel routineModel = dataSnapshot.getValue(RoutineModel.class);

                if (routineModel != null) {

                    routine.setRoutineId(routineModel.getRoutineId());
                    routine.setStatusId(routineModel.getStatusId());
                    routine.setTime(routineModel.getTime());
                    routine.setCourceCode(routineModel.getCourceId());
                    routine.setTchrCode(routineModel.getTchrId());


                    int courseNode = Integer.parseInt(routineModel.getCourceId());

                    dbChildReference = firebaseDatabase.getReference();
                    dbChildReference.child("course").child(String.valueOf(courseNode - 1))
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    Log.d("Course", dataSnapshot.toString());

                                    CourseModel courseModel = dataSnapshot.getValue(CourseModel.class);

                                    if (courseModel != null) {
                                        routine.setCourceCode(courseModel.getCourseCode());

                                        int teacherNode = Integer.parseInt(routineModel.getTchrId());

                                        dbChildReference = firebaseDatabase.getReference();
                                        dbChildReference.child("teacher").child(String.valueOf(teacherNode - 1))
                                                .addValueEventListener(new ValueEventListener() {

                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                        Log.d("Teacher", dataSnapshot.toString());

                                                        TeacherModel teacherModel = dataSnapshot.getValue(TeacherModel.class);

                                                        if (teacherModel != null) {
                                                            routine.setTchrCode(teacherModel.getNickName());

                                                            routineList.add(routine);

                                                            routineListAdapter = new RoutineListAdapter(
                                                                    RoutineActivity.this, routineList);
                                                            recyclerView.setAdapter(routineListAdapter);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        Log.d("OnCancelled", databaseError.getMessage());
                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.d("OnCancelled", databaseError.getMessage());
                                }
                            });
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("OnCancelled", databaseError.getMessage());
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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


    private TextView dashNameTextView;
    private TextView dashRegTextView;
}
