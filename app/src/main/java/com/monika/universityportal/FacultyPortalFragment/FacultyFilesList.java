package com.monika.universityportal.FacultyPortalFragment;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.monika.universityportal.Activity.ViewImageActivity;
import com.monika.universityportal.Adapter.GalleryAdapter;
import com.monika.universityportal.FacultyPortalActivity.FacultySendFIleActivity;
import com.monika.universityportal.Model.ImagesDetail;
import com.monika.universityportal.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FacultyFilesList extends Fragment {


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


    public FacultyFilesList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout =  inflater.inflate(R.layout.fragment_faculty_files_list, container, false);

        //collegeId = getIntent().getStringExtra(COLLEGE_KEY);
        collegeId = "-Kv8BNLUTAoYMu1h5giV";

//        makeToast(collegeId);
        emptyView = layout.findViewById(R.id.empty_view);
        emptyView.setVisibility(View.INVISIBLE);
        description = layout.findViewById(R.id.description_title);
        recyclerView = layout.findViewById(R.id.recycler_view);
        COLLEGE_NODE = getResources().getString(R.string.firebase_databse_node_colleges);
        COLLEGE_IMAGES_NODE = getResources().getString(R.string.firebase_databse_node_colleges_files);
        mDatabase = FirebaseDatabase.getInstance();
        mCollegeFileRef = mDatabase.getReference(COLLEGE_IMAGES_NODE);
        //mCollegeRef = mDatabase.getReference(COLLEGE_NODE).child(collegeId);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        childExist();
        setRecyclerView();

        return  layout;
    }

    private void setRecyclerView() {
        gridLayoutManager = new GridLayoutManager(getActivity(), getSpanCount());
        recyclerView.setLayoutManager(gridLayoutManager);
        loadfiles();
//        mAdapter = new FirebaseRecyclerAdapter<ImagesDetail, GalleryImagesViewHolder>(
//                ImagesDetail.class,
//                R.layout.card_image_view,
//                GalleryImagesViewHolder.class,
//                mCollegeFileRef) {
//            @Override
//            public void populateViewHolder(GalleryImagesViewHolder holder, final ImagesDetail imagesDetail, int position) {
//                Glide.with(getActivity()).load(imagesDetail.getImageURL()).fitCenter().crossFade(100).into(holder.imageView);
//
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        viewImage(imagesDetail.getImageURL());
//                    }
//                });
//
//                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View view) {
//                        sendImage(imagesDetail.getImageURL());
//                        makeToast("AS");
//                        return true;
//                    }
//                });
//            }
//
//
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
                    recyclerView.setAdapter(new GalleryAdapter(imagesDetailList, getActivity(),"Faculty"));

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
    private void sendImage(String URL){
        Intent intent = new Intent(getActivity(), FacultySendFIleActivity.class);
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

    private void viewImage(String URL){
        Intent intent = new Intent(getActivity(), ViewImageActivity.class);
        intent.putExtra(IMAGE_KEY, URL);
        startActivity(intent);
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


    private void makeToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        gridLayoutManager.setSpanCount(getSpanCount());
        recyclerView.setLayoutManager(gridLayoutManager);
        mAdapter.notifyDataSetChanged();
    }

    private boolean checkIsTablet() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        float widthInches = metrics.widthPixels / metrics.xdpi;
        float heightInches = metrics.heightPixels / metrics.ydpi;
        double diagonalInches = Math.sqrt(Math.pow(widthInches, 2) + Math.pow(heightInches, 2));
        return diagonalInches >= 7.0;
    }

}
