package com.example.btgk.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.btgk.Adapter.FilmAdapter;
import com.example.btgk.Adapter.GenreAdapter;
import com.example.btgk.Adapter.UserAdapter;
import com.example.btgk.Model.CommentModel;
import com.example.btgk.Model.FilmModel;
import com.example.btgk.Model.GenreModel;
import com.example.btgk.Model.MovieModel;
import com.example.btgk.Model.SeriesModel;
import com.example.btgk.Model.UserActions;
import com.example.btgk.Model.UserModel;
import com.example.btgk.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrototypeStatListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrototypeStatListFragment extends Fragment {
    FirebaseDatabase filmDatabase;
    Intent intent;
    ListView lvStatList;
    RecyclerView rvStatList;
    CardView cvSelectGenre;
    Spinner spSelectGenre;
    GenreAdapter genreAdapter;
    FilmAdapter filmAdapter;
    UserAdapter userAdapter;
    List<FilmModel> filmList = new ArrayList<>();
    List<FilmModel> filmsTemp = new ArrayList<>();
    List<MovieModel> movieList = new ArrayList<>();
    List<SeriesModel> seriesList = new ArrayList<>();
    List<GenreModel> genreList = new ArrayList<>();
    List<UserModel> userList = new ArrayList<>();
    Context context;
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    private String option;
    public void setOption(String option) {
        this.option = option;
    }
    public String getOption() {
        return option;
    }

    private String sort = "";
    public void setSort(String sort) {
        this.sort = sort;
    }
    public String getSort() {
        return sort;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prototype_stat_list, container, false);
        setControl(view);
        setEvent();
        return view;
    }

    private void setContent() {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String content = bundle.getString("content");
            assert content != null;
            if (content.equals("TotalFilm")) {
                filmsTemp.addAll(filmList);
                getCountOptionFilm();
                sortCountOptionFilm(content);
                rvStatList.setLayoutManager(new LinearLayoutManager(getContext()));
                rvStatList.setHasFixedSize(true);
                filmAdapter = new FilmAdapter(getContext(), filmsTemp, option, content);
                rvStatList.setAdapter(filmAdapter);
            }
            else if (content.equals("TotalAccount")) {
                getCountOptionUser();
                sortCountOptionUser();
                rvStatList.setLayoutManager(new LinearLayoutManager(getContext()));
                rvStatList.setHasFixedSize(true);
                userAdapter = new UserAdapter(getContext(), userList, content);
                rvStatList.setAdapter(userAdapter);
            }
            else if (content.equals("TopGenre")) {
                if (option.equals(getResources().getString(R.string.txt_film_amount))) {
                    getFilmCountOptionGenre();
                    sortFilmCountOptionGenre();
                }
                else if (this.getOption().equals(getResources().getString(R.string.txt_views))) {
                    getViewCountOptionGenre();
                    sortViewCountOptionGenre();
                }
                else if (this.getOption().equals(getResources().getString(R.string.txt_likes))) {
                    getFavoriteCountOptionGenre();
                    sortFavoriteCountOptionGenre();
                }
                else if (this.getOption().equals(getResources().getString(R.string.txt_shares))) {
                    getShareCountOptionGenre();
                    sortShareCountOptionGenre();
                }
                else if (this.getOption().equals(getResources().getString(R.string.txt_comments))) {
                    getCommentCountOptionGenre();
                    sortCommentCountOptionGenre();
                }
                else if (this.getOption().equals(getResources().getString(R.string.txt_downloads))) {
                    getDownloadCountOptionGenre();
                    sortDownloadCountOptionGenre();
                }
                rvStatList.setLayoutManager(new LinearLayoutManager(getContext()));
                rvStatList.setHasFixedSize(true);
                genreAdapter = new GenreAdapter(getContext(), genreList, option);
                rvStatList.setAdapter(genreAdapter);
            }
            else {
                filmsTemp.addAll(filmList);
                getCountOptionFilm();
                sortCountOptionFilm(content);
                if (this.getOption().equals(getResources().getString(R.string.txt_movies))) {
                    filterMovieOptionFilm(filmsTemp);
                }
                else if (this.getOption().equals(getResources().getString(R.string.txt_series))) {
                    filterSeriesOptionFilm(filmsTemp);
                }
                else if (this.getOption().equals(getResources().getString(R.string.txt_genres))) {
                    if (cvSelectGenre.getVisibility() == View.GONE) {
                        cvSelectGenre.setVisibility(View.VISIBLE);
                    }
                    createSelectGenreItem();
                    filterGenreOptionFilm(filmsTemp);
                }
                rvStatList.setLayoutManager(new LinearLayoutManager(getContext()));
                rvStatList.setHasFixedSize(true);
                filmAdapter = new FilmAdapter(getContext(), filmsTemp, option, content);
                rvStatList.setAdapter(filmAdapter);
            }
        }
    }

    private void createSelectGenreItem() {
        ArrayList<String> listItem = new ArrayList<>();
        for (GenreModel genre : genreList) {
            listItem.add(genre.getName());
        }
        if (getContext() != null) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, listItem);
            arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
            spSelectGenre.setAdapter(arrayAdapter);
        }
    }

    private void getFilmCountOptionGenre() {
        resetCountOptionGenre();
        for (GenreModel genre : genreList) {
            int countFilm = 0;
            for (FilmModel film : filmList) {
                if (film.getGenre().contains(genre.getName())) {
                    countFilm++;
                }
            }
            genre.setFilmCount(countFilm);
        }
    }

    private void sortFilmCountOptionGenre() {
        if (sort.equals("DESC")) {
            genreList.sort((g1, g2) -> g2.getFilmCount() - g1.getFilmCount());
        }
        else if (sort.equals("ASC")) {
            genreList.sort(Comparator.comparingInt(GenreModel::getFilmCount));
        }
    }

    private void getViewCountOptionGenre() {
        resetCountOptionGenre();
        for (UserModel user : userList) {
            if (user.getActions() != null ) {
                List<UserActions> actionsList = user.getActions();

                for (UserActions actions : actionsList) {
                    String filmId = actions.getFilmId();
                    int view = actions.getView();
                    FilmModel film = getFilmByFilmID(filmId);
                    if (film != null && view == 1) {
                        for (GenreModel genre : genreList) {
                            if (film.getGenre().contains(genre.getName())) {
                                genre.setViewCount(genre.getViewCount() + 1);
                            }
                        }
                    }
                }
            }
        }
    }

    private void sortViewCountOptionGenre() {
        if (sort.equals("DESC")) {
            genreList.sort((g1, g2) -> g2.getViewCount() - g1.getViewCount());
        }
        else if (sort.equals("ASC")) {
            genreList.sort(Comparator.comparingInt(GenreModel::getViewCount));
        }
    }

    private void getFavoriteCountOptionGenre() {
        resetCountOptionGenre();
        for (UserModel user : userList) {
            if (user.getActions() != null ) {
                List<UserActions> actionsList = user.getActions();

                for (UserActions actions : actionsList) {
                    String filmId = actions.getFilmId();
                    int fav = actions.getFavorite();
                    FilmModel film = getFilmByFilmID(filmId);
                    if (film != null && fav == 1) {
                        for (GenreModel genre : genreList) {
                            if (film.getGenre().contains(genre.getName())) {
                                genre.setFavoriteCount(genre.getFavoriteCount() + 1);
                            }
                        }
                    }
                }
            }
        }
    }

    private void sortFavoriteCountOptionGenre() {
        if (sort.equals("DESC")) {
            genreList.sort((g1, g2) -> g2.getFavoriteCount() - g1.getFavoriteCount());
        }
        else if (sort.equals("ASC")) {
            genreList.sort(Comparator.comparingInt(GenreModel::getFavoriteCount));
        }
    }

    private void getShareCountOptionGenre() {
        resetCountOptionGenre();
        for (UserModel user : userList) {
            if (user.getActions() != null ) {
                List<UserActions> actionsList = user.getActions();

                for (UserActions actions : actionsList) {
                    String filmId = actions.getFilmId();
                    int share = actions.getShare();
                    FilmModel film = getFilmByFilmID(filmId);
                    if (film != null && share > 0) {
                        for (GenreModel genre : genreList) {
                            if (film.getGenre().contains(genre.getName())) {
                                genre.setShareCount(genre.getShareCount() + share);
                            }
                        }
                    }
                }
            }
        }
    }

    private void sortShareCountOptionGenre() {
        if (sort.equals("DESC")) {
            genreList.sort((g1, g2) -> g2.getShareCount() - g1.getShareCount());
        }
        else if (sort.equals("ASC")) {
            genreList.sort(Comparator.comparingInt(GenreModel::getShareCount));
        }
    }

    private void getCommentCountOptionGenre() {
        resetCountOptionGenre();
        for (UserModel user : userList) {
            if (user.getActions() != null ) {
                List<UserActions> actionsList = user.getActions();

                for (UserActions actions : actionsList) {
                    String filmId = actions.getFilmId();
                    List<CommentModel> cmtList = actions.getCmts();
                    int cmt = 0;
                    if (cmtList != null) {
                        cmt = cmtList.size();
                    }

                    FilmModel film = getFilmByFilmID(filmId);
                    if (film != null && cmt > 0) {
                        for (GenreModel genre : genreList) {
                            if (film.getGenre().contains(genre.getName())) {
                                genre.setCommentCount(genre.getCommentCount() + cmt);
                            }
                        }
                    }
                }
            }
        }
    }

    private void sortCommentCountOptionGenre() {
        if (sort.equals("DESC")) {
            genreList.sort((g1, g2) -> g2.getCommentCount() - g1.getCommentCount());
        }
        else if (sort.equals("ASC")) {
            genreList.sort(Comparator.comparingInt(GenreModel::getCommentCount));
        }
    }

    private void getDownloadCountOptionGenre() {
        resetCountOptionGenre();
        for (UserModel user : userList) {
            if (user.getActions() != null ) {
                List<UserActions> actionsList = user.getActions();

                for (UserActions actions : actionsList) {
                    String filmId = actions.getFilmId();
                    int download = actions.getDownload();
                    FilmModel film = getFilmByFilmID(filmId);
                    if (film != null && download > 0) {
                        for (GenreModel genre : genreList) {
                            if (film.getGenre().contains(genre.getName())) {
                                genre.setDownloadCount(genre.getDownloadCount() + download);
                            }
                        }
                    }
                }
            }
        }
    }

    private void sortDownloadCountOptionGenre() {
        if (sort.equals("DESC")) {
            genreList.sort((g1, g2) -> g2.getDownloadCount() - g1.getDownloadCount());
        }
        else if (sort.equals("ASC")) {
            genreList.sort(Comparator.comparingInt(GenreModel::getDownloadCount));
        }
    }

    private FilmModel getFilmByFilmID(String filmId) {
        for (FilmModel film : filmList) {
            if (film.getId().equals(filmId)) {
                return film;
            }
        }
        return null;
    }

    private void resetCountOptionGenre() {
        for (GenreModel genre : genreList) {
            genre.setViewCount(0);
            genre.setFavoriteCount(0);
            genre.setShareCount(0);
            genre.setCommentCount(0);
            genre.setDownloadCount(0);
        }
    }

    private void getCountOptionFilm() {
        resetCountOptionFilm();
        for (UserModel user : userList) {
            if (user.getActions() != null ) {
                List<UserActions> actionsList = user.getActions();

                for (UserActions actions : actionsList) {
                    String filmId = actions.getFilmId();
                    int view = actions.getView();
                    int fav = actions.getFavorite();
                    int share = actions.getShare();
                    int download = actions.getDownload();
                    List<CommentModel> cmtList = actions.getCmts();
                    int cmt = 0;
                    if (cmtList != null) {
                        cmt = cmtList.size();
                    }
                    FilmModel film = getFilmByFilmID(filmId);

                    if (film != null) {
                        if (view > 0) {
                            film.setViewCount(film.getViewCount() + 1);
                        }
                        if (fav > 0) {
                            film.setFavoriteCount(film.getFavoriteCount() + 1);
                        }
                        if (share > 0) {
                            film.setShareCount(film.getShareCount() + 1);
                        }
                        if (download > 0) {
                            film.setDownloadCount(film.getDownloadCount() + 1);
                        }
                        if (cmt > 0) {
                            film.setCommentCount(film.getCommentCount() + cmt);
                        }
                    }
                }
            }
        }
    }

    private void sortCountOptionFilm(String topic) {
        filmsTemp.sort((f1, f2) -> {
            int sortResult = 0;
            if (sort.equals("DESC")) {
                if (topic.equals("View") || option.equals(getResources().getString(R.string.txt_views))) {
                    sortResult = f2.getViewCount() - f1.getViewCount();
                } else if (topic.equals("Favorite") || option.equals(getResources().getString(R.string.txt_likes))) {
                    sortResult = f2.getFavoriteCount() - f1.getFavoriteCount();
                } else if (topic.equals("Share") || option.equals(getResources().getString(R.string.txt_shares))) {
                    sortResult = f2.getShareCount() - f1.getShareCount();
                } else if (topic.equals("Comment") || option.equals(getResources().getString(R.string.txt_comments))) {
                    sortResult = f2.getCommentCount() - f1.getCommentCount();
                } else if (topic.equals("Download") || option.equals(getResources().getString(R.string.txt_downloads))) {
                    sortResult = f2.getDownloadCount() - f1.getDownloadCount();
                }
            }
            else {
                if (topic.equals("View") || option.equals(getResources().getString(R.string.txt_views))) {
                    sortResult = f1.getViewCount() - f2.getViewCount();
                } else if (topic.equals("Favorite") || option.equals(getResources().getString(R.string.txt_likes))) {
                    sortResult = f1.getFavoriteCount() - f2.getFavoriteCount();
                } else if (topic.equals("Share") || option.equals(getResources().getString(R.string.txt_shares))) {
                    sortResult = f1.getShareCount() - f2.getShareCount();
                } else if (topic.equals("Comment") || option.equals(getResources().getString(R.string.txt_comments))) {
                    sortResult = f1.getCommentCount() - f2.getCommentCount();
                } else if (topic.equals("Download") || option.equals(getResources().getString(R.string.txt_downloads))) {
                    sortResult = f1.getDownloadCount() - f2.getDownloadCount();
                }
            }

            return sortResult;
        });
    }

    private void filterMovieOptionFilm(List<FilmModel> filmsTemp) {
        filmsTemp.removeIf(film -> film.getType().equals("Series"));
    }

    private void filterSeriesOptionFilm(List<FilmModel> filmsTemp) {
        filmsTemp.removeIf(film -> film.getType().equals("Movie"));
    }

    private void filterGenreOptionFilm(List<FilmModel> filmsTemp) {
        String optionGenre = spSelectGenre.getSelectedItem().toString();
        filmsTemp.removeIf(film -> !film.getGenre().contains(optionGenre));
    }

    private void getCountOptionUser() {
        resetCountOptionUser();
        for (UserModel user : userList) {
            if (user.getActions() != null ) {
                List<UserActions> actionsList = user.getActions();

                for (UserActions actions : actionsList) {
                    String filmId = actions.getFilmId();
                    int view = actions.getView();
                    int fav = actions.getFavorite();
                    int share = actions.getShare();
                    int download = actions.getDownload();
                    List<CommentModel> cmtList = actions.getCmts();
                    int cmt = 0;
                    if (cmtList != null) {
                        cmt = cmtList.size();
                    }

                    if (view > 0) {
                        user.setViewCount(user.getViewCount() + 1);
                    }
                    if (fav > 0) {
                        user.setFavoriteCount(user.getFavoriteCount() + 1);
                    }
                    if (share > 0) {
                        user.setShareCount(user.getShareCount() + 1);
                    }
                    if (download > 0) {
                        user.setDownloadCount(user.getDownloadCount() + 1);
                    }
                    if (cmt > 0) {
                        user.setCommentCount(user.getCommentCount() + cmt);
                    }
                }
            }
        }
    }

    private void sortCountOptionUser() {
        userList.sort((u1, u2) -> {
            int sortResult = 0;
            if (sort.equals("DESC")) {
                if (option.equals(getResources().getString(R.string.txt_views))) {
                    sortResult = u2.getViewCount() - u1.getViewCount();
                } else if (option.equals(getResources().getString(R.string.txt_likes))) {
                    sortResult = u2.getFavoriteCount() - u1.getFavoriteCount();
                } else if (option.equals(getResources().getString(R.string.txt_shares))) {
                    sortResult = u2.getShareCount() - u1.getShareCount();
                } else if (option.equals(getResources().getString(R.string.txt_comments))) {
                    sortResult = u2.getCommentCount() - u1.getCommentCount();
                } else if (option.equals(getResources().getString(R.string.txt_downloads))) {
                    sortResult = u2.getDownloadCount() - u1.getDownloadCount();
                }
            }
            else {
                if (option.equals(getResources().getString(R.string.txt_views))) {
                    sortResult = u1.getViewCount() - u2.getViewCount();
                } else if (option.equals(getResources().getString(R.string.txt_likes))) {
                    sortResult = u1.getFavoriteCount() - u2.getFavoriteCount();
                } else if (option.equals(getResources().getString(R.string.txt_shares))) {
                    sortResult = u1.getShareCount() - u2.getShareCount();
                } else if (option.equals(getResources().getString(R.string.txt_comments))) {
                    sortResult = u1.getCommentCount() - u2.getCommentCount();
                } else if (option.equals(getResources().getString(R.string.txt_downloads))) {
                    sortResult = u1.getDownloadCount() - u2.getDownloadCount();
                }
            }

            return sortResult;
        });
    }

    private void resetCountOptionFilm() {
        for (FilmModel film : filmList) {
            film.setViewCount(0);
            film.setFavoriteCount(0);
            film.setShareCount(0);
            film.setCommentCount(0);
            film.setDownloadCount(0);
        }
    }

    private void resetCountOptionUser() {
        for (UserModel user : userList) {
            user.setViewCount(0);
            user.setFavoriteCount(0);
            user.setShareCount(0);
            user.setCommentCount(0);
            user.setDownloadCount(0);
        }
    }

    private void setControl(View view) {
        filmDatabase = FirebaseDatabase.getInstance();
        intent = getActivity().getIntent();
        lvStatList = view.findViewById(R.id.lv_stat_list);
        rvStatList = view.findViewById(R.id.rv_stat_list);
        spSelectGenre = view.findViewById(R.id.spSelectGenre);
        cvSelectGenre = view.findViewById(R.id.card_select_genre);
        context = getContext();
    }

    private void setEvent() {
        syncModelDatabase();

        spSelectGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String item = parent.getItemAtPosition(position).toString();
                if (filmList.size() != 0 && genreList.size() != 0 && userList.size() != 0) {
                    Bundle bundle = intent.getExtras();
                    assert bundle != null;
                    String content = bundle.getString("content");
                    filmsTemp.clear();
                    filmsTemp.addAll(filmList);
                    filterGenreOptionFilm(filmsTemp);
                    sortCountOptionFilm(content);
                    filmAdapter = new FilmAdapter(getContext(), filmsTemp, option, content);
                    rvStatList.setAdapter(filmAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cvSelectGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spSelectGenre.performClick();
            }
        });
    }

    private void syncModelDatabase() {
        DatabaseReference filmRef = filmDatabase.getReference("Film");
        DatabaseReference movieRef = filmDatabase.getReference("Movie");
        DatabaseReference seriesRef = filmDatabase.getReference("Series");
        DatabaseReference genreRef = filmDatabase.getReference("Genre");
        DatabaseReference userRef = filmDatabase.getReference("User");

        filmRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                filmList.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    FilmModel film = item.getValue(FilmModel.class);
                    filmList.add(film);
                }
                if (filmList.size() != 0 && genreList.size() != 0 && userList.size() != 0) {
                    setContent();
                }
//                if (filmList.size() > 0 && userList.size() == 0) {
//                    createUserData(filmDatabase.getReference().child("User"));
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });

        movieRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                movieList.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    MovieModel movie = item.getValue(MovieModel.class);
                    movieList.add(movie);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });

        seriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                seriesList.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    SeriesModel series = item.getValue(SeriesModel.class);
                    seriesList.add(series);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });

        genreRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                genreList.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    GenreModel genre = item.getValue(GenreModel.class);
                    genreList.add(genre);
                }
                if (filmList.size() != 0 && genreList.size() != 0 && userList.size() != 0 && getContext() != null) {
                    setContent();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
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
                if (filmList.size() != 0 && genreList.size() != 0 && userList.size() != 0 && getActivity() != null) {
                    setContent();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });
    }

    private void setDatabase() {
//        DatabaseReference filmRef = filmDatabase.getReference().child("Film");
//        DatabaseReference movieRef = filmDatabase.getReference().child(getResources().getString(R.string.txt_movies));
//        DatabaseReference seriesRef = filmDatabase.getReference().child("Series");
//        createFilmData(filmRef, movieRef, seriesRef);
//        DatabaseReference genreRef = filmDatabase.getReference().child("Genre");
//        createGenreData(genreRef);
//        DatabaseReference movieRef = database.getReference().child(getResources().getString(R.string.txt_movies));
//        createMovieData(movieRef);
//        DatabaseReference seriesRef = database.getReference().child("Series");
//        createSeriesData(seriesRef);
//        syncModelDatabase(database);
//        DatabaseReference userRef = filmDatabase.getReference().child("User");
//        createUserData(userRef);
    }

    private void createFilmData(DatabaseReference filmRef,
                                DatabaseReference mvRef,
                                DatabaseReference srRef) {

        List<FilmModel> films = new ArrayList<>();
        List<MovieModel> movies = new ArrayList<>();
//        List<SeriesModel> seriesList = new ArrayList<>();

        films.add(new FilmModel("Người Nhện: Vũ trụ mới",
                "Một vũ trụ hoàn toàn mới, nơi Người nhện không chỉ mang cái tên Peter Parker mà " +
                        "có thể là bất kỳ ai. Sở hữu năng lực vượt trội, liệu các Người nhện có thể" +
                        " hợp tác và đánh bại thế lực hắc ám mới?",
                "T13", "Rodney Rothman, Peter Ramsey, Bob Persichetti",
                "Shameik Moore, Jake Johnson, Hailee Steinfeld, Lily Tomlin, Luna Lauren Velez",
                "Movie", "https://youtu.be/g4Hbz2jLxvQ?si=Ycl6rzIxAb0OTiMq",
                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FNg%C6%B0%E1%BB%9Di%20Nh%E1%BB%87n%3A%20V%C5%A9%20tr%E1%BB%A5%20m%E1%BB%9Bi%2Fspider-man-into-the-spider-verse-portrait-i66655.jpg?alt=media&token=069b865b-8ba2-46b7-9098-bcc6ea5ab634",
                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FNg%C6%B0%E1%BB%9Di%20Nh%E1%BB%87n%3A%20V%C5%A9%20tr%E1%BB%A5%20m%E1%BB%9Bi%2Fspider-man-into-the-spider-verse_tv_posterLandscape_decbdd0224f5b176d57937266630a706.jpg?alt=media&token=ee341087-45de-49ef-af75-abb7922f605c",
                "Gia đình, Hành động, Hoạt hình", 2018,
                0, 0, 0, 0, 0));


        films.add(new FilmModel("Người Nhện: Du hành vũ trụ nhện", "Nối tiếp phần đầu thành công vang dội " +
                "với giải Oscars danh giá, Miles Morales tái xuất trong phần tiếp theo khi phải đối đầu với " +
                "các Người Nhện khác và định nghĩa lại trách nhiệm của một người hùng.",
                "K", "Joaquim Dos Santos, Justin K. Thompson, Kemp Powers",
                "Shameik Moore, Hailee Steinfeld, Brian Tyree Henry",
                "Movie", "https://youtu.be/cqGjhVJWtEg?si=1neCy9d3Ebo-KiMs",
                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FNg%C6%B0%E1%BB%9Di%20Nh%E1%BB%87n%3A%20Du%20h%C3%A0nh%20v%C5%A9%20tr%E1%BB%A5%20nh%E1%BB%87n%2Fspider-man-across-the-spider-verse_mobile_posterLandscape_6170fb908afbba34dfafe8b3ae9b552b.jpg?alt=media&token=8ed18a82-07a4-4b4d-8c2c-79ecfe31f8dd",
                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FNg%C6%B0%E1%BB%9Di%20Nh%E1%BB%87n%3A%20Du%20h%C3%A0nh%20v%C5%A9%20tr%E1%BB%A5%20nh%E1%BB%87n%2Fspider-man-across-portrait.jpg?alt=media&token=ecec9237-1d13-4509-9d1a-478c37baf1bc",
                "Gia đình, Hành động, Hoạt hình", 2023,
                0, 0, 0, 0, 0));


        films.add(new FilmModel("Chuyến phiêu lưu trời ơi đất hỡi",
                "Cuộc sống của Tootson và Ludiwood " +
                        "đột nhiên đảo lộn vì họ bị đuổi ra khỏi đường hầm mình đang sống. Đứng trước cảnh vô gia " +
                        "cư, hai cậu bạn quyết tâm đi tìm người ông đáng kính để giành lại căn nhà.",
                "P", "Gunhild Enger, Rune Spaans",
                "John Brungot, Hermann Sabado, Per Inge Torkelsen",
                "Movie", "https://youtu.be/4q8S0DAbYxA?si=3bEcPlHk75XSNHE1",
                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FChuy%E1%BA%BFn%20phi%C3%AAu%20l%C6%B0u%20tr%E1%BB%9Di%20%C6%A1i%20%C4%91%E1%BA%A5t%20h%E1%BB%A1i%2FwbFAZu3Sex7Rx0kvJrzEynMb5PP-portrait.jpg?alt=media&token=b221d65b-52bd-469c-b4c5-469447c1bde3",
                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FChuy%E1%BA%BFn%20phi%C3%AAu%20l%C6%B0u%20tr%E1%BB%9Di%20%C6%A1i%20%C4%91%E1%BA%A5t%20h%E1%BB%A1i%2Ftwo-buddies-and-a-badger-the-great-big-beast_web_posterLandscape_0c4fa174b40b38276e9b45f5bf55b59b.jpg?alt=media&token=ebef6f94-ddf7-4979-8729-0a94d3733628",
                "Gia đình, Hành động, Hoạt hình, Phiêu lưu", 2020,
                0, 0, 0, 0, 0));


        films.add(new FilmModel("Bài ca Vivu Vivo",
                "Để hoàn thành tâm nguyện của ông Andrés, chú gấu yêu âm nhạc Vivo cùng cô bé Gabi bắt đầu" +
                        " chuyến phiêu lưu \"bất ổn\" để trao tận tay bản tình ca của ông cho người bạn cũ.",
                "P", "Kirk De Micco",
                "Lin-Manuel Miranda, Juan de Marcos González, Gloria Estefan",
                "https://youtu.be/BOe8L69JpVI?si=gHRXMvzI3fHIaO0z",
                "Movie",
                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FB%C3%A0i%20ca%20Vivu%20Vivo%2FeRLlrhbdYE7XN6VtcZKy6o2BsOw-portrait.jpg?alt=media&token=ddf28b25-ae21-4a62-aeaf-97357906ec54",
                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FB%C3%A0i%20ca%20Vivu%20Vivo%2Fvivo_web_posterLandscape_a6f1a8c5af75b4c748e3ec9d17ba22f2.jpg?alt=media&token=3c43b60f-3a5a-4f2c-a25f-eecd9f9124dd",
                "Gia đình, Hành động, Hoạt hình, Phiêu lưu", 2021,
                0, 0, 0, 0, 0));


        films.add(new FilmModel("Thỏ Peter 2: Cuộc trốn chạy",
                "Trở thành \"sao\" nhưng mối quan hệ của Thomas và thỏ Peter vẫn chẳng cải thiện chút nào." +
                        " Vì thế mà Peter quyết định... bỏ nhà đi bụi để dấn thân vào " +
                        "những chuyến phiêu lưu mới.",
                "P", "Will Gluck",
                "Rose Byrne, Domhnall Gleeson, David Oyelowo",
                "Movie",
                "https://youtu.be/XDSbhWVxNIw?si=tNNI3OhiZUnT-4-H",
                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FTh%E1%BB%8F%20Peter%202%3A%20Cu%E1%BB%99c%20tr%E1%BB%91n%20ch%E1%BA%A1y%2FsU9lyhTQvzWrjqULcD8tr3KUvK0-portrait.jpg?alt=media&token=1877f690-ca7a-45e7-9a42-56a5cb1e7d25",
                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FTh%E1%BB%8F%20Peter%202%3A%20Cu%E1%BB%99c%20tr%E1%BB%91n%20ch%E1%BA%A1y%2Fpeter-rabbit-2-landscape.jpg?alt=media&token=f0d87728-ca01-4c82-bbc0-3755e041cb66",
                "Gia đình, Hành động, Hoạt hình", 2021,
                0, 0, 0, 0, 0));


        films.add(new FilmModel("Mèo siêu quậy ở viện bảo tàng",
                "Chuột và mèo mà là bạn thì sẽ ra sao? Đó chính là tình cảnh của đôi bạn mèo Vincent - chuột Maurice." +
                        " Lý tưởng khác biệt, nhưng cả hai phải cùng chống lại kẻ muốn cướp lấy tuyệt tác Mona Lisa.",
                "P", "Vasiliy Rovenskiy",
                "Anton Eldarov, Polina Gagarina, Aleksandr Gavrilin",
                "Movie",
                "https://youtu.be/cAxXxxBBzOQ?si=j5muDP_lf3dWUbhr",
                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FM%C3%A8o%20si%C3%AAu%20qu%E1%BA%ADy%20%E1%BB%9F%20vi%E1%BB%87n%20b%E1%BA%A3o%20t%C3%A0ng%2FMV5BNjM1NzM5MzktNWVkMC00YjhlLWFlYzYtMDM3MDJmZDYwMGEwXkEyXkFqcGdeQXVyOTYwNDg4ODU%40._V1_-portrait.jpg?alt=media&token=e0ba6a69-9781-424e-9eae-0694d6613cb3",
                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FM%C3%A8o%20si%C3%AAu%20qu%E1%BA%ADy%20%E1%BB%9F%20vi%E1%BB%87n%20b%E1%BA%A3o%20t%C3%A0ng%2F0001-4451081899700530271-landscape.jpg?alt=media&token=a5b4c51b-c5bb-4f65-a3f1-5323a55042c2",
                "Gia đình, Hoạt hình", 2023,
                0, 0, 0, 0, 0));


        films.add(new FilmModel("Mirai: Em gái đến từ tương lai",
                "Câu chuyện đáng yêu, đầy diệu kỳ của cậu bé Kun đã vinh dự được đề cử tại Oscar 2019. " +
                        "Đó là quá trình dở khóc dở cười của cậu khi phải chấp nhận san sẻ tình cảm của " +
                        "cha mẹ với em gái mới chào đời.",
                "P", "Mamoru Hosoda",
                "Haru Kuroki, Moka Kamishiraishi, Gen Hoshino",
                "Movie",
                "https://youtu.be/0RH2U6Ls9QI?si=uiassuKRFsyVM9UJ",
                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FMirai%3A%20Em%20g%C3%A1i%20%C4%91%E1%BA%BFn%20t%E1%BB%AB%20t%C6%B0%C6%A1ng%20lai%2Fanime-mirai-em-gai-den-tu-tuong-lai-3-696x981-portrait.jpg?alt=media&token=7c9d0b33-abb2-45cb-8185-84cd84d2ee5e",
                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FMirai%3A%20Em%20g%C3%A1i%20%C4%91%E1%BA%BFn%20t%E1%BB%AB%20t%C6%B0%C6%A1ng%20lai%2Fimage_06a97da3da-landscape.png?alt=media&token=b760878b-3fb4-481f-bbb1-7bcbabdd6752",
                "Gia đình, Hoạt hình", 2018,
                0, 0, 0, 0, 0));


        films.add(new FilmModel("Mặt trời nửa đêm",
                "Katie mắc một căn bệnh kì lạ, khiến cô không thể tiếp xúc với ánh sáng mặt trời. " +
                        "Thế nhưng, định mệnh sắp đặt cho cô gặp Charlie, chàng trai cô yêu thầm và ngắm nhìn" +
                        " hàng ngày qua khung cửa sổ.",
                "T13", "Scott Speer",
                "Bella Thorne, Patrick Schwarzenegger, Rob Riggle",
                "Movie",
                "https://youtu.be/fEskVQgtwaI?si=Ixw79mjLpTu-nsPC",
                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FM%E1%BA%B7t%20tr%E1%BB%9Di%20n%E1%BB%ADa%20%C4%91%C3%AAm%2FMidnightSun_PosterArt-portrait.jpg?alt=media&token=efe669b6-757a-4821-91b4-f8d1993e6f4e",
                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FM%E1%BA%B7t%20tr%E1%BB%9Di%20n%E1%BB%ADa%20%C4%91%C3%AAm%2Fmidnight-sun-landscape.jpg?alt=media&token=7111bd6c-8efa-43fd-ae9a-b2966a518bf3",
                "Lãng mạn, Tâm lý, Tình cảm", 2018,
                0, 0, 0, 0, 0));

        films.add(new FilmModel("Quỷ dữ",
                "Khắc họa một vụ bạo lực học đường qua ba góc nhìn của người liên quan, bộ phim mới nhất của " +
                        "Kore-eda mở ra những trăn trở về sự tổn thương, áp lực thời đại để ta thấu hiểu " +
                        "được \"ai mới là quỷ dữ?\".",
                "T13", "Hirokazu Koreeda",
                "Sakura Ando, Eita Nagayama, Soya Kurokawa",
                "Movie",
                "https://youtu.be/JYIRWnnatBU?si=se-DHt3lRvPLj2tR",
                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FQu%E1%BB%B7%20d%E1%BB%AF%2FkvUJUyUGOhEoiWWNH04IXoExPE2-portrait.jpg?alt=media&token=b350b57f-87de-4276-8a69-0759e1687c36",
                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FQu%E1%BB%B7%20d%E1%BB%AF%2Fmonster_mobile_posterLandscape_5403abd761dfaea4334a56b640a30b42-landscape.jpg?alt=media&token=3d104ef1-c4c3-4ced-a4e0-86413ef3044f",
                "Tâm lý, Tình cảm", 2023,
                0, 0, 0, 0, 0));


        films.add(new FilmModel("Lẩn trốn",
                "Trong lúc Na Moon Young không ngừng tìm kiếm người chồng đang mất tích, Do Jin Woo đã xuất hiện" +
                        " với chìa khóa vụ án. Liệu Moon Young có đủ sức chịu đựng khi bức màn sự thật vén lên?",
                "T18", "Kim Dong Hwi",
                "Lee Bo Young, Lee Mu Saeng, Lee Chung Ah",
                "Series",
                "https://youtu.be/WqDytDxu5Nw?si=GomUun318HjRzp1C",
                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FL%E1%BA%A9n%20tr%E1%BB%91n%2Fharper-bazaar-review-phim-lan-tron-hide-6-portrait.jpeg?alt=media&token=265bd057-8ab1-442b-a8b9-3a36d26be94f",
                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FL%E1%BA%A9n%20tr%E1%BB%91n%2Fnzc7tbey_1920x1080-lantron_1267_712-landscape.jpg?alt=media&token=ee32947f-aa1a-4bca-a3fd-221b1f5c2cc5",
                "Tâm lý", 2024,
                0, 0, 0, 0, 0));

        films.add(new FilmModel("Mất tích đêm 30",
                "Cuộc sống bình dị của gia đình bà Hòa và ông Vinh bị phá vỡ khi cô con gái mất tích vào đêm giao thừa." +
                        " Sau nỗ lực tự tìm kiếm con trong vô vọng, bà Hòa quyết định livestream nhờ công chúng giúp đỡ.",
                "T16", "Hàm Trần",
                "Kiều Trinh, Lê Huỳnh, Lý Hồng Ân",
                "Series",
                "https://youtu.be/-H6wmo8WGlI?si=rSvD8boULUk7UR0Q",
                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FM%E1%BA%A5t%20t%C3%ADch%20%C4%91%C3%AAm%2030%2F425472591_765599672286079_777632246466947741_n-portrait.jpg?alt=media&token=3b300ca2-58b2-4273-81f2-b74fc5db864d",
                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FM%E1%BA%A5t%20t%C3%ADch%20%C4%91%C3%AAm%2030%2Fmat_tich_dem_30_20240222143610-landscape.jpg?alt=media&token=502dc6f8-2d41-46cf-9f05-ddb7ade19c86",
                "Phim Việt, Tâm lý", 2024,
                0, 0, 0, 0, 0));
        films.add(new FilmModel("Hậu duệ mặt trời",
                "Chuyện tình của chàng đại úy Yoo Shi Jin và nữ bác sĩ Kang Mo Yeon đã tạo nên cơn sốt trên toàn châu Á. " +
                        "Không chỉ về tình yêu, phim còn cài cắm nhiều thông điệp về cuộc sống và tình yêu nước.",
                "T13", "Lee Eung Bok, Baek Sang Hoon",
                "Song Joong Ki, Song Hye Kyo, Jin Goo",
                "Series",
                "https://youtu.be/z7GTGBBbV2w?si=tHBznXQ9ex8AcQAy",
                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FH%E1%BA%ADu%20du%E1%BB%87%20m%E1%BA%B7t%20tr%E1%BB%9Di%2FDescendants_of_the_Sun-p1-portrait.jpg?alt=media&token=04251508-e843-44d2-b9eb-08c164ac5c8c",
                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FH%E1%BA%ADu%20du%E1%BB%87%20m%E1%BA%B7t%20tr%E1%BB%9Di%2F1jhhbn2x_1920x1080-carousel-hauduemattroi-landscape.jpg?alt=media&token=84d4b660-1e3d-413f-b762-27eeb7e0866f",
                "Hành động, Chiến tranh, Lãng mạn", 2016,
                0, 0, 0, 0, 0));
        films.add(new FilmModel("Hùng Long Phong Bá 2",
                "Sau cái \"chết\" của Hùng, Long Phong Bá rút khỏi thế giới ngầm để sống cuộc đời bình yên. " +
                        "Nhưng, thế lực mới nổi lên với những màn thanh trừng tàn độc, một lần nữa kéo " +
                        "họ vào con đường bóng tối.",
                "T18", "'Toni Dương Bảo Anh",
                "Steven Nguyễn, Võ Đình Hiếu, Trầm Minh Hoàng",
                "Series",
                "https://youtu.be/oZel47W6E1g?si=i8Ta5P4kuzL8PNQg",
                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FH%C3%B9ng%20Long%20Phong%20B%C3%A1%202%2F325729961_1402559110484525_5540358474760937268_n-portrait.jpg?alt=media&token=f12af969-2ca4-43b3-b3e5-52e1c2d00487",
                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FH%C3%B9ng%20Long%20Phong%20B%C3%A1%202%2Fmaxresdefault-landscape.jpg?alt=media&token=9033aef3-0be4-42b8-9242-4fc4b7eb1a3c",
                "Phim Việt, Hành động, Tội phạm", 2023,
                0, 0, 0, 0, 0));

        films.add(new FilmModel("You",
                "Người quản lý hiệu sách dùng internet và mạng xã hội để (tiến cận) một tác giả đầy tham vọng." +
                        " Tình cảm đơn phương bất an nhanh chóng biến thành ám ảnh khi anh ta loại bỏ mọi " +
                        "trở ngại - và cả những người - cản đường anh ta đạt được mục đích.",
                "T18", "Penn Badgley, Victoria Pedretti, Elizabeth Lail",
                "Greg Berlanti, Sera Gamble",
                "Series",
                "https://youtu.be/srx7fSBwvF4?si=nmDZdMkqd43acJyk",
                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FYou%2F1-1546254298208203420949-portrait.jpg?alt=media&token=fe2962d4-03be-4a2b-a9e2-d1e6278e190e",
                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FYou%2Fyou-poster-netflix-landscape.jpg?alt=media&token=b796bee1-f553-4324-89ed-46a5b5edd8a6",
                "Lãng mạn, Tâm lý, Tình cảm, Tội phạm", 2018,
                0, 0, 0, 0, 0));

        for (FilmModel film : films) {
            DatabaseReference newFilmRef = filmRef.push(); // Tạo khóa tự động và lưu tham chiếu
            String filmKey = newFilmRef.getKey(); // Lấy key được tạo ra
            film.setId(filmKey); // Đặt key cho đối tượng FilmModel

            newFilmRef.setValue(film); // Lưu đối tượng FilmModel vào cơ sở dữ liệu
        }

        movies.add(new MovieModel(films.get(0).getId(), 112));
        movies.add(new MovieModel(films.get(1).getId(), 135));
        movies.add(new MovieModel(films.get(2).getId(), 77));
        movies.add(new MovieModel(films.get(3).getId(), 92));
        movies.add(new MovieModel(films.get(4).getId(), 90));
        movies.add(new MovieModel(films.get(5).getId(), 77));
        movies.add(new MovieModel(films.get(6).getId(), 98));
        movies.add(new MovieModel(films.get(7).getId(), 92));
        movies.add(new MovieModel(films.get(8).getId(), 127));

        for (MovieModel movie : movies) {
            mvRef.push().setValue(movie);
        }

//        seriesList.add(new SeriesModel(films.get(9).getId(), "",
//                "Na Moon Young đau khổ khi nhận thi thể của Cha Seong Jae.",
//                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FL%E1%BA%A9n%20tr%E1%BB%91n%2Fthumbnail%2Fhide_episode_1_6d29841717d0e2f806cdb5f42404703a.jpg?alt=media&token=417fe165-25d5-4f7d-a6d0-62b12d83e21b",
//                1, 60));
//        seriesList.add(new SeriesModel(films.get(9).getId(), "",
//                "Na Moon Young liên hệ với Do Jin Woo điều tra bí ẩn sau tai nạn.",
//                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FL%E1%BA%A9n%20tr%E1%BB%91n%2Fthumbnail%2Fhide_episode_2_96d60d53ba6d75d4e4f742b1e9e3430b.jpg?alt=media&token=f0a5c995-bbae-4a35-be1b-7ce586c854f2",
//                2, 60));
//        seriesList.add(new SeriesModel(films.get(9).getId(), "",
//                "Do Jin Woo tố cáo Cha Seong Jae là người đã bắn mình. Cha Seong Jae thật sự còn sống?",
//                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FL%E1%BA%A9n%20tr%E1%BB%91n%2Fthumbnail%2Fhide_episode_3_5674d57bfb1b2818e0cb32341ecb630b.jpg?alt=media&token=7c77deb9-039e-4468-a00c-15059411ebb3",
//                3, 60));
//        seriesList.add(new SeriesModel(films.get(9).getId(), "",
//                "Na Moon Young và Cha Seong Jae gặp nhau.",
//                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FL%E1%BA%A9n%20tr%E1%BB%91n%2Fthumbnail%2Fhide_episode_4_889c2366d3805d6c8c278d74f5765835.jpg?alt=media&token=ab18dc2b-c9ab-411b-82c1-d895421acd7b",
//                4, 60));
//        seriesList.add(new SeriesModel(films.get(9).getId(), "",
//                "Ha Yeon Joo đe dọa Na Moon Young sẽ tiết lộ sự thật về Cha Seong Jae.",
//                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FL%E1%BA%A9n%20tr%E1%BB%91n%2Fthumbnail%2Fhide_episode_5_acdbc8c429c7869983a24dfdfa736a6b.jpg?alt=media&token=e620f671-c6a3-4992-96ce-0a361b348b40",
//                5, 60));
//        seriesList.add(new SeriesModel(films.get(9).getId(), "",
//                "Na Moon Young phát hiện Ha Yeon Joo và Cha Seong Jae ngoại tình.",
//                "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Film%2FL%E1%BA%A9n%20tr%E1%BB%91n%2Fthumbnail%2Fhide_episode_6_ec62091e0f3f35348b66d617ff031707.jpg?alt=media&token=100b9d0d-15a9-478c-aea4-2c6d41ef84ad",
//                6, 60));
    }

    private void createSeriesData(DatabaseReference srRef) {
//        List<SeriesModel> seriesList = new ArrayList<>();
//
//        for (SeriesModel series : seriesList) {
//            srRef.push().setValue(series);
//        }
    }

    private void createUserData(DatabaseReference userRef) {
//        List<UserModel> users = new ArrayList<>();
//        List<UserActions> actions = new ArrayList<>();
//        List<CommentModel> cmts = new ArrayList<>();
//        List<UserActions> actions2 = new ArrayList<>();
//        List<CommentModel> cmts2 = new ArrayList<>();
//        List<UserActions> actions3 = new ArrayList<>();
//        List<CommentModel> cmts3 = new ArrayList<>();
//        List<UserActions> actions4 = new ArrayList<>();
//        List<CommentModel> cmts4 = new ArrayList<>();
//        List<UserActions> actions5 = new ArrayList<>();
//        List<CommentModel> cmts5 = new ArrayList<>();
//        List<UserActions> actions6 = new ArrayList<>();
//        List<CommentModel> cmts6 = new ArrayList<>();
//        List<UserActions> actions7 = new ArrayList<>();
//        List<CommentModel> cmts7 = new ArrayList<>();
//        List<UserActions> actions8 = new ArrayList<>();
//        List<CommentModel> cmts8 = new ArrayList<>();
//        List<UserActions> actions9 = new ArrayList<>();
//        List<CommentModel> cmts9 = new ArrayList<>();
//        List<UserActions> actions10 = new ArrayList<>();
//        List<CommentModel> cmts10 = new ArrayList<>();
//
//        cmts.add(new CommentModel("Phim hay", new Date()));
//        actions.add(new UserActions(filmList.get(0).getId(), 1, 1, 2, 3, cmts));
//        actions.add(new UserActions(filmList.get(13).getId(), 1, 0, 1, 1, null));
//        users.add(new UserModel("John", "password123",
//                "john@example.com", "1234567890", actions));
//
//        actions2.add(new UserActions(filmList.get(9).getId(), 1, 0, 0, 0, null));
//        users.add(new UserModel("Alice", "alicepass",
//                "alice@example.com", "9876543210", actions2));
//
//        actions3.add(new UserActions(filmList.get(0).getId(), 1, 0, 0, 0, null));
//        actions3.add(new UserActions(filmList.get(13).getId(), 1, 1, 5, 1, null));
//        actions3.add(new UserActions(filmList.get(9).getId(), 1, 1, 5, 1, null));
//        users.add(new UserModel("Emma", "emmaworld",
//                "emma@example.com", "5551234567", actions3));
//        actions3.clear();
//
//        actions4.add(new UserActions(filmList.get(3).getId(), 1, 0, 0, 0, null));
//        users.add(new UserModel("Michael", "mikepass",
//                "michael@example.com", "9879879876", actions4));
//        actions4.clear();
//
//        cmts5.add(new CommentModel("Thật thứ vị", new Date()));
//        actions5.add(new UserActions(filmList.get(13).getId(), 1, 1, 1, 1, cmts5));
//        users.add(new UserModel("Sophia", "sophiapass",
//                "sophia@example.com", "1237894560", actions5));
//
//        cmts6.add(new CommentModel("Coi buồn ngủ quá", new Date()));
//        actions6.add(new UserActions(filmList.get(4).getId(), 1, 0, 0, 0, cmts6));
//        users.add(new UserModel("William", "willpass",
//                "william@example.com", "4567890123", actions6));
//        actions6.clear();
//        cmts6.clear();
//
//        cmts7.add(new CommentModel("Đáng yêu ghê", new Date()));
//        actions7.add(new UserActions(filmList.get(6).getId(), 1, 1, 0, 0, cmts7));
//        users.add(new UserModel("Olivia", "oliviapass",
//                "olivia@example.com", "7890123456", actions7));
//        actions7.clear();
//        cmts7.clear();
//
//        users.add(new UserModel("James", "jamespass",
//                "james@example.com", "3216549870", null));
//        users.add(new UserModel("Benjamin", "benpass",
//                "benjamin@example.com", "9870123456", null));
//        users.add(new UserModel("Isabella", "isapass",
//                "isabella@example.com", "6549873210", null));
//
//        for (UserModel user : users) {
//            userRef.push().setValue(user);
//        }
    }

    private void createGenreData(DatabaseReference genreRef) {
        List<GenreModel> genres = new ArrayList<>();

        genres.add(new GenreModel("Gia đình", "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Genre%2Ftop_genre_giadinh.jpg?alt=media&token=dca7a177-4ebd-4253-a3e4-b2c5bceaac2f",
                0, 0, 0, 0, 0, 0));
        genres.add(new GenreModel("Tâm lý", "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Genre%2Ftop_genre_tamly.jpg?alt=media&token=23521336-1aab-41bd-a9ed-07b1b9b45cb9",
                0, 0, 0, 0, 0, 0));
        genres.add(new GenreModel("Hành động", "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Genre%2Ftop_genre_hanhdong.jpg?alt=media&token=a21de7eb-04b2-4bda-a2ba-674277032427",
                0, 0, 0, 0, 0, 0));
        genres.add(new GenreModel("Hài", "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Genre%2Ftop_genre_hai.jpg?alt=media&token=72477fc7-0899-42ee-b76f-dddd7465cd12",
                0, 0, 0, 0, 0, 0));
        genres.add(new GenreModel("Phim Việt", "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Genre%2Ftop_genre_phimviet.jpg?alt=media&token=3bf29c2b-c928-4992-8d5d-f63be7215865",
                0, 0, 0, 0, 0, 0));
        genres.add(new GenreModel("Kiếm hiệp", "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Genre%2Ftop_genre_kiemhiep.jpg?alt=media&token=d3e0235f-e3f8-47d2-addf-e1e5e6ebac19",
                0, 0, 0, 0, 0, 0));
        genres.add(new GenreModel("Lãng mạn", "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Genre%2Ftop_genre_langman.jpg?alt=media&token=30844379-997f-4afb-a503-22dfc218b226",
                0, 0, 0, 0, 0, 0));
        genres.add(new GenreModel("Tài liệu", "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Genre%2Ftop_genre_tailieu.jpg?alt=media&token=3a75b15a-8df8-4d1a-9b52-a41c853f0ebb",
                0, 0, 0, 0, 0, 0));
        genres.add(new GenreModel("Cổ trang", "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Genre%2Ftop_genre_cotrang.jpg?alt=media&token=05472322-e321-4e19-9f57-c1670c607731",
                0, 0, 0, 0, 0, 0));
        genres.add(new GenreModel("Kinh dị", "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Genre%2Ftop_genre_kinhdi.jpg?alt=media&token=ea6d57f7-147f-453c-9f09-9e8a3d6b2807",
                0, 0, 0, 0, 0, 0));
        genres.add(new GenreModel("Web series", "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Genre%2Ftop_genre_webseries.jpg?alt=media&token=98dd203f-66ea-41c2-80e4-0108b84f9409",
                0, 0, 0, 0, 0, 0));
        genres.add(new GenreModel("Anime", "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Genre%2Ftop_genre_anime.jpg?alt=media&token=53645c09-9006-4a5c-ab1e-1b0691792a85",
                0, 0, 0, 0, 0, 0));
        genres.add(new GenreModel("Khoa học viễn tưởng", "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Genre%2Ftop_genre_khoahocvientuong.jpg?alt=media&token=29a93b42-dd52-463a-8ecd-79276b547f05",
                0, 0, 0, 0, 0, 0));
        genres.add(new GenreModel("Tiểu sử", "https://firebasestorage.googleapis.com/v0/b/btgk-99058.appspot.com/o/Genre%2Ftop_genre_tieu_su.jpg?alt=media&token=0d9c67df-c08d-40b0-965d-0be4caf384f6",
                0, 0, 0, 0, 0, 0));

        for (GenreModel genre : genres) {
            genreRef.push().setValue(genre);
        }
    }

    private void createCallBackUpdateGenreCountOption() {
        DatabaseReference genreRef = filmDatabase.getReference("Genre");
        for (GenreModel genre : genreList) {
            // Thực hiện một tác vụ không đồng bộ
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    // Kiểm tra xem genre đã tồn tại trên Firebase hay chưa
                    Query query = genreRef.orderByChild("name").equalTo(genre.getName());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Nếu genre đã tồn tại, cập nhật dữ liệu
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String key = snapshot.getKey();
                                    genreRef.child(key).setValue(genre);
                                }
                            } else {
                                // Nếu genre chưa tồn tại, thêm mới
                                String key = genreRef.push().getKey();
                                genreRef.child(key).setValue(genre);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Xảy ra lỗi khi truy vấn dữ liệu
                        }
                    });
                }
            });
        }
        executorService.shutdown();
    }
}