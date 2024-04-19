package com.example.btgk.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.btgk.Model.GenreModel;
import com.example.btgk.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GenreAdapter2 extends ArrayAdapter {
    Context context;
    int resource;
    List<GenreModel> data;
    List<String> dataOption;
    String option;

    public GenreAdapter2(@NonNull Context context, int resource, List<GenreModel> data, String option) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
        this.option = option;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        TextView txtRank = convertView.findViewById(R.id.txt_rank);
        ImageView imgGenre = convertView.findViewById(R.id.img_genre);
        TextView txtGenre = convertView.findViewById(R.id.txt_genre);
        TextView txtData = convertView.findViewById(R.id.txt_data);
//        ImageView imgDataType = convertView.findViewById(R.id.img_data_type);

        GenreModel genre = data.get(position);
        txtRank.setText(String.valueOf(position + 1));
        String imgUrl = genre.getImage();
        Picasso.get().load(imgUrl).into(imgGenre);
        txtGenre.setText(genre.getName());
        if (option != null && option.equals("Số lượng phim")) {
            txtData.setText(String.valueOf(genre.getFilmCount()));
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_film_movie_24px);
            txtData.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        }
        else if (option != null && option.equals("Lượt xem")) {
            txtData.setText(String.valueOf(genre.getViewCount()));
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_visibility_24px);
            txtData.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        }
        else if (option != null && option.equals("Lượt yêu thích")) {
            txtData.setText(String.valueOf(genre.getFavoriteCount()));
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_24px);
            txtData.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        }
        else if (option != null && option.equals("Lượt chia sẻ")) {
            txtData.setText(String.valueOf(genre.getShareCount()));
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_share_24px);
            txtData.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        }
        else if (option != null && option.equals("Lượt bình luận")) {
            txtData.setText(String.valueOf(genre.getCommentCount()));
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_comment_24px);
            txtData.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        }
        else if (option != null && option.equals("Lượt tải")) {
            txtData.setText(String.valueOf(genre.getDownloadCount()));
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_download_24px);
            txtData.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        }
        else {
            txtData.setText("0");
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_film_movie_24px);
            txtData.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
        }

        return convertView;
    }
}
