package com.example.hasan.schedule.models;

import com.google.firebase.database.PropertyName;

public class StatusModel {
    @PropertyName("status_id")
        String statusId;
    @PropertyName("ststus_name")
        String statusName;

    public String getStatusId() { return statusId; }

    public String getStatusName() { return statusName; }

    public void setStatusId(String statusId) { this.statusId = statusId; }

    public void setStatusName(String statusName) { this.statusName = statusName; }
}
