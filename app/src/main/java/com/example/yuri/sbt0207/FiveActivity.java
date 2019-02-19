package com.example.yuri.sbt0207;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

public class FiveActivity extends Activity {
    private TextView seekval5;
    private SeekBar moomin;
    private TextView Q5;
    String que5;
    int Value5;

    // 2월 19일 깃허브 테스트 주석
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five);
        Q5 = (TextView)findViewById(R.id.Q5);
        seekval5 = (TextView) findViewById(R.id.seekText5);
        moomin = (SeekBar) findViewById(R.id.seekBarMU);
        moomin.setOnSeekBarChangeListener(seekBarChangeListener); // 받아들이 값을 moomin 시크바에 적용시킴
        Intent intent = getIntent();

        que5 = intent.getExtras().getString("que5");

        Q5.setText(que5);
    }

    // 시크바의 체인지리스너 매개함수? 생성..
    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        // 프로그레스바가 움직일 때 작동하는 함수
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            progress = moomin.getProgress();
            if (progress < 20)
                moomin.setThumb(getResources().getDrawable(R.drawable.sad2));
            else if (progress < 40)
                moomin.setThumb(getResources().getDrawable(R.drawable.sad));
            else if (progress > 60 && progress <= 80)
                moomin.setThumb(getResources().getDrawable(R.drawable.happy));
            else if (progress > 80)
                moomin.setThumb(getResources().getDrawable(R.drawable.happy2));
            else
                moomin.setThumb(getResources().getDrawable(R.drawable.mu));
            Log.e("nothing - - ", "onStartTrackingTouch: " + progress);
            seekval5.setText(""+progress);
            Value5=progress;

            // view가 시크바의 thumb 따라다니게 만드는 함수
            int padding= moomin.getPaddingLeft() + moomin.getPaddingRight();
            int sPos = moomin.getLeft() + moomin.getPaddingLeft();
            int xPos = (moomin.getWidth()-padding) * moomin.getProgress() / moomin.getMax() + sPos - (seekval5.getWidth()/2);
            seekval5.setX(xPos);

        }

        // 프로그레스바를 눌릴 때 작동하는 함수
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        // 프로그레스바를 떼고 작동하는 함수
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            Intent intent = new Intent(getBaseContext(),FeedbackActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        }
    };
}