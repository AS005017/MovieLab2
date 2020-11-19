package com.labs.movielab2.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.labs.movielab2.models.MovieDetail;

@Database(entities = {MovieDetail.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MovieDao getMovieDao();
}