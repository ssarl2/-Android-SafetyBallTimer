package com.example.yuri.sbt0207;

import java.io.Serializable;

/**
 * Created by Wonhak on 2019-04-02.
 */

public class EachValue implements Serializable {

    public String sentTime;
    public int value;

    public EachValue(){
        sentTime = "empty";
        value = 0;
    }
}
