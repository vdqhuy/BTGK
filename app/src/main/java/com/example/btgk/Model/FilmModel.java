package com.example.btgk.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class FilmModel {
    private String id;
    private String name, summary, age, director, cast, type, trailer, imgPortrait, imgLandscape, genre;
    private int yearReleased;
    private int viewCount, favoriteCount, shareCount, downloadCount, commentCount;
    public FilmModel() {}

    public FilmModel(String id, String name, String summary, String age, String director, String cast, String type, String trailer, String imgPortrait, String imgLandscape, String genre, int yearReleased) {
        this.id = id;
        this.name = name;
        this.summary = summary;
        this.age = age;
        this.director = director;
        this.cast = cast;
        this.type = type;
        this.trailer = trailer;
        this.imgPortrait = imgPortrait;
        this.imgLandscape = imgLandscape;
        this.genre = genre;
        this.yearReleased = yearReleased;
    }

    public FilmModel(String name, String summary, String age, String director, String cast, String type,
                     String trailer, String imgPortrait, String imgLandscape, String genre, int yearReleased,
                     int viewCount, int favoriteCount, int shareCount, int commentCount, int downloadCount) {
        this.name = name;
        this.summary = summary;
        this.age = age;
        this.director = director;
        this.cast = cast;
        this.type = type;
        this.trailer = trailer;
        this.imgPortrait = imgPortrait;
        this.imgLandscape = imgLandscape;
        this.genre = genre;
        this.yearReleased = yearReleased;
        this.viewCount = viewCount;
        this.favoriteCount = favoriteCount;
        this.shareCount = shareCount;
        this.commentCount = commentCount;
        this.downloadCount = downloadCount;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public String getAge() {
        return age;
    }

    public String getDirector() {
        return director;
    }

    public String getCast() {
        return cast;
    }

    public String getType() {
        return type;
    }

    public String getTrailer() {
        return trailer;
    }

    public String getImgPortrait() {
        return imgPortrait;
    }

    public String getImgLandscape() {
        return imgLandscape;
    }

    public String getGenre() {
        return genre;
    }

    public int getYearReleased() {
        return yearReleased;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public void setImgPortrait(String imgPortrait) {
        this.imgPortrait = imgPortrait;
    }

    public void setImgLandscape(String imgLandscape) {
        this.imgLandscape = imgLandscape;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setYearReleased(int yearReleased) {
        this.yearReleased = yearReleased;
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
