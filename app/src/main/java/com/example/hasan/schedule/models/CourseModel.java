package com.example.hasan.schedule.models;

import com.google.firebase.database.PropertyName;

public class CourseModel {

    @PropertyName("course_id")
    String courseId;
    @PropertyName("course_code")
    String courseCode;
    @PropertyName("course_title")
    String courseTitle;
    @PropertyName("batch_id")
    String batchId;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }
}
