package com.example.btgk.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class UserModel {
    private String username, password, email, phone;
    private List<UserActions> actions;
    private int viewCount, favoriteCount, shareCount, downloadCount, commentCount;

    public UserModel() {
    }

    public UserModel(String username, String password, String email, String phone, List<UserActions> actions) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.actions = actions;
    }

    public UserModel(String username, String password, String email, String phone, List<UserActions> actions, int viewCount, int favoriteCount, int shareCount, int downloadCount, int commentCount) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.actions = actions;
        this.viewCount = viewCount;
        this.favoriteCount = favoriteCount;
        this.shareCount = shareCount;
        this.downloadCount = downloadCount;
        this.commentCount = commentCount;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public List<UserActions> getActions() {return actions;}

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setActions(List<UserActions> actions) {this.actions = actions;}

    public int getViewCount() {
        return viewCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public int getShareCount() {
        return shareCount;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
