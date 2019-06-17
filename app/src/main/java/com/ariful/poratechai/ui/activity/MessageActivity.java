package com.ariful.poratechai.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ariful.poratechai.R;
import com.ariful.poratechai.adapter.MessageAdapter;
import com.ariful.poratechai.model.Chat;
import com.ariful.poratechai.model.User;
import com.ariful.poratechai.utility.Constants;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    public static Intent instracneOfMeesageActivity(Context context) {
        return new Intent(context, MessageActivity.class);
    }

    @BindView(R.id.image_view_profile)
    CircleImageView otherUserProfileImage;
    @BindView(R.id.online_status)
    CircleImageView otherUserOnlineStatus;
    @BindView(R.id.text_view_user_name)
    TextView tvOtherUserName;



    @BindView(R.id.send_button)
    ImageButton sendButton;
    @BindView(R.id.message_content)
    EditText messageContent;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.message_list_recycler_view)
    RecyclerView recyclerView;

    Intent intent;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    ValueEventListener seenListener;

    String otherUserId = "";
    MessageAdapter messageAdapter;
    List<Chat> messageList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(MessageActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
        });

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);


        intent = getIntent();
        otherUserId = intent.getStringExtra(Constants.OTHER_USER_ID);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(otherUserId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                tvOtherUserName.setText(user.getUsername());
                if (user.getImageURL().equals("default")) {
                    otherUserProfileImage.setImageResource(R.drawable.default_profile);
                } else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(otherUserProfileImage);
                }
                retrieveMessages(firebaseUser.getUid(),otherUserId,user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        seenMessage(otherUserId);
    }

    private void seenMessage(String userId){
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userId)){
                            HashMap<String , Object> hashMap = new HashMap<>();
                            hashMap.put("isseen", true);
                            snapshot.getRef().updateChildren(hashMap);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(firebaseUser.getUid())
                .child(otherUserId);
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(otherUserId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessge(String userId, String otherUserId, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> map = new HashMap<>();
        map.put("sender", userId);
        map.put("receiver", otherUserId);
        map.put("message", message);
        map.put("isseen", false);
        reference.child("Chats").push().setValue(map);
        messageContent.setText("");
    }

    @OnClick(R.id.send_button)
    public void onSendButtonClicked() {
        String message = messageContent.getText().toString();
        if (!message.equals("")){
            sendMessge(firebaseUser.getUid(), otherUserId, message);    
        }else{
            Toast.makeText(this, "Please write something...", Toast.LENGTH_SHORT).show();
        }
    }

    private void retrieveMessages(String myId,String otherUserId,String imageUrl){
        messageList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 messageList.clear();
                 for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                     Chat chat = snapshot.getValue(Chat.class);
                     if (chat.getReceiver().equals(myId) && chat.getSender().equals(otherUserId)
                     || chat
                     .getReceiver().equals(otherUserId) && chat.getSender().equals(myId)){
                         messageList.add(chat);
                     }
                     messageAdapter = new MessageAdapter(MessageActivity.this,messageList,imageUrl);
                     recyclerView.setAdapter(messageAdapter);
                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String , Object> hashMap = new HashMap<>();
        hashMap.put("status",status);
        reference.updateChildren(hashMap);

    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
        status("offline");
    }


}
