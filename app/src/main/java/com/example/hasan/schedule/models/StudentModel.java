package com.example.hasan.schedule.models;

import com.google.firebase.database.PropertyName;

public class StudentModel {

    @PropertyName("std_id")
    String stdId;
    @PropertyName("std_name")
    String stdName;
    @PropertyName("std_email")
    String stdEmail;
    @PropertyName("register_no")
    String registerNo;
    @PropertyName("year")
    String year;
    @PropertyName("semester")
    String semester;
    @PropertyName("section")
    String section;

    public String getStdId() { return stdId; }

    public String getStdName() { return stdName; }

    public String getStdEmail() { return stdEmail; }

    public String getRegisterNo() { return registerNo; }

    public String getYear() { return year; }

    public String getSemester() { return semester; }

    public String getSection() { return section; }

    public void setStdId(String stdId) { this.stdId = stdId; }

    public void setStdName(String stdName) { this.stdName = stdName; }

    public void setStdEmail(String stdEmail) { this.stdEmail = stdEmail; }

    public void setRegisterNo(String registerNo) { this.registerNo = registerNo; }

    public void setYear(String year) { this.year = year; }

    public void setSemester(String semester) { this.semester = semester; }

    public void setSection(String section) { this.section = section; }

}


