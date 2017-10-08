package com.sehalsein.universityportal.FacultyPortalFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sehalsein.universityportal.Adapter.MoreAdapter;
import com.sehalsein.universityportal.Model.MoreOption;
import com.sehalsein.universityportal.R;

import java.util.ArrayList;
import java.util.List;

public class FacultyMoreOption extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private RecyclerView recyclerView;

    public FacultyMoreOption() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_faculty_more_option, container, false);


        recyclerView = layout.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new MoreAdapter(createList(), getActivity()));


        return layout;
    }

    private List<MoreOption> createList() {
        List<MoreOption> result = null;
        try {

            String title[] = {
                    "Notification",
                    "Files",
                    "Add Student",
                    "Time Table",
                    "Logout",
            };

            String description[] = {
                    "Send Notifications to Student",
                    "Send Files to Student",
                    "Add new Student",
                    "Upload Time Table for department",
                    "",
            };

            String code[] = {
                    "FacultyAddNotify",
                    "FacultyAddFile",
                    "FacultyAddStudent",
                    "FacultyAddTimeTable",
                    "Logout",
            };

            int icons[] = {
                    R.drawable.ic_notifications_black_24dp,
                    R.drawable.ic_picture_as_pdf_black_24dp,
                    R.drawable.ic_face_black_24dp,
                    R.drawable.ic_view_comfy_black_24dp,
                    R.drawable.ic_exit_to_app_black_24dp,
            };



            result = new ArrayList<MoreOption>();
            for (int i = 0; i < title.length ; i++) {
                MoreOption moreOption = new MoreOption(title[i],description[i%description.length],icons[i%icons.length]);
                moreOption.setCode(code[i]);
                result.add(moreOption);
            }
        } catch (Exception e) {
            System.out.print(e);
        }
        return result;
    }

}
