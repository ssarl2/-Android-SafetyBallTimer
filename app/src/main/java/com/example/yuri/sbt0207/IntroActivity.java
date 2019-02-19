package com.example.yuri.sbt0207;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class IntroActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_activity); //xml , java 소스 연결
        Handler handler = new Handler();

        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        final List<String> q_parametersList = new ArrayList<>();

        final Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        Log.d("안됌?", "됌?");
        mDatabase.getReference().child("Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("파이어베이스 확인", "여전히 안됌?");
                q_parametersList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String data = snapshot.getValue(String.class);
                    q_parametersList.add(data);
                }
                Random rnd = new Random();
                int position[] = new int[5];
                for(int i=0; i<5; i++){
                    position[i] = rnd.nextInt(5)+1;
                    Log.d("Check data", q_parametersList.get(position[i]));
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
        },2000); //2초 뒤에 Runner객체 실행하도록 함
    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}


