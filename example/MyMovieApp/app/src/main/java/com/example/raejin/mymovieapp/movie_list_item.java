package com.example.raejin.mymovieapp;

/**
 * Created by 501-00 on 2018-03-14.
 */

public class movie_list_item {
    int movie_poster_img;

    public int getMovie_poster_img() {
        return movie_poster_img;
    }

    public void setMovie_poster_img(int movie_poster_img) {
        this.movie_poster_img = movie_poster_img;
    }

    public String getMovie_title() {
        return movie_title;
    }

    public void setMovie_title(String movie_title) {
        this.movie_title = movie_title;
    }

    String movie_title;

    public movie_list_item(int movie_poster_img, String movie_title) {
        this.movie_poster_img = movie_poster_img;
        this.movie_title = movie_title;
    }
}
