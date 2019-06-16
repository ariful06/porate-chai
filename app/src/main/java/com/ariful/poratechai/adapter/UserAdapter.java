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
import com.ariful.poratechai.model.User;
import com.ariful.poratechai.ui.activity.MessageActivity;
import com.ariful.poratechai.utility.Constants;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private List<User> userList;
    private boolean isChat;

    public UserAdapter(Context context, List<User> userList, boolean isChat) {
        this.context = context;
        this.userList = userList;
        this.isChat = isChat;
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

        if (user.getImageURL().equals("default")){
            holder.profileImage.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(context).load(user.getImageURL()).into(holder.profileImage);
        }
        if (isChat){
            if (user.getStatus().equals("online")){
                holder.statusOn.setVisibility(View.VISIBLE);
                holder.statusOff.setVisibility(View.GONE);
            }else{
                holder.statusOn.setVisibility(View.GONE);
                holder.statusOff.setVisibility(View.VISIBLE);
            }
        }else {
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

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.text_view_user_name);
            profileImage = itemView.findViewById(R.id.profile_image);
            statusOn = itemView.findViewById(R.id.online_status);
            statusOff = itemView.findViewById(R.id.offline_status);


        }
    }
}
