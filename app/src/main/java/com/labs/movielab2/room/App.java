package com.labs.movielab2.room;

import android.app.Application;
import android.util.Log;

import androidx.room.Room;

import io.reactivex.plugins.RxJavaPlugins;


public class App extends Application {
    private static final String TAG = "myLogs";
    public static App instance;
    private AppDatabase database;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, "populus-database").build();
        RxJavaPlugins.setErrorHandler(throwable -> {
            Log.d(TAG, "error " + throwable.getMessage());});
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
