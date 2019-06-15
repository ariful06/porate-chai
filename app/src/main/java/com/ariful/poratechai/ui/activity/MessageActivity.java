package com.ariful.poratechai.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.ariful.poratechai.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    public static Intent instracneOfMeesageActivity(Context context) {
        return new Intent(context,MessageActivity.class);
    }

    @BindView(R.id.profile_image)
    CircleImageView otherUserProfileImage;

    @BindView(R.id.online_status)
    CircleImageView otherUserOnlineStatu;

    @BindView(R.id.text_view_user_name)
    TextView tvOtherUserName;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
    }
}
