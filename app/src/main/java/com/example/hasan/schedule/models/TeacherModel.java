package com.example.hasan.schedule.models;

import com.google.firebase.database.PropertyName;

public class TeacherModel {

    @PropertyName("tchr_id")
    String tchrId;
    @PropertyName("tchr_name")
    String tchrName;
    @PropertyName("tchr_email")
    String tchrEmail;
    @PropertyName("nick_name")
    String nickName;
    @PropertyName("tchr_title")
    String tchrTitle;
    @PropertyName("tchr_password")
    String tchrPassword;

    public String getTchrId() {
        return tchrId;
    }

    public void setTchrId(String tchrId) {
        this.tchrId = tchrId;
    }

    public String getTchrName() {
        return tchrName;
    }

    public void setTchrName(String tchrName) {
        this.tchrName = tchrName;
    }

    public String getTchrEmail() {
        return tchrEmail;
    }

    public void setTchrEmail(String tchrEmail) {
        this.tchrEmail = tchrEmail;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getTchrTitle() {
        return tchrTitle;
    }

    public void setTchrTitle(String tchrTitle) {
        this.tchrTitle = tchrTitle;
    }

    public String getTchrPassword() {
        return tchrPassword;
    }

    public void setTchrPassword(String tchrPassword) {
        this.tchrPassword = tchrPassword;
    }
}
