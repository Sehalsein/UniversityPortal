package com.monika.universityportal.UniversityPortalActivity;

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
import com.monika.universityportal.Model.CollegeDetail;
import com.monika.universityportal.Model.UserData;
import com.monika.universityportal.Model.UserDetail;
import com.monika.universityportal.R;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UniverstyAddCollege extends AppCompatActivity {

    private EditText collegeNameEditText;
    private EditText collegeAddressEditText;
    private EditText passwordEditText;
    private EditText emailIdEditText;
    private CollegeDetail collegeDetail;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mCollegeRef;
    private DatabaseReference mUserRef;
    private final static String TAG = "UniverstyAddCollege";
    private String COLLEGE_NODE = null;
    private String USER_NODE = null;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universty_add_college);

        COLLEGE_NODE = getResources().getString(R.string.firebase_databse_node_colleges);
        USER_NODE = getResources().getString(R.string.firebase_databse_node_user_detail);
        mDatabase = FirebaseDatabase.getInstance();
        mCollegeRef = mDatabase.getReference(COLLEGE_NODE);
        mUserRef = mDatabase.getReference(USER_NODE);
        mAuth = FirebaseAuth.getInstance();

        collegeNameEditText = findViewById(R.id.collge_name_edit_text);
        passwordEditText = findViewById(R.id.agent_password_edit_text);
        collegeAddressEditText = findViewById(R.id.college_address_edit_text);
        emailIdEditText = findViewById(R.id.college_email_id_edit_text);

        //setupActionBar("Notifications");

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


        mAuth.createUserWithEmailAndPassword(collegeDetail.getEmailId(), collegeDetail.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification();
                            updateUserDetail(user);
                            String key = user.getUid();
                            collegeDetail.setId(key);
                            mCollegeRef.child(key).setValue(collegeDetail, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if(databaseError != null){
                                        makeToast(databaseError.getMessage());
                                    }else{
                                        updateUserDetal(collegeDetail);
                                        successProgress();
                                    }

                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            makeToast("Authentication failed");
                            dialog.dismiss();
                        }
                    }
                });
    }

    private void updateUserDetal(CollegeDetail data){
        UserDetail userDetail = new UserDetail(data.getId(),data.getCollegeName(),data.getEmailId(),"Faculty");
        mUserRef.child(userDetail.getId()).setValue(userDetail);
        reAuthenticateUser();
    }

    private void updateUserDetail(FirebaseUser user){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(collegeDetail.getCollegeName())
                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });
    }

    private void reAuthenticateUser(){
        String emailId = UserData.emailId;
        String password = UserData.password;

        makeToast(emailId + "" + password);
    }

    private void successProgress() {
        new MaterialDialog.Builder(UniverstyAddCollege.this)
                .title("Successful")
                .content("College has been added")
                .iconRes(R.drawable.ic_school_black_24dp)
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
        boolean emailIdCheck ;
        boolean passwordCheck;
        String emailId = "";
        String collegeName = "";
        String collgeAddress = "";
        String password = "";

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

        if (isEmpty(emailIdEditText)) {
            emailIdEditText.setError("Enter email Id");
            emailIdCheck = false;
        } else {
            if (isValidEmail(emailIdEditText.getText().toString())) {
                emailIdCheck = true;
                emailId = emailIdEditText.getText().toString();
            } else {
                emailIdCheck = false;
                emailIdEditText.setError("Enter a valid email Id");
            }
        }
        if (isEmpty(passwordEditText)) {
            passwordEditText.setError("Generate Password");
            passwordCheck = false;
        } else {
            passwordCheck = true;
            password = passwordEditText.getText().toString();
        }
        if (collegeNameCheck && collegeAddressCheck && emailIdCheck && passwordCheck) {
            collegeDetail = new CollegeDetail(collegeName, collgeAddress, emailId,password);
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
    public void generatePassword(View view) {
        passwordEditText.setText(generatePassword(14));
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    private void makeToast(String message) {
        Toast.makeText(UniverstyAddCollege.this, message, Toast.LENGTH_SHORT).show();
    }
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    static String generatePassword(int argPasswordLength) {
        String password = "";
        Random random = new Random();
        String full = "ABCDEFGHIJKLMNOPQRESTUVWXYZabcdefgghiklmnopqrstuvwxyz1234567890!@#$%^&*()";
        while (password.length() < argPasswordLength) {
            int index = random.nextInt(full.length());
            String buffer = "" + full.charAt(index);
            password += buffer;
        }
        return password;
    }
}
