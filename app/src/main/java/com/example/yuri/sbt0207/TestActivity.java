package com.example.yuri.sbt0207;

import android.os.Handler;
import android.support.annotation.NonNull;
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
import java.util.Date;

public class TestActivity extends AppCompatActivity {
    private TextView seekval6;
    private SeekBar moomin;
    private TextView Q6;
    String que1;
    String questionNum;
    int Value6;
    int answer1;
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

            // 일,월,년, 하루시간을 분으로 "ddmmyy-minutes"
            // ex) 2019년3월29일 오전7시3분 "290319-423"


            final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference databaseReference = firebaseDatabase.getReference();

            // START Analyze(전체 질문에 대한 분석값들)의 데이터 받기
            // 다시 수신할 때 ValueEventListener는 모든 값을 수신하였습니다.
            databaseReference.child("Analyze").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) { // 자식 데이터를 처리하는 공간

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { // Snapshot 이후에 or 처음부터 데이터가 없다면 호출되는 공간

                }
            });
            // END Analyze(전체 질문에 대한 분석값들)의 데이터 처리

            // reference https://stack07142.tistory.com/282

            // START Analyze(전체 질문에 대한 분석값들)의 데이터 받기
            // 다시 수신할 때 Single ValueEventListener는 더 이상 동작하지 않음을 알 수 있습니다.
            databaseReference.child("Analyze").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String target_id = null;
                    int target_answer = 0;

                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){ // 자식(데이터(질문에 대한))이 있다면 계속 반복 반환

                        Analyze analyze = snapshot.getValue(Analyze.class); // 만족하는 if값이 나올때까지 계속 반복

                        if(analyze.que_num == Integer.valueOf(questionNum)){ // 현재 클라이언트가 받은 질문에 대한 번호와 일치하는 analze가 있다면

                            target_id = snapshot.getKey(); // 파이어베이스의 클래스 키 값
                            target_answer = analyze.total_value; // 토탈 벨류는 일단 따로 저장해 놓는다

                            // START getTime
                            long now = System.currentTimeMillis();
                            Date date = new Date(now);
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm-dd/MM/yy");
                            getTime = sdf.format(date);
                            Log.d("현재 시각", getTime);
                            // END getTime

                            break;
                        }
                    }

                    // count answers
                    String data = dataSnapshot.child(target_id).child("count").getValue().toString();
                    int count = Integer.valueOf(data);
                    count++;

                    // START PUSH
                    EachValue eachValue = new EachValue();
                    eachValue.sentTime = getTime;
                    eachValue.value = Value6;
                    databaseReference.child("Analyze").child(target_id).child("total_value").setValue(Value6 + target_answer);
                    databaseReference.child("Analyze").child(target_id).child("count").setValue(count);
                    databaseReference.child("Analyze").child(target_id).child("EachValue").push().setValue(eachValue);
                    // END PUSH

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            // END Analyze(전체 질문에 대한 분석값들)의 데이터 받기

            Handler delayHandler = new Handler();
            delayHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), FeedbackActivity.class);
                    startActivity(intent);
                }
            }, 2000);
        }
    };

}
