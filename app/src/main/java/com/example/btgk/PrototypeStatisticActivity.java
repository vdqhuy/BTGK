package com.example.btgk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.btgk.Fragment.PrototypeStatChartFragment;
import com.example.btgk.Fragment.PrototypeStatListFragment;
import com.example.btgk.Model.GenreModel;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class PrototypeStatisticActivity extends AppCompatActivity {
    TextView txtHeaderStat;
    ImageView btnBack, btnSort;
    Button btnList, btnChart;
    CardView cardSelectItem, cardSelectChart;
    Spinner spSelect, spSelectChart;
    ImageView imgSelectChart;
    FrameLayout fragmentPlacement;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> listItem = new ArrayList<>();
    ArrayList<String> listChart = new ArrayList<>();
    Fragment layoutFragmentList, layoutFragmentChart;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prototype_statistic);
        setControl();
        setContent();
        setEvent();
//        setDatabase();
        btnList.setTextColor(ContextCompat.getColor(PrototypeStatisticActivity.this, R.color.textThemeColor));
        btnList.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        btnChart.setTextColor(Color.BLACK);
        btnChart.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
    }

    private void setContent() {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String content = bundle.getString("content");
//            if (content.equals("TotalFilm") || content.equals("TotalAccount")) {
//                btnChart.setEnabled(false);
//                btnChart.setBackgroundResource(R.drawable.round_right_corner_disabled);
//            }
            setHeaderContent(content);
            setSpinnerContent(content);
        }

//        hideSpSelectIfTotalAccount();
        PrototypeStatListFragment prototypeStatListFragment = new PrototypeStatListFragment();
        prototypeStatListFragment.setOption(getResources().getString(R.string.txt_film_amount));
        prototypeStatListFragment.setSort("DESC");
        replaceFragment(prototypeStatListFragment, "fragment_list");
    }

    private void setHeaderContent(String content) {
        switch (content) {
            case "TotalFilm":
                txtHeaderStat.setText(getResources().getString(R.string.txt_all_films));
                break;
            case "TotalAccount":
                txtHeaderStat.setText(getResources().getString(R.string.txt_all_accs));
                break;
            case "TopGenre":
                txtHeaderStat.setText(getResources().getString(R.string.txt_top_genres));
                break;
            case "View":
                txtHeaderStat.setText(getResources().getString(R.string.txt_views));
                break;
            case "Favorite":
                txtHeaderStat.setText(getResources().getString(R.string.txt_likes));
                break;
            case "Comment":
                txtHeaderStat.setText(getResources().getString(R.string.txt_comments));
                break;
            case "Share":
                txtHeaderStat.setText(getResources().getString(R.string.txt_shares));
                break;
            case "Download":
                txtHeaderStat.setText(getResources().getString(R.string.txt_downloads));
                break;
        }
    }

    private void setSpinnerContent(String content) {
        if (content.equals("TopGenre")) {
            listItem.add(getResources().getString(R.string.txt_film_amount));
            listItem.add(getResources().getString(R.string.txt_views));
            listItem.add(getResources().getString(R.string.txt_likes));
            listItem.add(getResources().getString(R.string.txt_shares));
            listItem.add(getResources().getString(R.string.txt_comments));
            listItem.add(getResources().getString(R.string.txt_downloads));
        }
        else if (content.equals("TotalFilm") || content.equals("TotalAccount")) {
            listItem.add(getResources().getString(R.string.txt_default_list));
            listItem.add(getResources().getString(R.string.txt_views));
            listItem.add(getResources().getString(R.string.txt_likes));
            listItem.add(getResources().getString(R.string.txt_shares));
            listItem.add(getResources().getString(R.string.txt_comments));
            listItem.add(getResources().getString(R.string.txt_downloads));
        }
        else {
            listItem.add(getResources().getString(R.string.txt_all_films));
            listItem.add(getResources().getString(R.string.txt_movies));
            listItem.add(getResources().getString(R.string.txt_series));
            listItem.add(getResources().getString(R.string.txt_genres));
        }
    }

    private void setControl() {
        txtHeaderStat = findViewById(R.id.txt_header_stat);
        btnBack = findViewById(R.id.btn_back);
        btnSort = findViewById(R.id.btn_sort);
        btnList = findViewById(R.id.btn_list);
        btnChart = findViewById(R.id.btn_chart);
        spSelect = findViewById(R.id.spSelect);
        spSelectChart = findViewById(R.id.spSelectChart);
        cardSelectItem = findViewById(R.id.card_select_item);
        cardSelectChart = findViewById(R.id.card_select_chart);
        imgSelectChart = findViewById(R.id.img_select_chart);
        fragmentPlacement = findViewById(R.id.fragment_placement);
        intent = getIntent();
    }

    private void setEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutFragmentList = getSupportFragmentManager().findFragmentByTag("fragment_list");
                layoutFragmentChart = getSupportFragmentManager().findFragmentByTag("fragment_chart");
                if (layoutFragmentList != null && layoutFragmentList.isVisible()) {
                    PrototypeStatListFragment prototypeStatListFragment = new PrototypeStatListFragment();
                    prototypeStatListFragment.setOption(spSelect.getSelectedItem().toString());
                    if (btnSort.getScaleY() != -1) {
                        prototypeStatListFragment.setSort("ASC");
                        btnSort.setScaleY(-1);
                    }
                    else {
                        prototypeStatListFragment.setSort("DESC");
                        btnSort.setScaleY(1);
                    }
                    replaceFragment(prototypeStatListFragment, "fragment_list");
                }
                else if (layoutFragmentChart != null && layoutFragmentChart.isVisible()) {
                    PrototypeStatChartFragment prototypeStatChartFragment = new PrototypeStatChartFragment();
                    prototypeStatChartFragment.setOption(spSelect.getSelectedItem().toString());
                    prototypeStatChartFragment.setOptionChart(spSelectChart.getSelectedItem().toString());
                    if (btnSort.getScaleY() != -1) {
                        prototypeStatChartFragment.setSort("DESC");
                        btnSort.setScaleY(-1);
                    }
                    else {
                        prototypeStatChartFragment.setSort("ASC");
                        btnSort.setScaleY(1);
                    }
                    replaceFragment(prototypeStatChartFragment, "fragment_chart");
                }
            }
        });

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDefaultOption();
                hideSpSelectChart();
//                hideSpSelectIfTotalAccount();
                btnList.setTextColor(ContextCompat.getColor(PrototypeStatisticActivity.this, R.color.textThemeColor));
                btnList.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                btnChart.setTextColor(Color.BLACK);
                btnChart.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                PrototypeStatListFragment prototypeStatListFragment = new PrototypeStatListFragment();
                prototypeStatListFragment.setOption(spSelect.getSelectedItem().toString());
                prototypeStatListFragment.setSort("DESC");
                btnSort.setRotation(0);
                replaceFragment(prototypeStatListFragment, "fragment_list");
            }
        });

        btnChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeDefaultOption();
                showSpSelectChart();
//                showSpSelectIfTotalAccount();
                btnChart.setTextColor(ContextCompat.getColor(PrototypeStatisticActivity.this, R.color.textThemeColor));
                btnChart.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                btnList.setTextColor(Color.BLACK);
                btnList.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                PrototypeStatChartFragment prototypeStatChartFragment = new PrototypeStatChartFragment();
                prototypeStatChartFragment.setOption(spSelect.getSelectedItem().toString());
                prototypeStatChartFragment.setOptionChart(spSelectChart.getSelectedItem().toString());
                prototypeStatChartFragment.setSort("DESC");
                btnSort.setRotation(0);
                replaceFragment(prototypeStatChartFragment, "fragment_chart");
            }
        });

        spSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
//                ((TextView) parent.getChildAt(0)).setTextSize(20);

                layoutFragmentList = getSupportFragmentManager().findFragmentByTag("fragment_list");
                layoutFragmentChart = getSupportFragmentManager().findFragmentByTag("fragment_chart");
                if (layoutFragmentList != null && layoutFragmentList.isVisible()) {
                    PrototypeStatListFragment prototypeStatListFragment = new PrototypeStatListFragment();
                    prototypeStatListFragment.setOption(spSelect.getSelectedItem().toString());
                    if (btnSort.getScaleY() == -1) {
                        prototypeStatListFragment.setSort("ASC");
                        btnSort.setScaleY(-1);
                    }
                    else {
                        prototypeStatListFragment.setSort("DESC");
                        btnSort.setScaleY(1);
                    }
                    replaceFragment(prototypeStatListFragment, "fragment_list");
                }
                else if (layoutFragmentChart != null && layoutFragmentChart.isVisible()) {
                    PrototypeStatChartFragment prototypeStatChartFragment = new PrototypeStatChartFragment();
                    prototypeStatChartFragment.setOption(spSelect.getSelectedItem().toString());
                    prototypeStatChartFragment.setOptionChart(spSelectChart.getSelectedItem().toString());
                    if (btnSort.getScaleY() == -1) {
                        prototypeStatChartFragment.setSort("ASC");
                        btnSort.setScaleY(-1);
                    }
                    else {
                        prototypeStatChartFragment.setSort("DESC");
                        btnSort.setScaleY(1);
                    }
                    replaceFragment(prototypeStatChartFragment, "fragment_chart");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        arrayAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
        arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spSelect.setAdapter(arrayAdapter);

        spSelectChart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                layoutFragmentList = getSupportFragmentManager().findFragmentByTag("fragment_list");
                layoutFragmentChart = getSupportFragmentManager().findFragmentByTag("fragment_chart");
                if (layoutFragmentList != null && layoutFragmentList.isVisible()) {
                    PrototypeStatListFragment prototypeStatListFragment = new PrototypeStatListFragment();
                    prototypeStatListFragment.setOption(spSelect.getSelectedItem().toString());
                    replaceFragment(prototypeStatListFragment, "fragment_list");
                }
                else if (layoutFragmentChart != null && layoutFragmentChart.isVisible()) {
                    PrototypeStatChartFragment prototypeStatChartFragment = new PrototypeStatChartFragment();
                    prototypeStatChartFragment.setOption(spSelect.getSelectedItem().toString());
                    prototypeStatChartFragment.setOptionChart(spSelectChart.getSelectedItem().toString());
                    if (spSelectChart.getSelectedItem().toString().equals(getResources().getString(R.string.txt_pie_chart))) {
                        imgSelectChart.setImageResource(R.drawable.ic_pie_chart_24px);
                    }
                    else if (spSelectChart.getSelectedItem().toString().equals(getResources().getString(R.string.txt_bar_chart))) {
                        imgSelectChart.setImageResource(R.drawable.ic_bar_chart_24px);
                    }
                    else if (spSelectChart.getSelectedItem().toString().equals("Phân tán")) {
                        imgSelectChart.setImageResource(R.drawable.ic_scatter_chart_24px);
                    }
                    else if (spSelectChart.getSelectedItem().toString().equals("Bong bóng")) {
                        imgSelectChart.setImageResource(R.drawable.ic_bubble_chart_24px);
                    }
                    replaceFragment(prototypeStatChartFragment, "fragment_chart");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cardSelectItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spSelect.performClick();
            }
        });

        cardSelectChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spSelectChart.performClick();
            }
        });
    }

    private void addDefaultOption() {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String content = bundle.getString("content");
            if ((content.equals("TotalFilm") || content.equals("TotalAccount")) && !listItem.contains("Default")) {
                listItem.add(0, "Default");
                arrayAdapter =
                        new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
                arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                spSelect.setAdapter(arrayAdapter);
            }
        }
    }

    private void removeDefaultOption() {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String content = bundle.getString("content");
            if (content.equals("TotalFilm") || content.equals("TotalAccount")) {
                listItem.remove("Default");
                arrayAdapter =
                        new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
                arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                spSelect.setAdapter(arrayAdapter);
            }
        }
    }

    private void showSpSelectChart() {
        int dpWidth = 210;
        float density = getResources().getDisplayMetrics().density;
        int pixelWidth = (int) (dpWidth * density + 0.5f);

        ViewGroup.LayoutParams layoutParams = cardSelectItem.getLayoutParams();
        layoutParams.width = pixelWidth;

        layoutParams = spSelect.getLayoutParams();
        layoutParams.width = pixelWidth - 40;

        cardSelectChart.setVisibility(View.VISIBLE);
        listChart.clear();
        if (txtHeaderStat.getText().toString().equals(getResources().getString(R.string.txt_top_genres))) {
            listChart.add(getResources().getString(R.string.txt_bar_chart));
            listChart.add(getResources().getString(R.string.txt_pie_chart));
        }
        else {
            listChart.add(getResources().getString(R.string.txt_bar_chart));
            listChart.add(getResources().getString(R.string.txt_pie_chart));
        }

        arrayAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listChart);
        arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spSelectChart.setAdapter(arrayAdapter);
    }

    private void hideSpSelectChart() {
        int dpWidth = 350;
        float density = getResources().getDisplayMetrics().density;
        int pixelWidth = (int) (dpWidth * density + 0.5f);

        ViewGroup.LayoutParams layoutParams = cardSelectItem.getLayoutParams();
        layoutParams.width = pixelWidth;

        layoutParams = spSelect.getLayoutParams();
        layoutParams.width = pixelWidth - 40;

        cardSelectChart.setVisibility(View.GONE);
    }

    private void hideSpSelectIfTotalAccount() {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String content = bundle.getString("content");
            if (content.equals("TotalAccount")) {
                cardSelectItem.setVisibility(View.GONE);
                int dpHeight = 610;
                float density = getResources().getDisplayMetrics().density;
                int pixelHeight = (int) (dpHeight * density + 0.5f);

                ViewGroup.LayoutParams layoutParams = fragmentPlacement.getLayoutParams();
                layoutParams.height = pixelHeight;
            }
        }
    }

    private void showSpSelectIfTotalAccount() {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String content = bundle.getString("content");
            if (content.equals("TotalAccount")) {
                cardSelectItem.setVisibility(View.VISIBLE);
                int dpHeight = 545;
                float density = getResources().getDisplayMetrics().density;
                int pixelHeight = (int) (dpHeight * density + 0.5f);

                ViewGroup.LayoutParams layoutParams = fragmentPlacement.getLayoutParams();
                layoutParams.height = pixelHeight;
            }
        }
    }

    private void replaceFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_placement, fragment, tag);
        fragmentTransaction.commit();
    }

    private void setDatabase() {
//        DatabaseReference filmRef = filmDatabase.getReference().child("Film");
//        createFilmData(filmRef);
//        FirebaseDatabase filmDatabase = FirebaseDatabase.getInstance();
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
}