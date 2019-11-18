package com.hyunbin.yuri.sbt0207.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.hyunbin.yuri.sbt0207.IntroActivity;
import com.hyunbin.yuri.sbt0207.MainActivity;
import com.hyunbin.yuri.sbt0207.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    private Intent intent;

    private SharedPreferences sharedPreferences;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        long validTime;
        long limitTime;
//        long now;

        String question;

        validTime = Long.parseLong(remoteMessage.getData().get("validTime")); // get long type data  long 타입의 변수로 데이터를 받음
        limitTime = System.currentTimeMillis() + (validTime * 1000); // set variable of limit time that is added validTime and current time 현재 시간에 validTime초를 더해준 제한시간 변수를 설정.
//        now = (int)limitTime/(1000*60)+(int)limitTime/(1000*60*60*60);//파이어베이스에서 받고 서버에서 나타낼때 now/60
        intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.putExtra("now",now);

        question = remoteMessage.getData().get("question"); // data of questions 질문 데이터


        SavingData(limitTime, question);



        // START BroadCast
        /** make broadcast **/
        /** 1. create intent that is stored message to send
         * 2. put key, value to be able to make sure whether it may receive data well or not
         * 3. put intent to send to using 'sendBroadcast(intent);' method and broadcast */
        /** 브로드 캐스트를 발생시킨다. **/
        /** 1. 전달할 메세지를 담은 인텐트 생성
         * 2. DATA를 잘 전달받는지 확인할 수 있게 Key, value 넣기
         * 3. sendBroadcast(intent); 메서드를 이용해서 전달할 intent를 넣고, 브로드캐스트한다. */
        intent = new Intent("sbt.noQuestions");
        intent.putExtra("limitTime", String.valueOf(limitTime));
        intent.putExtra("question", question);
        sendBroadcast(intent);
        // END BroadCast



        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ false) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //scheduleJob();

            } else {
                // Handle message within 10 seconds
                // handleNow(limitTime, questionNum, question); // It's disposed
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        // This is for dividing between when the Background Notification is appearing
        // and this app is being executing
        if(sharedPreferences.getBoolean("CHECK",true)) {
            sendNotification(validTime, limitTime, question); // 알림탭에 관한 알림함수 실행
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("CHECK",true);
        editor.commit();
    }
    // [END receive_message]



    private void sendNotification(long validTime, long limitTime, String question) {
        intent = new Intent(this, IntroActivity.class); // 알림탭 눌렀을 시 데이터를 받아서 이 클래스로 이동

        intent.putExtra("limitTime",String.valueOf(limitTime));
        intent.putExtra("question",question);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(android.R.drawable.btn_star)
                        .setContentTitle("There is a question")
                        .setContentText(validTime + "second (limit time)")
                        .setAutoCancel(true)
                        //.setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setTimeoutAfter(validTime * 1000); // remove alert after validTime   validTime 지난 후 알림제거

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        } else {

        }
        MediaPlayer player = MediaPlayer.create(this,R.raw.alram);
        player.start();
        //Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        //vib.vibrate(1500);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }




    // [START on_new_token]

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }
    // [END on_new_token]




    // [START dispatch_job]
    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private void scheduleJob() {
        /*FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("my-job-tag")
                .build();
        dispatcher.schedule(myJob);*/
    }
    // [END dispatch_job]





    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private boolean handleNow(long limitTime, String question) {

        intent = new Intent(this, IntroActivity.class); // move to this class when touch the noticebar   알림탭 눌렀을 시 데이터를 받아서 이 클래스로 이동

        intent.putExtra("limitTime",String.valueOf(limitTime));
        intent.putExtra("question",question);
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

        return false;
    }




    // START save data to backGround
    void SavingData(long limitTime, String question){
        SharedPreferences sharedPreferences = getSharedPreferences("backgroundData",MODE_PRIVATE); //SharedPreferences를 기본모드로 설정
        SharedPreferences.Editor editor = sharedPreferences.edit(); //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.

        editor.putString("limitTime",String.valueOf(limitTime));
        editor.putString("question",question);

        editor.commit();
    }
    // END save data to backGround





    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // This is for dividing between when the Background Notification is appearing
        // and this app is being executing
        sharedPreferences = getSharedPreferences("NoQuestionsActivityIsAlive", MODE_PRIVATE);
        Log.d(getClass().getSimpleName(), "OnCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(getClass().getSimpleName(), "OnDestroy");
    }

}
