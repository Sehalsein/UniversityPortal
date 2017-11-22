package com.monika.universityportal.FacultyPortalFragment;


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
import com.monika.universityportal.Model.CollegeDetail;
import com.monika.universityportal.Model.UserData;
import com.monika.universityportal.R;

import java.util.ArrayList;
import java.util.UUID;

import gun0912.tedbottompicker.TedBottomPicker;

/**
 * A simple {@link Fragment} subclass.
 */
public class FacultyCollegeDetail extends Fragment {

    private String collegeId;
    private String imageURL;
    private static String TAG = "FacultyCollegeDetail";
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUserRef;
    private String USER_NODE = null;
    private TextView nameTextView;
    private TextView addressTextView;
    private TextView emailTextView;
    private ImageView imageView;
    private PermissionListener permissionlistener;
    private static String COLLEGE_NODE ;
    private StorageReference storageRef;
    private FirebaseStorage storage;
    private CollegeDetail collegeDetail;
    private Button uploadImageButton;

    public FacultyCollegeDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout =  inflater.inflate(R.layout.activity_college_detail, container, false);


        COLLEGE_NODE = getResources().getString(R.string.firebase_databse_node_colleges);
        nameTextView = layout.findViewById(R.id.nameTextView);
        addressTextView = layout.findViewById(R.id.addressTextView);
        emailTextView = layout.findViewById(R.id.emailTextView);
        imageView = layout.findViewById(R.id.imageView);
        collegeId = UserData.collegeId;

        USER_NODE = getResources().getString(R.string.firebase_databse_node_colleges);
        mDatabase = FirebaseDatabase.getInstance();
        mUserRef = mDatabase.getReference(USER_NODE);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        getCollegeDetail(collegeId);

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

    private void getCollegeDetail(final String userId) {
        mUserRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 collegeDetail = dataSnapshot.getValue(CollegeDetail.class);
                //makeToast(userId);
                updateUI(collegeDetail);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void uploadImage(Uri uri) {
        StorageReference filePath = storageRef.child(COLLEGE_NODE).child(collegeId).child(UUID.randomUUID().toString() + "");
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
        String key = mUserRef.push().getKey();
        collegeDetail.setLogoURL(imageURL);
        mUserRef.child(collegeId).setValue(collegeDetail);
    }

    private void loadImage(String url){
        Glide.with(getActivity()).load(url).fitCenter().crossFade(100).into(imageView);
    }



    private void makeToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void updateUI(CollegeDetail data){
        nameTextView.setText(data.getCollegeName());
        addressTextView.setText(data.getCollegeAddress());
        emailTextView.setText(data.getEmailId());
        if(data.getLogoURL() != null){
            loadImage(data.getLogoURL());
        }
    }

}
