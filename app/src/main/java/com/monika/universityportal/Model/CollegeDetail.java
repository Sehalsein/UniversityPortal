package com.monika.universityportal.Model;

/**
 * Created by sehalsein on 28/09/17.
 */

public class CollegeDetail {
    private String id;
    private String collegeName;
    private String collegeAddress;
    private String emailId;
    private String password;
    private String logoURL;

    public CollegeDetail() {
    }

    public CollegeDetail(String collegeName, String collegeAddress) {
        this.collegeName = collegeName;
        this.collegeAddress = collegeAddress;
    }

    public CollegeDetail(String id, String collegeName, String collegeAddress) {
        this.id = id;
        this.collegeName = collegeName;
        this.collegeAddress = collegeAddress;
    }

    public CollegeDetail(String collegeName, String collegeAddress, String emailId, String password) {
        this.collegeName = collegeName;
        this.collegeAddress = collegeAddress;
        this.emailId = emailId;
        this.password = password;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getCollegeAddress() {
        return collegeAddress;
    }

    public void setCollegeAddress(String collegeAddress) {
        this.collegeAddress = collegeAddress;
    }
}
