package com.example.btgk.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.btgk.Adapter.FilmAdapter;
import com.example.btgk.Model.CommentModel;
import com.example.btgk.Model.FilmModel;
import com.example.btgk.Model.GenreModel;
import com.example.btgk.Model.MovieModel;
import com.example.btgk.Model.SeriesModel;
import com.example.btgk.Model.UserActions;
import com.example.btgk.Model.UserModel;
import com.example.btgk.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrototypeStatChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrototypeStatChartFragment extends Fragment {
    PieChart pieChart;
    BarChart barChart;
    ScatterChart scatterChart;
    BubbleChart bubbleChart;
    List<FilmModel> filmList = new ArrayList<>();
    List<FilmModel> filmsTemp = new ArrayList<>();
    List<MovieModel> movieList = new ArrayList<>();
    List<SeriesModel> seriesList = new ArrayList<>();
    List<GenreModel> genreList = new ArrayList<>();
    List<UserModel> userList = new ArrayList<>();
    FirebaseDatabase filmDatabase;
    Intent intent;
    Context context;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    CardView cvSelectGenre;
    Spinner spSelectGenre;

    private String option, optionChart;
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

    public void setOptionChart(String optionChart) {
        this.optionChart = optionChart;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prototype_stat_chart, container, false);
        setControl(view);
        setEvent();
        return view;
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
                    if (content != null) {
                        filmsTemp.clear();
                        filmsTemp.addAll(filmList);
                        filterGenreOptionFilm(filmsTemp);
                        if (filmsTemp.size() == 0) {
                            pieChart.clear();
                            barChart.clear();
                        }
                        sortCountOptionFilm(content);
                        if (optionChart.equals(getResources().getString(R.string.txt_pie_chart))) {
                            if (!pieChart.isShown()) {
                                pieChart.setVisibility(View.VISIBLE);
                                barChart.setVisibility(View.GONE);
                            }
                            createPieChartCountFilmOption();
                        }
                        else if (optionChart.equals(getResources().getString(R.string.txt_bar_chart))) {
                            if (!barChart.isShown()) {
                                barChart.setVisibility(View.VISIBLE);
                                pieChart.setVisibility(View.GONE);
                            }
                            createBarChartCountFilmOption();
                        }
                    }
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

    private void setControl(View view) {
        filmDatabase = FirebaseDatabase.getInstance();
        intent = getActivity().getIntent();
        context = getContext();
        pieChart = view.findViewById(R.id.pieChart);
        barChart = view.findViewById(R.id.barChart);
//        scatterChart = view.findViewById(R.id.scatterChart);
//        bubbleChart = view.findViewById(R.id.bubbleChart);
        cvSelectGenre = view.findViewById(R.id.card_select_genre);
        spSelectGenre = view.findViewById(R.id.spSelectGenre);
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

                if (optionChart.equals(getResources().getString(R.string.txt_pie_chart))) {
                    if (!pieChart.isShown()) {
                        pieChart.setVisibility(View.VISIBLE);
                        barChart.setVisibility(View.GONE);
                    }
                    createPieChartCountFilmOption();
                }
                else if (optionChart.equals(getResources().getString(R.string.txt_bar_chart))) {
                    if (!barChart.isShown()) {
                        barChart.setVisibility(View.VISIBLE);
                        pieChart.setVisibility(View.GONE);
                    }
                    createBarChartCountFilmOption();
                }
            }
            else if (content.equals("TotalAccount")) {
                getCountOptionUser();
                sortCountOptionUser();

                if (optionChart.equals(getResources().getString(R.string.txt_pie_chart))) {
                    if (!pieChart.isShown()) {
                        pieChart.setVisibility(View.VISIBLE);
                        barChart.setVisibility(View.GONE);
                    }
                    createPieChartCountUserOption();
                }
                else if (optionChart.equals(getResources().getString(R.string.txt_bar_chart))) {
                    if (!barChart.isShown()) {
                        barChart.setVisibility(View.VISIBLE);
                        pieChart.setVisibility(View.GONE);
                    }
                    createBarChartCountUserOption();
                }
            }
            else if (content.equals("TopGenre")) {
                if (option.equals(getResources().getString(R.string.txt_film_amount))) {
                    getFilmCountGenreOption();
                    sortFilmCountGenreOption();
                }
                else if (this.getOption().equals(getResources().getString(R.string.txt_views))) {
                    getViewCountGenreOption();
                    sortViewCountGenreOption();
                }
                else if (this.getOption().equals(getResources().getString(R.string.txt_likes))) {
                    getFavoriteCountGenreOption();
                    sortFavoriteCountGenreOption();
                }
                else if (this.getOption().equals(getResources().getString(R.string.txt_shares))) {
                    getShareCountGenreOption();
                    sortShareCountGenreOption();
                }
                else if (this.getOption().equals(getResources().getString(R.string.txt_comments))) {
                    getCommentCountGenreOption();
                    sortCommentCountGenreOption();
                }
                else if (this.getOption().equals(getResources().getString(R.string.txt_downloads))) {
                    getDownloadCountGenreOption();
                    sortDownloadCountGenreOption();
                }

                if (optionChart.equals(getResources().getString(R.string.txt_pie_chart))) {
                    if (!pieChart.isShown()) {
                        pieChart.setVisibility(View.VISIBLE);
                        barChart.setVisibility(View.GONE);
                    }
                    createPieChartCountGenreOption();
                }
                else if (optionChart.equals(getResources().getString(R.string.txt_bar_chart))) {
                    if (!barChart.isShown()) {
                        barChart.setVisibility(View.VISIBLE);
                        pieChart.setVisibility(View.GONE);
                    }
                    createBarChartCountGenreOption();
                }
            }
            else {
                filmsTemp.clear();
                filmsTemp.addAll(filmList);
                getCountOptionFilm();
                sortCountOptionFilm(content);
                if (option.equals(getResources().getString(R.string.txt_all_films))) {
                }
                else if (this.getOption().equals(getResources().getString(R.string.txt_movies))) {
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
                    if (filmsTemp.size() == 0) {
                        pieChart.clear();
                        barChart.clear();
                    }
                }

                if (optionChart.equals(getResources().getString(R.string.txt_bar_chart))) {
                    if (!barChart.isShown()) {
                        barChart.setVisibility(View.VISIBLE);
                        pieChart.setVisibility(View.GONE);
                    }
                    createBarChartCountFilmOption();
                }
                else if (optionChart.equals(getResources().getString(R.string.txt_pie_chart))) {
                    if (!pieChart.isShown()) {
                        pieChart.setVisibility(View.VISIBLE);
                        barChart.setVisibility(View.GONE);
                    }
                    createPieChartCountFilmOption();
                }
            }
        }
    }

    private void createSelectGenreItem() {
        ArrayList<String> listItem = new ArrayList<>();
        for (GenreModel genre : genreList) {
            listItem.add(genre.getName());
        }
        if (getActivity() != null) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listItem);
            arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
            spSelectGenre.setAdapter(arrayAdapter);
        }
    }

    private void createPieChartCountGenreOption() {
        ArrayList<PieEntry> data =new ArrayList<>();
        if (genreList.size() > 0) {
            if (option.equals(getResources().getString(R.string.txt_film_amount))) {
                for (GenreModel genre : genreList) {
                    if (genre.getFilmCount() > 0) {
                        data.add(new PieEntry(genre.getFilmCount(), genre.getName()));
                    }
                }
            }
            else if (option.equals(getResources().getString(R.string.txt_views))) {
                for (GenreModel genre : genreList) {
                    if (genre.getViewCount() > 0) {
                        data.add(new PieEntry(genre.getViewCount(), genre.getName()));
                    }
                }
            }
            else if (option.equals(getResources().getString(R.string.txt_likes))) {
                for (GenreModel genre : genreList) {
                    if (genre.getFavoriteCount() > 0) {
                        data.add(new PieEntry(genre.getFavoriteCount(), genre.getName()));
                    }
                }
            }
            else if (option.equals(getResources().getString(R.string.txt_shares))) {
                for (GenreModel genre : genreList) {
                    if (genre.getShareCount() > 0) {
                        data.add(new PieEntry(genre.getShareCount(), genre.getName()));
                    }
                }
            }
            else if (option.equals(getResources().getString(R.string.txt_comments))) {
                for (GenreModel genre : genreList) {
                    if (genre.getCommentCount() > 0) {
                        data.add(new PieEntry(genre.getCommentCount(), genre.getName()));
                    }
                }
            }
            else if (option.equals(getResources().getString(R.string.txt_downloads))) {
                for (GenreModel genre : genreList) {
                    if (genre.getDownloadCount() > 0) {
                        data.add(new PieEntry(genre.getDownloadCount(), genre.getName()));
                    }
                }
            }
            PieDataSet pieDataSet =new PieDataSet(data, getResources().getString(R.string.txt_genres));
            pieDataSet.setColors(getColorDataChart());
            pieDataSet.setValueTextColor(Color.WHITE);
            pieDataSet.setValueTextSize(15);
            PieData pieData = new PieData(pieDataSet);
            pieChart.setData(pieData);
            pieChart.getDescription().setEnabled(false);
            pieChart.invalidate();
        }
    }

    private void createBarChartCountGenreOption() {
        ArrayList<BarEntry> data = new ArrayList<>();
        int yMax = 0;
        if (genreList.size() > 0) {
            int i = 1;
            List<String> genreNames = new ArrayList<>();
            genreNames.add("");
            if (option.equals(getResources().getString(R.string.txt_film_amount))) {
                for (GenreModel genre : genreList) {
                    if (yMax <= genre.getFilmCount()) {
                        yMax = genre.getFilmCount();
                    }
                    data.add(new BarEntry(i, genre.getFilmCount()));
                    genreNames.add(genre.getName());
                    i++;
                }
            }
            else if (option.equals(getResources().getString(R.string.txt_views))) {
                for (GenreModel genre : genreList) {
                    if (yMax <= genre.getViewCount()) {
                        yMax = genre.getViewCount();
                    }
                    data.add(new BarEntry(i, genre.getViewCount()));
                    genreNames.add(genre.getName());
                    i++;
                }
            }
            else if (option.equals(getResources().getString(R.string.txt_likes))) {
                for (GenreModel genre : genreList) {
                    if (yMax <= genre.getFavoriteCount()) {
                        yMax = genre.getFavoriteCount();
                    }
                    data.add(new BarEntry(i, genre.getFavoriteCount()));
                    genreNames.add(genre.getName());
                    i++;
                }
            }
            else if (option.equals(getResources().getString(R.string.txt_shares))) {
                for (GenreModel genre : genreList) {
                    if (yMax <= genre.getShareCount()) {
                        yMax = genre.getShareCount();
                    }
                    data.add(new BarEntry(i, genre.getShareCount()));
                    genreNames.add(genre.getName());
                    i++;
                }
            }
            else if (option.equals(getResources().getString(R.string.txt_comments))) {
                for (GenreModel genre : genreList) {
                    if (yMax <= genre.getCommentCount()) {
                        yMax = genre.getCommentCount();
                    }
                    data.add(new BarEntry(i, genre.getCommentCount()));
                    genreNames.add(genre.getName());
                    i++;
                }
            }
            else if (option.equals(getResources().getString(R.string.txt_downloads))) {
                for (GenreModel genre : genreList) {
                    if (yMax <= genre.getDownloadCount()) {
                        yMax = genre.getDownloadCount();
                    }
                    data.add(new BarEntry(i, genre.getDownloadCount()));
                    genreNames.add(genre.getName());
                    i++;
                }
            }
            BarDataSet barDataSet = new BarDataSet(data, getResources().getString(R.string.txt_genres));



            barDataSet.setColors(getColorDataChart());
            BarData barData = new BarData(barDataSet);

            YAxis yAxis = barChart.getAxisLeft();
            yAxis.setAxisMaximum(yMax + 1);
            yAxis.setAxisMinimum(0f);
            yAxis.setAxisLineWidth(2f);
            yAxis.setAxisLineColor(Color.BLACK);
            yAxis.setLabelCount(yMax);

            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(genreNames));

            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            barChart.getXAxis().setGranularity(1f);
            barChart.getXAxis().setGranularityEnabled(true);
            barChart.getXAxis().setAxisMaximum(genreList.size() + 1);
            barChart.getXAxis().setLabelCount(genreList.size());
//            barChart.getXAxis().setLabelRotationAngle(20);

            barChart.getAxisRight().setDrawLabels(false);
            barChart.setFitBars(true);
            barChart.setData(barData);
            barChart.getDescription().setEnabled(false);
            barChart.animateY(500);
            barChart.invalidate();
        }
    }

    private void createBarChartCountFilmOption() {
        Bundle bundle = intent.getExtras();
        String content = "";
        if (bundle != null) {
            content = bundle.getString("content");
        }

        ArrayList<BarEntry> data = new ArrayList<>();
        int yMax = 0;
        if (filmsTemp.size() > 0) {
            int i = 1;
            List<String> filmNames = new ArrayList<>();
            filmNames.add("");
            if (content.equals("View") || option.equals(getResources().getString(R.string.txt_views))) {
                for (FilmModel film : filmsTemp) {
                    if (yMax <= film.getViewCount()) {
                        yMax = film.getViewCount();
                    }
                    data.add(new BarEntry(i, film.getViewCount()));
                    filmNames.add(film.getName());
                    i++;
                }
            }
            else if (content.equals("Favorite") || option.equals(getResources().getString(R.string.txt_likes))) {
                for (FilmModel film : filmsTemp) {
                    if (yMax <= film.getFavoriteCount()) {
                        yMax = film.getFavoriteCount();
                    }
                    data.add(new BarEntry(i, film.getFavoriteCount()));
                    filmNames.add(film.getName());
                    i++;
                }
            }
            else if (content.equals("Share") || option.equals(getResources().getString(R.string.txt_shares))) {
                for (FilmModel film : filmsTemp) {
                    if (yMax <= film.getShareCount()) {
                        yMax = film.getShareCount();
                    }
                    data.add(new BarEntry(i, film.getShareCount()));
                    filmNames.add(film.getName());
                    i++;
                }
            }
            else if (content.equals("Comment") || option.equals(getResources().getString(R.string.txt_comments))) {
                for (FilmModel film : filmsTemp) {
                    if (yMax <= film.getCommentCount()) {
                        yMax = film.getCommentCount();
                    }
                    data.add(new BarEntry(i, film.getCommentCount()));
                    filmNames.add(film.getName());
                    i++;
                }
            }
            else if (content.equals("Download") || option.equals(getResources().getString(R.string.txt_downloads))) {
                for (FilmModel film : filmsTemp) {
                    if (yMax <= film.getDownloadCount()) {
                        yMax = film.getDownloadCount();
                    }
                    data.add(new BarEntry(i, film.getDownloadCount()));
                    filmNames.add(film.getName());
                    i++;
                }
            }
            BarDataSet barDataSet = new BarDataSet(data, "Label");



            barDataSet.setColors(getColorDataChart());
            BarData barData = new BarData(barDataSet);

            YAxis yAxis = barChart.getAxisLeft();
            yAxis.setAxisMaximum(yMax + 1);
            yAxis.setAxisMinimum(0f);
            yAxis.setAxisLineWidth(2f);
            yAxis.setAxisLineColor(Color.BLACK);
            yAxis.setLabelCount(yMax);

            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(filmNames));

            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            barChart.getXAxis().setGranularity(1f);
            barChart.getXAxis().setGranularityEnabled(true);
            barChart.getXAxis().setAxisMaximum(filmsTemp.size() + 1);
            barChart.getXAxis().setLabelCount(filmsTemp.size());
//            barChart.getXAxis().setLabelRotationAngle(90);

            barChart.getAxisRight().setDrawLabels(false);
            barChart.setFitBars(true);
            barChart.setData(barData);
            barChart.getDescription().setEnabled(false);
            barChart.animateY(500);
            barChart.invalidate();
        }
    }

    private void createPieChartCountFilmOption() {
        Bundle bundle = intent.getExtras();
        String content = "";
        if (bundle != null) {
            content = bundle.getString("content");
        }
        ArrayList<PieEntry> data =new ArrayList<>();
        if (filmsTemp.size() > 0) {
            if (content.equals("View")) {
                for (FilmModel film : filmsTemp) {
                    if (film.getViewCount() > 0) {
                        data.add(new PieEntry(film.getViewCount(), film.getName()));
                    }
                }
            }
            else if (content.equals("Favorite")) {
                for (FilmModel film : filmsTemp) {
                    if (film.getFavoriteCount() > 0) {
                        data.add(new PieEntry(film.getFavoriteCount(), film.getName()));
                    }
                }
            }
            else if (content.equals("Share")) {
                for (FilmModel film : filmsTemp) {
                    if (film.getShareCount() > 0) {
                        data.add(new PieEntry(film.getShareCount(), film.getName()));
                    }
                }
            }
            else if (content.equals("Comment")) {
                for (FilmModel film : filmsTemp) {
                    if (film.getCommentCount() > 0) {
                        data.add(new PieEntry(film.getCommentCount(), film.getName()));
                    }
                }
            }
            else if (content.equals("Download")) {
                for (FilmModel film : filmsTemp) {
                    if (film.getDownloadCount() > 0) {
                        data.add(new PieEntry(film.getDownloadCount(), film.getName()));
                    }
                }
            }
            PieDataSet pieDataSet = new PieDataSet(data, "");
            pieDataSet.setColors(getColorDataChart());
            pieDataSet.setValueTextColor(Color.WHITE);
            pieDataSet.setValueTextSize(15);
            PieData pieData = new PieData(pieDataSet);
            pieChart.setData(pieData);
            pieChart.getDescription().setEnabled(false);
            pieChart.invalidate();
        }
    }

    private void createPieChartCountUserOption() {
        Bundle bundle = intent.getExtras();
        String content = "";
        if (bundle != null) {
            content = bundle.getString("content");
        }
        ArrayList<PieEntry> data =new ArrayList<>();
        if (userList.size() > 0) {
            if (option.equals(getResources().getString(R.string.txt_views))) {
                for (UserModel user : userList) {
                    if (user.getViewCount() > 0) {
                        data.add(new PieEntry(user.getViewCount(), user.getUsername()));
                    }
                }
            }
            else if (option.equals(getResources().getString(R.string.txt_likes))) {
                for (UserModel user : userList) {
                    if (user.getFavoriteCount() > 0) {
                        data.add(new PieEntry(user.getFavoriteCount(), user.getUsername()));
                    }
                }
            }
            else if (option.equals(getResources().getString(R.string.txt_shares))) {
                for (UserModel user : userList) {
                    if (user.getShareCount() > 0) {
                        data.add(new PieEntry(user.getShareCount(), user.getUsername()));
                    }
                }
            }
            else if (option.equals(getResources().getString(R.string.txt_comments))) {
                for (UserModel user : userList) {
                    if (user.getCommentCount() > 0) {
                        data.add(new PieEntry(user.getCommentCount(), user.getUsername()));
                    }
                }
            }
            else if (option.equals(getResources().getString(R.string.txt_downloads))) {
                for (UserModel user : userList) {
                    if (user.getDownloadCount() > 0) {
                        data.add(new PieEntry(user.getDownloadCount(), user.getUsername()));
                    }
                }
            }
            PieDataSet pieDataSet = new PieDataSet(data, "");
            pieDataSet.setColors(getColorDataChart());
            pieDataSet.setValueTextColor(Color.WHITE);
            pieDataSet.setValueTextSize(15);
            PieData pieData = new PieData(pieDataSet);
            pieChart.setData(pieData);
            pieChart.getDescription().setEnabled(false);
            pieChart.invalidate();
        }
    }

    private void createBarChartCountUserOption() {

        ArrayList<BarEntry> data = new ArrayList<>();
        int yMax = 0;
        if (userList.size() > 0) {
            int i = 1;
            List<String> usernames = new ArrayList<>();
            usernames.add("");
            if (option.equals(getResources().getString(R.string.txt_views))) {
                for (UserModel user : userList) {
                    if (yMax <= user.getViewCount()) {
                        yMax = user.getViewCount();
                    }
                    data.add(new BarEntry(i, user.getViewCount()));
                    usernames.add(user.getUsername());
                    i++;
                }
            }
            else if (option.equals(getResources().getString(R.string.txt_likes))) {
                for (UserModel user : userList) {
                    if (yMax <= user.getFavoriteCount()) {
                        yMax = user.getFavoriteCount();
                    }
                    data.add(new BarEntry(i, user.getFavoriteCount()));
                    usernames.add(user.getUsername());
                    i++;
                }
            }
            else if (option.equals(getResources().getString(R.string.txt_shares))) {
                for (UserModel user : userList) {
                    if (yMax <= user.getShareCount()) {
                        yMax = user.getShareCount();
                    }
                    data.add(new BarEntry(i, user.getShareCount()));
                    usernames.add(user.getUsername());
                    i++;
                }
            }
            else if (option.equals(getResources().getString(R.string.txt_comments))) {
                for (UserModel user : userList) {
                    if (yMax <= user.getCommentCount()) {
                        yMax = user.getCommentCount();
                    }
                    data.add(new BarEntry(i, user.getCommentCount()));
                    usernames.add(user.getUsername());
                    i++;
                }
            }
            else if (option.equals(getResources().getString(R.string.txt_downloads))) {
                for (UserModel user : userList) {
                    if (yMax <= user.getDownloadCount()) {
                        yMax = user.getDownloadCount();
                    }
                    data.add(new BarEntry(i, user.getDownloadCount()));
                    usernames.add(user.getUsername());
                    i++;
                }
            }
            BarDataSet barDataSet = new BarDataSet(data, "Label");



            barDataSet.setColors(getColorDataChart());
            BarData barData = new BarData(barDataSet);

            YAxis yAxis = barChart.getAxisLeft();
            yAxis.setAxisMaximum(yMax + 1);
            yAxis.setAxisMinimum(0f);
            yAxis.setAxisLineWidth(2f);
            yAxis.setAxisLineColor(Color.BLACK);
            yAxis.setLabelCount(yMax);

            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(usernames));

            barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            barChart.getXAxis().setGranularity(1f);
            barChart.getXAxis().setGranularityEnabled(true);
            barChart.getXAxis().setAxisMaximum(userList.size() + 1);
            barChart.getXAxis().setLabelCount(userList.size());
//            barChart.getXAxis().setLabelRotationAngle(90);

            barChart.getAxisRight().setDrawLabels(false);
            barChart.setFitBars(true);
            barChart.setData(barData);
            barChart.getDescription().setEnabled(false);
            barChart.animateY(500);
            barChart.invalidate();
        }
    }

    private ArrayList<Integer> getColorDataChart() {
        ArrayList<Integer> data_color = new ArrayList<>();
        data_color.add(Color.parseColor("#1d6fb1"));
        data_color.add(Color.parseColor("#1c97bb"));
        data_color.add(Color.parseColor("#ac2011"));
        data_color.add(Color.parseColor("#da4d31"));
        data_color.add(Color.parseColor("#d5ad01"));
        data_color.add(Color.parseColor("#1454a8"));
        data_color.add(Color.parseColor("#8b4b9e"));
        data_color.add(Color.parseColor("#62b177"));
        data_color.add(Color.parseColor("#6fb6a9"));
        data_color.add(Color.parseColor("#15234f"));
        data_color.add(Color.parseColor("#d77b08"));
        data_color.add(Color.parseColor("#5800c6"));
        data_color.add(Color.parseColor("#789d1e"));
        data_color.add(Color.parseColor("#7c5229"));
        return data_color;
    }

    private void getFilmCountGenreOption() {
        resetFilmCountGenreOption();
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

    private void sortFilmCountGenreOption() {
        if (sort.equals("DESC")) {
            genreList.sort((g1, g2) -> g2.getFilmCount() - g1.getFilmCount());
        }
        else if (sort.equals("ASC")) {
            genreList.sort(Comparator.comparingInt(GenreModel::getFilmCount));
        }
    }

    private void getViewCountGenreOption() {
        resetViewCountGenreOption();
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

    private void sortViewCountGenreOption() {
        if (sort.equals("DESC")) {
            genreList.sort((g1, g2) -> g2.getViewCount() - g1.getViewCount());
        }
        else if (sort.equals("ASC")) {
            genreList.sort(Comparator.comparingInt(GenreModel::getViewCount));
        }
    }

    private void getFavoriteCountGenreOption() {
        resetFavoriteCountGenreOption();
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

    private void sortFavoriteCountGenreOption() {
        if (sort.equals("DESC")) {
            genreList.sort((g1, g2) -> g2.getFavoriteCount() - g1.getFavoriteCount());
        }
        else if (sort.equals("ASC")) {
            genreList.sort(Comparator.comparingInt(GenreModel::getFavoriteCount));
        }
    }

    private void getShareCountGenreOption() {
        resetShareCountGenreOption();
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

    private void sortShareCountGenreOption() {
        if (sort.equals("DESC")) {
            genreList.sort((g1, g2) -> g2.getShareCount() - g1.getShareCount());
        }
        else if (sort.equals("ASC")) {
            genreList.sort(Comparator.comparingInt(GenreModel::getShareCount));
        }
    }

    private void getCommentCountGenreOption() {
        resetCommentCountGenreOption();
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

    private void sortCommentCountGenreOption() {
        if (sort.equals("DESC")) {
            genreList.sort((g1, g2) -> g2.getCommentCount() - g1.getCommentCount());
        }
        else if (sort.equals("ASC")) {
            genreList.sort(Comparator.comparingInt(GenreModel::getCommentCount));
        }
    }

    private void getDownloadCountGenreOption() {
        resetDownloadCountGenreOption();
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

    private void sortDownloadCountGenreOption() {
        if (sort.equals("DESC")) {
            genreList.sort((g1, g2) -> g2.getDownloadCount() - g1.getDownloadCount());
        }
        else if (sort.equals("ASC")) {
            genreList.sort(Comparator.comparingInt(GenreModel::getDownloadCount));
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
            } else if (sort.equals("ASC")) {
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

    private FilmModel getFilmByFilmID(String filmId) {
        for (FilmModel film : filmList) {
            if (film.getId().equals(filmId)) {
                return film;
            }
        }
        return null;
    }

    private void resetViewCountGenreOption() {
        for (GenreModel genre : genreList) {
            genre.setViewCount(0);
        }
    }

    private void resetFilmCountGenreOption() {
        for (GenreModel genre : genreList) {
            genre.setFilmCount(0);
        }
    }

    private void resetFavoriteCountGenreOption() {
        for (GenreModel genre : genreList) {
            genre.setFavoriteCount(0);
        }
    }

    private void resetShareCountGenreOption() {
        for (GenreModel genre : genreList) {
            genre.setShareCount(0);
        }
    }

    private void resetCommentCountGenreOption() {
        for (GenreModel genre : genreList) {
            genre.setCommentCount(0);
        }
    }

    private void resetDownloadCountGenreOption() {
        for (GenreModel genre : genreList) {
            genre.setDownloadCount(0);
        }
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
                if (filmList.size() != 0 && genreList.size() != 0 && userList.size() != 0) {
                    setContent();
                }
//                genreAdapter.notifyDataSetChanged();
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
}