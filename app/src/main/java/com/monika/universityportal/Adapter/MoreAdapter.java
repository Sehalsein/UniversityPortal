package com.monika.universityportal.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.monika.universityportal.Activity.CollegeDetailActivity;
import com.monika.universityportal.Activity.LoginActivity;
import com.monika.universityportal.Activity.NotificationActivity;
import com.monika.universityportal.FacultyPortalActivity.FacultyAddStudentActivity;
import com.monika.universityportal.FacultyPortalActivity.FacultySendFIleActivity;
import com.monika.universityportal.FacultyPortalActivity.FacultyTimeTableListActivity;
import com.monika.universityportal.Model.MoreOption;
import com.monika.universityportal.Model.UserData;
import com.monika.universityportal.R;
import com.monika.universityportal.StudentPortalActivity.StudentViewAllFiles;
import com.monika.universityportal.StudentPortalActivity.StudentViewTimeTable;
import com.monika.universityportal.UniversityPortalActivity.UniversitySendFileActivity;
import com.monika.universityportal.UniversityPortalActivity.UniverstyAddCollege;

import java.util.List;

/**
 * Created by sehalsein on 28/09/17.
 */

public class MoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MoreOption> moreOptionList;
    private Context context;
    private static final String PORTAL_KEY = "portal";
    private static final String COLLEGE_KEY = "CollegeID";

    public MoreAdapter(List<MoreOption> moreOptionList, Context context) {
        this.moreOptionList = moreOptionList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_more_option, parent, false);
        return new MoreOptionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MoreOption item = moreOptionList.get(position);
        MoreOptionViewHolder moreOptionViewHolder = (MoreOptionViewHolder) holder;

        moreOptionViewHolder.setRow(item);
        String code[] = {
                "FacultyAddNotify",
                "FacultyAddFile",
                "FacultyAddTimeTable",
                "Logout",
        };
        moreOptionViewHolder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (item.getCode()) {
                    case "UniAddCollege":
                        context.startActivity(new Intent(context, UniverstyAddCollege.class));
                        break;
                    case "Logout":
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(context.getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    case "UniAddFile":
                        context.startActivity(new Intent(context, UniversitySendFileActivity.class));
                        break;
                    case "UniAddNotify":
                        context.startActivity(new Intent(context, NotificationActivity.class));
                        break;
                    case "FacultyAddNotify":
                        sendNotification();
                        break;
                    case "FacultyAddStudent":
                        context.startActivity(new Intent(context, FacultyAddStudentActivity.class));
                        break;
                   case "FacultyAddFile":
                        context.startActivity(new Intent(context, FacultySendFIleActivity.class));
                        break;
                    case "FacultyAddTimeTable":
                        context.startActivity(new Intent(context, FacultyTimeTableListActivity.class));
                        break;
                    case "StudentViewFile":
                        context.startActivity(new Intent(context, StudentViewAllFiles.class));
                        break;
                    case "StudentCollegeDetail":
                        viewCollegeDetail();
                        break;
                    case "StudentViewTimeTable":
                        context.startActivity(new Intent(context, StudentViewTimeTable.class));
                        break;
                    default:
                        makeToast("Comming Soon");
                }
            }
        });


    }

    private void sendNotification() {
        Intent intent = new Intent(context, NotificationActivity.class);
        intent.putExtra(PORTAL_KEY, "Faculty");
        context.startActivity(intent);
    }

    private void viewCollegeDetail() {
        Intent intent = new Intent(context, CollegeDetailActivity.class);
        intent.putExtra(COLLEGE_KEY, UserData.collegeId);
        context.startActivity(intent);
    }

    private void makeToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return moreOptionList.size();
    }

    public class MoreOptionViewHolder extends RecyclerView.ViewHolder {

        private TextView moreOptionTitle;
        private TextView moreOptionDescription;
        private ImageView moreOptionIcon;
        private View row;

        public MoreOptionViewHolder(View itemView) {
            super(itemView);
            moreOptionTitle = itemView.findViewById(R.id.more_option_title);
            moreOptionDescription = itemView.findViewById(R.id.more_option_description);
            moreOptionIcon = itemView.findViewById(R.id.more_option_icon_image_view);
            this.row = itemView;
        }

        public void setRow(MoreOption data) {
            this.moreOptionTitle.setText(data.getTitle());
            if (data.getDescription().equals("")) {
                this.moreOptionDescription.setVisibility(View.GONE);
            } else {
                this.moreOptionDescription.setVisibility(View.VISIBLE);
                this.moreOptionDescription.setText(data.getDescription());
            }
            this.moreOptionIcon.setImageResource(data.getImage());

        }
    }
}
