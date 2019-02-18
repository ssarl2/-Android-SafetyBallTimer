package com.example.yuri.sbt0207;

import android.app.Activity;
import android.os.Bundle;

public class Activity4 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);  // layout xml 과 자바파일을 연결
    } // end onCreate()

}
