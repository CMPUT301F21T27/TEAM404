package com.example.team404;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class AccountActivity extends AppCompatActivity {
    //--------------------------------
    //Display account information
    //--------------------------------
    private ImageView notifcationImage;
    private ImageView editImage;
    private ImageView changePwdImage;
    private ImageView profileView;

    private TextView username;
    private TextView email;
    private TextView phone;
    private int log_out_count=0;
    private String requestedListString;
    private String old_password;
    private String profile_uri_string;

    FirebaseAuth mAuth;
    FirebaseUser user;
    String userEmail;

    String TAG = "AccountActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userEmail = user.getEmail();
        System.out.println("---------------------------uid:"+user.getUid());
        String userEmail = user.getEmail();
        setContentView(R.layout.activity_account);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setSelectedItemId(R.id.nav_account);
        bottomNav.setOnItemSelectedListener(navListener);

        username = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        phone = (TextView) findViewById(R.id.phone);
        profileView= findViewById((R.id.profile));

        email.setText(userEmail);
        final FirebaseFirestore db;
        db= FirebaseFirestore.getInstance();
        DocumentReference userDocRef = db.collection("User").document(userEmail);
        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        old_password = document.get("userPassword").toString();
                        String userPhone = document.get("phone").toString();
                        String userName = document.get("userName").toString();
                        requestedListString = document.get("requestedList").toString();
                        if (document.get("profile_uri")!=null){
                            profile_uri_string=document.get("profile_uri").toString();
                            Uri storageURL = Uri.parse(profile_uri_string);
                            Glide.with(getApplicationContext()).load(storageURL).into(profileView);
                            System.out.println("------------*************************************---------------storage url:"+storageURL);
                            System.out.println("---------------------------uid:"+user.getUid());
                            username.setText(userName);
                            phone.setText(userPhone);

                        }
                        if(userPhone==null){
                            userPhone = "Empty phone number";
                            phone.setText(userPhone);
                        }
                        username.setText(userName);
                        phone.setText(userPhone);
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());





                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        System.out.println("------------******************1*******************--------------");

        notifcationImage = findViewById(R.id.notificationImage);
        notifcationImage.setOnClickListener(view -> {
            Intent intent = new Intent(AccountActivity.this, NotificationActivity.class);
            intent.putExtra("reqListString", requestedListString);
            startActivity(intent);
        });
        changePwdImage = findViewById(R.id.change_password_Image);
        changePwdImage.setOnClickListener(view -> {
            Intent intent = new Intent(AccountActivity.this, AccountPwdEditActivity.class);
            intent.putExtra("old_password", old_password);
            startActivity(intent);
        });

        editImage = findViewById(R.id.EditImage);
        editImage.setOnClickListener(v -> {

            String name = username.getText().toString();
            String  phone_ = phone.getText().toString();

            Intent intent = new Intent(AccountActivity.this, AccountEditActivity.class);
            Bundle extras = new Bundle();
            extras.putString("phone", phone_);
            extras.putString("profile_uri", profile_uri_string);
            extras.putString("name", name);
            intent.putExtras(extras);
            startActivityForResult(intent, 333);
        });


        Button logoutButton = findViewById(R.id.log_out_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finishAffinity();
                log_out_count++;
                if (log_out_count >1){
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(AccountActivity.this, "Press again to exit!", Toast.LENGTH_SHORT).show();
                }
            }
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
                // Get String data from Intent
                System.out.println("------------********************66*****************-----66------");
                System.out.println("---------------------------uid:"+user.getUid());
                String returnString = data.getStringExtra("editName");
                String returnphone = data.getStringExtra("editPhone");
                String  profile_uri_string=data.getStringExtra("uri_string");
                if (profile_uri_string!=null){
                    Uri storageURL = Uri.parse(profile_uri_string);
                    Glide.with(getApplicationContext()).load(storageURL).into(profileView);
                    System.out.println("------------*************************************---------------storage url:"+storageURL);
                    System.out.println("---------------------------uid:"+user.getUid());

                }
                // Set text view with string
                TextView textView = (TextView) findViewById(R.id.name);
                textView.setText(returnString);
                TextView textView1 = (TextView) findViewById(R.id.phone);
                textView1.setText(returnphone);
            }
        }

    private NavigationBarView.OnItemSelectedListener navListener =
            new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Intent intent = null;
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            intent = new Intent(getApplicationContext(), MainActivity.class);
                            //startActivity(intent);
                            //overridePendingTransition(0, 0);
                            //return true;
                            break;

                        case R.id.nav_account:
                            intent = new Intent(getApplicationContext(), AccountActivity.class);
                            //startActivity(intent);
                            //overridePendingTransition(0, 0);
                            return true;
                            //;
                        case R.id.nav_my:
                            intent = new Intent(getApplicationContext(), MyActivity.class);
                            //startActivity(intent);
                            //overridePendingTransition(0, 0);
                            //return true;
                            break;

                        case R.id.nav_subscribe:
                            intent = new Intent(getApplicationContext(), SubscribeActivity.class);
                            //startActivity(intent);
                            //overridePendingTransition(0, 0);
                            //return true;
                            break;
                    }
                    startActivity(intent);
                    finish();
                    overridePendingTransition(0, 0);
                    return true;
                }
            };

    private int count = 0;
    @Override
    public void onBackPressed() {
        count++;
        if (count >1){
            new AlertDialog.Builder(this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            AccountActivity.super.onBackPressed();
                            finishAffinity();
                        }
                    }).create().show();
        }else{

        }

    }
}
