package com.example.btgk.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btgk.Model.FilmModel;
import com.example.btgk.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.MyFilmViewHolder> {

    Context context;
    List<FilmModel> filmList;
    String option;
    String topic;

    public FilmAdapter(Context context, List<FilmModel> filmList, String option, String topic) {
        this.context = context;
        this.filmList = filmList;
        this.option = option;
        this.topic = topic;
    }

    @NonNull
    @Override
    public MyFilmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false);
        return new MyFilmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyFilmViewHolder holder, int position) {
        FilmModel film = filmList.get(position);
        holder.txtRank.setText(String.valueOf(position + 1));
        String imgUrl = film.getImgLandscape();
        Picasso.get().load(imgUrl).into(holder.imgFilm);
        holder.txtFilm.setText(film.getName());

        if (option.equals(context.getResources().getString(R.string.txt_all_films))) {
            setTxtData(holder.txtData, film);
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_film_24px);
            holder.txtData.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        }
        else if (option.equals(context.getResources().getString(R.string.txt_movies))) {
            setTxtData(holder.txtData, film);
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_film_movie_24px);
            holder.txtData.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        }
        else if (option.equals(context.getResources().getString(R.string.txt_series))) {
            setTxtData(holder.txtData, film);
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_film_series_24px);
            holder.txtData.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        }
        else if (option.equals(context.getResources().getString(R.string.txt_genres))) {
            setTxtData(holder.txtData, film);
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_genre_24px);
            holder.txtData.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        }
        else if (topic.equals("TotalFilm")) {
            setTxtData(holder.txtData, film);
        }
        else {
            holder.txtData.setText("1500");
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_film_24px);
            holder.txtData.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        }
    }

    private void setTxtData(TextView txtData, FilmModel film) {
        if (topic.equals("View") || option.equals(context.getResources().getString(R.string.txt_views))) {
            txtData.setText(String.valueOf(film.getViewCount()));
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_visibility_24px);
            txtData.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        }
        else if (topic.equals("Favorite") || option.equals(context.getResources().getString((R.string.txt_likes)))) {
            txtData.setText(String.valueOf(film.getFavoriteCount()));
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_favorite_24px);
            txtData.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        }
        else if (topic.equals("Share") || option.equals(context.getResources().getString(R.string.txt_shares))) {
            txtData.setText(String.valueOf(film.getShareCount()));
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_share_24px);
            txtData.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        }
        else if (topic.equals("Comment") || option.equals(context.getResources().getString(R.string.txt_comments))) {
            txtData.setText(String.valueOf(film.getCommentCount()));
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_comment_24px);
            txtData.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        }
        else if (topic.equals("Download") || option.equals(context.getResources().getString(R.string.txt_downloads))) {
            txtData.setText(String.valueOf(film.getDownloadCount()));
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_download_24px);
            txtData.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        }
        else if (option.equals(context.getResources().getString(R.string.txt_default_list))) {
            txtData.setText("");
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_film_24px);
            txtData.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }
    }

    @Override
    public int getItemCount() {
        return filmList.size();
    }

    public static class MyFilmViewHolder extends RecyclerView.ViewHolder {
        TextView txtRank;
        ImageView imgFilm;
        TextView txtFilm;
        TextView txtData;

        public MyFilmViewHolder(@NonNull View itemView) {
            super(itemView);
            txtRank = itemView.findViewById(R.id.txt_rank);
            imgFilm = itemView.findViewById(R.id.img_content);
            txtFilm = itemView.findViewById(R.id.txt_content);
            txtData = itemView.findViewById(R.id.txt_data);
        }
    }
}
