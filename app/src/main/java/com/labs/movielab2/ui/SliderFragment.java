package com.labs.movielab2.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.labs.movielab2.BuildConfig;
import com.labs.movielab2.R;
import com.labs.movielab2.adapters.MovieItemClickListener;
import com.labs.movielab2.adapters.SliderPagerAdapter;
import com.labs.movielab2.api.Client;
import com.labs.movielab2.api.Service;
import com.labs.movielab2.models.Movie;
import com.labs.movielab2.models.MovieResponse;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class SliderFragment extends Fragment implements MovieItemClickListener {

    private static final String TAG = "myLogs";
    private ViewPager sliderpager;
    private TabLayout indicator;
    private List<Movie> topratedMovies;
    private Timer timer = new Timer();
    private ProgressBar progressBar;

    public static Fragment newInstance() {
        return new SliderFragment();
    }

    @Override
    public void onMovieClick(Movie clickedDataItem, ImageView imageView) {
        String movieList = new Gson().toJson(clickedDataItem);
        Bundle argument = new Bundle();
        argument.putString("Movie", movieList);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(argument);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slider, container, false);
        sliderpager = view.findViewById(R.id.slider_pager) ;
        progressBar = view.findViewById(R.id.progressBarSlider);
        indicator = view.findViewById(R.id.indicator);
        loadJSON();
        return view;
    }

    private void loadJSON() {
        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()){
                Toast.makeText(getContext(),"Please set API Key",Toast.LENGTH_LONG).show();
            }
            Service apiService = Client.GetClient().create(Service.class);
            Observable<MovieResponse> call = apiService.getTopRatedMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN, Locale.getDefault().getLanguage());
            call.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<MovieResponse>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull MovieResponse movieResponse) {
                            topratedMovies = movieResponse.getResults();
                            topratedMovies = topratedMovies.subList(0, topratedMovies.size()/2);
                            sliderpager.setAdapter(new SliderPagerAdapter(getContext(), topratedMovies, SliderFragment.this));
                            // setup timer
                            timer.scheduleAtFixedRate(new SliderTimer(),4000,5000);
                            indicator.setupWithViewPager(sliderpager,true);
                            if (topratedMovies.isEmpty())
                                progressBar.setVisibility(View.VISIBLE);
                            else
                                progressBar.setVisibility(View.INVISIBLE);
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

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(),4000,5000);
    }

    class SliderTimer extends TimerTask {
        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (topratedMovies != null)
                        if (sliderpager.getCurrentItem() < topratedMovies.size() - 1) {
                            sliderpager.setCurrentItem(sliderpager.getCurrentItem() + 1);
                        } else
                            sliderpager.setCurrentItem(0);
                    }
            });
        }
    }
}