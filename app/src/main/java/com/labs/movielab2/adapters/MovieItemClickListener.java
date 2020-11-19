package com.labs.movielab2.adapters;

import android.widget.ImageView;

import com.labs.movielab2.models.Movie;

public interface MovieItemClickListener {

    void onMovieClick(Movie clickedDataItem, ImageView imageView);

}
