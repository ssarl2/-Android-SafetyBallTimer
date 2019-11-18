package com.hyunbin.yuri.sbt0207;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NoQuestionsActivity extends AppCompatActivity implements Runnable{

    final String BROADCAST_MESSAGE = "sbt.noQuestions"; // address of broadcast 브로드캐스트 주소

    BroadcastReceiver mReceiver = null;

    long limitTime;

    String question;
    String token;

    @Override
    protected void onStop() {
        super.onStop();
        // In order to exit app exactly when you go to home or elsewhere
        ActivityCompat.finishAffinity(NoQuestionsActivity.this);
    }

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

        SharedPreferences sharedPreferences = getSharedPreferences("NoQuestionsActivityIsAlive", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("CHECK",false);

        //최종 커밋
        editor.commit();



        Intent intent = getIntent();

        token = intent.getStringExtra("token");



        TextView tokenView = (TextView)findViewById(R.id.token);

        tokenView.setText(token);
    }







    NoQuestionsActivity that = this;
    /** register broadcast automatically in code **/
    /** 동적으로(코드상으로) 브로드 캐스트를 등록한다. **/
    private void registerReceiver(){
        /** 1. make intent filter
         *  2. add action into intent filter
         *  3. implement BroadCastReceiver anonymous class
         *  4. register intent filter and BroadCastReceiver
         * */
        /** 1. intent filter를 만든다
         *  2. intent filter에 action을 추가한다.
         *  3. BroadCastReceiver를 익명클래스로 구현한다.
         *  4. intent filter와 BroadCastReceiver를 등록한다.
         * */
        if(mReceiver != null) return;

        final IntentFilter theFilter = new IntentFilter(BROADCAST_MESSAGE);
//        theFilter.addAction(BROADCAST_MESSAGE);

        this.mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) { // I think it receives broadcast whether correct address to sender or not 송신자가 보낸 주소와 일치하던 안하던 브로드캐스트는 받는 듯

                if(intent.getAction().equals(BROADCAST_MESSAGE)){ // if it is correct address of this class with address to sender, start this method 송신자가 보낸 주소와 이 클래스의 주소가 일치하면 함수 실행
                    limitTime = Long.parseLong(intent.getStringExtra("limitTime"));
                    question = intent.getStringExtra("question");
                    (new Thread(that)).start(); // declaring that. objects are above of registerReceiver. that is same with NoQuestionsActivity.this
                    // that선언 registerReceiver 위에 객체 있음. that은 NoQuestionsActivity.this 와 같음.
                }
            }
        };

        this.registerReceiver(this.mReceiver, theFilter);

    } // End RegisterReceiver








    /** end broadcast automatically in code **/
    /** 동적으로(코드상으로) 브로드 캐스트를 종료한다. **/
    private void unregisterReceiver() {
        if(mReceiver != null){
            this.unregisterReceiver(mReceiver);
            mReceiver = null;
        }

    } // End UnregisterReceiver







    @Override
    public void run() {
        Intent intent;

        long now = System.currentTimeMillis();

        if (limitTime > now) { // if there is data, 데이터가 있으면

            intent = new Intent(getApplicationContext(), MainActivity.class);

            intent.putExtra("question",question);

            startActivity(intent);
        } else { // there is nothing 아무일도 없음

        }
        finish();
    }




}
