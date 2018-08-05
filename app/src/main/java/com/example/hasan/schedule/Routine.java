package com.example.hasan.schedule;

public class Routine {

    private String routineId;
    private String tchrCode;
    private String courceCode;
    private String statusId;
    private String time;
    private String batchId;

    public String getRoutineId() {
        return routineId;
    }

    public void setRoutineId(String routineId) {
        this.routineId = routineId;
    }

    public String getTchrCode() {
        return tchrCode;
    }

    public void setTchrCode(String tchrCode) {
        this.tchrCode = tchrCode;
    }

    public String getCourceCode() {
        return courceCode;
    }

    public void setCourceCode(String courceCode) {
        this.courceCode = courceCode;
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

    public String getBatchId() { return batchId; }

    public void setBatchId(String batchId) { this.batchId = batchId; }
}
