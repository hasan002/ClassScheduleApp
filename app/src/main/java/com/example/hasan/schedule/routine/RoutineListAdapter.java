package com.example.hasan.schedule.routine;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hasan.schedule.R;
import com.example.hasan.schedule.models.Routine;

import java.util.List;

public class RoutineListAdapter extends RecyclerView.Adapter<RoutineListAdapter.RoutineViewHolder>{

    private Context context;
    private List<Routine> routineList;

    public RoutineListAdapter(Context context, List<Routine> routineList) {
        this.context = context;
        this.routineList = routineList;
    }

    @NonNull
    @Override
    public RoutineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_routine, parent, false);
        return new RoutineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineViewHolder holder, int position) {
        final Routine routine = routineList.get(position);

        holder.classTimeTextView.setText(routine.getTime());
        holder.courseCodeTextView.setText(routine.getCourceCode());
        holder.teacherCodeTextView.setText(routine.getTchrCode());
        holder.statusTextView.setText(routine.getStatusId());


    }

    @Override
    public int getItemCount() {
        return routineList.size();
    }

    public class RoutineViewHolder extends RecyclerView.ViewHolder{

        private TextView classTimeTextView;
        private TextView courseCodeTextView;
        private TextView teacherCodeTextView;
        private TextView statusTextView;

        public RoutineViewHolder(View itemView) {
            super(itemView);

            classTimeTextView = itemView.findViewById(R.id.item_time_text);
            courseCodeTextView = itemView.findViewById(R.id.item_c_code_text);
            teacherCodeTextView = itemView.findViewById(R.id.item_t_code_text);
            statusTextView = itemView.findViewById(R.id.item_status_text);
        }
    }
}
