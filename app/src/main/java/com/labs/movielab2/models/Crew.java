package com.labs.movielab2.models;

import com.google.gson.annotations.SerializedName;

public class Crew {
    @SerializedName("credit_id")
    private String credit_id;
    @SerializedName("department")
    private String department;
    @SerializedName("gender")
    private int gender;
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("job")
    private String job;
    @SerializedName("profile_path")
    private String profile_path;

    public Crew(String credit_id, String department, int gender, int id, String name, String job, String profile_path) {
        this.credit_id = credit_id;
        this.department = department;
        this.gender = gender;
        this.id = id;
        this.name = name;
        this.job = job;
        this.profile_path = profile_path;
    }

    public String getCredit_id() {
        return credit_id;
    }

    public void setCredit_id(String credit_id) {
        this.credit_id = credit_id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getProfile_path() {
        return  "https://image.tmdb.org/t/p/w500" + profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }
}
