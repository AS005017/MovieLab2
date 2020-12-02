package com.labs.movielab2.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.labs.movielab2.R;
import com.labs.movielab2.adapters.FavoriteMovieClickListener;
import com.labs.movielab2.adapters.FavoritesAdapter;
import com.labs.movielab2.models.MovieDetail;
import com.labs.movielab2.room.App;
import com.labs.movielab2.room.AppDatabase;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class FavoriteFragment extends Fragment implements FavoriteMovieClickListener {

    private RecyclerView slider_favorite;
    private TextView favorites_out;
    private ImageView back_favorite;
    private List<MovieDetail> ourMovie;

    public static Fragment newInstance() {
        return new FavoriteFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        slider_favorite = view.findViewById(R.id.rv_favorites);
        favorites_out = view.findViewById(R.id.favorites_movie_out);
        back_favorite = view.findViewById(R.id.back_favorite);
        back_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    @SuppressLint("CheckResult")
    @Override
    public void onResume() {
        super.onResume();
        AppDatabase db = App.getInstance().getDatabase();

        db.getMovieDao().getMovies()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<MovieDetail>>() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void accept(List<MovieDetail> movies) {
                        ourMovie = movies;
                        if (ourMovie.size() == 0) {
                            favorites_out.setVisibility(View.VISIBLE);
                        } else {
                            favorites_out.setVisibility(View.INVISIBLE);
                        }
                        slider_favorite.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                        slider_favorite.setAdapter(new FavoritesAdapter(getContext(), ourMovie, FavoriteFragment.this));
                    }
                });
    }

    @Override
    public void onMovieClick(MovieDetail clickedDataItem, ImageView imageView) {
        String movieList = new Gson().toJson(clickedDataItem);
        Bundle argument = new Bundle();
        argument.putString("Movie", movieList);
        DetailFragmentFavorite fragment = new DetailFragmentFavorite();
        fragment.setArguments(argument);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
    }
}
