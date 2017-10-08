package com.sehalsein.universityportal.FacultyPortalFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sehalsein.universityportal.Adapter.NotificationAdapter;
import com.sehalsein.universityportal.Model.NotificationDetail;
import com.sehalsein.universityportal.R;
import com.sehalsein.universityportal.UniversityPortalActivity.UniversityViewAllNotification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FacultyNotificationList extends Fragment {

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

    public FacultyNotificationList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout =  inflater.inflate(R.layout.fragment_faculty_notification_list, container, false);

        NODE = getResources().getString(R.string.firebase_databse_node_college_notification);
        collegeId = "-Kv8BNLUTAoYMu1h5giV";


        emptyView = layout.findViewById(R.id.empty_view);
        emptyView.setVisibility(View.INVISIBLE);


        myRef = database.getReference(NODE);

        //Inititalizing Recycler View
        recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        loadNotification();

        return layout;
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
                    recyclerView.setAdapter(new NotificationAdapter(notificationDetailList, getActivity(),"Faculty"));

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
                Toast.makeText(getActivity(), "Something went wrong while searching", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void makeToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
