package com.sehalsein.universityportal.Model;

import java.io.Serializable;

/**
 * Created by sehalsein on 28/09/17.
 */

public class ImagesDetail implements Serializable {

    private String imageURL;

    public ImagesDetail() {
    }

    public ImagesDetail(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
