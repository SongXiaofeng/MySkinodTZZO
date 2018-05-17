package com.skinod.tzzo.skinod.constant;

/**
 * Created by Administrator on 2018/2/6.
 */

public class RobotMessage {

    private String name;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public RobotMessage(String n, String c){
        this.name=n;
        this.content=c;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
