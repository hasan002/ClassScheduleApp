package com.example.hasan.schedule.models;


import com.google.firebase.database.PropertyName;

public class RoutineModel {

    @PropertyName("routine_id")
    String routineId;
    @PropertyName("tchr_id")
    String tchrId;
    @PropertyName("batch_id")
    String batchId;
    @PropertyName("cource_id")
    String courceId;
    @PropertyName("status_id")
    String statusId;
    @PropertyName("time")
    String time;
    @PropertyName("working_day_id")
    String workingDayId;

    public String getRoutineId() {
        return routineId;
    }

    public void setRoutineId(String routineId) {
        this.routineId = routineId;
    }

    public String getTchrId() {
        return tchrId;
    }

    public void setTchrId(String tchrId) {
        this.tchrId = tchrId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getCourceId() {
        return courceId;
    }

    public void setCourceId(String courceId) {
        this.courceId = courceId;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWorkingDayId() {
        return workingDayId;
    }

    public void setWorkingDayId(String workingDayId) {
        this.workingDayId = workingDayId;
    }
}
