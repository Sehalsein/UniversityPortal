package com.sehalsein.universityportal.FacultyPortalFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sehalsein.universityportal.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FacultyCollegeDetail extends Fragment {


    public FacultyCollegeDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout =  inflater.inflate(R.layout.activity_college_detail, container, false);

        return layout;
    }

}
