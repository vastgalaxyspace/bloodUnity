package com.example.bloodunityapp;

public class Donatemodel {
    private int image;
    private String hname;
    private String blooodgrp;
    private String location;

    public Donatemodel(int image, String hname, String blooodgrp,String location) {
        this.image = image;
        this.hname = hname;
        this.blooodgrp = blooodgrp;
        this.location=location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Donatemodel() {
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getHname() {
        return hname;
    }

    public void setHname(String hname) {
        this.hname = hname;
    }

    public String getBlooodgrp() {
        return blooodgrp;
    }

    public void setBlooodgrp(String blooodgrp) {
        this.blooodgrp = blooodgrp;
    }
}
