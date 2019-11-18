package com.hyunbin.yuri.sbt0207;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashSet;


public class IntroActivity extends Activity {
    private static final String TAG = "MyFirebaseMsgService";
    private final String BROADCAST_MESSAGE = "sbt.noQuestions"; // 브로드캐스트 주소

    BroadcastReceiver mReceiver = null;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    long count = 0;
    long delayTime = 2000;
    
    String question;
    String token;

    long limitTime = 0;

    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        intent = getIntent();
        

        Handler handler = new Handler();


        
        
        // START FCM PUSH
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
            notificationManager.cancelAll(); // Remove the notification as you start this app
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
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken();

                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg); // log value of the token 토큰 값 출력

                        // If there is no token of this device in the firebase, add token to firebase
                        databaseReference.child("getToken").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                HashSet<String> set = new HashSet<>();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // execute for syntax in order to get whole data 데이터 전체를 받기위해 반복문 실행

                                    String key = snapshot.getValue().toString();
                                    set.add(key);
                                }
                                if(!set.contains(token)){
                                    databaseReference.child("getToken").push().setValue(token); // push token value into firebase 토큰 값 파이어베이스에 푸시
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });

        // [END retrieve_current_token]

        // END FCM PUSH

        
        
        
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TossData();

                long now = System.currentTimeMillis();
                if (limitTime > now) { // if there is data, 데이터가 있으면,

                    intent = new Intent(getApplicationContext(), MainActivity.class);

                    intent.putExtra("question", question);

                    startActivity(intent);
                } else {
                    intent = new Intent(getApplicationContext(), NoQuestionsActivity.class);

                    intent.putExtra("token", token);

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

    // START Get DataFromBackground
    // 앱 위젯을 통해 어플로 접근 했을 시 데이터 삽입 방법
    // 저장된 값(valid)을 백그라운드에서 불러오기 위해 같은 네임파일을 찾음.
    void TossData(){
        SharedPreferences sharedPreferences = getSharedPreferences("backgroundData", MODE_PRIVATE);

        limitTime = Long.parseLong(sharedPreferences.getString("limitTime", "0"));

        SharedPreferences.Editor editor = sharedPreferences.edit(); // 설문조사를 끝나고 다시 들어갈 수 있는 것을 방지하기 위해 제한시간 초기화
        editor.putString("limitTime",String.valueOf(limitTime));

        question = sharedPreferences.getString("question", "");
    }
    // END Get DataFromBackground
}


