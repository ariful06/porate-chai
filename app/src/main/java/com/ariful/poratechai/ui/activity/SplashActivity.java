package com.ariful.poratechai.ui.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.ariful.poratechai.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Pulse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.nio.file.attribute.FileAttribute;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {


    FirebaseUser firebaseUser;
    @BindView(R.id.spin_kit)
    ProgressBar progressBar;

    private static final int SPLASH_SCREEN_TIMEOUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (firebaseUser != null) {
                    startActivity(MainActivity.newInstanceOfMainActivity(SplashActivity.this));
                    finish();
                } else {
                    startActivity(LoginActivity.newLoginIntent(SplashActivity.this));
                }
            }
        }, SPLASH_SCREEN_TIMEOUT);
        progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        Sprite pulse = new Pulse();
        progressBar.setIndeterminateDrawable(pulse);
    }
}
