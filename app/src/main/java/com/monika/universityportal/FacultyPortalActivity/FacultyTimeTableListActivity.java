package com.monika.universityportal.FacultyPortalActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.monika.universityportal.Adapter.MoreAdapter;
import com.monika.universityportal.Adapter.TimeTableAdapter;
import com.monika.universityportal.Model.MoreOption;
import com.monika.universityportal.R;

import java.util.ArrayList;
import java.util.List;

public class FacultyTimeTableListActivity extends AppCompatActivity {


    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_time_table_list);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(FacultyTimeTableListActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new TimeTableAdapter(createList(), FacultyTimeTableListActivity.this));

    }

    private List<MoreOption> createList() {
        List<MoreOption> result = null;
        try {

            String title[] = getResources().getStringArray(R.array.all_course_available);

            String description[] = {
                    "",
                    "",
                    "",
                    "",
                    "",
            };

            String code[] = getResources().getStringArray(R.array.all_course_available);

            int icons[] = {
                    R.drawable.ic_view_comfy_black_24dp,
                    R.drawable.ic_view_comfy_black_24dp,
                    R.drawable.ic_view_comfy_black_24dp,
                    R.drawable.ic_view_comfy_black_24dp,
                    R.drawable.ic_view_comfy_black_24dp,
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
