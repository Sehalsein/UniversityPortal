package com.sehalsein.universityportal.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sehalsein.universityportal.FacultyPortalActivity.FacultyPortalHomeTabActivity;
import com.sehalsein.universityportal.R;
import com.sehalsein.universityportal.StudentPortalActivity.StudentPortalHomeActivity;
import com.sehalsein.universityportal.UniversityPortalActivity.UniversityHomeTabActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view){
        //startActivity(new Intent(LoginActivity.this, UniversityHomeTabActivity.class));
        startActivity(new Intent(LoginActivity.this, FacultyPortalHomeTabActivity.class));
        //startActivity(new Intent(LoginActivity.this, StudentPortalHomeActivity.class));
    }
}
