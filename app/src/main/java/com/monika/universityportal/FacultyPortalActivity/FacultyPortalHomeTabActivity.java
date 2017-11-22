package com.monika.universityportal.FacultyPortalActivity;

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

import com.monika.universityportal.FacultyPortalFragment.FacultyCollegeDetail;
import com.monika.universityportal.FacultyPortalFragment.FacultyFilesList;
import com.monika.universityportal.FacultyPortalFragment.FacultyMoreOption;
import com.monika.universityportal.FacultyPortalFragment.FacultyNotificationList;
import com.monika.universityportal.R;

public class FacultyPortalHomeTabActivity extends AppCompatActivity {

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
                case R.id.navigation_files:
                    changeFragment(2);
                    bottomTabIndexNo = 2;
                    setupActionBar(getNavTitle(bottomTabIndexNo));
                    return true;
                case R.id.navigation_more_option:
                    changeFragment(3);
                    bottomTabIndexNo = 3;
                    setupActionBar(getNavTitle(bottomTabIndexNo));
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_portal);

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
                return "College Detail";
            case 1 :
                return "Notification";
            case 3 :
                return "More Option";
            case 2 :
                return "Files";
            default:
                return "Not Available";
        }
    }

    private void makeToast(String message){
        Toast.makeText(FacultyPortalHomeTabActivity.this,message,Toast.LENGTH_SHORT).show();
    }

    private void changeFragment(int position) {

        Fragment newFragment = null;

        if (position == 0) {
            newFragment = new FacultyCollegeDetail();
        } else if (position == 1) {
            newFragment = new FacultyNotificationList();
        } else if (position == 2) {
            newFragment = new FacultyFilesList();
        } else {
            newFragment = new FacultyMoreOption();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, newFragment);
        fragmentTransaction.commit();

    }
}
