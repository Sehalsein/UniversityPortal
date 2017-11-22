package com.monika.universityportal.Model;

import java.io.Serializable;

/**
 * Created by sehalsein on 28/09/17.
 */

public class ImagesDetail implements Serializable {

    private String id;
    private String imageURL;
    private String timeStamp;
    private String topic;


    public ImagesDetail() {
    }

    public ImagesDetail(String id, String imageURL, String timeStamp, String topic) {
        this.id = id;
        this.imageURL = imageURL;
        this.timeStamp = timeStamp;
        this.topic = topic;
    }

    public ImagesDetail(String imageURL, String timeStamp, String topic) {
        this.imageURL = imageURL;
        this.timeStamp = timeStamp;
        this.topic = topic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
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
