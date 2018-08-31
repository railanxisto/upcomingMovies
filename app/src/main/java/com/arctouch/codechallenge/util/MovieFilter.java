package com.arctouch.codechallenge.util;

import android.widget.Filter;

import com.arctouch.codechallenge.model.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by railan on 31/08/18.
 */

public class MovieFilter extends Filter {
    private List<Movie> movies;

    private MovieFilter.OnResultFilteredReceivedListener listener;

    public MovieFilter(List<Movie> movies, OnResultFilteredReceivedListener listener) {
        this.movies = movies;
        this.listener = listener;
        if (this.movies == null) {
            this.movies = new ArrayList<>();
        }
    }

    @Override
    protected FilterResults performFiltering(CharSequence text) {
        Filter.FilterResults filterResults = new Filter.FilterResults();

        List<Movie> filteredMovies = new ArrayList<>();
        for (Movie movie : movies) {
            if (movie.getTitle().toLowerCase(Locale.FRENCH).contains(text.toString().toLowerCase(Locale.FRENCH))) {
                filteredMovies.add(movie);
            }
        }

        filterResults.values = filteredMovies;
        filterResults.count = filteredMovies.size();

        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
        if (listener != null)
            listener.onResultFilteredReceived((List<Movie>) results.values);
    }

    public interface OnResultFilteredReceivedListener {
        void onResultFilteredReceived(List<Movie> filteredMovies);
    }
}
