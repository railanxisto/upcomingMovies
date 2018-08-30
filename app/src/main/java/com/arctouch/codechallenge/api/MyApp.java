package com.arctouch.codechallenge.api;

import android.app.Application;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Created by railan on 30/08/18.
 */

public class MyApp extends Application {
    public static TmdbApi api;

    @Override
    public void onCreate() {
        super.onCreate();

        api = new Retrofit.Builder()
                .baseUrl(TmdbApi.URL)
                .client(new OkHttpClient.Builder().build())
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(TmdbApi.class);
    }
}
