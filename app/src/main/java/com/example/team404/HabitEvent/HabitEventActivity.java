package com.example.team404.HabitEvent;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import com.bumptech.glide.Glide;
import com.example.team404.R;

public class HabitEventActivity extends AppCompatActivity {
    //--------------------------------
    //display information of current habit event
    //view optional photo, optional location, optional comment
    //Back to previous: HabitEventListActivity.java
    //Go to next: EditHabitEventActivity.java
    //--------------------------------
    private ImageView backImage;
    private ImageView imageView;
    private ImageView editImage;
    private String today;
    private String current_habit_id;

    private TextView commentTextView;
    private TextView locationTextView;
    String TAG= "HabitEventActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_event);
        ContentLoadingProgressBar contentLoadingProgressBar = findViewById(R.id.progress_bar);
        contentLoadingProgressBar.hide();

        commentTextView= findViewById(R.id.comment_textView);
        locationTextView = findViewById(R.id.locationTextView);
        editImage = findViewById(R.id.editImage);
        imageView = findViewById(R.id.imageView);

        Bundle extras = getIntent().getExtras();
        String id = extras.getString("id");
        String location = extras.getString("location");
        String comment = extras.getString("comment");
        today = extras.getString("today");
        current_habit_id= extras.getString("current_habit_id");

        String storageUrlString = extras.getString("storageUrlString");

        if (storageUrlString!=null && storageUrlString.length()>=1){
            contentLoadingProgressBar.show();
            Uri storageURL = Uri.parse(storageUrlString);
            Glide.with(getApplicationContext()).load(storageURL).into(imageView);
            contentLoadingProgressBar.hide();
        }else{
            contentLoadingProgressBar.hide();
        }


        commentTextView.setText(comment);
        locationTextView.setText(location);

        backImage = findViewById(R.id.backImage);
        backImage.setOnClickListener(v -> onBackPressed());


        editImage.setOnClickListener(v -> {

            String comment1 = commentTextView.getText().toString();
            String location1 = locationTextView.getText().toString();

            HabitEventActivity.this.finish();
            Intent intent = new Intent(HabitEventActivity.this, EditHabitEventActivity.class);
            Bundle extras1 = new Bundle();
            extras1.putString("id", id);
            extras1.putString("location", location1);
            extras1.putString("comment", comment1);
            extras1.putString("storageUrlString", storageUrlString);
            extras1.putString("today", today);
            extras1.putString("current_habit_id", current_habit_id);
            intent.putExtras(extras1);

            startActivity(intent);
        });


    }
    //https://stackoverflow.com/questions/920306/sending-data-back-to-the-main-activity-in-android
    // author: Suragch (answered) GabrielBB(edited)
    //date: 11-5-2021; 12-13-2021
    // protected void onActivityResult(int requestCode, int resultCode, Intent data) is created by Suragch
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 333) {
            // Get String data from later Intent (get data back from EditHabitEventActivity.java)
            String locationString = data.getStringExtra("location");
            String commentString = data.getStringExtra("comment");
            String storageUrlString = data.getStringExtra("storageUrlString");
            if (storageUrlString!=null){
                imageView.setImageResource(android.R.color.transparent);
                imageView = findViewById(R.id.imageView);
                Uri storageURL = Uri.parse(storageUrlString);
                Glide.with(getApplicationContext()).load(storageURL).into(imageView);

            }else {
                imageView.setImageResource(android.R.color.transparent);
                imageView=findViewById(R.id.imageView_delete);
                imageView.setVisibility(ImageView.VISIBLE);
            }

            // Set text view with string
            TextView textView = findViewById(R.id.locationTextView);
            textView.setText(locationString);
            TextView textView1 = findViewById(R.id.comment_textView);
            textView1.setText(commentString);

        }
    }

}
