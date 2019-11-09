package com.example.yuri.sbt0207.Object;

import java.io.Serializable;

/**
 * Created by Wonhak on 2019-03-31.
 */

public class Analyze implements Serializable{

    private String question;
    private int que_num;
    private int total_value;
    private int count;

    public Analyze(){
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getQue_num() {
        return que_num;
    }

    public void setQue_num(int que_num) {
        this.que_num = que_num;
    }

    public int getTotal_value() {
        return total_value;
    }

    public void setTotal_value(int total_value) {
        this.total_value = total_value;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
