package com.example.saksham.laundromat.fragments.Home;

import android.graphics.Bitmap;

/**
 * Created by USER on 05-02-2017.
 */
public class Home {
    private int id;
    private String filename;
    private Bitmap image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}


