package com.monika.universityportal.StudentPortalActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.gun0912.tedpermission.PermissionListener;
import com.monika.universityportal.Activity.ViewImageActivity;
import com.monika.universityportal.FacultyPortalActivity.FacultyUploadTimeTable;
import com.monika.universityportal.Model.ImagesDetail;
import com.monika.universityportal.Model.UserData;
import com.monika.universityportal.R;

public class StudentViewTimeTable extends AppCompatActivity {


    private static final String IMAGE_KEY = "ImageURL";
    private String imageURL;
    private ImageView imageView;
    //private MaterialBetterSpinner courseSpinner;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private ImagesDetail imagesDetail;
    private String topic;
    private static String STUDENT_FILE_NODE;
    private StorageReference storageRef;
    private FirebaseStorage storage;
    private String collegeId;
    private PermissionListener permissionlistener;

    private String course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view_time_table);


        course = UserData.studentCourse;

        collegeId = UserData.collegeId;
        STUDENT_FILE_NODE = getResources().getString(R.string.firebase_databse_node_time_table);
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference(STUDENT_FILE_NODE);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        imageURL = getIntent().getStringExtra(IMAGE_KEY);
        imageView = findViewById(R.id.imageView);
        // courseSpinner = findViewById(R.id.course_better_spinner);


        if (imageURL != null) {
            loadImage(imageURL);
        } else {
            getTimeTable();
        }

        topic = course;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewImage(imageURL);
            }
        });


        //makeToast(course);
    }

    private void getTimeTable() {
        //makeToast(collegeId);
        //makeToast(course);
        mRef.child(collegeId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ImagesDetail imagesDetail = snapshot.getValue(ImagesDetail.class);
                    if(imagesDetail.getTopic().equals(course)) {
                        loadImage(imagesDetail.getImageURL());
                        imageURL = imagesDetail.getImageURL();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadImage(String url) {
        Glide.with(StudentViewTimeTable.this).load(url).fitCenter().crossFade(100).into(imageView);
    }


    private void makeToast(String message) {
        Toast.makeText(StudentViewTimeTable.this, message, Toast.LENGTH_SHORT).show();
    }

    private void viewImage(String URL) {
        Intent intent = new Intent(StudentViewTimeTable.this, ViewImageActivity.class);
        intent.putExtra(IMAGE_KEY, URL);
        startActivity(intent);
    }
}
