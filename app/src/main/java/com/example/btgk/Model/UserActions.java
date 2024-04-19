package com.example.btgk.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class UserActions implements Parcelable {
    private String filmId;
    private int view, favorite, share, download;
    private List<CommentModel> cmts;

    public UserActions() {
    }

    public UserActions(String filmId, int view, int favorite, int share, int download, List<CommentModel> cmts) {
        this.filmId = filmId;
        this.view = view;
        this.favorite = favorite;
        this.share = share;
        this.download = download;
        this.cmts = cmts;
    }

    protected UserActions(Parcel in) {
        filmId = in.readString();
        view = in.readInt();
        favorite = in.readInt();
        share = in.readInt();
        download = in.readInt();
        cmts = in.createTypedArrayList(CommentModel.CREATOR);
    }

    public static final Creator<UserActions> CREATOR = new Creator<UserActions>() {
        @Override
        public UserActions createFromParcel(Parcel in) {
            return new UserActions(in);
        }

        @Override
        public UserActions[] newArray(int size) {
            return new UserActions[size];
        }
    };

    public String getFilmId() {
        return filmId;
    }

    public int getView() {
        return view;
    }

    public int getFavorite() {
        return favorite;
    }

    public int getShare() {
        return share;
    }

    public int getDownload() {
        return download;
    }

    public List<CommentModel> getCmts() {
        return cmts;
    }

    public void setFilmId(String filmId) {
        this.filmId = filmId;
    }

    public void setView(int view) {
        this.view = view;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public void setShare(int share) {
        this.share = share;
    }

    public void setDownload(int download) {
        this.download = download;
    }

    public void setCmts(List<CommentModel> cmts) {
        this.cmts = cmts;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(filmId);
        dest.writeInt(view);
        dest.writeInt(favorite);
        dest.writeInt(share);
        dest.writeInt(download);
        dest.writeTypedList(cmts);
    }
}
