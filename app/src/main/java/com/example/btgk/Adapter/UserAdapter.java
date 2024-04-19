package com.example.btgk.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btgk.Model.UserModel;
import com.example.btgk.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyUserViewHolder> {

    Context context;
    List<UserModel> userList;
    String option;
    String topic;

    public UserAdapter(Context context, List<UserModel> userList, String topic) {
        this.context = context;
        this.userList = userList;
        this.topic = topic;
    }

    @NonNull
    @Override
    public MyUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_user, parent, false);
        return new UserAdapter.MyUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyUserViewHolder holder, int position) {
        UserModel user = userList.get(position);
        holder.txtOrder.setText(String.valueOf(position + 1));
//        String imgUrl = "";
//        Picasso.get().load(imgUrl).into(holder.imgAvatar);
        holder.txtUserName.setText(user.getUsername());
        holder.txtEmail.setText(user.getEmail());
        holder.txtPhone.setText(user.getPhone());
        holder.txtViews.setText(context.getResources().getString(R.string.txt_views) + ": " + user.getViewCount());
        holder.txtLikes.setText(context.getResources().getString(R.string.txt_likes) + ": " + user.getFavoriteCount());
        holder.txtShares.setText(context.getResources().getString(R.string.txt_shares) + ": " + user.getFavoriteCount());
        holder.txtComments.setText(context.getResources().getString(R.string.txt_comments) + ": " + user.getFavoriteCount());
        holder.txtDownloads.setText(context.getResources().getString(R.string.txt_downloads) + ": " + user.getDownloadCount());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class MyUserViewHolder extends RecyclerView.ViewHolder {

        TextView txtOrder;
        ImageView imgAvatar;
        TextView txtUserName, txtEmail, txtPhone;
        TextView txtViews, txtLikes, txtShares, txtComments, txtDownloads;

        public MyUserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrder = itemView.findViewById(R.id.txt_order);
            imgAvatar = itemView.findViewById(R.id.img_avatar);
            txtUserName = itemView.findViewById(R.id.txt_username);
            txtEmail = itemView.findViewById(R.id.txt_email);
            txtPhone = itemView.findViewById(R.id.txt_phone);
            txtViews = itemView.findViewById(R.id.txt_views);
            txtLikes = itemView.findViewById(R.id.txt_likes);
            txtShares = itemView.findViewById(R.id.txt_shares);
            txtComments = itemView.findViewById(R.id.txt_comments);
            txtDownloads = itemView.findViewById(R.id.txt_downloads);
        }
    }
}
