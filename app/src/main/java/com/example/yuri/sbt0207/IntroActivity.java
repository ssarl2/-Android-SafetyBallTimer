package com.example.yuri.sbt0207;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class IntroActivity extends Activity {
    private static final String TAG = "MyFirebaseMsgService";
    long count = 0;
    long delayTime = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_activity); //xml , java 소스 연결
        Handler handler = new Handler();


        // START FCM PUSH
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
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
                Log.d(TAG, "Key: " + key + " Value: " + value);
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
                        String token = task.getResult().getToken();
                        Log.d(TAG, token);

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        // [END retrieve_current_token]

        // END FCM PUSH

        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        final List<String> q_parametersList = new ArrayList<>();

        final Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        mDatabase.getReference().child("Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                q_parametersList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String data = snapshot.getValue(String.class);
                    count = count + 1000;
                    q_parametersList.add(data);
                }
                Random rnd = new Random();
                int position[] = new int[5];
                for(int i=0; i<5; i++){
                    position[i] = rnd.nextInt(5)+1;
                    for(int j=0; j<i; j++){
                        if(position[i] == position[j]){
                            i--;
                        }
                    }
                }
                for(int k=0; k<5; k++){
                    Log.d("Check data", q_parametersList.get(position[k]));
                    Log.d("카운트", String.valueOf(count));
                }
                intent.putExtra("que1", q_parametersList.get(position[0]));
                intent.putExtra("que2", q_parametersList.get(position[1]));
                intent.putExtra("que3", q_parametersList.get(position[2]));
                intent.putExtra("que4", q_parametersList.get(position[3]));
                intent.putExtra("que5", q_parametersList.get(position[4]));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    startActivity(intent); //다음화면으로 넘어감
                    finish();
                }

            },delayTime+count); //2초 뒤에 Runner객체 실행하도록 함



    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}


