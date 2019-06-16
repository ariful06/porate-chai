package com.ariful.poratechai.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ariful.poratechai.R;
import com.ariful.poratechai.model.Chat;
import com.ariful.poratechai.model.User;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MESSAGE_TYPE_LEFT = 0;
    public static final int MESSAGE_TYPE_RIGHT = 1;

    private Context context;
    private List<Chat> chats;
    private String imageURL;

    FirebaseUser firebaseUser;

    public MessageAdapter(Context context, List<Chat> chats , String imageURL) {
        this.context = context;
        this.chats= chats;
        this.imageURL = imageURL;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

       if (viewType == MESSAGE_TYPE_RIGHT){
           View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right,viewGroup,false);
           return new MessageAdapter.ViewHolder(view);
       }else{
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left,viewGroup,false);
            return new MessageAdapter.ViewHolder(view);
       }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = chats.get(position);
        holder.tvMessage.setText(chat.getMessage());
        if (imageURL.equals("default")){
            holder.profileImage.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(context).load(imageURL).into(holder.profileImage);
        }

        if (position == chats.size()-1){
            if (chat.isIsseen()){
                holder.isSeen.setText("Seen");
            }else{
                holder.isSeen.setText("Delivered");
            }
        }else{
            holder.isSeen.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        CircleImageView profileImage;
        TextView isSeen;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.message_content);
            profileImage = itemView.findViewById(R.id.profile_image);
            isSeen = itemView.findViewById(R.id.text_view_seen);

        }
    }


    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chats.get(position).getSender().equals(firebaseUser.getUid())){
            return MESSAGE_TYPE_RIGHT;
        }else{
            return MESSAGE_TYPE_LEFT;
        }
    }
}
