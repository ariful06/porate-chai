package com.ariful.poratechai.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ariful.poratechai.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {


    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null){
            startActivity(MainActivity.newInstranceOfMainActivity(SplashActivity.this));
            finish();
        }else{
            startActivity(LoginActivity.newLoginIntent(SplashActivity.this));
        }
    }
}
