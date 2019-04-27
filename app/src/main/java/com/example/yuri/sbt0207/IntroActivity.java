package com.example.yuri.sbt0207;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.List;


public class IntroActivity extends Activity {
    private static final String TAG = "MyFirebaseMsgService";
    private final String BROADCAST_MESSAGE = "sbt.noQuestions"; // 브로드캐스트 주소

    private BroadcastReceiver mReceiver = null;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    public SharedPreferences prefs;
    long count = 0;
    long delayTime = 2000;
    private String questionNum;
    private String question;
    private Intent intent;
    private long limitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_activity);

        intent = getIntent();
        prefs = getSharedPreferences("isFirstRun", MODE_PRIVATE);
        Handler handler = new Handler();


        /**
         * if you want to change how to start app
         * you can use this way
         */
/*
        // START Get DataFromBackground
        // 앱 위젯을 통해 어플로 접근 했을 시 데이터 삽입 방법
        // 저장된 값(valid)을 백그라운드에서 불러오기 위해 같은 네임파일을 찾음.
        SharedPreferences sharedPreferences = getSharedPreferences("backgroundData", MODE_PRIVATE);
        limitTime = Long.parseLong(sharedPreferences.getString("limitTime", "0"));

        SharedPreferences.Editor editor = sharedPreferences.edit(); // 설문조사를 끝나고 다시 들어갈 수 있는 것을 방지하기 위해 제한시간 초기화
        editor.putString("limitTime",String.valueOf(limitTime));

        questionNum = sharedPreferences.getString("questionNum", "");
        question = sharedPreferences.getString("question", "");
        questionNum = "a@"+questionNum;
        // END Get DataFromBackground
*/

        // START FCM PUSH
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " value: " + value);
            }
        }
        // [END handle_data_extras]

        // Get token
        // [START retrieve_current_token]
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        boolean isFirstRun = prefs.getBoolean("isFirstRun", true); // transfer data only when app is executed first time 처음실행할때만 데이터 전달
                        if (isFirstRun) {
                            String token = task.getResult().getToken();
                            Log.d(TAG, "GETTOKEN : " + token);
                            databaseReference.child("gettoken").push().setValue(token); // push token value into firebase 토큰 값 파이어베이스에 푸시
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean("isFirstRun",false); // save putBoolean for false so that it can forbid to enter here   false로 저장함으로써 다시는 이 if 문에 들어오지 못하게 한다.
                            editor.commit(); // once push data, it never push again -> if you want to push data again, delete app and install it. when your token value is changed
                            // 한번 푸시 한 이후로 다시 푸시안함 -> 삭제 후 재 다운로드(토큰값 변경)시 다시 토큰값 푸시
                        }
                    }
                });

        // [END retrieve_current_token]

        // END FCM PUSH

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (intent.getStringExtra("limitTime") != null) { // put variable after check whether exist empty value at limitTime   limitTime에 빈 값이 있는지 없는지 체크하고 변수 삽입.
                    limitTime = Long.parseLong(intent.getStringExtra("limitTime"));
                    Log.e( "runnnnnn: ",String.valueOf(limitTime));
                }
                long now = System.currentTimeMillis();

                if (limitTime > now) { // if there is data, 데이터가 있으면,

                    /**
                     * if you want to change how to start app
                     * you can use this way
                     */
                    /*
                    if (questionNum.split("@")[0].equals("a")) { // 앱 위젯을 통해 들어왔을시 데이터 넣는 방법
                        questionNum = questionNum.split("@")[1];
                    } else { // 알림창을 통해 들어왔을시 데이터 넣는 방법
                    */
                        questionNum = intent.getStringExtra("questionNum");
                        question = intent.getStringExtra("question");
                    // }

                    intent = new Intent(getApplicationContext(), MainActivity.class);

                    intent.putExtra("questionNum", questionNum);
                    intent.putExtra("question", question);

                    startActivity(intent);
                } else {
                    intent = new Intent(getApplicationContext(), NoQuestionsActivity.class);
                    startActivity(intent);
                }
                finish();
            }

        }, delayTime + count); // start Runner object in 2 seconds  2초 뒤에 Runner객체 실행하도록 함


    }
    // END onCreate

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}


