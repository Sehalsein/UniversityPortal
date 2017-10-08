package com.sehalsein.universityportal.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sehalsein.universityportal.R;

public class CollegeDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_detail);
        Button chooseFile = findViewById(R.id.chooseFileButton);
        chooseFile.setVisibility(View.GONE);
    }
}
