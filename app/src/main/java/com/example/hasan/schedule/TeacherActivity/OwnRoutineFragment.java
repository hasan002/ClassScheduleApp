package com.example.hasan.schedule.TeacherActivity;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.example.hasan.schedule.R;
import com.example.hasan.schedule.Routine;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class OwnRoutineFragment extends Fragment implements AdapterView.OnItemSelectedListener{


    private List<Routine> routineList;

    private RecyclerView recyclerView;
    private RoutineListAdapter routineListAdapter;
    private LinearLayoutManager linearLayoutManager;

    private NiceSpinner spinnerDayId;


    private int dayOfWeek;
    private int dayId;


    private String userId;
    private String teacherId;

    private FirebaseDatabase firebaseDatabase;

    private DatabaseReference databaseReference;
    private DatabaseReference dbChildReference;


    private Context context;

    public OwnRoutineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_own_routine, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = view.getContext();


        recyclerView = view.findViewById(R.id.wtc_routine_recyclerview);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        userId = getArguments().getString("user_id");

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
        spinnerDayId = view.findViewById(R.id.wtc_dayOfWeek);
        List<String> listDayId = new LinkedList<>(dayList);
        spinnerDayId.attachDataSource(listDayId);
        spinnerDayId.setSelectedIndex(dayId);
        spinnerDayId.setOnItemSelectedListener(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        getTeacherData();

    }

    private void getTeacherData() {


        firebaseDatabase = FirebaseDatabase.getInstance();
        dbChildReference = firebaseDatabase.getReference();
        dbChildReference.child("teacherData").child(userId)
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Log.d("teacherData", dataSnapshot.toString());

                        TeacherDataModel teacherDataModel = dataSnapshot.getValue(TeacherDataModel.class);

                        if (teacherDataModel != null) {

                            teacherId = teacherDataModel.getTchrId();
                            getListFromFirebase(String.valueOf(dayId));

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("OnCancelled"," teacherData :"+ databaseError.getMessage());
                    }
                });


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

                                                            routineListAdapter = new RoutineListAdapter(context, routineList);
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

        dayId = position;

        Log.d("DayID", String.valueOf(dayId));
//        Log.d("BatchId", String.valueOf(batchId));

        routineList.clear();
        routineListAdapter = new RoutineListAdapter(context, routineList);
        recyclerView.setAdapter(routineListAdapter);

        getListFromFirebase(String.valueOf(dayId));


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
