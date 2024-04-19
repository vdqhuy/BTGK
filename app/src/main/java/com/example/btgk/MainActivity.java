package com.example.btgk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.btgk.Model.FilmModel;
import com.example.btgk.Model.GenreModel;
import com.example.btgk.Model.MovieModel;
import com.example.btgk.Model.SeriesModel;
import com.example.btgk.Model.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView btnBack, btnMore;
    CardView cardTopGenre, cardView, cardFav, cardCmt, cardShare, cardDownload;
    TextView txtSoPhim, txtSoAcc;
    LinearLayout layoutSoPhim, layoutSoAcc;

    List<FilmModel> filmList = new ArrayList<>();
    List<MovieModel> movieList = new ArrayList<>();
    List<SeriesModel> seriesList = new ArrayList<>();
    List<GenreModel> genreList = new ArrayList<>();
    List<UserModel> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setControl();
        setEvent();
    }

    private void setEvent() {
        cardTopGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTopGenre = new Intent(MainActivity.this, PrototypeStatisticActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("content", "TopGenre");
                intentTopGenre.putExtras(bundle);
                startActivity(intentTopGenre);
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentView = new Intent(MainActivity.this, PrototypeStatisticActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("content", "View");
                intentView.putExtras(bundle);
                startActivity(intentView);
            }
        });

        cardFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFav = new Intent(MainActivity.this, PrototypeStatisticActivity.class);
                intentFav.putExtra("content", "Favorite");
                startActivity(intentFav);
            }
        });

        cardShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentShare = new Intent(MainActivity.this, PrototypeStatisticActivity.class);
                intentShare.putExtra("content", "Share");
                startActivity(intentShare);
            }
        });

        cardCmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentComment = new Intent(MainActivity.this, PrototypeStatisticActivity.class);
                intentComment.putExtra("content", "Comment");
                startActivity(intentComment);
            }
        });

        cardDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDownload = new Intent(MainActivity.this, PrototypeStatisticActivity.class);
                intentDownload.putExtra("content", "Download");
                startActivity(intentDownload);
            }
        });

        FirebaseDatabase filmDatabase = FirebaseDatabase.getInstance();
        DatabaseReference filmRef = filmDatabase.getReference("Film");
        DatabaseReference userRef = filmDatabase.getReference("User");

        filmRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                filmList.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    FilmModel film = item.getValue(FilmModel.class);
                    filmList.add(film);
                }
                txtSoPhim.setText(String.valueOf(filmList.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    UserModel user = item.getValue(UserModel.class);
                    userList.add(user);
                }
                txtSoAcc.setText(String.valueOf(userList.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        layoutSoPhim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTotalFilm = new Intent(MainActivity.this, PrototypeStatisticActivity.class);
                intentTotalFilm.putExtra("content", "TotalFilm");
                startActivity(intentTotalFilm);
            }
        });

        layoutSoAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTotalAccount = new Intent(MainActivity.this, PrototypeStatisticActivity.class);
                intentTotalAccount.putExtra("content", "TotalAccount");
                startActivity(intentTotalAccount);
            }
        });
    }

    private void setControl() {
        btnBack = findViewById(R.id.btn_back_main);
        btnMore = findViewById(R.id.btn_more_main);
        cardTopGenre = findViewById(R.id.card_top_genre);
        cardView = findViewById(R.id.card_view);
        cardFav = findViewById(R.id.card_fav);
        cardCmt = findViewById(R.id.card_cmt);
        cardShare = findViewById(R.id.card_share);
        cardDownload = findViewById(R.id.card_download);
        txtSoPhim = findViewById(R.id.txt_so_phim);
        txtSoAcc = findViewById(R.id.txt_so_acc);
        layoutSoPhim = findViewById(R.id.layout_so_phim);
        layoutSoAcc = findViewById(R.id.layout_so_acc);
    }
}