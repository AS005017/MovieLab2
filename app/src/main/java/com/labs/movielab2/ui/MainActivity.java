package com.labs.movielab2.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.labs.movielab2.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "myLogs";
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       fab = findViewById(R.id.fab_main);
       fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment, FavoriteFragment.newInstance()).addToBackStack(null).commit();
           }
       });


        //loadJSON();
        //initButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager().beginTransaction().replace(R.id.recycleFrame, RecycleFragment.newInstance()).commitNow();
        getSupportFragmentManager().beginTransaction().replace(R.id.sliderFrame, SliderFragment.newInstance()).commitNow();

    }

    /*
    @Override
    public void onMovieClick(Movie clickedDataItem, ImageView imageView) {
        Intent intent = new Intent(this,MovieDetailActivity.class);
        intent.putExtra("movieId",clickedDataItem.getId());
        intent.putExtra("title", clickedDataItem.getTitle());
        intent.putExtra("background_path", clickedDataItem.getBackdropPath());
        intent.putExtra("poster_path", clickedDataItem.getPosterPath());
        intent.putExtra("overview", clickedDataItem.getOverview());
        intent.putExtra("release_date", clickedDataItem.getReleaseDate());
        intent.putExtra("vote_average", clickedDataItem.getVoteAverage());
        intent.putExtra("adult", clickedDataItem.isAdult());
        intent.putIntegerArrayListExtra("genreIds", clickedDataItem.getGenreIds());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,
                                                    imageView,"sharedName");

        startActivity(intent,options.toBundle());

    }
*/

/*
    private void loadJSON() {
        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()){
                Toast.makeText(getApplicationContext(),"Please set API Key",Toast.LENGTH_LONG).show();
            }
            Service apiService = Client.GetClient().create(Service.class);
            Observable<MovieResponse> cal = apiService.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN, Locale.getDefault().getLanguage(),page);
            cal.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<MovieResponse>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull MovieResponse movieResponse) {
                            popularMovies = movieResponse.getResults();
                            allpages = movieResponse.getTotalPages();
                            moviesRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
                            moviesRV.setAdapter(new MovieAdapter(getApplicationContext(), popularMovies, MainActivity.this));

                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.d(TAG, "error " + e.getMessage());
                            Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch (Exception e) {
            Toast.makeText(this, "Err: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void reloadPopularFilms(int newpage) {
        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()){
                Toast.makeText(getApplicationContext(),"Please set API Key",Toast.LENGTH_LONG).show();
            }
        Service apiService = Client.GetClient().create(Service.class);
        Observable<MovieResponse> cal = apiService.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN, Locale.getDefault().getLanguage(),newpage);
        cal.subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())
           .subscribe(new Observer<MovieResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull MovieResponse movieResponse) {
                        popularMovies = movieResponse.getResults();
                        allpages = movieResponse.getTotalPages();
                        moviesRV.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
                        moviesRV.setAdapter(new MovieAdapter(getApplicationContext(), popularMovies, MainActivity.this));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    } catch (Exception e) {
        Toast.makeText(this, "Err: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }
    }

    private void initButtons() {
        editText.setText(String.valueOf(page));
        btnFirst.setImageResource(R.drawable.ic_baseline_first_page_black);
        btnFirst.setEnabled(false);
        btnPrev.setImageResource(R.drawable.ic_baseline_chevron_left_black);
        btnPrev.setEnabled(false);
        btnFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 1;
                reloadPopularFilms(page);
                editText.setText(String.valueOf(page));
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page --;
                reloadPopularFilms(page);
                editText.setText(String.valueOf(page));
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page++;
                reloadPopularFilms(page);
                editText.setText(String.valueOf(page));
            }
        });
        btnLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = allpages;
                reloadPopularFilms(page);
                editText.setText(String.valueOf(page));
            }
        });
        numPage.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int value =0;
                if (charSequence.length()>0)
            value = Integer.parseInt(charSequence.toString());
                if (value > allpages && value != 0) {
                charSequence = String.valueOf(allpages);
                editText.setText(charSequence);
                }
                if (charSequence.length() == 0)
                    editText.setText("1");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    if (Integer.parseInt(editable.toString()) > 0) {
                        if (Integer.parseInt(editable.toString()) < allpages) {
                            page = Integer.parseInt(editable.toString());
                            reloadPopularFilms(page);
                        }
                        if (Integer.parseInt(editable.toString())>1 && Integer.parseInt(editable.toString())<allpages) {
                            btnFirst.setEnabled(true);
                            btnFirst.setImageResource(R.drawable.ic_baseline_first_page_24);
                            btnPrev.setEnabled(true);
                            btnPrev.setImageResource(R.drawable.ic_baseline_chevron_left_24);
                            btnLast.setImageResource(R.drawable.ic_baseline_last_page_24);
                            btnNext.setImageResource(R.drawable.ic_baseline_chevron_right_24);
                            btnNext.setEnabled(true);
                            btnLast.setEnabled(true);
                        } else if(editable.toString().equals("1")) {
                            btnFirst.setImageResource(R.drawable.ic_baseline_first_page_black);
                            btnFirst.setEnabled(false);
                            btnPrev.setImageResource(R.drawable.ic_baseline_chevron_left_black);
                            btnPrev.setEnabled(false);
                            btnNext.setEnabled(true);
                            btnLast.setEnabled(true);
                            btnNext.setImageResource(R.drawable.ic_baseline_chevron_right_24);
                            btnLast.setImageResource(R.drawable.ic_baseline_last_page_24);
                        } else if (editable.toString().equals(String.valueOf(allpages))) {
                            btnLast.setEnabled(false);
                            btnLast.setImageResource(R.drawable.ic_baseline_last_page_black);
                            btnNext.setEnabled(false);
                            btnNext.setImageResource(R.drawable.ic_baseline_chevron_right_black);
                            btnFirst.setEnabled(true);
                            btnPrev.setEnabled(true);
                            btnFirst.setImageResource(R.drawable.ic_baseline_first_page_24);
                            btnPrev.setImageResource(R.drawable.ic_baseline_chevron_left_24);
                        }
                    }
                    if (Integer.parseInt(editable.toString()) > allpages)
                        reloadPopularFilms(allpages);
                }
            }
        });
    }
*/

}
