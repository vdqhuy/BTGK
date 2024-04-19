package com.example.btgk.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class MovieModel implements Parcelable {
    private String filmId;
    private int duration;

    public MovieModel() {
    }

    public MovieModel(String filmId, int duration) {
        this.filmId = filmId;
        this.duration = duration;
    }

    protected MovieModel(Parcel in) {
        filmId = in.readString();
        duration = in.readInt();
    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    public String getFilmId() {
        return filmId;
    }

    public int getDuration() {
        return duration;
    }

    public void setFilmId(String filmId) {
        this.filmId = filmId;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(filmId);
        dest.writeInt(duration);
    }
}
