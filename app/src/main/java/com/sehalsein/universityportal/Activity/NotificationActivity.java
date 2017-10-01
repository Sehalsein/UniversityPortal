package com.sehalsein.universityportal.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sehalsein.universityportal.Model.NotificationDetail;
import com.sehalsein.universityportal.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationActivity extends AppCompatActivity {

    private EditText titleEditText;
    private NotificationDetail notificationDetail;
    private EditText messageEditText;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mCollegeNotificationRef;
    private String COLLEGE_NOTIFICATION_NODE = null;

    private String collegeId;
    private String topic;
    private static final String COLLEGE_KEY = "CollegeID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        COLLEGE_NOTIFICATION_NODE = getResources().getString(R.string.firebase_databse_node_notification);
        mDatabase = FirebaseDatabase.getInstance();
        mCollegeNotificationRef = mDatabase.getReference(COLLEGE_NOTIFICATION_NODE);

        collegeId = getIntent().getStringExtra(COLLEGE_KEY);
        if(collegeId != null) {
            topic = collegeId;
        }else{
            topic = "all";
        }
        titleEditText = findViewById(R.id.notification_title_edit_text);
        messageEditText = findViewById(R.id.notification_message_edit_text);
    }

    public void sendNotification(View view){

        //showProgressDialog();
        if (validate()) {
            //Toast.makeText(this, "timestamp"+ notifyDetail.getTimeStamp(), Toast.LENGTH_SHORT).show();
            updateDatabsewithNotification();
        } else {
            //hideProgressDialog();
            Toast.makeText(this, "Please fill in all the information.", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean validate() {
        String title, message ;
        if (titleEditText.length() > 0) {
            title = String.valueOf(titleEditText.getText());
        } else {
            title = null;
        }

        if (messageEditText.length() > 0) {
            message = String.valueOf(messageEditText.getText());
        } else {
            message = null;
        }

        if (title != null && message != null) {
            notificationDetail = new NotificationDetail(title, message, getCurrentTimeStamp(), topic);
            return true;
        } else {
            return false;
        }
    }

    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    private void updateDatabsewithNotification() {
        String key = mCollegeNotificationRef.push().getKey();
        notificationDetail.setId(key);
        mCollegeNotificationRef.child(key).setValue(notificationDetail);
        this.finish();
    }

}
