package com.labs.movielab2.adapters;

import android.widget.ImageView;

import com.labs.movielab2.models.MovieDetail;

public interface FavoriteMovieClickListener {

    void onMovieClick(MovieDetail clickedDataItem, ImageView imageView);
}
