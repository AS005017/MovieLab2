package com.labs.movielab2.api;

import com.labs.movielab2.models.CreditsResponse;
import com.labs.movielab2.models.MovieDetail;
import com.labs.movielab2.models.MovieResponse;
import com.labs.movielab2.models.TrailerResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {

    @GET("movie/popular")
    Observable<MovieResponse> getPopularMovies(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") Integer page);

    @GET("movie/top_rated")
    Observable<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey, @Query("language") String language);

    @GET("movie/{movie_id}")
    Observable<MovieDetail> getMovieDetail(@Path("movie_id") Integer movie_id, @Query("api_key") String apiKey,@Query("language") String language, @Query("append_to_response") String appendToResponse);

    @GET("movie/{movie_id}/credits")
    Observable<CreditsResponse> getCredits(@Path("movie_id") int movie_id, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/videos")
    Observable<TrailerResponse> getVideos(@Path("movie_id") int movie_id, @Query("api_key") String apiKey, @Query("language") String language);
}
