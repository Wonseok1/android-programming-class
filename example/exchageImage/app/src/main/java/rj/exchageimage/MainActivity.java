package rj.exchageimage;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    Button btn_up, btn_down;
    ImageView iv_smileUp, iv_smileDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_up = (Button)findViewById(R.id.btn_up);
        btn_down = (Button)findViewById(R.id.btn_down);
        iv_smileUp = (ImageView) findViewById(R.id.iv_smileUp);
        iv_smileDown = (ImageView) findViewById(R.id.iv_smileDown);


        btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_smileDown.setVisibility(View.VISIBLE);
                iv_smileUp.setVisibility(View.INVISIBLE);
            }
        });
        btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_smileDown.setVisibility(View.INVISIBLE);
                iv_smileUp.setVisibility(View.VISIBLE);
            }
        });
    }
}
