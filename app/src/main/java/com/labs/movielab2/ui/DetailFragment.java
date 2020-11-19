package com.labs.movielab2.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.labs.movielab2.BuildConfig;
import com.labs.movielab2.R;
import com.labs.movielab2.adapters.CreditsAdapter;
import com.labs.movielab2.adapters.TrailerAdapter;
import com.labs.movielab2.adapters.TrailerItemClickListener;
import com.labs.movielab2.api.Client;
import com.labs.movielab2.api.Service;
import com.labs.movielab2.models.Cast;
import com.labs.movielab2.models.CreditsResponse;
import com.labs.movielab2.models.Movie;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DetailFragment extends Fragment implements TrailerItemClickListener {

    private static final String TAG = "myLogs";
    private ImageView moviePoster, movieBackgroung, movieAdult;
    private TextView movieTitle, movieOverview, movieRate, movieDate, movieGenres, movieTrailerNone, movieCastsNone;
    private List<Cast> castList;
    private CreditsResponse creditsResponses;
    private TrailerResponse trailerResponses;
    private MovieDetail ourMovie;
    private HashMap<Integer, String> genresMap;
    private Movie movie;
    private ArrayList<Integer> newGenres;
    private List<Trailer> trailersList;
    private RecyclerView creditsRv, trailersRV;
    private FloatingActionButton fab;
    private int movieId = 0;
    private boolean containFilm = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Type movieType = new TypeToken<Movie>() {}.getType();
        String movieStringRecvFromFrag = getArguments().getString("Movie");
        movie = new Gson().fromJson(movieStringRecvFromFrag,movieType);
        if (Locale.getDefault().getLanguage().equals("ru"))
            initRUgenres();
        else if (Locale.getDefault().getLanguage().equals("en"))
            initENgenres();

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

        return view;
    }

    @Override
    public void onViewCreated(@androidx.annotation.NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iniViews();
        if (movieId != 0)
              loadFullDetail(movieId);

    }

    void iniViews() {
            StringBuilder genres = new StringBuilder();
            movieId = movie.getId();
            String movieTitle = movie.getTitle();
            String movieBackground = movie.getBackdropPath();
            String moviePoster = movie.getPosterPath();
            String movieOverview = movie.getOverview();
            String movieReleaseDate = movie.getReleaseDate();
            Double movieVoteAverage = movie.getVoteAverage();
            Boolean movieAdult = movie.isAdult();
            AppDatabase db = App.getInstance().getDatabase();

            Type genresType = new TypeToken<ArrayList<Integer>>() {}.getType();
            String genresString = new Gson().toJson(movie.getGenre_ids());
            newGenres = new Gson().fromJson(genresString,genresType);

            for (int i =0; i<newGenres.size(); i++) {
                if(genresMap.containsKey(newGenres.get(i)) && i+1 < newGenres.size())
                    genres.append(genresMap.get(newGenres.get(i))).append(", ");
                else if (genresMap.containsKey(newGenres.get(i)))
                    genres.append(genresMap.get(newGenres.get(i)));
            }

            try {
                movieReleaseDate = convertDate(movieReleaseDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            this.movieGenres.setText(genres);
            Glide.with(this).load(moviePoster).into(this.moviePoster);
            Glide.with(this).load(movieBackground).into(movieBackgroung);
            this.movieTitle.setText(movieTitle);
            if (!movieOverview.equals(""))
                this.movieOverview.setText(movieOverview);
            else
                this.movieOverview.setText(getString(R.string.translation_n_available));
            if (movieAdult) {
                this.movieAdult.setVisibility(View.VISIBLE);
            } else {
                this.movieAdult.setVisibility(View.INVISIBLE);
            }
            this.movieRate.setText(Math.round(movieVoteAverage * 10) + "%");
            this.movieDate.setText(movieReleaseDate);

            movieBackgroung.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.scale_animation));


            /*try {
                if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                    Toast.makeText(getContext(), "Please set API Key", Toast.LENGTH_LONG).show();
                }
                Service apiService = Client.GetClient().create(Service.class);
                Observable<CreditsResponse> call = apiService.getCredits(movieId, BuildConfig.THE_MOVIE_DB_API_TOKEN);
                call.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<CreditsResponse>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(@NonNull CreditsResponse creditsResponse) {
                                castList = creditsResponse.getCastList();
                                creditsRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                                creditsRv.setAdapter(new CreditsAdapter(getContext(), castList));
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d(TAG, "error " + e.getMessage());
                                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                Observable<TrailerResponse> cal = apiService.getVideos(movieId, BuildConfig.THE_MOVIE_DB_API_TOKEN, Locale.getDefault().getLanguage());
                cal.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<TrailerResponse>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(@NonNull TrailerResponse trailerResponse) {
                                trailersList = trailerResponse.getResults();
                                trailersRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                                trailersRV.setAdapter(new TrailerAdapter(getContext(), trailersList, DetailFragment.this));
                                if (trailersList.size() == 0)
                                   movieTrailerNone.setVisibility(View.VISIBLE);

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d(TAG, "error " + e.getMessage());
                                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            } catch (Exception e) {
                Log.d(TAG, "error " + e.getMessage());
                Toast.makeText(getContext(), "Err: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } */
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
                                            public void onSubscribe(@NonNull Disposable d) {

                                            }

                                            @Override
                                            public void onNext(@NonNull AppDatabase appDatabase) {
                                                if (ourMovie != null) {
                                                if (containFilm) {
                                                    db.getMovieDao().delete(ourMovie);
                                                    fab.setImageResource(R.drawable.ic_baseline_favorite_24);
                                                    containFilm = false;
                                                }
                                                else {
                                                    containFilm = true;
                                                    db.getMovieDao().insertAll(ourMovie);
                                                    fab.setImageResource(R.drawable.ic_baseline_favorite_red);
                                                }
                                                } else
                                                    Toast.makeText(getContext(), "Try again later", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onError(@NonNull Throwable e) {
                                                Toast.makeText(getContext(), "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
                                            }

                                            @Override
                                            public void onComplete() {
                                                if (containFilm)
                                                    Toast.makeText(getContext(), "Movie deleted from favorite!", Toast.LENGTH_SHORT).show();
                                                else
                                                Toast.makeText(getContext(), "Movie added to favorite!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
    }

    private void loadFullDetail(int movie_Id) {
        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()){
                Toast.makeText(getContext(),"Please set API Key",Toast.LENGTH_LONG).show();
            }
            Service apiService = Client.GetClient().create(Service.class);
            Observable<MovieDetail> cal = apiService.getMovieDetail(movie_Id,BuildConfig.THE_MOVIE_DB_API_TOKEN, Locale.getDefault().getLanguage(),"credits,videos");
            cal.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<MovieDetail>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull MovieDetail movieDetail) {
                            ourMovie = movieDetail;

                            Type creditType = new TypeToken<CreditsResponse>() {}.getType();
                            String crResp = new Gson().toJson(ourMovie.getCredits());
                            creditsResponses = new Gson().fromJson(crResp,creditType);
                            castList = creditsResponses.getCastList();
                            creditsRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                            creditsRv.setAdapter(new CreditsAdapter(getContext(), castList));
                            if (castList.size() == 0)
                                movieCastsNone.setVisibility(View.VISIBLE);

                            Type listTypeTr = new TypeToken<TrailerResponse>() {}.getType();
                            String trResp = new Gson().toJson(ourMovie.getVideos());
                            trailerResponses = new Gson().fromJson(trResp,listTypeTr);
                            trailersList = trailerResponses.getResults();
                            trailersRV.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            trailersRV.setAdapter(new TrailerAdapter(getContext(), trailersList, DetailFragment.this));
                            if (trailersList.size() == 0)
                                movieTrailerNone.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.d(TAG, "error " + e.getMessage());
                            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch (Exception e) {
            Toast.makeText(getContext(), "Err: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initRUgenres(){
        genresMap = new HashMap<>();
        genresMap.put(28,"Боевик");
        genresMap.put(12,"Приключения");
        genresMap.put(16,"Мультфильм");
        genresMap.put(35,"Комедия");
        genresMap.put(80,"Криминал");
        genresMap.put(99,"Документальный");
        genresMap.put(18,"Драма");
        genresMap.put(10751,"Семейный");
        genresMap.put(14,"Фэнтези");
        genresMap.put(36,"История");
        genresMap.put(27,"Ужасы");
        genresMap.put(10402,"Музыка");
        genresMap.put(9648,"Детектив");
        genresMap.put(10749,"Мелодрама");
        genresMap.put(878,"Фантастика");
        genresMap.put(10770,"Телевизионный фильм");
        genresMap.put(53,"Триллер");
        genresMap.put(10752,"Военный");
        genresMap.put(37,"Вестерн");
    }

    private  void initENgenres(){
        genresMap = new HashMap<>();
        genresMap.put(28,"Action");
        genresMap.put(12,"Adventure");
        genresMap.put(16,"Animation");
        genresMap.put(35,"Comedy");
        genresMap.put(80,"Crime");
        genresMap.put(99,"Documentary");
        genresMap.put(18,"Drama");
        genresMap.put(10751,"Family");
        genresMap.put(14,"Fantasy");
        genresMap.put(36,"History");
        genresMap.put(27,"Horror");
        genresMap.put(10402,"Music");
        genresMap.put(9648,"Mystery");
        genresMap.put(10749,"Romance");
        genresMap.put(878,"Science fiction");
        genresMap.put(10770,"TV movie");
        genresMap.put(53,"Thriller");
        genresMap.put(10752,"War");
        genresMap.put(37,"Western");
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
