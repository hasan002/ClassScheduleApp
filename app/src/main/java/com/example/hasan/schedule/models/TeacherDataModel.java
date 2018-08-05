package com.example.hasan.schedule.models;

import com.google.firebase.database.PropertyName;


public class TeacherDataModel {

    @PropertyName("tchr_id")
    String tchrId;
    @PropertyName("tchr_name")
    String tchrName;

    public String getTchrId() {
        return tchrId;
    }

    public String getTchrName() {
        return tchrName;
    }

    public void setTchrId(String tchrId) {
        this.tchrId = tchrId;
    }

    public void setTchrName(String tchrName) {
        this.tchrName = tchrName;
    }
}
