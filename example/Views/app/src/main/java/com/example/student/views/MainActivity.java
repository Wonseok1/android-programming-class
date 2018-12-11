package com.example.student.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // 텍스트 뷰의 객체를 만든다.
    TextView tv_myName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 레이아웃에 뷰 중에서 tv_myName을 객체로 만든다.
        tv_myName = (TextView)findViewById(R.id.tv_myName);

        String message = tv_myName.getText().toString();
        //tv_myName.setText("안드로이드는 힘들어~~~");

        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
