package com.sehalsein.universityportal.UniversityPortalActivity;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.sehalsein.universityportal.Activity.ViewImageActivity;
import com.sehalsein.universityportal.Adapter.GalleryAdapter;
import com.sehalsein.universityportal.Model.ImagesDetail;
import com.sehalsein.universityportal.R;
import com.sehalsein.universityportal.StudentPortalActivity.StudentViewAllFiles;
import com.sehalsein.universityportal.ViewHolder.GalleryImagesViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import gun0912.tedbottompicker.TedBottomPicker;

public class UniversityViewAllFiles extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private String collegeId;
    private FirebaseDatabase mDatabase;
    private static final String COLLEGE_KEY = "CollegeID";
    private static final String IMAGE_KEY = "ImageURL";
    private DatabaseReference mCollegeFileRef;
    private String COLLEGE_NODE = null;
    private String COLLEGE_IMAGES_NODE = null;
    private RelativeLayout emptyView;
    private TextView description;
    private FirebaseRecyclerAdapter mAdapter;
    private StorageReference storageRef;
    private FirebaseStorage storage;
    private List<ImagesDetail> imagesDetailList =  new ArrayList<>();

    private static final int MOBILE_COLOUM_COUNT_POTRAIT = 3;
    private static final int MOBILE_COLOUM_COUNT_LANDSCAPE = 5;
    private static final int TABLET_COLOUM_COUNT_POTRAIT = 5;
    private static final int TABLET_COLOUM_COUNT_LANDSCAPE = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university_view_all_files);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        collegeId = getIntent().getStringExtra(COLLEGE_KEY);

        //makeToast(collegeId);
        emptyView = findViewById(R.id.empty_view);
        emptyView.setVisibility(View.INVISIBLE);
        description = findViewById(R.id.description_title);
        recyclerView = findViewById(R.id.recycler_view);
        COLLEGE_NODE = getResources().getString(R.string.firebase_databse_node_colleges);
        COLLEGE_IMAGES_NODE = getResources().getString(R.string.firebase_databse_node_colleges_files);
        mDatabase = FirebaseDatabase.getInstance();
        mCollegeFileRef = mDatabase.getReference(COLLEGE_IMAGES_NODE);
        //mCollegeRef = mDatabase.getReference(COLLEGE_NODE).child(collegeId);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();


        final FloatingActionButton fab = findViewById(R.id.fab);

        final PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(UniversityViewAllFiles.this)
                        .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                            @Override
                            public void onImageSelected(Uri uri) {
                                makeToast("Image Selected");
                                uploadImage(uri);
                            }
                        })
                        .create();

                tedBottomPicker.show(getSupportFragmentManager());
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Snackbar.make(fab, "Permission Denied", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }


        };

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TedPermission.with(UniversityViewAllFiles.this)
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("Permission Denied")
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();
            }
        });

        childExist();
        setRecyclerView();

    }

    private void uploadImage(Uri uri) {
        StorageReference filePath = storageRef.child(COLLEGE_NODE).child(collegeId).child(UUID.randomUUID().toString() + "");
        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                makeToast("Image Upload Successful");
                updateDatabaseWithImage(taskSnapshot.getDownloadUrl().toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                makeToast("Image Upload Failed");
            }
        });
    }

    private void updateDatabaseWithImage(final String url) {
        String key = mCollegeFileRef.push().getKey();
        ImagesDetail imagesDetail = new ImagesDetail(key,url,getCurrentTimeStamp(),collegeId);
        imagesDetail.setId(key);
        mCollegeFileRef.child(key).setValue(imagesDetail);
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
    private void setRecyclerView() {
        gridLayoutManager = new GridLayoutManager(UniversityViewAllFiles.this, getSpanCount());
        recyclerView.setLayoutManager(gridLayoutManager);
        loadfiles();
//        mAdapter = new FirebaseRecyclerAdapter<ImagesDetail, GalleryImagesViewHolder>(
//                ImagesDetail.class,
//                R.layout.card_image_view,
//                GalleryImagesViewHolder.class,
//                mCollegeFileRef) {
//            @Override
//            public void populateViewHolder(GalleryImagesViewHolder holder, final ImagesDetail imagesDetail, int position) {
//                Glide.with(UniversityViewAllFiles.this).load(imagesDetail.getImageURL()).fitCenter().crossFade(100).into(holder.imageView);
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

        mCollegeFileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imagesDetailList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ImagesDetail imagesDetail = snapshot.getValue(ImagesDetail.class);
                    if(imagesDetail.getTopic().equals(collegeId) || imagesDetail.getTopic().contains("all")) {
                        imagesDetailList.add(imagesDetail);
                    }
                }
                if (!imagesDetailList.isEmpty()) {
                    emptyView.setVisibility(View.INVISIBLE);
                    Collections.reverse(imagesDetailList);
                    recyclerView.setAdapter(new GalleryAdapter(imagesDetailList, UniversityViewAllFiles.this));

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
                Toast.makeText(UniversityViewAllFiles.this, "Something went wrong while searching", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void viewImage(String URL){
        Intent intent = new Intent(UniversityViewAllFiles.this, ViewImageActivity.class);
        intent.putExtra(IMAGE_KEY, URL);
        startActivity(intent);
    }

    private void childExist() {
        mCollegeFileRef.limitToLast(1).addValueEventListener(new ValueEventListener() {
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
        Toast.makeText(UniversityViewAllFiles.this, message, Toast.LENGTH_SHORT).show();
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
