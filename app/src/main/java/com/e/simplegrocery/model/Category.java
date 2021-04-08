package com.e.simplegrocery.model;

public class Category {
    public String name;
    public String imageurl;

    public Category() {
    }

    public Category(String name,String imageurl) {

        this.name = name;
        this.imageurl = imageurl;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
