package com.angry.web.modele;

import java.util.ArrayList;

/**
 * Created by Artur on 2014-06-10.
 */
public class World{
    public World() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Level> getLevelArrayList() {
        return levelArrayList;
    }

    public void setLevelArrayList(ArrayList<Level> levelArrayList) {
        this.levelArrayList = levelArrayList;
    }

    public int id;
    public ArrayList<Level> levelArrayList = new ArrayList<Level>();

}
