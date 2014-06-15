package com.angry.web.modele;

/**
 * Created by Artur on 2014-06-10.
 */
public class Level{
    public Level() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public int id;
    public Result result;

}