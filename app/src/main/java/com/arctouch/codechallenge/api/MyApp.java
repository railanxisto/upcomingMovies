package com.arctouch.codechallenge.api;

import android.app.Application;

/**
 * Created by railan on 30/08/18.
 */

public class MyApp extends Application {
    public static TmdbApi api;

    @Override
    public void onCreate() {
        super.onCreate();

        api = ApiManager.getClient().create(TmdbApi.class);
    }
}
