package com.example.yuri.sbt0207;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

public class LastActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.last_activity);

        SavingData();

        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // In order to exit app exactly when you go to home or elsewhere
                ActivityCompat.finishAffinity(LastActivity.this);
                System.runFinalizersOnExit(true);
                System.exit(0);
            }
        }, 2000);

    }

    // START save data to backGround
    void SavingData(){
        SharedPreferences sharedPreferences = getSharedPreferences("backgroundData",MODE_PRIVATE); //SharedPreferences를 기본모드로 설정
        SharedPreferences.Editor editor = sharedPreferences.edit(); //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.

        long limitTime = 0;
        editor.putString("limitTime",String.valueOf(limitTime));

        editor.commit();
    }
    // END save data to backGround

}
