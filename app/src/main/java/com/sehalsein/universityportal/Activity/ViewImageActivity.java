package com.sehalsein.universityportal.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sehalsein.universityportal.Model.UserDetail;
import com.sehalsein.universityportal.R;

public class ViewImageActivity extends AppCompatActivity {

    private static final String IMAGE_KEY = "ImageURL";
    private String imageURL;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);



        imageURL = getIntent().getStringExtra(IMAGE_KEY);
        imageView = findViewById(R.id.imageView);
        Glide.with(ViewImageActivity.this).load(imageURL).fitCenter().crossFade(100).into(imageView);
    }


}
