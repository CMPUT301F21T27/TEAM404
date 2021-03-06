package com.example.team404.Account;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.team404.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccountPwdEditActivity extends AppCompatActivity {
    //--------------------------------
    //change account password
    //if the user can input old password correctly, then can change to a new password
    //previous activity: AccountActivity.java
    //--------------------------------
    private TextView usernameEditText;
    private TextView emailTextView;
    private TextView phoneEditText;
    private EditText changePwdEditText;
    private  EditText oldPwdEditText;
    private ImageView backImage;
    private ImageView saveImage;
    private String userOldPwd;
    private String old_password;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String userEmail;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();

        userEmail = user.getEmail();

        setContentView(R.layout.activity_change_account_pwd);
        emailTextView = findViewById(R.id.emailEditText);
        backImage = findViewById(R.id.backImage);
        changePwdEditText = findViewById(R.id.change_password_EditText);
        oldPwdEditText = findViewById(R.id.old_psw_EditText);
        emailTextView.setText(userEmail);
        old_password = getIntent().getStringExtra("old_password");


        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        saveImage = findViewById(R.id.saveImage);
        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseFirestore db;
                db= FirebaseFirestore.getInstance();
                DocumentReference userDocRef = db.collection("User").document(userEmail);

                String newPwd = changePwdEditText.getText().toString();
                String oldPws = oldPwdEditText.getText().toString();
                if(checkInvalid(newPwd, oldPws, old_password)==false){
                    return;
                }


                user.updatePassword(newPwd)
                    .addOnCompleteListener(AccountPwdEditActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                userDocRef.update("userPassword", newPwd);
                                finish();
                                onBackPressed();
                                Toast.makeText(getApplicationContext(), "Password Updated Successfully", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getApplicationContext(), "For Security reasons you have to re-login first.\nThen try to update password", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                    });
            }
        });


    }
    //check input if valid
    public boolean checkInvalid(String newPwd, String oldPws, String userOldPwd) {


        if (!oldPws.equals(userOldPwd)) {
            Toast.makeText(AccountPwdEditActivity.this, "Old password does not match!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (oldPws.equals(userOldPwd)) {
            if (newPwd.length() == 0) {
                Toast.makeText(AccountPwdEditActivity.this, "new password is empty", Toast.LENGTH_SHORT).show();
                return false;
            } else if (newPwd.length() < 6) {
                Toast.makeText(AccountPwdEditActivity.this, "password must contain at least 6 character", Toast.LENGTH_SHORT).show();
                return false;
            } else if (newPwd.length() > 10) {
                Toast.makeText(AccountPwdEditActivity.this, "password must contain at moat 10 character", Toast.LENGTH_SHORT).show();
                return false;
            } else if (newPwd.equals(oldPws)) {
                Toast.makeText(AccountPwdEditActivity.this, "new password are not different", Toast.LENGTH_SHORT).show();
                return false;
            }

        }
        return true;
    }



    @Override
    public void onBackPressed() {
     //super.onBackPressed();
        finish();
    }
}
