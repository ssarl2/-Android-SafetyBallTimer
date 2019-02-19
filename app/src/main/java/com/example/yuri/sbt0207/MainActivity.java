package com.example.yuri.sbt0207;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageButton;
import android.app.Activity;
import android.view.View.OnClickListener;
import java.net.Authenticator;

import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView seekval1;
    private SeekBar moomin;
    int Value1;
    // 2월 19일 깃허브 테스트 주석
    // 2월 19일 깃허브 테스트 주석 -원학- 잘되는듯
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekval1= (TextView)findViewById(R.id.seekText1);
        moomin = (SeekBar)findViewById(R.id.seekBarMU);
        moomin.setOnSeekBarChangeListener(seekBarChangeListener); // 받아들이 값을 moomin 시크바에 적용시킴

    }

    // 시크바의 체인지리스너 매개함수? 생성..
    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener(){

        // 프로그레스바가 움직일 때 작동하는 함수
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            progress = moomin.getProgress();
            if(progress<20)
                moomin.setThumb(getResources().getDrawable(R.drawable.sad2));
            else if(progress<40)
                moomin.setThumb(getResources().getDrawable(R.drawable.sad));
            else if(progress>60 && progress<=80)
                moomin.setThumb(getResources().getDrawable(R.drawable.happy));
            else if(progress>80)
                moomin.setThumb(getResources().getDrawable(R.drawable.happy2));
            else
                moomin.setThumb(getResources().getDrawable(R.drawable.mu));
            Log.e("nothing - - ", "onStartTrackingTouch: "+progress);
            seekval1.setText("현재값 : "+ progress);
            Value1=progress;

        }

        // 프로그레스바를 눌릴 때 작동하는 함수
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        // 프로그레스바를 떼고 작동하는 함수
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            Intent intent = new Intent(getBaseContext(),Activity2.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        }
    };


}
