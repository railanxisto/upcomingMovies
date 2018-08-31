package com.arctouch.codechallenge.details;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.databinding.ActivityMovieDetailsBinding;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.util.MovieImageUrlBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.arctouch.codechallenge.home.HomeActivity.PARAM_MOVIE;

public class MovieDetailsActivity extends AppCompatActivity {

    //dataBinding
    ActivityMovieDetailsBinding binding;

    private final MovieImageUrlBuilder movieImageUrlBuilder = new MovieImageUrlBuilder();

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);

        setSupportActionBar(binding.mainToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if (extras != null) {
            movie = (Movie) extras.getSerializable(PARAM_MOVIE);
        }

        binding.setMovie(movie);

        String posterPath = movie.getPosterPath();
        if (!TextUtils.isEmpty(posterPath)) {
            Glide.with(binding.getRoot())
                    .load(movieImageUrlBuilder.buildPosterUrl(posterPath))
                    .apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                    .into(binding.posterImageView);
        }

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.FRANCE);

        binding.genresTextView.setText(TextUtils.join(", ", movie.getGenres()));

        String backdropPath = movie.getBackdropPath();
        if (!TextUtils.isEmpty(backdropPath)) {
            Glide.with(binding.getRoot())
                    .load(movieImageUrlBuilder.buildBackdropUrl(backdropPath))
                    .apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder).dontAnimate())
                    .into(binding.backdropImageView);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
