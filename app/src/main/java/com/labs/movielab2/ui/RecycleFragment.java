package com.labs.movielab2.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.labs.movielab2.BuildConfig;
import com.labs.movielab2.R;
import com.labs.movielab2.adapters.MovieAdapter;
import com.labs.movielab2.adapters.MovieItemClickListener;
import com.labs.movielab2.api.Client;
import com.labs.movielab2.api.Service;
import com.labs.movielab2.models.Movie;
import com.labs.movielab2.models.MovieResponse;

import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class RecycleFragment extends Fragment implements MovieItemClickListener {

    private RecyclerView moviesRV;
    private List<Movie> popularMovies;
    private ImageView btnFirst,btnPrev,btnNext,btnLast;
    private TextInputLayout numPage;
    private EditText editText;
    private ProgressBar progressBar;
    private int page = 1,allpages = 1;

    public static Fragment newInstance() {
        return new RecycleFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycle, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        moviesRV = view.findViewById(R.id.Rv_movies);
        btnFirst = view.findViewById(R.id.imgFirstPage);
        btnPrev = view.findViewById(R.id.imgPrevPage);
        btnNext = view.findViewById(R.id.imgNextPage);
        btnLast = view.findViewById(R.id.imgLastPage);
        numPage = view.findViewById(R.id.tilNumPage);
        editText = view.findViewById(R.id.etNumPage);
        reloadPopularFilms(page);
        initButtons();

        return view;
    }

    private void reloadPopularFilms(int newpage) {
        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()){
                Toast.makeText(getContext(),"Please set API Key",Toast.LENGTH_LONG).show();
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
                            moviesRV.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
                            moviesRV.setAdapter(new MovieAdapter(getContext(), popularMovies, RecycleFragment.this));
                            if (popularMovies.isEmpty())
                                progressBar.setVisibility(View.VISIBLE);

                            else
                                progressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
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

    @Override
    public void onMovieClick(Movie clickedDataItem, ImageView imageView) {
        String movieList = new Gson().toJson(clickedDataItem);
        Bundle argument = new Bundle();
        argument.putString("Movie", movieList);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(argument);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).addToBackStack(null).commit();
    }
}