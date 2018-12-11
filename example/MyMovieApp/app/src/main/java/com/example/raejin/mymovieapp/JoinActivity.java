package com.example.raejin.mymovieapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Raejin on 2018-03-12.
 */

public class JoinActivity extends AppCompatActivity {

    TextView textView_pw_info;
    EditText editText_id, editText_pw, editText_re_pw,
            editText_name, editText_phone, editText_year;
    CheckBox checkBox_man, checkBox_woman;
    Spinner spinner_month, spinner_day;
    Button button_join;
    ArrayAdapter monthAdapter, dayAdapter;

    Boolean isIdChecked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        // 2. 컴포넌트의 객체 만들기
        setComponent();
        // 스피너 관련 처리 함수
        setAdapters();

        // 4 리스너의 객체를 만들기
        IdCheckListener idCheckListener = new IdCheckListener();

        //5. 리스너 객체를 컴포넌트에 등록하기
        editText_id.setOnFocusChangeListener(idCheckListener);
        editText_re_pw.setOnFocusChangeListener(new PwCheckListener());
        button_join.setOnClickListener(new JoinBtnListener());
    }

    //3 리스너의 클래스 만들기
    class JoinBtnListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if(isIdChecked) {
                String temp_id = editText_id.getText().toString();
                //output stream을 생성
                FileOutputStream fos = null;
                try {
                    fos = openFileOutput("id.bin", Context.MODE_PRIVATE);
                    // 비어있는 파일에 아이디를 저장
                    fos.write(temp_id.getBytes());

                    //output stream 제거
                    fos.close();
                } catch (Exception e) {}
            } else {
                Toast.makeText(JoinActivity.this,
                        "정상적인 아이디를 입력해주세요.",
                        Toast.LENGTH_LONG).show();
            }

            if(!editText_name.getText().toString().equals("") &&
                    !editText_phone.getText().toString().equals("")){
                SharedPreferences pref = getSharedPreferences("user_info", 0);
                SharedPreferences.Editor editor = pref.edit();

                editor.putString("user_name", editText_name.getText().toString());
                editor.putString("user_phone", editText_phone.getText().toString());

                editor.commit();

                SharedPreferences pref_read = getSharedPreferences("user_info", 0);
                String user_name = pref_read.getString("user_name", "error");
                String user_phone = pref_read.getString("user_phone", "error");

                Toast.makeText(JoinActivity.this,
                        user_name+"/"+user_phone,
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(JoinActivity.this,
                        "이름, 전화번호를 확인하세요.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
    class IdCheckListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View view, boolean b) {
            String user_input_id = editText_id.getText().toString();

            int num_of_id = user_input_id.length();

            if(num_of_id >= 5) {
                if(num_of_id <= 12) {
                    textView_pw_info.setText("정상적인 아이디 입니다.");
                    isIdChecked = true;
                } else {
                    textView_pw_info.setText("비 정상적인 아이디 입니다.(아이디의 글자수가 12보다 작아야합니다.)");
                    isIdChecked = false;
                }
            } else {
                textView_pw_info.setText("비 정상적인 아이디 입니다.(아이디의 글자 수가 5보다 커야합니다.)");
                isIdChecked = false;
            }
        }
    }

    class PwCheckListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View view, boolean b) {
            String user_pw = editText_pw.getText().toString();
            String user_pw_re = editText_re_pw.getText().toString();

            if(user_pw.equals(user_pw_re)) {
                textView_pw_info.setText("정상적인 비밀번호 입니다.");
            } else {
                textView_pw_info.setText("비밀번호가 일치하지 않습니다.");
            }
        }
    }


    private void setComponent() {
        textView_pw_info = (TextView)findViewById(R.id.textView_pw_info);
        editText_id = (EditText)findViewById(R.id.editText_id);
        editText_pw = (EditText)findViewById(R.id.editText_pw);
        editText_re_pw = (EditText)findViewById(R.id.editText_re_pw);
        editText_name = (EditText)findViewById(R.id.editText_name);
        editText_phone = (EditText)findViewById(R.id.editText_phone);
        checkBox_man = (CheckBox)findViewById(R.id.checkBox_man);
        checkBox_woman = (CheckBox)findViewById(R.id.checkBox_woman);
        editText_year = (EditText)findViewById(R.id.editText_year);
        spinner_month = (Spinner)findViewById(R.id.spinner_month);
        spinner_day = (Spinner)findViewById(R.id.spinner_day);
        button_join = (Button)findViewById(R.id.button_join);


        setAdapters();
    }
    private void setAdapters() {
        monthAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.month_info,
                android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_month.setAdapter(monthAdapter);
        spinner_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dayAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.day_info,
                android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_day.setAdapter(dayAdapter);
        spinner_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
