package com.example.hasan.schedule.models;

import com.google.firebase.database.PropertyName;


public class TeacherDataModel {

    @PropertyName("tchr_id")
    String tchrId;
    @PropertyName("varification_code")
    String varificationCode;
    @PropertyName("tchr_name")
    String tchrName;


    public String getVarificationCode() { return varificationCode; }

    public String getTchrId() {
        return tchrId;
    }

    public String getTchrName() {
        return tchrName;
    }

    public void setVarificationCode(String varificationCode) { this.varificationCode = varificationCode; }

    public void setTchrId(String tchrId) {
        this.tchrId = tchrId;
    }

    public void setTchrName(String tchrName) {
        this.tchrName = tchrName;
    }
}
