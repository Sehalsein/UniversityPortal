package com.sehalsein.universityportal.StudentPortalActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.sehalsein.universityportal.FacultyPortalActivity.FacultyPortalHomeTabActivity;
import com.sehalsein.universityportal.FacultyPortalFragment.FacultyMoreOption;
import com.sehalsein.universityportal.FacultyPortalFragment.FacultyNotificationList;
import com.sehalsein.universityportal.R;
import com.sehalsein.universityportal.StudentPortalFragment.StudentDetailFragment;
import com.sehalsein.universityportal.StudentPortalFragment.StudentMoreOption;
import com.sehalsein.universityportal.StudentPortalFragment.StudentNotificationList;

public class StudentPortalHomeActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private static int bottomTabIndexNo;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    changeFragment(0);
                    bottomTabIndexNo = 0;
                    setupActionBar(getNavTitle(bottomTabIndexNo));
                    return true;
                case R.id.navigation_notification:
                    changeFragment(1);
                    bottomTabIndexNo = 1;
                    setupActionBar(getNavTitle(bottomTabIndexNo));
                    return true;
                case R.id.navigation_more_option:
                    changeFragment(2);
                    bottomTabIndexNo = 2;
                    setupActionBar(getNavTitle(bottomTabIndexNo));
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_portal_home);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        changeFragment(bottomTabIndexNo);
        setupActionBar(getNavTitle(bottomTabIndexNo));
    }

    private void setupActionBar(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    private String getNavTitle(int index){
        switch (index){
            case 0 :
                return "Student Detail";
            case 1 :
                return "Notification";
            case 2 :
                return "More Option";
            default:
                return "Not Available";
        }
    }

    private void makeToast(String message){
        Toast.makeText(StudentPortalHomeActivity.this,message,Toast.LENGTH_SHORT).show();
    }

    private void changeFragment(int position) {

        Fragment newFragment = null;

        if (position == 0) {
            newFragment = new StudentDetailFragment();
        } else if (position == 1) {
            newFragment = new StudentNotificationList();
        } else {
            newFragment = new StudentMoreOption();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, newFragment);
        fragmentTransaction.commit();

    }
}
