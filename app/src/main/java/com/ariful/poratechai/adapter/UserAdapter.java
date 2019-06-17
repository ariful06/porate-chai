package com.ariful.poratechai.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ariful.poratechai.R;
import com.ariful.poratechai.model.Chat;
import com.ariful.poratechai.model.User;
import com.ariful.poratechai.ui.activity.MessageActivity;
import com.ariful.poratechai.utility.Constants;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private List<User> userList;
    private boolean isChat;

    private String theLastMessage;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;

    public UserAdapter(Context context, List<User> userList, boolean isChat) {
        this.context = context;
        this.userList = userList;
        this.isChat = isChat;
        auth= FirebaseAuth.getInstance();

    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_list_row_item, viewGroup, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.userName.setText(user.getUsername());

        if (user.getImageURL().equals("default")) {
            holder.profileImage.setImageResource(R.drawable.default_profile);
        } else {
            Glide.with(context).load(user.getImageURL()).into(holder.profileImage);
        }

        if (isChat){
            lastMessageRetrieve(user.getId(),holder.lastMessage);
        }else{
            holder.lastMessage.setVisibility(View.GONE);
        }

        if (isChat) {
            if (user.getStatus().equals("online")) {
                holder.statusOn.setVisibility(View.VISIBLE);
                holder.statusOff.setVisibility(View.GONE);
            } else {
                holder.statusOn.setVisibility(View.GONE);
                holder.statusOff.setVisibility(View.VISIBLE);
            }
        } else {
            holder.statusOn.setVisibility(View.GONE);
            holder.statusOff.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra(Constants.OTHER_USER_ID, user.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        CircleImageView profileImage;
        CircleImageView statusOn;
        ImageView statusOff;
        TextView lastMessage;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.text_view_user_name);
            profileImage = itemView.findViewById(R.id.profile_image);
            statusOn = itemView.findViewById(R.id.online_status);
            statusOff = itemView.findViewById(R.id.offline_status);
            lastMessage = itemView.findViewById(R.id.text_view_last_message);
        }
    }

    private void lastMessageRetrieve(String userId,TextView lastMessageContent){
        theLastMessage  = "default";
        firebaseUser = auth.getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Chats");
        if (firebaseUser != null){
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                            Chat chat = snapshot.getValue(Chat.class);
                            if (chat != null){
                                if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userId) ||
                                        chat.getReceiver().equals(userId) && chat.getSender().equals(firebaseUser.getUid())){
                                    theLastMessage = chat.getMessage();
                                }
                            }
                        }
                        if ("default".equals(theLastMessage)) {
                            lastMessageContent.setText("No message.");
                        } else {
                            lastMessageContent.setText(theLastMessage);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

}
