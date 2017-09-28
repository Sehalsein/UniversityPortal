package com.sehalsein.universityportal.Model;

/**
 * Created by sehalsein on 28/09/17.
 */

public class CollegeDetail {
    private String id;
    private String collegeName;
    private String collegeAddress;

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
