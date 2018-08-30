package com.arctouch.codechallenge.home;

import android.content.Context;

import com.arctouch.codechallenge.model.Movie;

import java.util.List;

/**
 * Created by railan on 30/08/18.
 */

public class HomeRepository {

    private static final HomeRepository instance = new HomeRepository();

    private static Context context;

    public static HomeRepository getInstance(Context applicationContext) {
        HomeRepository.context = applicationContext.getApplicationContext();
        return instance;
    }

    private HomeRepository() {
        //ignore
    }

    public void loadMovies(final GetMoviesListener listener) {

    }

    public interface GetMoviesListener extends OnErrorListener {
        void getMoviesSuccess(List<Movie> movies);
    }

    private void handleThrowable(Throwable t, OnErrorListener listener) {
        if (listener != null) {
            listener.onError("Ops. An error just occurred");
        }
        System.err.println(t.getMessage());
    }

    public interface OnErrorListener {
        void onError(String message);
    }

}
