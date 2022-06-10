package com.aseds.reviewapp.Pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aseds.reviewapp.Model.User;
import com.aseds.reviewapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfilPage extends AppCompatActivity {
    ImageView backBtn;
    TextView username, email;
    FirebaseUser fUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_page);

        backBtn = findViewById(R.id.back_pressed);
        username = findViewById(R.id.user_name);
        email = findViewById(R.id.user_email);
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        String profileId;
        SharedPreferences sharedPreferences = getSharedPreferences("PROFILE",MODE_PRIVATE);
        profileId = sharedPreferences.getString("profileId", "none");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfilPage.super.onBackPressed();
            }
        });

        userInfo(profileId);
    }

    private void userInfo(String profileId) {

        FirebaseDatabase.getInstance().getReference().child("Users").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user != null ){
                    username.setText(user.getUsername());
                    email.setText(user.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}