package com.sehalsein.universityportal.StudentPortalFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sehalsein.universityportal.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentDetailFragment extends Fragment {


    public StudentDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_detail, container, false);
    }

}
