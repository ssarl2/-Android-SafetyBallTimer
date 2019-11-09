package com.example.yuri.sbt0207.Object;

import java.io.Serializable;

/**
 * Created by Wonhak on 2019-04-02.
 */

public class EachValue implements Serializable {

    private String sentTime;
    private int value;

    public EachValue(){
    }

    public String getSentTime() {
        return sentTime;
    }

    public void setSentTime(String sentTime) {
        this.sentTime = sentTime;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
