package com.sehalsein.universityportal.UniversityPortalActivity;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sehalsein.universityportal.Model.CollegeDetail;
import com.sehalsein.universityportal.R;

public class UniverstyAddCollege extends AppCompatActivity {

    private EditText collegeNameEditText;
    private EditText collegeAddressEditText;
    private CollegeDetail collegeDetail;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mCollegeRef;
    private final static String TAG = "UniverstyAddCollege";
    private String COLLEGE_NODE = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universty_add_college);

        COLLEGE_NODE = getResources().getString(R.string.firebase_databse_node_colleges);
        mDatabase = FirebaseDatabase.getInstance();
        mCollegeRef = mDatabase.getReference(COLLEGE_NODE);
        mAuth = FirebaseAuth.getInstance();

        collegeNameEditText = findViewById(R.id.collge_name_edit_text);
        collegeAddressEditText = findViewById(R.id.college_address_edit_text);

    }

    private void setupActionBar(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void createUser() {

        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Loading")
                .content("Please wait!!")
                .progress(true, 0)
                .cancelable(false)
                .progressIndeterminateStyle(true)
                .show();


        String key = mCollegeRef.push().getKey();
        collegeDetail.setId(key);
        mCollegeRef.child(key).setValue(collegeDetail, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                dialog.dismiss();
                if (databaseError != null) {
                    makeToast(databaseError.getMessage());
                } else {
                    successProgress();
                }
            }
        });


    }


    private void successProgress() {
        new MaterialDialog.Builder(UniverstyAddCollege.this)
                .title("Successfull")
                .content("College has been added")
                .iconRes(R.drawable.ic_notifications_black_24dp)
                .positiveText("Ok")
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        UniverstyAddCollege.this.finish();
                    }
                })
                .show();

    }

    private boolean validate() {
        boolean collegeNameCheck;
        boolean collegeAddressCheck;
        String collegeName = "";
        String collgeAddress = "";

        if (isEmpty(collegeNameEditText)) {
            collegeNameEditText.setError("Enter College name");
            collegeNameCheck = false;
        } else {
            collegeNameCheck = true;
            collegeName = collegeNameEditText.getText().toString();
        }

        if (isEmpty(collegeAddressEditText)) {
            collegeAddressEditText.setError("Enter Address name");
            collegeAddressCheck = false;
        } else {
            collegeAddressCheck = true;
            collgeAddress = collegeAddressEditText.getText().toString();
        }

        if (collegeNameCheck && collegeAddressCheck) {
            collegeDetail = new CollegeDetail(collegeName, collgeAddress);
            return true;
        } else {
            return false;
        }
    }

    public void addCollege(View view) {
        if (validate()) {
            createUser();
        } else {
            makeToast("Error");
        }
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    private void makeToast(String message) {
        Toast.makeText(UniverstyAddCollege.this, message, Toast.LENGTH_SHORT).show();
    }
}
