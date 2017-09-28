package com.sehalsein.universityportal.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.sehalsein.universityportal.Model.CollegeDetail;
import com.sehalsein.universityportal.R;

/**
 * Created by sehalsein on 28/09/17.
 */

public class CollegeListViewHolder extends RecyclerView.ViewHolder{

    private TextView collegeName;
    private TextView collegeDescription;
    private View row;

    private String getLetter(String name) {
        return String.valueOf(name.charAt(0));
    }

    public CollegeListViewHolder(View itemView) {
        super(itemView);
        collegeName = itemView.findViewById(R.id.college_name_text_view);
        collegeDescription = itemView.findViewById(R.id.college_description_text_view);
        this.row = itemView;
    }

    public void setRow(CollegeDetail data) {
        collegeName.setText(data.getCollegeName());
        collegeDescription.setText(data.getCollegeAddress());
    }
}