package com.labs.movielab2.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailerResponse {
    //@SerializedName("id")
    //private Integer id;
    @SerializedName("results")
    private List<Trailer> results;

    public TrailerResponse(List<Trailer> results) {
        //this.id = id;
        this.results = results;
    }
/*
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
*/
    public List<Trailer> getResults() {
        return results;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }
}
