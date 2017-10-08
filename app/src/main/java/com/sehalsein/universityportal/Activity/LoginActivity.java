package com.sehalsein.universityportal.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sehalsein.universityportal.FacultyPortalActivity.FacultyPortalHomeTabActivity;
import com.sehalsein.universityportal.Model.ImagesDetail;
import com.sehalsein.universityportal.Model.StudentDetail;
import com.sehalsein.universityportal.Model.UserData;
import com.sehalsein.universityportal.Model.UserDetail;
import com.sehalsein.universityportal.R;
import com.sehalsein.universityportal.StudentPortalActivity.StudentPortalHomeActivity;
import com.sehalsein.universityportal.UniversityPortalActivity.UniversityHomeTabActivity;
import com.sehalsein.universityportal.UniversityPortalActivity.UniversitySendFileActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private EditText emailIdEditText;
    private EditText passwordEditText;
    private FirebaseAuth mAuth;
    private static String TAG = "LoginActivity";
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUserRef;
    private DatabaseReference mStudentRef;
    private String USER_NODE = null;
    private String STUDENT_NODE = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        USER_NODE = getResources().getString(R.string.firebase_databse_node_user_detail);
        STUDENT_NODE = getResources().getString(R.string.firebase_databse_node_student);
        mDatabase = FirebaseDatabase.getInstance();
        mUserRef = mDatabase.getReference(USER_NODE);
        mStudentRef = mDatabase.getReference(STUDENT_NODE);

        emailIdEditText = findViewById(R.id.emailId_editText);
        passwordEditText = findViewById(R.id.password_editText);
    }

    public void login(View view){

        if(validate()){
            final String email= emailIdEditText.getText().toString();
            final String password = passwordEditText.getText().toString();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                UserData.emailId = email;
                                UserData.password = password;
                                getTypeOfUser(user.getUid());
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                makeToast("Authentication failed.");
                                //updateUI(null);
                            }
                        }
                    });
        }else {
            makeToast("Please fill in all the fields!");
        }
        //startActivity(new Intent(LoginActivity.this, UniversityHomeTabActivity.class));
        //startActivity(new Intent(LoginActivity.this, FacultyPortalHomeTabActivity.class));
        //startActivity(new Intent(LoginActivity.this, StudentPortalHomeActivity.class));
    }

    private void getTypeOfUser(String userId) {
        mUserRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserDetail userDetail = dataSnapshot.getValue(UserDetail.class);
                updateUI(userDetail.getType(),userDetail);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateUI(String type,UserDetail userDetail){
        switch (type){
            case "University" :
                startActivity(new Intent(LoginActivity.this, UniversityHomeTabActivity.class));
                this.finish();
                break;
            case "Faculty" :
                startActivity(new Intent(LoginActivity.this, FacultyPortalHomeTabActivity.class));
                UserData.collegeId = userDetail.getId();
                this.finish();
                break;
            case "Student" :
                getCollegeId(userDetail.getId());
                break;
            default:
                makeToast("Not a valid user!!");
        }
    }

    private void getCollegeId(String userId) {
        mStudentRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StudentDetail studentDetail = dataSnapshot.getValue(StudentDetail.class);
                UserData.collegeId = studentDetail.getCollegeId();
                UserData.studentId = studentDetail.getId();
                UserData.studentCourse = studentDetail.getCourse();
                startActivity(new Intent(LoginActivity.this, StudentPortalHomeActivity.class));
                LoginActivity.this.finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private boolean validate() {
        boolean emailIdCheck;
        boolean passwordCheck;
        if (isEmpty(emailIdEditText)) {
            emailIdEditText.setError("Enter Email Id");
            emailIdCheck = false;
        } else {
            if (isValidEmail(emailIdEditText.getText().toString())) {
                emailIdCheck = true;
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
        }

        if (emailIdCheck && passwordCheck) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void makeToast(String message){
        Toast.makeText(LoginActivity.this,message,Toast.LENGTH_SHORT).show();
    }
}
