package com.ariful.poratechai.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ariful.poratechai.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationActivity extends AppCompatActivity {
    
    //Edit text
    @BindView(R.id.edit_text_user_name)
    EditText userName;
    @BindView(R.id.edit_text_user_email)
    EditText userEmail;
    @BindView(R.id.edit_text_user_password)
    EditText userPassword;
    //Button
    @BindView(R.id.button_register)
    Button btnRegister;

    //toolbar
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    //Firebase
    FirebaseAuth auth;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        //initializing auth
        auth = FirebaseAuth.getInstance();

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Register");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @OnClick(R.id.button_register)
    private void onRegisterButtonClicked() {
        String strUserName = userName.getText().toString().trim();
        String strUserEmail = userEmail.getText().toString().trim();
        String strUserPassword = userPassword.getText().toString().trim();

        if (TextUtils.isEmpty(strUserName) || TextUtils.isEmpty(strUserEmail) || TextUtils.isEmpty(strUserPassword)) {
            Toast.makeText(this, "ALl fields are required", Toast.LENGTH_SHORT).show();
        } else if (strUserPassword.length() < 6) {
            Toast.makeText(this, "Password mush be al least 6 characters", Toast.LENGTH_SHORT).show();
        } else {
            register(strUserName, strUserEmail, strUserPassword);
        }

    }

    private void register(String userName, String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userId = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                            HashMap<String, String> hasMap = new HashMap<>();
                            hasMap.put("id", userId);
                            hasMap.put("username", userName);
                            hasMap.put("imageURL", "default");

                            reference.setValue(hasMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(RegistrationActivity.this, "you can't register with this email or password,", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
