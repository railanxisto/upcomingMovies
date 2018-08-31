package com.arctouch.codechallenge.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.util.MovieFilter;
import com.arctouch.codechallenge.util.MovieImageUrlBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> implements Filterable {

    private List<Movie> movies = new ArrayList<>();
    private List<Movie> filteredMovies = new ArrayList<>();
    private OnItemClick listener;
    private static final MovieImageUrlBuilder movieImageUrlBuilder = new MovieImageUrlBuilder();

    public HomeAdapter(List<Movie> movies, OnItemClick listener) {
        this.movies.addAll(movies);
        this.filteredMovies.addAll(movies);
        this.listener = listener;
    }

    public void setMovies(List<Movie> movies) {
        HomeAdapter.this.movies.addAll(movies);
        HomeAdapter.this.filteredMovies.addAll(movies);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new MovieFilter(movies, new MovieFilter.OnResultFilteredReceivedListener() {
            @Override
            public void onResultFilteredReceived(List<Movie> movies) {
                HomeAdapter.this.filteredMovies = movies;
                notifyDataSetChanged();
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleTextView;
        private final TextView genresTextView;
        private final TextView releaseDateTextView;
        private final ImageView posterImageView;
        private final RelativeLayout movieLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            genresTextView = itemView.findViewById(R.id.genresTextView);
            releaseDateTextView = itemView.findViewById(R.id.releaseDateTextView);
            posterImageView = itemView.findViewById(R.id.posterImageView);
            movieLayout = itemView.findViewById(R.id.movieLayout);
        }

        public void bind(Movie movie) {
            titleTextView.setText(movie.getTitle());
            genresTextView.setText(TextUtils.join(", ", movie.getGenres()));
            releaseDateTextView.setText(movie.releaseDate);

            String posterPath = movie.getPosterPath();
            if (TextUtils.isEmpty(posterPath) == false) {
                Glide.with(itemView)
                        .load(movieImageUrlBuilder.buildPosterUrl(posterPath))
                        .apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder).dontAnimate())
                        .into(posterImageView);
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return filteredMovies.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(filteredMovies.get(position));
        holder.movieLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMovieClick(filteredMovies.get(position));
            }
        });
    }

    public interface OnItemClick {
        void onMovieClick(Movie movie);
    }
}
