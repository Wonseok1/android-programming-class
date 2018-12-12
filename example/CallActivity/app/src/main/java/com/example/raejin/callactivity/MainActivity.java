package com.example.raejin.callactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final int REQUERT_CONE_SECOND = 101;
    Button btn_1, btn_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_1 = (Button)findViewById(R.id.btn_1);
        btn_2 = (Button)findViewById(R.id.btn_2);

        MyButtonListener myButtonListener = new MyButtonListener();

        btn_1.setOnClickListener(myButtonListener);
        btn_2.setOnClickListener(myButtonListener);
    }

    class MyButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent intent;

            switch(view.getId()) {
                case R.id.btn_1:
                    intent = new Intent(MainActivity.this, FirstButtonActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_2:
                    intent = new Intent(MainActivity.this, SecondButtonActivity.class);
                    startActivityForResult(intent, REQUERT_CONE_SECOND) ;
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUERT_CONE_SECOND) {
            if(resultCode == RESULT_OK) {
                String msg = data.getStringExtra("msg");
                Toast.makeText(MainActivity.this,
                        "두번째 엑티비티에서 전달된 메시지 : " + msg,
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
