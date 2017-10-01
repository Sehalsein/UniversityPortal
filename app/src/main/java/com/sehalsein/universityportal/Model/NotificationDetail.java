package com.sehalsein.universityportal.Model;

/**
 * Created by sehalsein on 01/10/17.
 */

public class NotificationDetail {

    private String id;
    private String title;
    private String message;
    private String timeStamp;
    private String topic;

    public NotificationDetail() {
    }

    public NotificationDetail(String title, String message, String timeStamp, String topic) {
        this.title = title;
        this.message = message;
        this.timeStamp = timeStamp;
        this.topic = topic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
}
