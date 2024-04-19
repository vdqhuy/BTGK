package com.example.btgk.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class SeriesModel implements Parcelable {
    private String filmId, epName, epSummary, thumbnail;
    private int epOrder, epDuration;

    public SeriesModel() {
    }

    public SeriesModel(String filmId, String epName, String epSummary, String thumbnail, int epOrder, int epDuration) {
        this.filmId = filmId;
        this.epName = epName;
        this.epSummary = epSummary;
        this.thumbnail = thumbnail;
        this.epOrder = epOrder;
        this.epDuration = epDuration;
    }

    protected SeriesModel(Parcel in) {
        filmId = in.readString();
        epName = in.readString();
        epSummary = in.readString();
        thumbnail = in.readString();
        epOrder = in.readInt();
        epDuration = in.readInt();
    }

    public static final Creator<SeriesModel> CREATOR = new Creator<SeriesModel>() {
        @Override
        public SeriesModel createFromParcel(Parcel in) {
            return new SeriesModel(in);
        }

        @Override
        public SeriesModel[] newArray(int size) {
            return new SeriesModel[size];
        }
    };

    public String getFilmId() {
        return filmId;
    }

    public String getEpName() {
        return epName;
    }

    public String getEpSummary() {
        return epSummary;
    }

    public int getEpOrder() {
        return epOrder;
    }

    public int getEpDuration() {
        return epDuration;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setFilmId(String filmId) {
        this.filmId = filmId;
    }

    public void setEpName(String epName) {
        this.epName = epName;
    }

    public void setEpSummary(String epSummary) {
        this.epSummary = epSummary;
    }

    public void setEpOrder(int epOrder) {
        this.epOrder = epOrder;
    }

    public void setEpDuration(int epDuration) {
        this.epDuration = epDuration;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(filmId);
        dest.writeString(epName);
        dest.writeString(epSummary);
        dest.writeString(thumbnail);
        dest.writeInt(epOrder);
        dest.writeInt(epDuration);
    }
}
