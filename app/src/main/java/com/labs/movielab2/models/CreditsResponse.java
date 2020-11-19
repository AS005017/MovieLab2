package com.labs.movielab2.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreditsResponse {
    //@SerializedName("id")
    //private Integer id;
    @SerializedName("cast")
    private List<Cast> castList;
    @SerializedName("crew")
    private  List<Crew> crewList;

    public CreditsResponse(List<Cast> cast, List<Crew> crew) {
        //this.id = id;
        this.castList = cast;
        this.crewList = crew;
    }
/*
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
*/
    public List<Cast> getCastList() {
        return castList;
    }

    public void setCastList(List<Cast> castList) {
        this.castList = castList;
    }

    public List<Crew> getCrewList() {
        return crewList;
    }

    public void setCrewList(List<Crew> crewList) {
        this.crewList = crewList;
    }
}
