package com.sehalsein.universityportal.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sehalsein.universityportal.Model.CollegeDetail;
import com.sehalsein.universityportal.Model.UserDetail;
import com.sehalsein.universityportal.R;

public class CollegeDetailActivity extends AppCompatActivity {

    private static final String COLLEGE_KEY = "CollegeID";
    private String collegeId;
    private static String TAG = "LoginActivity";
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUserRef;
    private String USER_NODE = null;
    private TextView nameTextView;
    private TextView addressTextView;
    private TextView emailTextView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_detail);
        Button chooseFile = findViewById(R.id.chooseFileButton);
        chooseFile.setVisibility(View.GONE);

        nameTextView = findViewById(R.id.nameTextView);
        addressTextView = findViewById(R.id.addressTextView);
        emailTextView = findViewById(R.id.emailTextView);
        imageView = findViewById(R.id.imageView);

        USER_NODE = getResources().getString(R.string.firebase_databse_node_colleges);
        mDatabase = FirebaseDatabase.getInstance();
        mUserRef = mDatabase.getReference(USER_NODE);

        collegeId = getIntent().getStringExtra(COLLEGE_KEY);
        getCollegeDetail(collegeId);

    }

    private void getCollegeDetail(String userId) {
        mUserRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CollegeDetail userDetail = dataSnapshot.getValue(CollegeDetail.class);
                updateUI(userDetail);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void updateUI(CollegeDetail data){
        nameTextView.setText(data.getCollegeName());
        addressTextView.setText(data.getCollegeAddress());
        emailTextView.setText(data.getEmailId());
        if(data.getLogoURL() != null){
            Glide.with(CollegeDetailActivity.this).load(data.getLogoURL()).fitCenter().crossFade(100).into(imageView);
        }
    }

}
