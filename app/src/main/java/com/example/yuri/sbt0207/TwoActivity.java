package com.example.yuri.sbt0207;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

public class TwoActivity extends Activity {
    private TextView seekval2;
    private SeekBar moomin;
    private TextView Q2;
    String que2, que3, que4, que5;
    int Value2;
    int answer1, answer2;

    // 2월 19일 깃허브 테스트 주석
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        Q2 = (TextView)findViewById(R.id.Q2);
        seekval2= (TextView)findViewById(R.id.seekText2);
        moomin = (SeekBar)findViewById(R.id.seekBarMU);
        moomin.setOnSeekBarChangeListener(seekBarChangeListener); // 받아들이 값을 moomin 시크바에 적용시킴
        Intent intent = getIntent();

        que2 = intent.getExtras().getString("que2");
        que3 = intent.getExtras().getString("que3");
        que4 = intent.getExtras().getString("que4");
        que5 = intent.getExtras().getString("que5");

        answer1 = intent.getExtras().getInt("ans1");

        Q2.setText(que2);
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
            seekval2.setText(""+progress);
            Value2=progress;

            // view가 시크바의 thumb 따라다니게 만드는 함수
            int padding= moomin.getPaddingLeft() + moomin.getPaddingRight();
            int sPos = moomin.getLeft() + moomin.getPaddingLeft();
            int xPos = (moomin.getWidth()-padding) * moomin.getProgress() / moomin.getMax() + sPos - (seekval2.getWidth()/2);
            seekval2.setX(xPos);
            answer2 = progress;
        }

        // 프로그레스바를 눌릴 때 작동하는 함수
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        // 프로그레스바를 떼고 작동하는 함수
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            Handler delayHandler = new Handler();
            delayHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent next_intent = new Intent(getBaseContext(), ThreeActivity.class);
                    next_intent.putExtra("que3", que3);
                    next_intent.putExtra("que4", que4);
                    next_intent.putExtra("que5", que5);
                    next_intent.putExtra("ans1", answer1);
                    next_intent.putExtra("ans2", answer2);
                    startActivity(next_intent);
                    overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                }
            }, 3000);

        }
    };


}
