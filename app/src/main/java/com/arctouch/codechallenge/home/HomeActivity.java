package com.arctouch.codechallenge.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.details.MovieDetailsActivity;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.util.InfiniteScrollListener;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity implements HomeAdapter.OnItemClick, HomeContract.View {
    public static final String PARAM_MOVIE = "movie";

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 1;
    private int totalPages = 0;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private HomePresenter presenter;
    private HomeAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        this.recyclerView = findViewById(R.id.recyclerView);
        this.progressBar = findViewById(R.id.progressBar);

        presenter = new HomePresenter(this, HomeRepository.getInstance(this.getApplicationContext()));
        adapter = new HomeAdapter(new ArrayList<>(), this);
        presenter.loadGenres();
    }

    private void setUpScrollView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new InfiniteScrollListener(linearLayoutManager) {
            @Override
            protected void loadNextPage() {
                isLoading = true;
                currentPage++;
                if (currentPage <= totalPages) {
                    presenter.loadMovies(currentPage);
                }
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
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
        isLoading = false;
        totalPages = presenter.getTotalPages();
        adapter.setMovies(movies);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void genrerIsLoaded() {
        setUpScrollView();
        presenter.loadMovies(currentPage);
    }
}