package com.example.raejin.mymovieapp.form;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by student on 2018-12-13.
 */

public class ListViewItem {
    String title;
    String date;
    int img_id;

    // 추가된 변수들
    int number;
    int runningTime;
    ArrayList<String> directors;
    ArrayList<String> actors;
    ArrayList<String> categorys;
    String imgFileName;

    public ListViewItem(String title, String date, int img_id) {
        this.title = title;
        this.date = date;
        this.img_id = img_id;
    }

    public ListViewItem(int number, String title, String date,
                        int runningTime, ArrayList<String> directors,
                        ArrayList<String> actors, ArrayList<String> categorys,
                        String imgFileName) {
        this.title = title;
        this.date = date;
        this.number = number;
        this.runningTime = runningTime;
        this.directors = directors;
        this.actors = actors;
        this.categorys = categorys;
        this.imgFileName = imgFileName;
        this.img_id = -1;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(int runningTime) {
        this.runningTime = runningTime;
    }

    public ArrayList<String> getDirectors() {
        return directors;
    }

    public void setDirectors(ArrayList<String> directors) {
        this.directors = directors;
    }

    public ArrayList<String> getActors() {
        return actors;
    }

    public void setActors(ArrayList<String> actors) {
        this.actors = actors;
    }

    public ArrayList<String> getCategorys() {
        return categorys;
    }

    public void setCategorys(ArrayList<String> categorys) {
        this.categorys = categorys;
    }

    public String getImgFileName() {
        return imgFileName;
    }

    public void setImgFileName(String imgFileName) {
        this.imgFileName = imgFileName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getImg_id() {
        return img_id;
    }

    public void setImg_id(int img_id) {
        this.img_id = img_id;
    }
}
