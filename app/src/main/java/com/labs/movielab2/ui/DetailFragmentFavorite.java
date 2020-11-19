package com.labs.movielab2.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.labs.movielab2.R;
import com.labs.movielab2.adapters.CreditsAdapter;
import com.labs.movielab2.adapters.TrailerAdapter;
import com.labs.movielab2.adapters.TrailerItemClickListener;
import com.labs.movielab2.models.Cast;
import com.labs.movielab2.models.CreditsResponse;
import com.labs.movielab2.models.Genres;
import com.labs.movielab2.models.MovieDetail;
import com.labs.movielab2.models.Trailer;
import com.labs.movielab2.models.TrailerResponse;
import com.labs.movielab2.room.App;
import com.labs.movielab2.room.AppDatabase;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DetailFragmentFavorite extends Fragment implements TrailerItemClickListener {

    private static final String TAG = "myLogs";
    private MovieDetail movie;
    private ImageView moviePoster, movieBackgroung, movieAdult;
    private TextView movieTitle, movieOverview, movieRate, movieDate, movieGenres, movieTrailerNone, movieCastsNone;
    private FloatingActionButton fab;
    private ArrayList<Genres> newGenres;
    private List<Trailer> trailersList;
    private RecyclerView creditsRv, trailersRV;
    private List<Cast> castList;
    private CreditsResponse creditsResponses;
    private TrailerResponse trailerResponses;
    private boolean containFilm = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Type movieType = new TypeToken<MovieDetail>() {}.getType();
        String movieStringRecvFromFrag = getArguments().getString("Movie");
        movie = new Gson().fromJson(movieStringRecvFromFrag,movieType);
        View view = inflater.inflate(R.layout.activity_movie_detail, container, false);
        movieAdult = view.findViewById(R.id.detail_movie_adult);
        movieOverview = view.findViewById(R.id.detail_movie_desc);
        movieTitle = view.findViewById(R.id.detail_movie_title);
        moviePoster = view.findViewById(R.id.detail_movie_img);
        movieBackgroung = view.findViewById(R.id.detail_movie_cover);
        movieRate = view.findViewById(R.id.detail_movie_rate);
        movieDate = view.findViewById(R.id.detail_movie_date);
        movieGenres = view.findViewById(R.id.detail_movie_genres);
        creditsRv = view.findViewById(R.id.Rv_credits);
        trailersRV = view.findViewById(R.id.Rv_trailers);
        movieTrailerNone = view.findViewById(R.id.detail_movie_trailers_none);
        movieCastsNone = view.findViewById(R.id.detail_movie_credits_none);
        fab = view.findViewById(R.id.fab_detail);

        initViews();

        return view;
    }

    void initViews() {
        StringBuilder genres = new StringBuilder();
        int movieId = movie.getId();
        String movie_Title = movie.getTitle();
        String movie_Background = movie.getBackdropPath();
        String movie_Poster = movie.getPosterPath();
        String movie_Overview = movie.getOverview();
        String movie_ReleaseDate = movie.getReleaseDate();
        Double movie_VoteAverage = movie.getVoteAverage();
        Boolean movie_Adult = movie.isAdult();
        AppDatabase db = App.getInstance().getDatabase();

        Type genresType = new TypeToken<ArrayList<Genres>>() {}.getType();
        String genresString = new Gson().toJson(movie.getGenres());
        newGenres = new Gson().fromJson(genresString,genresType);

        newGenres.forEach(genres1 -> {
            genres.append(genres1.getName()).append(", ");
        });
        genres.deleteCharAt(genres.length()-2);

        try {
            movie_ReleaseDate = convertDate(movie_ReleaseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        movieGenres.setText(genres);
        Glide.with(this).load(movie_Poster).placeholder(R.drawable.empty_profile).into(moviePoster);
        Glide.with(this).load(movie_Background).placeholder(R.drawable.loading).into(movieBackgroung);
        movieTitle.setText(movie_Title);
        if (!movie_Overview.equals(""))
            movieOverview.setText(movie_Overview);
        else
            movieOverview.setText(getString(R.string.translation_n_available));
        if (movie_Adult) {
            movieAdult.setVisibility(View.VISIBLE);
        } else {
            movieAdult.setVisibility(View.INVISIBLE);
        }
        movieRate.setText(Math.round(movie_VoteAverage * 10) + "%");
        movieDate.setText(movie_ReleaseDate);

        movieBackgroung.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.scale_animation));

        Type creditType = new TypeToken<CreditsResponse>() {}.getType();
        String crResp = new Gson().toJson(movie.getCredits());
        creditsResponses = new Gson().fromJson(crResp,creditType);
        castList = creditsResponses.getCastList();
        creditsRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        creditsRv.setAdapter(new CreditsAdapter(getContext(), castList));
        if (castList.size() == 0)
            movieCastsNone.setVisibility(View.VISIBLE);

        Type listTypeTr = new TypeToken<TrailerResponse>() {}.getType();
        String trResp = new Gson().toJson(movie.getVideos());
        trailerResponses = new Gson().fromJson(trResp,listTypeTr);
        trailersList = trailerResponses.getResults();
        trailersRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        trailersRV.setAdapter(new TrailerAdapter(getContext(), trailersList, DetailFragmentFavorite.this));
        if (trailersList.size() == 0)
            movieTrailerNone.setVisibility(View.VISIBLE);

        Observable.just(db)
                .subscribeOn(Schedulers.computation())
                .subscribe(new Observer<AppDatabase>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AppDatabase appDatabase) {
                        containFilm = db.getMovieDao().getMovieById(movieId);
                        if (containFilm)
                            fab.setImageResource(R.drawable.ic_baseline_favorite_red);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Observable.just(db)
                        .subscribeOn(Schedulers.computation())
                        .subscribe(new Observer<AppDatabase>() {
                            @Override
                            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(@io.reactivex.annotations.NonNull AppDatabase appDatabase) {
                                if (containFilm) {
                                    db.getMovieDao().delete(movie);
                                    fab.setImageResource(R.drawable.ic_baseline_favorite_24);
                                    containFilm = false;
                                }
                                else {
                                    containFilm = true;
                                    db.getMovieDao().insertAll(movie);
                                    fab.setImageResource(R.drawable.ic_baseline_favorite_red);
                                }
                            }

                            @Override
                            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                                Toast.makeText(getContext(), "Error"+e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onComplete() {
                                Toast.makeText(getContext(), "Movie deleted from favorite!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }


    private String convertDate(String date) throws ParseException {
        SimpleDateFormat spf=new SimpleDateFormat("yyyy-mm-dd");
        Date newDate=spf.parse(date);
        spf= new SimpleDateFormat("dd.mm.yyyy");
        return spf.format(newDate);
    }

    @Override
    public void onTrailerClick(Trailer clickedDataItem) {
        String videoId = clickedDataItem.getKey();
        Intent intent = new Intent( Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.youtube.com/watch?v="+videoId));
        this.startActivity(intent);
    }
}
