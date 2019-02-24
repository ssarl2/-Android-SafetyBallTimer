package com.example.yuri.sbt0207;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;

import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView seekval1;
    private SeekBar moomin;
    private TextView Q1;
    String que1, que2, que3, que4, que5;
    int Value1;
    int answer1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekval1= (TextView)findViewById(R.id.seekText1);
        moomin = (SeekBar)findViewById(R.id.seekBarMU);
        moomin.setOnSeekBarChangeListener(seekBarChangeListener); // 받아들인 값을 moomin 시크바에 적용시킴
        Q1 = (TextView)findViewById(R.id.Q1);
        Intent intent = getIntent();

        que1 = intent.getExtras().getString("que1");
        que2 = intent.getExtras().getString("que2");
        que3 = intent.getExtras().getString("que3");
        que4 = intent.getExtras().getString("que4");
        que5 = intent.getExtras().getString("que5");

        Q1.setText(que1);
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
            seekval1.setText(""+progress);
            Value1=progress;

            // view가 시크바의 thumb 따라다니게 만드는 함수
            int padding= moomin.getPaddingLeft() + moomin.getPaddingRight();
            int sPos = moomin.getLeft() + moomin.getPaddingLeft();
            int xPos = (moomin.getWidth()-padding) * moomin.getProgress() / moomin.getMax() + sPos - (seekval1.getWidth()/2);
            seekval1.setX(xPos);
            answer1 = progress;
        }

        // 프로그레스바를 눌릴 때 작동하는 함수
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        // 프로그레스바를 떼고 작동하는 함수
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            Intent next_intent = new Intent(getBaseContext(), TwoActivity.class);
            next_intent.putExtra("que2", que2);
            next_intent.putExtra("que3", que3);
            next_intent.putExtra("que4", que4);
            next_intent.putExtra("que5", que5);
            next_intent.putExtra("ans1", answer1);
            startActivity(next_intent);
            overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        }
    };

}
