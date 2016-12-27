package com.android.example.sherlock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

public class ViewVideoTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_video_test);
        VideoView videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setVideoPath("https://www.youtube.com/watch?v=-bBHT158E0s");
    }
}
