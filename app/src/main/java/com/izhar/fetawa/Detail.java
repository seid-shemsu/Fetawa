package com.izhar.fetawa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Detail extends AppCompatActivity {
    TextView q, a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        String question = getIntent().getExtras().getString("question");
        String answer = getIntent().getExtras().getString("answer");
        q = findViewById(R.id.question);
        a = findViewById(R.id.answer);
        q.setText(question);
        a.setText(answer);
    }
}
