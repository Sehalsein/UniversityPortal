package com.monika.universityportal.FacultyPortalActivity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.monika.universityportal.Model.StudentDetail;
import com.monika.universityportal.Model.UserData;
import com.monika.universityportal.Model.UserDetail;
import com.monika.universityportal.R;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FacultyAddStudentActivity extends AppCompatActivity {

    private EditText nameEditText;
    private MaterialBetterSpinner courseSpinner;
    private EditText passwordEditText;
    private EditText emailIdEditText;
    private StudentDetail studentDetail;
    private String collegeId ;
    private FirebaseDatabase mDatabase;
    private static String STUDENT_NODE;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private final static String TAG = "FacultyAddStudent";
    private String USER_NODE = null;
    private DatabaseReference mUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_add_student);

        collegeId = UserData.collegeId;
        USER_NODE = getResources().getString(R.string.firebase_databse_node_user_detail);
        STUDENT_NODE = getResources().getString(R.string.firebase_databse_node_student);
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference(STUDENT_NODE);
        mUserRef = mDatabase.getReference(USER_NODE);
        mAuth = FirebaseAuth.getInstance();
        courseSpinner = findViewById(R.id.course_better_spinner);
        nameEditText = findViewById(R.id.student_name_edit_text);
        passwordEditText = findViewById(R.id.agent_password_edit_text);
        emailIdEditText = findViewById(R.id.college_email_id_edit_text);

        String[] courseArray = getResources().getStringArray(R.array.all_course_available);
        ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, courseArray);
        courseSpinner.setAdapter(courseAdapter);
    }

    private void createUser() {

        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Loading")
                .content("Please wait!!")
                .progress(true, 0)
                .cancelable(false)
                .progressIndeterminateStyle(true)
                .show();


        mAuth.createUserWithEmailAndPassword(studentDetail.getEmailId(), studentDetail.getPassword())
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
                            studentDetail.setId(key);
                            mRef.child(key).setValue(studentDetail, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if(databaseError != null){
                                        makeToast(databaseError.getMessage());
                                    }else{
                                        updateUserDetal(studentDetail);
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

    private void updateUserDetal(StudentDetail data){
        UserDetail userDetail = new UserDetail(data.getId(),data.getName(),data.getEmailId(),"Student");
        mUserRef.child(userDetail.getId()).setValue(userDetail);
        reAuthenticateUser();
    }

    private void updateUserDetail(FirebaseUser user){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(studentDetail.getName())
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
        new MaterialDialog.Builder(FacultyAddStudentActivity.this)
                .title("Successful")
                .content("Student has been added")
                .iconRes(R.drawable.ic_face_black_24dp)
                .positiveText("Ok")
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        FacultyAddStudentActivity.this.finish();
                    }
                })
                .show();

    }
    private boolean validate() {
        boolean collegeNameCheck;
        boolean courseCheck;
        boolean emailIdCheck ;
        boolean passwordCheck;
        String emailId = "";
        String collegeName = "";
        String course = "";
        String password = "";

        if (isEmpty(nameEditText)) {
            nameEditText.setError("Enter Student name");
            collegeNameCheck = false;
        } else {
            collegeNameCheck = true;
            collegeName = nameEditText.getText().toString();
        }

        course = courseSpinner.getText().toString();
        if (course == null || course.equals("")) {
            courseCheck = false;
        } else {
            courseCheck = true;
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

        if (collegeNameCheck && courseCheck && emailIdCheck && passwordCheck) {
            studentDetail = new StudentDetail(collegeName, course, emailId,password,collegeId);
            return true;
        } else {
            return false;
        }
    }

    public void addStudent(View view) {
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
        Toast.makeText(FacultyAddStudentActivity.this, message, Toast.LENGTH_SHORT).show();
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
