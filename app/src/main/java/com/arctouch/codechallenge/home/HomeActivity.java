package com.arctouch.codechallenge.home;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.details.MovieDetailsActivity;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.util.InfiniteScrollListener;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity implements HomeAdapter.OnItemClick, HomeContract.View, SearchView.OnQueryTextListener {
    public static final String PARAM_MOVIE = "movie";

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 1;
    private int totalPages = 0;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private HomePresenter presenter;
    private HomeAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private boolean isFiltering = false;

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
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new InfiniteScrollListener(linearLayoutManager) {
            @Override
            protected void loadNextPage() {
                if (!isFiltering) {
                    isLoading = true;
                    currentPage++;
                    if (currentPage <= totalPages) {
                        presenter.loadMovies(currentPage);
                    }
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() == 0) {
            isFiltering = false;
        } else {
            isFiltering = true;
        }
        adapter.getFilter().filter(newText);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search)
                .getActionView();

        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setMaxWidth(Integer.MAX_VALUE);
        }
        searchView.setOnQueryTextListener(this);
        return true;
    }
}