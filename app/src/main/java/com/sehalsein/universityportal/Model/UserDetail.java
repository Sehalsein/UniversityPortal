package com.sehalsein.universityportal.Model;

/**
 * Created by sehalsein on 08/10/17.
 */

public class UserDetail {
    private String id;
    private String name;
    private String emailId;
    private String type;

    public UserDetail() {
    }

    public UserDetail(String id, String name, String emailId, String type) {
        this.id = id;
        this.name = name;
        this.emailId = emailId;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
