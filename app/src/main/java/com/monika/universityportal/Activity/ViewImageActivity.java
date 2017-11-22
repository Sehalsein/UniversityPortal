package com.monika.universityportal.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.monika.universityportal.R;

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
