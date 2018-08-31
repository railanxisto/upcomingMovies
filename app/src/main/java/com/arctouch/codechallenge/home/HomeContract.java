package com.arctouch.codechallenge.home;

import com.arctouch.codechallenge.model.Movie;

import java.util.List;

/**
 * Created by railan on 30/08/18.
 */

public interface HomeContract {
    interface View {
        void showMovies (List<Movie> movies);

        void genrerIsLoaded();

        void showProgress (boolean isToShow);
    }

    interface Presenter {
        void loadMovies(int currentPage);

        void loadGenres();

        int getTotalPages();
    }
}
