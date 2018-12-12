package com.example.raejin.callactivity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by Raejin on 2018-02-27.
 */

public class FirstButtonActivity extends AppCompatActivity {
    VideoView videoView_first;
    Button btn_back_1st;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_button);

        btn_back_1st = (Button)findViewById(R.id.btn_back_1st);
        btn_back_1st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setVideo();
    }

    private void setVideo() {
        videoView_first = (VideoView) findViewById(R.id.videoview_first_button);
        String uriPath = "android.resource://" + getPackageName() + "/" + R.raw.piggy;
        Uri uri = Uri.parse(uriPath);

        videoView_first.setVideoURI(uri);

        final MediaController mediaController =
                new MediaController(this);
        videoView_first.setMediaController(mediaController);
    }
}
