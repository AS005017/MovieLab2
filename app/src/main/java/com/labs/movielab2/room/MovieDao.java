package com.labs.movielab2.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.labs.movielab2.models.MovieDetail;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(MovieDetail... movie);

    @Delete
    void delete(MovieDetail movie);

    @Query("SELECT * FROM moviedetail")
    Flowable<List<MovieDetail>> getMovies();

    @Query("SELECT EXISTS (SELECT * FROM moviedetail WHERE id= :id)")
    boolean getMovieById(Integer id);
}
