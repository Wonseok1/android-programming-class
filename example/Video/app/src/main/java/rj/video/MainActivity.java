package rj.video;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements TextureView.SurfaceTextureListener {

    MediaRecorder mediaRecorder;
    Camera camera;
    boolean bPerm;
    boolean isRecording = false;
    TextureView textureV_video;
    Button btn_shot;
    int result;
    SurfaceTexture surface;

    List<Camera.Size> supportedPreviewSizes;
    //최종 결정된 preview size
    Camera.Size previewSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textureV_video = (TextureView) findViewById(R.id.textureV_video);
        textureV_video.setSurfaceTextureListener(this);

        btn_shot = (Button)findViewById(R.id.btn_shot);
        btn_shot.setOnClickListener(new ShotBtnListener());


        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch(Exception e) {
            e.printStackTrace();
        }

        setPermission();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        //카메라 점유
        camera = Camera.open();

        Camera.Parameters parameters = camera.getParameters();
        supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        if (supportedPreviewSizes != null) {
            previewSize = CameraUtil.getOptimalPreviewSize(supportedPreviewSizes, i, i1);
            parameters.setPreviewSize(previewSize.width, previewSize.height);
        }

        int result=CameraUtil.setCameraDisplayOrientation(this, 0);
        this.result=result;
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        //사진 촬영시 방향이 안맞는 데이터 전달
        parameters.setRotation(result);

        camera.setParameters(parameters);
        //화면에 출력되는 형상의 방향
        camera.setDisplayOrientation(result);

        try {
            camera.setPreviewTexture(surfaceTexture);
        } catch (IOException t) {
        }

        camera.startPreview();

        this.surface=surfaceTexture;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        camera.stopPreview();
        camera.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    class ShotBtnListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if(camera != null) {
                if(isRecording) {
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    mediaRecorder = null;
                    isRecording = false;
                    btn_shot.setText("Recoding");
                } else {
                    try{
                        File dir = new File(Environment.getExternalStorageDirectory()
                                .getAbsolutePath()+"/myApp");
                        if(!dir.exists()) {
                            dir.mkdir();
                        }

                        File file = File.createTempFile("VIDEO-", ".3gp", dir);

                        mediaRecorder = new MediaRecorder();

                        camera.unlock();
                        mediaRecorder.setCamera(camera);
                        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

                        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
                        mediaRecorder.setOutputFile(file.getAbsolutePath());

                        mediaRecorder.setOrientationHint(result);
                        mediaRecorder.prepare();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mediaRecorder.start();
                    isRecording = true;
                    btn_shot.setText("Stop");
                }
            }
        }
    }
    private void setPermission() {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) ==
                PackageManager.PERMISSION_GRANTED
                ) {
            bPerm = true;
        }


        if(!bPerm) {
            ActivityCompat.requestPermissions(this,
                    new String[] {
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO
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
