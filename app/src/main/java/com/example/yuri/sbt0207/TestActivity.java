package com.example.yuri.sbt0207;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;

import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TestActivity extends AppCompatActivity {
    private TextView seekval6;
    private SeekBar moomin;
    private TextView Q6;
    String que1;
    String questionNum;
    int Value6;
    int answer1;
    String target_id;
    int target_answer;
    String getTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekval6= (TextView)findViewById(R.id.seekText1);
        moomin = (SeekBar)findViewById(R.id.seekBarMU);
        moomin.setOnSeekBarChangeListener(seekBarChangeListener); // 받아들인 값을 moomin 시크바에 적용시킴
        Q6 = (TextView)findViewById(R.id.Q1);
        Intent intent = getIntent();

        questionNum = intent.getStringExtra("questionNum");
        que1 = intent.getStringExtra("question");

        Q6.setText(que1);
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
            seekval6.setText(""+progress);
            Value6=progress;

            // view가 시크바의 thumb 따라다니게 만드는 함수
            int padding= moomin.getPaddingLeft() + moomin.getPaddingRight();
            int sPos = moomin.getLeft() + moomin.getPaddingLeft();
            int xPos = (moomin.getWidth()-padding) * moomin.getProgress() / moomin.getMax() + sPos - (seekval6.getWidth()/2);
            seekval6.setX(xPos);
            answer1 = progress;
        }

        // 프로그레스바를 눌릴 때 작동하는 함수
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        // 프로그레스바를 떼고 작동하는 함수
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            seekBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });

            Calendar cal = new GregorianCalendar();

            // 일,월,년, 하루시간을 분으로 "ddmmyy-minutes"
            // ex) 2019년3월29일 오전7시3분 "290319-423"
            String nowTime = String.valueOf(((cal.get(Calendar.HOUR_OF_DAY)*60)+cal.get(Calendar.MINUTE))); // 이곳에 보낸시간을 스트링값으로 넣으면 됩니다.

            final Answer answer_about_question = new Answer(); // Answer 클래스 초기화
            answer_about_question.questionNum = Integer.parseInt(questionNum); // 클래스에 데이터를 담아서
            answer_about_question.value = answer1;
            answer_about_question.sentTime = Integer.parseInt(nowTime);
            Log.d("태그",nowTime);
            //Intent intentt = getIntent();
            //int now = intentt.getExtras().getInt("now::"); /*int형*/

            final Intent intent = new Intent(getApplicationContext(), FeedbackActivity.class);

            final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference databaseReference = firebaseDatabase.getReference();
            databaseReference.child("Answers").push().setValue(answer_about_question);// 데이터를 담은 클래스 자체를 서버로 푸시

            databaseReference.child("Analyze").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Analyze analyze = snapshot.getValue(Analyze.class);
                        target_answer = analyze.total_value;
                        String id = snapshot.getKey();
                        if(analyze.que_num == answer_about_question.questionNum){
                            target_id = id;
                            long now = System.currentTimeMillis();
                            Date date = new Date(now);
                            SimpleDateFormat sdf = new SimpleDateFormat("HH");
                            getTime = sdf.format(date);
                            Log.d("현재 시각", getTime);
                        }
                    }

                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            databaseReference.child("Analyze").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("타켓 아이디", target_id);
                    if(target_id != null){
                        String data = dataSnapshot.child(target_id).child("count").getValue().toString();
                        int count = Integer.valueOf(data);
                        count++;

                        EachValue eachValue = new EachValue();
                        eachValue.senttime = getTime;
                        eachValue.value = answer_about_question.value;
                        databaseReference.child("Analyze").child(target_id).child("total_value").setValue(answer_about_question.value + target_answer);
                        databaseReference.child("Analyze").child(target_id).child("count").setValue(count);
                        databaseReference.child("Analyze").child(target_id).child("EachValue").push().setValue(eachValue);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            /*Handler delayHandler = new Handler();
            delayHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), FeedbackActivity.class);
                    startActivity(intent);
                }
            }, 2000);*/
        }
    };

}
