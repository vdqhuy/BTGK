package com.example.btgk.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class GenreModel implements Parcelable {
    private String name, image;
    private int filmCount, viewCount, favoriteCount, shareCount, downloadCount, commentCount;

    public GenreModel() {
    }

    public GenreModel(String name, String image, int filmCount, int viewCount, int favoriteCount, int shareCount, int downloadCount, int commentCount) {
        this.name = name;
        this.image = image;
        this.filmCount = filmCount;
        this.viewCount = viewCount;
        this.favoriteCount = favoriteCount;
        this.shareCount = shareCount;
        this.downloadCount = downloadCount;
        this.commentCount = commentCount;
    }

    protected GenreModel(Parcel in) {
        name = in.readString();
        image = in.readString();
    }

    public static final Creator<GenreModel> CREATOR = new Creator<GenreModel>() {
        @Override
        public GenreModel createFromParcel(Parcel in) {
            return new GenreModel(in);
        }

        @Override
        public GenreModel[] newArray(int size) {
            return new GenreModel[size];
        }
    };

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setFilmCount(int filmCount) {
        this.filmCount = filmCount;
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

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public int getFilmCount() {
        return filmCount;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(image);
    }
}
