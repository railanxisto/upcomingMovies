package com.arctouch.codechallenge.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.api.TmdbApi;
import com.arctouch.codechallenge.base.BaseActivity;
import com.arctouch.codechallenge.data.Cache;
import com.arctouch.codechallenge.details.MovieDetailsActivity;
import com.arctouch.codechallenge.model.Genre;
import com.arctouch.codechallenge.model.Movie;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends BaseActivity implements HomeAdapter.OnItemClick{
    public static final String PARAM_MOVIE = "movie";

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        this.recyclerView = findViewById(R.id.recyclerView);
        this.progressBar = findViewById(R.id.progressBar);

        api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, 1L, TmdbApi.DEFAULT_REGION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    for (Movie movie : response.results) {
                        for (Genre genre : Cache.getGenres()) {
                            if (movie.getGenreIds().contains(genre.id)) {
                                movie.getGenres().add(genre);
                            }
                        }
                    }

                    recyclerView.setAdapter(new HomeAdapter(response.results, this));
                    progressBar.setVisibility(View.GONE);
                });
    }


    @Override
    public void onMovieClick(Movie clickedMovie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(PARAM_MOVIE, clickedMovie);
        startActivity(intent);
    }

}