package com.arctouch.codechallenge.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.details.MovieDetailsActivity;
import com.arctouch.codechallenge.model.Movie;

import java.util.List;


public class HomeActivity extends AppCompatActivity implements HomeAdapter.OnItemClick, HomeContract.View {
    public static final String PARAM_MOVIE = "movie";

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private HomePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        this.recyclerView = findViewById(R.id.recyclerView);
        this.progressBar = findViewById(R.id.progressBar);
        presenter = new HomePresenter(this, HomeRepository.getInstance(this.getApplicationContext()));

        presenter.loadGenres();

    }


    @Override
    public void onMovieClick(Movie clickedMovie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(PARAM_MOVIE, clickedMovie);
        startActivity(intent);
    }

    @Override
    public void showProgress(boolean isToShow) {
        if (isToShow) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showMovies(List<Movie> movies) {
        recyclerView.setAdapter(new HomeAdapter(movies, this));
    }

    @Override
    public void genrerIsLoaded() {
        presenter.loadMovies();
    }
}