package com.arctouch.codechallenge.home;

import android.support.annotation.NonNull;

import com.arctouch.codechallenge.model.Movie;

import java.util.List;


/**
 * Created by railan on 30/08/18.
 */

public class HomePresenter implements HomeContract.Presenter {
    @NonNull
    private HomeContract.View viewListener;

    @NonNull
    private HomeRepository repository;

    public HomePresenter(@NonNull HomeContract.View viewListener, @NonNull HomeRepository repository) {
        this.viewListener = viewListener;
        this.repository = repository;
    }


    @Override
    public void loadMovies() {
        viewListener.showProgress(true);
        repository.loadMovies(new HomeRepository.GetMoviesListener() {
            @Override
            public void getMoviesSuccess(List<Movie> movies) {
                viewListener.showMovies(movies);
            }

            @Override
            public void onError(String message) {

            }
        });
    }
}
