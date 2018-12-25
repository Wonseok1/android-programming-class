package rj.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageActivity extends AppCompatActivity {
    ImageView iv_show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        iv_show = (ImageView)findViewById(R.id.iv_show);

        Intent intent = getIntent();
        if(intent != null) {
            String img_name = intent.getStringExtra("img_name");
            if(img_name != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(img_name);
                iv_show.setImageBitmap(bitmap);
            } else {
                Toast.makeText(ImageActivity.this,
                        "이미지가 없습니다.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
