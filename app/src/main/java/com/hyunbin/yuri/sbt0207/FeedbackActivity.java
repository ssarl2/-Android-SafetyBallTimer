package com.hyunbin.yuri.sbt0207;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackActivity extends AppCompatActivity {

    EditText editText;

    String feedback;

    Button sendBtn;
    Button nothing;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();


    @Override
    protected void onStop() {
        super.onStop();
        // In order to exit app exactly when you go to home or elsewhere
        ActivityCompat.finishAffinity(FeedbackActivity.this);
        System.runFinalizersOnExit(true);
        System.exit(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE); // it's command to be able to adjust when textview is hidden by keyboard layout   text뷰가 키보드 레이아웃에 의해 가려질 때 조절할 수 있는 명령어

        editText = (EditText)findViewById(R.id.editText);
        sendBtn = (Button)findViewById(R.id.sendBtn);
        nothing = (Button)findViewById(R.id.Nothing);


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback = editText.getText().toString();
                if(feedback.isEmpty()) { // If it is empty, just skip.
                    return;
                }
                databaseReference.child("Feedback").push().setValue(feedback);

                Intent intent = new Intent(getBaseContext(),LastActivity.class);

                startActivity(intent);

                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });
        
        nothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),LastActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });

    }
}
