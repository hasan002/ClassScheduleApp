package com.example.hasan.schedule.models;

import com.google.firebase.database.PropertyName;

public class BatchModel {
    @PropertyName("batch_id")
    String batchId;
    @PropertyName("batch_year")
    String batchYear;
    @PropertyName("batch_semester")
    String batchSemester;
    @PropertyName("batch_section")
    String batchSection;

    public String getBatchId() { return batchId; }

    public String getBatchYear() { return batchYear; }

    public String getBatchSemester() { return batchSemester; }

    public String getBatchSection() { return batchSection; }

    public void setBatchId(String batchId) { this.batchId = batchId; }

    public void setBatchYear(String batchYear) { this.batchYear = batchYear; }

    public void setBatchSemester(String batchSemester) { this.batchSemester = batchSemester; }

    public void setBatchSection(String batchSection) { this.batchSection = batchSection; }

}
