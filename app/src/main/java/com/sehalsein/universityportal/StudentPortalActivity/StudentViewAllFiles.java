package com.sehalsein.universityportal.StudentPortalActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sehalsein.universityportal.Activity.ViewImageActivity;
import com.sehalsein.universityportal.Adapter.GalleryAdapter;
import com.sehalsein.universityportal.Adapter.NotificationAdapter;
import com.sehalsein.universityportal.Model.ImagesDetail;
import com.sehalsein.universityportal.Model.NotificationDetail;
import com.sehalsein.universityportal.Model.UserData;
import com.sehalsein.universityportal.R;
import com.sehalsein.universityportal.UniversityPortalActivity.UniversityViewAllFiles;
import com.sehalsein.universityportal.ViewHolder.GalleryImagesViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentViewAllFiles extends AppCompatActivity {


    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private String collegeId;
    private FirebaseDatabase mDatabase;
    private static final String COLLEGE_KEY = "CollegeID";
    private static final String IMAGE_KEY = "ImageURL";
    private DatabaseReference mStudentFileRef;
    private String STUDENT_IMAGES_NODE = null;
    private RelativeLayout emptyView;
    private TextView description;
    private FirebaseRecyclerAdapter mAdapter;
    private StorageReference storageRef;
    private FirebaseStorage storage;
    private String courseId;
    private List<ImagesDetail> imagesDetailList =  new ArrayList<>();

    private static final int MOBILE_COLOUM_COUNT_POTRAIT = 3;
    private static final int MOBILE_COLOUM_COUNT_LANDSCAPE = 5;
    private static final int TABLET_COLOUM_COUNT_POTRAIT = 5;
    private static final int TABLET_COLOUM_COUNT_LANDSCAPE = 7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view_all_files);

        emptyView = findViewById(R.id.empty_view);
        emptyView.setVisibility(View.INVISIBLE);
        description = findViewById(R.id.description_title);
        recyclerView = findViewById(R.id.recycler_view);
        //COLLEGE_NODE = getResources().getString(R.string.firebase_databse_node_colleges);
        STUDENT_IMAGES_NODE = getResources().getString(R.string.firebase_databse_node_student_files);
        mDatabase = FirebaseDatabase.getInstance();
        mStudentFileRef = mDatabase.getReference(STUDENT_IMAGES_NODE).child(UserData.collegeId);
        //mCollegeRef = mDatabase.getReference(COLLEGE_NODE).child(collegeId);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        courseId = UserData.studentCourse;
        childExist();
        setRecyclerView();

    }

    private void setRecyclerView() {
        gridLayoutManager = new GridLayoutManager(StudentViewAllFiles.this, getSpanCount());
        recyclerView.setLayoutManager(gridLayoutManager);
        loadfiles();
//        mAdapter = new FirebaseRecyclerAdapter<ImagesDetail, GalleryImagesViewHolder>(
//                ImagesDetail.class,
//                R.layout.card_image_view,
//                GalleryImagesViewHolder.class,
//                mStudentFileRef) {
//            @Override
//            public void populateViewHolder(GalleryImagesViewHolder holder, final ImagesDetail imagesDetail, int position) {
//                Glide.with(StudentViewAllFiles.this).load(imagesDetail.getImageURL()).fitCenter().crossFade(100).into(holder.imageView);
//
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        viewImage(imagesDetail.getImageURL());
//                    }
//                });
//
//            }
//        };
//        recyclerView.setAdapter(mAdapter);
    }

    private void loadfiles(){

        mStudentFileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imagesDetailList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ImagesDetail imagesDetail = snapshot.getValue(ImagesDetail.class);
                    if(imagesDetail.getTopic().equals(courseId) || imagesDetail.getTopic().contains("All Course")) {
                        imagesDetailList.add(imagesDetail);
                    }
                }
                if (!imagesDetailList.isEmpty()) {
                    emptyView.setVisibility(View.INVISIBLE);
                    Collections.reverse(imagesDetailList);
                    recyclerView.setAdapter(new GalleryAdapter(imagesDetailList, StudentViewAllFiles.this));

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
                Toast.makeText(StudentViewAllFiles.this, "Something went wrong while searching", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void viewImage(String URL){
        Intent intent = new Intent(StudentViewAllFiles.this, ViewImageActivity.class);
        intent.putExtra(IMAGE_KEY, URL);
        startActivity(intent);
    }

    private void childExist() {
        mStudentFileRef.limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                setUI(snapshot.exists());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("Chat", "The read failed: " + error.getMessage());
            }
        });
    }

    private void setUI(Boolean exists) {
        if (exists) {
            emptyView.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            description.setText("NO FILES AVAILABLE");
        }
    }

    private int getSpanCount() {

        int orientation = getResources().getConfiguration().orientation;
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                return checkIsTablet() ? TABLET_COLOUM_COUNT_POTRAIT : MOBILE_COLOUM_COUNT_POTRAIT;
            case Configuration.ORIENTATION_LANDSCAPE:
                return checkIsTablet() ? TABLET_COLOUM_COUNT_LANDSCAPE : MOBILE_COLOUM_COUNT_LANDSCAPE;
            default:
                return 1;
        }
    }

    private void setupActionBar(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void makeToast(String message) {
        Toast.makeText(StudentViewAllFiles.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        gridLayoutManager.setSpanCount(getSpanCount());
        recyclerView.setLayoutManager(gridLayoutManager);
        mAdapter.notifyDataSetChanged();
    }

    private boolean checkIsTablet() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        float widthInches = metrics.widthPixels / metrics.xdpi;
        float heightInches = metrics.heightPixels / metrics.ydpi;
        double diagonalInches = Math.sqrt(Math.pow(widthInches, 2) + Math.pow(heightInches, 2));
        return diagonalInches >= 7.0;
    }
}
