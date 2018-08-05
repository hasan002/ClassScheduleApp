package com.example.hasan.schedule;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.example.hasan.schedule.models.BatchModel;
import com.example.hasan.schedule.models.CourseModel;
import com.example.hasan.schedule.models.RoutineModel;
import com.example.hasan.schedule.models.TeacherDataModel;
import com.example.hasan.schedule.models.TeacherModel;
import com.example.hasan.schedule.routine.RoutineListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class TeacherOwnRoutine extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private List<Routine> routineList;

    private RecyclerView recyclerView;
    private RoutineListAdapter routineListAdapter;
    private LinearLayoutManager linearLayoutManager;

    private NiceSpinner spinnerDayId;
    private NiceSpinner spinnerBatchId;

    private int batchId = 1;
    private int dayOfWeek;
    private int dayId;

    Button button_stdRoutine;

    private String userName;
    private String teacherTitle;
    private String teacherId;

    private FirebaseDatabase firebaseDatabase;

    private DatabaseReference databaseReference;
    private DatabaseReference dbChildReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_own_routine);


        dashNameTextView = findViewById(R.id.wtc_dashboard_name);
        dashRegTextView = findViewById(R.id.wtc_dashboard_reg);
         button_stdRoutine = findViewById(R.id.btn_std_routine);

        recyclerView = findViewById(R.id.wtc_routine_recyclerview);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        if (getIntent() != null) {

            userName = getIntent().getStringExtra("userName");
            teacherId = getIntent().getStringExtra("teacherId");
            teacherTitle = getIntent().getStringExtra("teacherTitle");

            dashNameTextView.setText(userName);
            dashRegTextView.setText(teacherTitle);



        }

        routineList = new ArrayList<>();


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

        //        Log.d("batchId", String.valueOf(batchId));

        ArrayList<String> dayList = new ArrayList<>();

        dayList.add("Friday");
        dayList.add("Saturday");
        dayList.add("Sunday");
        dayList.add("Monday");
        dayList.add("Tuesday");
        dayList.add("Wednesday");
        dayList.add("Thusday");

        //for dayofweek
        spinnerDayId = findViewById(R.id.wtc_dayOfWeek);
        List<String> listDayId = new LinkedList<>(dayList);
        spinnerDayId.attachDataSource(listDayId);
        spinnerDayId.setSelectedIndex(dayId);
        spinnerDayId.setOnItemSelectedListener(this);

//        //for batch
//        ArrayList<String> batchList = new ArrayList<>();
//
//        batchList.add("1/1A");
//        batchList.add("1/1B");
//        batchList.add("1/2A");
//        batchList.add("1/2B");
//        batchList.add("2/1A");
//        batchList.add("2/1B");
//        batchList.add("2/2A");
//        batchList.add("2/2B");
//        batchList.add("3/1");
//        batchList.add("3/2");
//        batchList.add("4/1");
//        batchList.add("4/2");
//
//
//        spinnerBatchId = findViewById(R.id.wtc_batch);
//        List<String> listBatchName = new LinkedList<>(batchList);
//        spinnerBatchId.attachDataSource(listBatchName);
//        spinnerBatchId.setSelectedIndex(batchId - 1);
//        spinnerBatchId.setOnItemSelectedListener(this);

        button_stdRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(TeacherOwnRoutine.this));


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        getListFromFirebase(String.valueOf(dayId));
    }

    private void getListFromFirebase(String dayId) {

        routineList = new ArrayList<>();
        databaseReference = firebaseDatabase.getReference("routine");
        Query queryReference = databaseReference
                .orderByChild("teacher_day_id")
                .equalTo(String.valueOf(teacherId + "_" + dayId));

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
                    routine.setTchrCode(routineModel.getBatchId());


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

                                        int batchNode = Integer.parseInt(routineModel.getBatchId());

                                        dbChildReference = firebaseDatabase.getReference();
                                        dbChildReference.child("batch").child(String.valueOf(batchNode - 1))
                                                .addValueEventListener(new ValueEventListener() {

                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                        Log.d("Teacher", dataSnapshot.toString());

                                                        BatchModel batchModel = dataSnapshot.getValue(BatchModel.class);

                                                        if (batchModel != null) {
                                                            String year =batchModel.getBatchYear();
                                                            String batchId = year+"/"+batchModel.getBatchSemester();

                                                            if(Integer.parseInt(year)<3)
                                                                batchId = batchId + batchModel.getBatchSection();

                                                            routine.setTchrCode(batchId);

                                                            routineList.add(routine);

                                                            routineListAdapter = new RoutineListAdapter(
                                                                    TeacherOwnRoutine.this, routineList);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//        switch (parent.getId()) {
//            case R.id.wtc_dayOfWeek:
//                dayId = position;
//                break;
//
//            case R.id.wtc_batch:
//                batchId = position + 1;
//                break;
//        }
        dayId = position;

        Log.d("DayID", String.valueOf(dayId));
//        Log.d("BatchId", String.valueOf(batchId));

        routineList.clear();
        routineListAdapter = new RoutineListAdapter(TeacherOwnRoutine.this, routineList);
        recyclerView.setAdapter(routineListAdapter);

        getListFromFirebase(String.valueOf(dayId));


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private TextView dashNameTextView;
    private TextView dashRegTextView;
}
