package com.monika.universityportal.UniversityPortalFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.monika.universityportal.Adapter.MoreAdapter;
import com.monika.universityportal.Model.MoreOption;
import com.monika.universityportal.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UniversityMoreOption extends Fragment {

    private RecyclerView recyclerView;

    public UniversityMoreOption() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_university_more_option, container, false);


        recyclerView = layout.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new MoreAdapter(createList(), getActivity()));


        return layout;
    }

    private List<MoreOption> createList() {
        List<MoreOption> result = null;
        try {

            String title[] = {
                    "Add College",
                    "Notification",
                    "Files",
                    "Logout",
            };

            String description[] = {
                    "Add new Colleges",
                    "Send Notifications to all College",
                    "Send Files to all College",
                    "",
            };

            String code[] = {
                    "UniAddCollege",
                    "UniAddNotify",
                    "UniAddFile",
                    "Logout",
            };

            int icons[] = {
                    R.drawable.ic_school_black_24dp,
                    R.drawable.ic_notifications_black_24dp,
                    R.drawable.ic_picture_as_pdf_black_24dp,
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
