package com.arctouch.codechallenge.home;

import android.content.Context;

import com.arctouch.codechallenge.api.MyApp;
import com.arctouch.codechallenge.api.TmdbApi;
import com.arctouch.codechallenge.data.Cache;
import com.arctouch.codechallenge.model.Genre;
import com.arctouch.codechallenge.model.Movie;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Created by railan on 30/08/18.
 */

public class HomeRepository {
    private static final HomeRepository instance = new HomeRepository();

    private static Context context;
    private static int totalPagesCache = 0;

    public static HomeRepository getInstance(Context applicationContext) {
        HomeRepository.context = applicationContext.getApplicationContext();
        return instance;
    }

    private HomeRepository() {
        //ignore
    }

    public int getTotalPages() {
        return totalPagesCache;
    }

    public void loadMovies(int currentPage, final GetMoviesListener listener) {
        MyApp.api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, currentPage, TmdbApi.DEFAULT_REGION)
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
                    totalPagesCache = response.totalPages;
                    listener.getMoviesSuccess(response.results);
                });
    }

    public void loadGenres(final GetGenresListener listener) {
        MyApp.api.genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Cache.setGenres(response.genres);
                    listener.getGenresSuccess();
                });
    }

    public interface GetMoviesListener extends OnErrorListener {
        void getMoviesSuccess(List<Movie> movies);
    }

    public interface GetGenresListener extends OnErrorListener {
        void getGenresSuccess();
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
