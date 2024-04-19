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

import com.example.btgk.Model.GenreModel;
import com.example.btgk.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.MyGenreViewHolder> {
    Context context;
    List<GenreModel> genreList;
    String option;

    public GenreAdapter(Context context, List<GenreModel> genreList, String option) {
        this.context = context;
        this.genreList = genreList;
        this.option = option;
    }

    @NonNull
    @Override
    public GenreAdapter.MyGenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false);
        return new GenreAdapter.MyGenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreAdapter.MyGenreViewHolder holder, int position) {
        GenreModel genre = genreList.get(position);
        holder.txtRank.setText(String.valueOf(position + 1));
        String imgUrl = genre.getImage();
        Picasso.get().load(imgUrl).into(holder.imgGenre);
        holder.txtGenre.setText(genre.getName());

        if (option != null && option.equals(context.getResources().getString(R.string.txt_film_amount))) {
            holder.txtData.setText(String.valueOf(genre.getFilmCount()));
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_film_movie_24px);
            holder.txtData.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        }
        else if (option != null && option.equals(context.getResources().getString(R.string.txt_views))) {
            holder.txtData.setText(String.valueOf(genre.getViewCount()));
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_visibility_24px);
            holder.txtData.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        }
        else if (option != null && option.equals(context.getResources().getString(R.string.txt_likes))) {
            holder.txtData.setText(String.valueOf(genre.getFavoriteCount()));
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_favorite_24px);
            holder.txtData.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        }
        else if (option != null && option.equals(context.getResources().getString(R.string.txt_shares))) {
            holder.txtData.setText(String.valueOf(genre.getShareCount()));
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_share_24px);
            holder.txtData.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        }
        else if (option != null && option.equals(context.getResources().getString(R.string.txt_comments))) {
            holder.txtData.setText(String.valueOf(genre.getCommentCount()));
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_comment_24px);
            holder.txtData.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        }
        else if (option != null && option.equals(context.getResources().getString(R.string.txt_downloads))) {
            holder.txtData.setText(String.valueOf(genre.getDownloadCount()));
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_download_24px);
            holder.txtData.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        }
        else {
            holder.txtData.setText("0");
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_film_movie_24px);
            holder.txtData.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        }
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }

    public static class MyGenreViewHolder extends RecyclerView.ViewHolder {
        TextView txtRank;
        ImageView imgGenre;
        TextView txtGenre;
        TextView txtData;

        public MyGenreViewHolder(@NonNull View itemView) {
            super(itemView);
            txtRank = itemView.findViewById(R.id.txt_rank);
            imgGenre = itemView.findViewById(R.id.img_content);
            txtGenre = itemView.findViewById(R.id.txt_content);
            txtData = itemView.findViewById(R.id.txt_data);
        }
    }
}
