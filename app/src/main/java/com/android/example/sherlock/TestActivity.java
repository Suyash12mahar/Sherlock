package com.android.example.sherlock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Bundle newBundle = getIntent().getExtras();
        TextView textView = (TextView) findViewById(R.id.sampleTextView);
        textView.setText(newBundle.getString("episode_title"));
    }
}
