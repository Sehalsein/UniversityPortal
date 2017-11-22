package com.monika.universityportal.UniversityPortalActivity;

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

import com.monika.universityportal.R;
import com.monika.universityportal.UniversityPortalFragment.UniversityCollegeList;
import com.monika.universityportal.UniversityPortalFragment.UniversityMoreOption;

public class UniversityHomeTabActivity extends AppCompatActivity {

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
                case R.id.navigation_dashboard:
                    changeFragment(1);
                    bottomTabIndexNo = 1;
                    setupActionBar(getNavTitle(bottomTabIndexNo));
                    return true;
            }
            return false;
        }

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university_home_tab);

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
                return "College List";
            case 1 :
                return "More Option";
            default:
                return "Not Available";
        }
    }

    private void makeToast(String message){
        Toast.makeText(UniversityHomeTabActivity.this,message,Toast.LENGTH_SHORT).show();
    }
    private void changeFragment(int position) {

        Fragment newFragment = null;

        if (position == 0) {
            newFragment = new UniversityCollegeList();
        } else if (position == 1) {
            newFragment = new UniversityMoreOption();
        } else {
            newFragment = new UniversityCollegeList();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, newFragment);
        fragmentTransaction.commit();

    }
}
