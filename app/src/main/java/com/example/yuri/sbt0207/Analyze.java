package com.example.yuri.sbt0207;

import java.io.Serializable;

/**
 * Created by Wonhak on 2019-03-31.
 */

public class Analyze implements Serializable{
    public int que_num;
    public int total_value;
    public int count;

    public Analyze(){
        que_num = 0;
        total_value = 0;
        count = 0;
    }
}
