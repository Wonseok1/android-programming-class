package rj.videoplay;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FilenameFilter;

public class MainActivity extends AppCompatActivity {
    VideoView vv_show;
    MediaController mediaController;
    boolean bPerm = false;
    Button btn_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vv_show = (VideoView)findViewById(R.id.vv_show);
        mediaController = new MediaController(this);
        vv_show.setMediaController(mediaController);

        setPermission();

        btn_start = (Button)findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bPerm) {
                    String path = Environment.getExternalStorageDirectory().getAbsoluteFile()+"/myApp";
                    File dir = new File(path);

                    File[] list = dir.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File file, String s) {
                            Log.d("test", s);
                            return s.endsWith(".3gp");
                        }
                    });

                    if(list != null) {
                        vv_show.setVideoPath(list[0].getPath());
                    } else {
                        Toast.makeText(MainActivity.this, "동영상 파일이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });



    }
    private void setPermission() {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            bPerm = true;
        }


        if(!bPerm) {
            ActivityCompat.requestPermissions(this,
                    new String[] {
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    }, 200);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 200 && grantResults.length > 0) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                bPerm = true;
            }
        }
    }
}
