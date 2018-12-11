package rj.layouts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btn_blue, btn_red;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        /**
         * Frame Layout
         */

        /*
        btn_blue = (Button)findViewById(R.id.btn_blue);
        btn_red = (Button)findViewById(R.id.btn_red);

        btn_blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_red.setVisibility(View.VISIBLE);
                btn_blue.setVisibility(View.INVISIBLE);
            }
        });

        btn_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_red.setVisibility(View.INVISIBLE);
                btn_blue.setVisibility(View.VISIBLE);
            }
        });
        //*/

        /**
         * End of Frame Layout
         */
    }
}
