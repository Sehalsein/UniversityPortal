package com.monika.universityportal.Model;

/**
 * Created by sehalsein on 08/10/17.
 */

public class StudentDetail {
    private String id;
    private String name;
    private String course;
    private String emailId;
    private String password;
    private String profileUrl;
    private String collegeId;


    public StudentDetail() {
    }

    public StudentDetail(String name, String course, String emailId, String password, String collegeId) {
        this.name = name;
        this.course = course;
        this.emailId = emailId;
        this.password = password;
        this.collegeId = collegeId;
    }


    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(String collegeId) {
        this.collegeId = collegeId;
    }
}
