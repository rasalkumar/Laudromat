package com.example.saksham.laundromat.fragments.laundromat.fragments.Prev;

import java.util.ArrayList;

/**
 * Created by USER on 08-02-2017.
 */

public class Prev {
    private String washesDone;
    private int id;
    private String name;
    private ArrayList<String> history;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getWashesDone() {
        return washesDone;
    }

    public ArrayList<String> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<String> history) {
        this.history = history;
    }

    public void setWashesDone(String washesDone) {
        this.washesDone = washesDone;
    }

    public void setName(String name) {
        this.name = name;
    }
}
