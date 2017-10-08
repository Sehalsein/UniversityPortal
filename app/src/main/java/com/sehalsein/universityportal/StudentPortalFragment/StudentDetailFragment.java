package com.sehalsein.universityportal.StudentPortalFragment;


import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.sehalsein.universityportal.Model.CollegeDetail;
import com.sehalsein.universityportal.Model.StudentDetail;
import com.sehalsein.universityportal.Model.UserData;
import com.sehalsein.universityportal.R;

import java.util.ArrayList;
import java.util.UUID;

import gun0912.tedbottompicker.TedBottomPicker;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentDetailFragment extends Fragment {

    private String studentId;
    private String imageURL;
    private static String TAG = "StudentDetailFragment";
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUserRef;
    private String USER_NODE = null;
    private TextView nameTextView;
    private TextView dobTextView;
    private TextView emailTextView;
    private TextView courseTextView;
    private ImageView imageView;
    private PermissionListener permissionlistener;
    private static String STUDENT_NODE ;
    private StorageReference storageRef;
    private FirebaseStorage storage;
    private StudentDetail studentDetail;
    private Button uploadImageButton;

    public StudentDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_student_detail, container, false);
        STUDENT_NODE = getResources().getString(R.string.firebase_databse_node_student);
        nameTextView = layout.findViewById(R.id.nameTextView);
        dobTextView = layout.findViewById(R.id.dobTextView);
        emailTextView = layout.findViewById(R.id.emailTextView);
        courseTextView = layout.findViewById(R.id.courseTextView);
        imageView = layout.findViewById(R.id.imageView);
        studentId = UserData.studentId;

        mDatabase = FirebaseDatabase.getInstance();
        mUserRef = mDatabase.getReference(STUDENT_NODE);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        getStudentDetail(studentId);

        uploadImageButton = layout.findViewById(R.id.chooseFileButton);

        permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(getActivity())
                        .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                            @Override
                            public void onImageSelected(Uri uri) {
                                makeToast("Image Selected");
                                uploadImage(uri);
                            }
                        })
                        .create();

                tedBottomPicker.show(getFragmentManager());
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
//                Snackbar.make(fab, "Permission Denied", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                makeToast("Permission Denied");
            }
        };

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TedPermission.with(getActivity())
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("Permission Denied")
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();
            }
        });

        return layout;
    }

    private void getStudentDetail(final String userId) {
        mUserRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                studentDetail = dataSnapshot.getValue(StudentDetail.class);
                //makeToast(userId);
                updateUI(studentDetail);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void uploadImage(Uri uri) {
        StorageReference filePath = storageRef.child(STUDENT_NODE).child(studentId).child(UUID.randomUUID().toString() + "");
        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                makeToast("Image Upload Successful");
                imageURL = taskSnapshot.getDownloadUrl().toString();
                loadImage(imageURL);
                updateDatabsewithFileURL();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                makeToast("Image Upload Failed");
            }
        });
    }
    private void updateDatabsewithFileURL(){
        studentDetail.setProfileUrl(imageURL);
        mUserRef.child(studentId).setValue(studentDetail);
    }

    private void loadImage(String url){
        Glide.with(getActivity()).load(url).fitCenter().crossFade(100).into(imageView);
    }

    private void makeToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void updateUI(StudentDetail data){
        nameTextView.setText(data.getName());
        courseTextView.setText(data.getCourse());
        //dobTextView.setText(data.getCourse());
        emailTextView.setText(data.getEmailId());
        if(data.getProfileUrl() != null){
            loadImage(data.getProfileUrl());
        }
    }

}
