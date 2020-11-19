package com.labs.movielab2.models;

import com.google.gson.annotations.SerializedName;

public class Genres {
    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;

    public Genres(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Genres() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
