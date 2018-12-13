package com.example.raejin.mymovieapp.format;

import java.util.ArrayList;

public class MovieInfo {


    String title;
    String director;
    String actors;
    ArrayList<Integer> images;

    public MovieInfo(String title, String director, String actors, ArrayList<Integer> images) {
        this.title = title;
        this.director = director;
        this.actors = actors;
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public String getActors() {
        return actors;
    }

    public ArrayList<Integer> getImages() {
        return images;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setActor(String actor) {
        this.actors = actor;
    }

    public void setImages(ArrayList<Integer> images) {
        this.images = images;
    }
}
