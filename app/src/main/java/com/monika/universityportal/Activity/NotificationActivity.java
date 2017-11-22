package com.monika.universityportal.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.monika.universityportal.Model.NotificationDetail;
import com.monika.universityportal.Model.UserData;
import com.monika.universityportal.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationActivity extends AppCompatActivity {

    private EditText titleEditText;
    private NotificationDetail notificationDetail;
    private MaterialBetterSpinner courseSpinner;
    private EditText messageEditText;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private String NODE = null;

    private String collegeId;
    private String portal;
    private String topic;
    private static final String COLLEGE_KEY = "CollegeID";
    private static final String PORTAL_KEY = "portal";
    private static final String TITLE_KEY = "title";
    private static final String MESSAGE_KEY = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        collegeId = getIntent().getStringExtra(COLLEGE_KEY);
        portal = getIntent().getStringExtra(PORTAL_KEY);

        //makeToast(portal);

        titleEditText = findViewById(R.id.notification_title_edit_text);
        messageEditText = findViewById(R.id.notification_message_edit_text);
        courseSpinner = findViewById(R.id.course_better_spinner);

        String[] courseArray = getResources().getStringArray(R.array.all_course_available);
        ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, courseArray);
        courseSpinner.setAdapter(courseAdapter);

        mDatabase = FirebaseDatabase.getInstance();
        if(portal != null){
            //makeToast("FACULTY");
            titleEditText.setText(getIntent().getStringExtra(TITLE_KEY));
            messageEditText.setText(getIntent().getStringExtra(MESSAGE_KEY));
            NODE = getResources().getString(R.string.firebase_databse_node_student_notification);
            courseSpinner.setVisibility(View.VISIBLE);
            mRef = mDatabase.getReference(NODE).child(UserData.collegeId);
        }else{
            NODE = getResources().getString(R.string.firebase_databse_node_college_notification);
            courseSpinner.setVisibility(View.GONE);
            if(collegeId != null) {
                topic = collegeId;
            }else{
                topic = "all";
            }
            mRef = mDatabase.getReference(NODE);
        }






    }

    private void makeToast(String message){
        Toast.makeText(NotificationActivity.this,message,Toast.LENGTH_SHORT).show();
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
            if(portal != null){
                topic = courseSpinner.getText().toString();
                //makeToast(topic);
                if(topic == null || topic.equals("")){
                    return false;
                }
            }
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
        String key = mRef.push().getKey();
        notificationDetail.setId(key);
        mRef.child(key).setValue(notificationDetail);
        this.finish();
    }

}
