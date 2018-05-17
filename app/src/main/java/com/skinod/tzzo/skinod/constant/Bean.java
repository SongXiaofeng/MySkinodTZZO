package com.skinod.tzzo.skinod.constant;

/**
 * Created by Administrator on 2018/3/7.
 */

public class Bean {
    private String title;
    private int id;

    public Bean(String title,int id) {
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
