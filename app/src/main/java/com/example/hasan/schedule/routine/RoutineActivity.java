package com.example.hasan.schedule.routine;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.hasan.schedule.R;
import com.example.hasan.schedule.Routine;
import com.example.hasan.schedule.models.CourseModel;
import com.example.hasan.schedule.models.RoutineModel;
import com.example.hasan.schedule.models.TeacherModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RoutineActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private List<Routine> routineList;

    private RecyclerView recyclerView;
    private RoutineListAdapter routineListAdapter;
    private LinearLayoutManager linearLayoutManager;

    private int batchId = 12;
    private int dayOfWeek;
    private int dayId = 2;

    private FirebaseDatabase firebaseDatabase;

    private DatabaseReference databaseReference;
    private DatabaseReference dbChildReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);

        dashNameTextView = findViewById(R.id.dashboard_name);
        dashRegTextView = findViewById(R.id.dashboard_reg);

        recyclerView = findViewById(R.id.routine_recyclerview);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        routineList = new ArrayList<>();

        String userName = getIntent().getStringExtra("user_name");
        dashNameTextView.setText(userName);

        Date date = new Date();
        //Log.d("date", String.valueOf(date));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        Log.d("day", String.valueOf(dayOfWeek));

        if(dayOfWeek < 7){
            dayId = dayOfWeek+1;
        } else {
            dayId = 1;
        }

        //for dayofweek
        Spinner spinnerDayId = findViewById(R.id.dayOfWeek);
        ArrayAdapter<CharSequence> adapterDayId = ArrayAdapter.createFromResource(this,
                R.array.nameOfDayOfWeek,
                android.R.layout.simple_spinner_item);
        spinnerDayId.setSelection(dayId);

        adapterDayId.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDayId.setAdapter(adapterDayId);
        spinnerDayId.setOnItemSelectedListener(this);

        //for batch
        Spinner spinnerBatchName = findViewById(R.id.batch);
        ArrayAdapter<CharSequence> adapterBatchName = ArrayAdapter.createFromResource(this,
                R.array.batch_name,
                android.R.layout.simple_spinner_item);
        adapterBatchName.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBatchName.setAdapter(adapterBatchName);
        spinnerBatchName.setOnItemSelectedListener(this);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(RoutineActivity.this));


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        getListFromFirebase(String.valueOf(batchId),String.valueOf(dayId));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()){
            case R.id.dayOfWeek:
                dayId= position+1;
                Log.d("DayID", String.valueOf(dayId));
                break;
            case R.id.batch:
                batchId = position+1;
                Log.d("BatchId", String.valueOf(batchId));
                break;
        }

        routineList.clear();
        routineListAdapter = new RoutineListAdapter(RoutineActivity.this,routineList);
        recyclerView.setAdapter(routineListAdapter);

        getListFromFirebase(String.valueOf(batchId),String.valueOf(dayId));


    }

    private void getListFromFirebase(String batchId,String dayId) {

        routineList = new ArrayList<>();
        databaseReference = firebaseDatabase.getReference("routine");
        Query queryReference = databaseReference
                .orderByChild("batch_day_id")
                .equalTo(String.valueOf(batchId+"_"+dayId));

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
                    dbChildReference.child("course").child(String.valueOf(courseNode-1))
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    Log.d("Course", dataSnapshot.toString());

                                    CourseModel courseModel = dataSnapshot.getValue(CourseModel.class);

                                    if (courseModel != null) {
                                        routine.setCourceCode(courseModel.getCourseCode());

                                        int teacherNode = Integer.parseInt(routineModel.getTchrId());

                                        dbChildReference = firebaseDatabase.getReference();
                                        dbChildReference.child("teacher").child(String.valueOf(teacherNode-1))
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


    private TextView dashNameTextView;
    private TextView dashRegTextView;
}
