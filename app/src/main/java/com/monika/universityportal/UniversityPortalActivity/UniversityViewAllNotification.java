package com.monika.universityportal.UniversityPortalActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.monika.universityportal.Activity.NotificationActivity;
import com.monika.universityportal.Adapter.NotificationAdapter;
import com.monika.universityportal.Model.NotificationDetail;
import com.monika.universityportal.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UniversityViewAllNotification extends AppCompatActivity {

    private String collegeId;
    private String topic;
    private static final String COLLEGE_KEY = "CollegeID";

    private RecyclerView recyclerView;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private static String NODE = null;
    private List<NotificationDetail> notificationDetailList =  new ArrayList<>();
    private RelativeLayout emptyView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university_view_all_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NODE = getResources().getString(R.string.firebase_databse_node_college_notification);
        collegeId = getIntent().getStringExtra(COLLEGE_KEY);


        emptyView = findViewById(R.id.empty_view);
        emptyView.setVisibility(View.INVISIBLE);


        myRef = database.getReference(NODE);

        //Inititalizing Recycler View
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(UniversityViewAllNotification.this, LinearLayoutManager.VERTICAL, false));

        loadNotification();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification(collegeId);
            }
        });
    }

    private void loadNotification(){

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notificationDetailList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    NotificationDetail notificationDetail = snapshot.getValue(NotificationDetail.class);
                    if(notificationDetail.getTopic().equals(collegeId) || notificationDetail.getTopic().contains("all")) {
                        notificationDetailList.add(notificationDetail);
                    }
                }
                if (!notificationDetailList.isEmpty()) {
                    emptyView.setVisibility(View.INVISIBLE);
                    Collections.reverse(notificationDetailList);
                    recyclerView.setAdapter(new NotificationAdapter(notificationDetailList, UniversityViewAllNotification.this,"University"));

                } else {
                    emptyView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                }
               // hideProgressView();
                //geometricProgressView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //hideProgressView();
                Toast.makeText(UniversityViewAllNotification.this, "Something went wrong while searching", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void makeToast(String message) {
        Toast.makeText(UniversityViewAllNotification.this, message, Toast.LENGTH_SHORT).show();
    }

    private void sendNotification(String data){
        Intent intent = new Intent(UniversityViewAllNotification.this, NotificationActivity.class);
        intent.putExtra(COLLEGE_KEY, data);
        startActivity(intent);
    }
}
