package com.example.yuri.sbt0207;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.FirebaseDatabase;

public class FeedbackActivity extends AppCompatActivity {
    EditText edit;
    String feedbackval;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE); // text뷰가 키보드 레이아웃에 의해 가려질 때 조절할 수 있는 명령어

        edit = (EditText)findViewById(R.id.editText);
        Button sendbtn = (Button)findViewById(R.id.sendBtn);
        Button Noting = (Button)findViewById(R.id.Notting);

        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),LastActivity.class);
                feedbackval = edit.getText().toString();
                mDatabase.getReference().child("Feedbacks").push().setValue(feedbackval);

                startActivity(intent);

                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });
        Noting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),LastActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });

    }
}
