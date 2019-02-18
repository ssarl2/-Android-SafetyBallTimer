package com.example.yuri.sbt0207;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FeedbackActivity extends AppCompatActivity {
    EditText edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        edit = (EditText)findViewById(R.id.editText);
        String feedbackval = edit.getText().toString();

        Button btn = (Button)findViewById(R.id.sendBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });

    }
}
