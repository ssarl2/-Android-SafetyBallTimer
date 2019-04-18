package com.example.yuri.sbt0207;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NoQuestionsActivity extends AppCompatActivity implements Runnable{

    private final String BROADCAST_MESSAGE = "sbt.noQuestions"; // 브로드캐스트 주소
    private BroadcastReceiver mReceiver = null;
    private long limitTime;
    private String questionNum;
    private String question;

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_questions);
    }

    NoQuestionsActivity that = this;
    /** 동적으로(코드상으로) 브로드 캐스트를 등록한다. **/
    private void registerReceiver(){
        /** 1. intent filter를 만든다
         *  2. intent filter에 action을 추가한다.
         *  3. BroadCastReceiver를 익명클래스로 구현한다.
         *  4. intent filter와 BroadCastReceiver를 등록한다.
         * */
        if(mReceiver != null) return;

        final IntentFilter theFilter = new IntentFilter();
        theFilter.addAction(BROADCAST_MESSAGE);

        this.mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) { // 송신자가 보낸 주소와 일치하던 안하던 브로드캐스트는 받는 듯

                if(intent.getAction().equals(BROADCAST_MESSAGE)){ // 송신자가 보낸 주소와 이 클래스의 주소가 일치하면 함수 실행
                    limitTime = Long.parseLong(intent.getStringExtra("limitTime"));
                    questionNum = intent.getStringExtra("questionNum");
                    question = intent.getStringExtra("question");
                    (new Thread(that)).start(); // that선언 registerReceiver 위에 객체 있음. that은 NoQuestionsActivity.this 와 같음.
                }
            }
        };

        this.registerReceiver(this.mReceiver, theFilter);

    }

    /** 동적으로(코드상으로) 브로드 캐스트를 종료한다. **/
    private void unregisterReceiver() {
        if(mReceiver != null){
            this.unregisterReceiver(mReceiver);
            mReceiver = null;
        }

    }

    @Override
    public void run() {
        Intent intent;

        long now = System.currentTimeMillis();

        if (limitTime > now) { // 데이터가 있으면

            intent = new Intent(getApplicationContext(), MainActivity.class);

            intent.putExtra("questionNum",questionNum);
            intent.putExtra("question",question);

            startActivity(intent);
        } else { // 아무일도 없음

        }
        finish();
    }

}
