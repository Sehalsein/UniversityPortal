package com.monika.universityportal.Model;

import java.io.Serializable;

/**
 * Created by sehalsein on 28/09/17.
 */

public class MoreOption implements Serializable {
    private String title;
    private String description;
    private String code;
    private int image;

    public MoreOption() {
    }

    public MoreOption(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public MoreOption(String title, String description, int image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getImage() {
        return image;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
