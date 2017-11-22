package com.monika.universityportal.UniversityPortalActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.monika.universityportal.Activity.ViewImageActivity;
import com.monika.universityportal.Model.ImagesDetail;
import com.monika.universityportal.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import gun0912.tedbottompicker.TedBottomPicker;

public class UniversitySendFileActivity extends AppCompatActivity {


    private static final String IMAGE_KEY = "ImageURL";
    private String imageURL;
    private ImageView imageView;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private ImagesDetail imagesDetail;
    private String topic;
    private static String COLLEGE_FILE_NODE;
    private StorageReference storageRef;
    private FirebaseStorage storage;
    private String collegeId = "Sample College";
    private PermissionListener permissionlistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university_send_file);

        COLLEGE_FILE_NODE = getResources().getString(R.string.firebase_databse_node_colleges_files);
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference(COLLEGE_FILE_NODE);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        imageView = findViewById(R.id.imageView);
        if(imageURL != null) {
            loadImage(imageURL);
        }
        topic = "all";

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageURL != null) {
                    viewImage(imageURL);
                }
            }
        });

        permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(UniversitySendFileActivity.this)
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
//                Snackbar.make(fab, "Permission Denied", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                makeToast("Permission Denied");
            }


        };
    }

    private void loadImage(String url){
        Glide.with(UniversitySendFileActivity.this).load(url).fitCenter().crossFade(100).into(imageView);
    }

    public void uploadFile(View view){
        if(validate()){
            makeToast("Upload Successful");
            updateDatabsewithFileURL();

        }else {
            makeToast("Please upload an image to proceed");
        }
    }

    private void updateDatabsewithFileURL(){
        String key = mRef.push().getKey();
        imagesDetail.setId(key);
        mRef.child(key).setValue(imagesDetail);
        this.finish();
    }

    public void chooseFile(View view){
        TedPermission.with(UniversitySendFileActivity.this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("Permission Denied")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private void uploadImage(Uri uri) {
        StorageReference filePath = storageRef.child(COLLEGE_FILE_NODE).child(UUID.randomUUID().toString() + "");
        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                makeToast("Image Upload Successful");
                imageURL = taskSnapshot.getDownloadUrl().toString();
                loadImage(imageURL);
                //updateDatabaseWithImage(taskSnapshot.getDownloadUrl().toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                makeToast("Image Upload Failed");
            }
        });
    }

    private boolean validate() {
        boolean URL;
        if (imageURL != null) {
            URL = true;
        } else {
            URL = false;
        }
        if (URL) {
            imagesDetail = new ImagesDetail(imageURL, getCurrentTimeStamp(), topic);
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

    private void makeToast(String message){
        Toast.makeText(UniversitySendFileActivity.this,message,Toast.LENGTH_SHORT).show();
    }

    private void viewImage(String URL){
        Intent intent = new Intent(UniversitySendFileActivity.this, ViewImageActivity.class);
        intent.putExtra(IMAGE_KEY, URL);
        startActivity(intent);
    }
}
