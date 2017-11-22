package com.monika.universityportal.UniversityPortalFragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetMenuDialog;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.monika.universityportal.Activity.CollegeDetailActivity;
import com.monika.universityportal.Model.CollegeDetail;
import com.monika.universityportal.R;
import com.monika.universityportal.UniversityPortalActivity.UniversityViewAllFiles;
import com.monika.universityportal.UniversityPortalActivity.UniversityViewAllNotification;
import com.monika.universityportal.UniversityPortalActivity.UniverstyAddCollege;
import com.monika.universityportal.ViewHolder.CollegeListViewHolder;


public class UniversityCollegeList extends Fragment {

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;

    private static final int MOBILE_COLOUM_COUNT_POTRAIT = 1;
    private static final int MOBILE_COLOUM_COUNT_LANDSCAPE = 2;
    private static final int TABLET_COLOUM_COUNT_POTRAIT = 2;
    private static final int TABLET_COLOUM_COUNT_LANDSCAPE = 3;
    private static final String COLLEGE_KEY = "CollegeID";

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mAgentRef;
    private String COLLEGE_NODE = null;
    private RelativeLayout emptyView;
    private TextView description;
    private FirebaseRecyclerAdapter mAdapter;

    public UniversityCollegeList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_university_college_list, container, false);

        emptyView = layout.findViewById(R.id.empty_view);
        emptyView.setVisibility(View.INVISIBLE);
        description = layout.findViewById(R.id.description_title);
        recyclerView = layout.findViewById(R.id.recycler_view);

        COLLEGE_NODE = getResources().getString(R.string.firebase_databse_node_colleges);
        mDatabase = FirebaseDatabase.getInstance();
        mAgentRef = mDatabase.getReference(COLLEGE_NODE);

        FloatingActionButton fab = layout.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), UniverstyAddCollege.class));
            }
        });

        childExist();
        setRecyclerView();
        return layout;
    }

    private void setRecyclerView(){
        gridLayoutManager = new GridLayoutManager(getActivity(), getSpanCount());
        recyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new FirebaseRecyclerAdapter<CollegeDetail, CollegeListViewHolder>(
                CollegeDetail.class,
                R.layout.card_college_list,
                CollegeListViewHolder.class,
                mAgentRef) {
            @Override
            public void populateViewHolder(CollegeListViewHolder holder, final CollegeDetail collegeDetail, int position) {
                holder.setRow(collegeDetail);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewCollegeDetail(collegeDetail);
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        BottomSheetMenuDialog dialog = new BottomSheetBuilder(getActivity(), R.style.AppTheme_BottomSheetDialog)
                                .expandOnStart(true)
                                .setMode(BottomSheetBuilder.MODE_LIST)
                                .setMenu(R.menu.college_list_bottom_menu)
                                .setItemClickListener(new BottomSheetItemClickListener() {
                                    @Override
                                    public void onBottomSheetItemClick(MenuItem item) {
                                        switch (item.getItemId()) {
                                            case R.id.college_option_view:
                                                //makeToast("View");
                                                viewCollegeDetail(collegeDetail);
                                                break;
                                            case R.id.college_option_send_notification:
                                                //makeToast("Notification");
                                                sendNotification(collegeDetail);
                                                break;
                                            case R.id.college_option_send_file:
                                                //makeToast("File");
                                                viewImagesPage(collegeDetail);
                                                break;
//                                            case R.id.college_option_delete:
//                                                //makeToast("Delete");
//                                                break;
//                                            case R.id.college_option_block:
//                                                makeToast("Block");
//                                                break;
                                        }
                                    }
                                })
                                .createDialog();
                        dialog.show();
                        return true;
                    }
                });
            }
        };
        recyclerView.setAdapter(mAdapter);
    }

    private void makeToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void childExist() {
        mAgentRef.limitToLast(1).addValueEventListener(new ValueEventListener() {
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
            description.setText("No Colleges Available");
        }
    }

    private int getSpanCount() {

        int orientation = getActivity().getResources().getConfiguration().orientation;
        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                return checkIsTablet() ? TABLET_COLOUM_COUNT_POTRAIT : MOBILE_COLOUM_COUNT_POTRAIT;
            case Configuration.ORIENTATION_LANDSCAPE:
                return checkIsTablet() ? TABLET_COLOUM_COUNT_LANDSCAPE : MOBILE_COLOUM_COUNT_LANDSCAPE;
            default:
                return 1;
        }
    }

    private void viewCollegeDetail(CollegeDetail data) {
        Intent intent = new Intent(getContext(), CollegeDetailActivity.class);
        intent.putExtra(COLLEGE_KEY, data.getId());
        startActivity(intent);
    }

    private void viewImagesPage(CollegeDetail data) {
        Intent intent = new Intent(getContext(), UniversityViewAllFiles.class);
        intent.putExtra(COLLEGE_KEY, data.getId());
        startActivity(intent);
    }

    private void sendNotification(CollegeDetail data){
        Intent intent = new Intent(getContext(), UniversityViewAllNotification.class);
        intent.putExtra(COLLEGE_KEY, data.getId());
        startActivity(intent);
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