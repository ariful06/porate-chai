package com.ariful.poratechai.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ariful.poratechai.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.text_view_create_new_account)
    TextView createNewAccount;

    //Edit text
    @BindView(R.id.edit_text_user_email)
    EditText userEmail;
    @BindView(R.id.edit_text_user_password)
    EditText userPassword;
    //Button
    @BindView(R.id.button_login)
    Button btnLogin;

    //toolbar
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    //firebase auth
    FirebaseAuth auth;

    public static Intent newLoginIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Login");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        auth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.button_login)
    public void onLoginButtonClicked() {
        String strUserEmail = userEmail.getText().toString().trim();
        String strUserPassword = userPassword.getText().toString().trim();

        if (TextUtils.isEmpty(strUserEmail) || TextUtils.isEmpty(strUserPassword)) {
            Toast.makeText(this, "ALl fields are required", Toast.LENGTH_SHORT).show();
        } else if (strUserPassword.length() < 6) {
            Toast.makeText(this, "Password mush be al least 6 characters", Toast.LENGTH_SHORT).show();
        } else {
            auth.signInWithEmailAndPassword(strUserEmail, strUserPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this, "User name or Password does not match.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    @OnClick(R.id.text_view_create_new_account)
    public void onNewAccountCreatedClicked() {
        startActivity(RegistrationActivity.newRegistrationInstance(LoginActivity.this));
        finish();
    }


}
