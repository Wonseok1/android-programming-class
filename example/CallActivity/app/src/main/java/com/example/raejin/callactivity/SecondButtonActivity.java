package com.example.raejin.callactivity;

import android.content.Intent;
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

public class SecondButtonActivity extends AppCompatActivity {
    VideoView videoView_second;
    Button btn_back_2nd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_button);

        setVideo();
        btn_back_2nd = (Button)findViewById(R.id.btn_back_2nd);
        btn_back_2nd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = new Intent();
        intent.putExtra("msg" , "Hello MainActivity");
        setResult(RESULT_OK, intent);

    }

    private void setVideo() {
        videoView_second = (VideoView) findViewById(R.id.videoview_second_button);
        String uriPath = "android.resource://" + getPackageName() + "/" + R.raw.countdown;
        Uri uri = Uri.parse(uriPath);

        videoView_second.setVideoURI(uri);

        final MediaController mediaController =
                new MediaController(this);
        videoView_second.setMediaController(mediaController);

    }
}
