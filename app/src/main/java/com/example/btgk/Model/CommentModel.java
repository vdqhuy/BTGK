package com.example.btgk.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Date;

public class CommentModel implements Parcelable {
    private String cmtContent;
    private Date cmtTimeCreated;

    public CommentModel() {
    }

    public CommentModel(String cmtContent, Date cmtTimeCreated) {
        this.cmtContent = cmtContent;
        this.cmtTimeCreated = cmtTimeCreated;
    }

    protected CommentModel(Parcel in) {
        cmtContent = in.readString();
    }

    public static final Creator<CommentModel> CREATOR = new Creator<CommentModel>() {
        @Override
        public CommentModel createFromParcel(Parcel in) {
            return new CommentModel(in);
        }

        @Override
        public CommentModel[] newArray(int size) {
            return new CommentModel[size];
        }
    };

    public String getCmtContent() {
        return cmtContent;
    }

    public Date getCmtTimeCreated() {
        return cmtTimeCreated;
    }

    public void setCmtContent(String cmtContent) {
        this.cmtContent = cmtContent;
    }

    public void setCmtTimeCreated(Date cmtTimeCreated) {
        this.cmtTimeCreated = cmtTimeCreated;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(cmtContent);
    }
}
