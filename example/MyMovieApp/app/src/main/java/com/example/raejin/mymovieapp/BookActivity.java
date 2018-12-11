package com.example.raejin.mymovieapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by Raejin on 2018-03-05.
 */

public class BookActivity extends AppCompatActivity {

    Button btn_time_picker, btn_date_picker, btn_reset_seats, btn_book_cancel, btn_book_confirm;
    DatePickerListener datePickerListener;
    TimePickerListener timePickerListener;
    // 시크바 추가 컴포넌트 변수
    SeekBar seekbar_seats;
    Button btn_seats;
    TextView textview_book_date, textview_book_time, textview_seats;
    TextView textview_info_title, textview_info_director, textview_info_actor, textview_info_type;
    SeekBar seekbar_searts;
    public static int booked_seats = 1;
    String[] movie_title, movie_director, movie_actor, movie_type;
    int movie_index;
    final static int ERROR = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        Intent intent = getIntent();        // 전달받은 인텐트를 수신
        movie_index = intent.getIntExtra("movie_index", -1);    // 인텐트에서 전달된 데이터 읽음

        if(movie_index != ERROR) {
            // 2. 컴포넌트(버튼)의 객체 만들기
            btn_date_picker = (Button)findViewById(R.id.btn_date_picker);
            btn_time_picker = (Button)findViewById(R.id.btn_time_picker);
            btn_reset_seats = (Button)findViewById(R.id.btn_reset_seats);
            btn_book_cancel = (Button)findViewById(R.id.btn_book_cancel);
            btn_book_confirm = (Button)findViewById(R.id.btn_book_confirm);
            textview_book_date = (TextView)findViewById(R.id.textview_book_date);
            textview_book_time = (TextView)findViewById(R.id.textview_book_time);
            textview_seats = (TextView)findViewById(R.id.textview_seats);
            textview_info_title = (TextView)findViewById(R.id.textview_info_title);
            textview_info_director = (TextView)findViewById(R.id.textview_info_director);
            textview_info_actor = (TextView)findViewById(R.id.textview_info_actor);
            textview_info_type = (TextView)findViewById(R.id.textview_info_type);
            seekbar_searts = (SeekBar)findViewById(R.id.seekbar_seats);

            // 4. 리스너 객체 만들기
            PickerBtnListener pickerBtnListener = new PickerBtnListener();
            datePickerListener = new DatePickerListener();
            timePickerListener = new TimePickerListener();
            NormalBtnListener normalBtnListener = new NormalBtnListener();
            SeekBarListener seekBarListener = new SeekBarListener();


            // 5. 리스너 객체를 컴포넌트에 등록하기
            btn_date_picker.setOnClickListener(pickerBtnListener);
            btn_time_picker.setOnClickListener(pickerBtnListener);
            btn_reset_seats.setOnClickListener(normalBtnListener);
            btn_book_cancel.setOnClickListener(normalBtnListener);
            btn_book_confirm.setOnClickListener(normalBtnListener);
            seekbar_searts.setOnSeekBarChangeListener(seekBarListener);

            // 파일에 저장된 데이터를 읽어옴
            movie_title = getResources().getStringArray(R.array.movie_title);
            movie_director = getResources().getStringArray(R.array.movie_director);
            movie_actor = getResources().getStringArray(R.array.movie_actor);
            movie_type = getResources().getStringArray(R.array.movie_type);

            textview_info_title.setText(movie_title[movie_index]);
            textview_info_director.setText(movie_director[movie_index]);
            textview_info_actor.setText(movie_actor[movie_index]);
            textview_info_type.setText(movie_type[movie_index]);
        } else {
            Toast.makeText(BookActivity.this,
                    "동작중에 오류가 발생하였습니다.",
                    Toast.LENGTH_LONG).show();
            finish();
        }
        textview_seats = (TextView)findViewById(R.id.textview_seats);
        seekbar_seats = (SeekBar)findViewById(R.id.seekbar_seats);
        btn_seats = (Button)findViewById(R.id.btn_reset_seats);

        // 4. 리스너 객체 만들기
        PickerBtnListener pickerBtnListener = new PickerBtnListener();
        datePickerListener = new DatePickerListener();
        timePickerListener = new TimePickerListener();
        SeekBarListener seekBarListener = new SeekBarListener();
        SeekBtnListener seekBtnListener = new SeekBtnListener();

        // 5. 리스너 객체를 컴포넌트에 등록하기
        btn_date_picker.setOnClickListener(pickerBtnListener);
        btn_time_picker.setOnClickListener(pickerBtnListener);
        seekbar_seats.setOnSeekBarChangeListener(seekBarListener);
        btn_seats.setOnClickListener(seekBtnListener);
    }


    // 3. 리스너 클래스 만들기
    class SeekBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            seekbar_seats.setProgress(0);
        }
    }
    class SeekBarListener implements  SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            textview_seats.setText("선택 좌석 : "+ (i+1) + "석");
            booked_seats = (i+1);

        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    }

    class NormalBtnListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.btn_reset_seats:
                    seekbar_searts.setProgress(0);
                    break;
                case R.id.btn_book_cancel:
                    Toast.makeText(BookActivity.this,
                            "예매가 취소되었습니다.",
                            Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case R.id.btn_book_confirm:
                    Toast.makeText(BookActivity.this,
                            movie_title[movie_index]+ " " + booked_seats +
                                    "석 예매가 완료되었습니다.",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BookActivity.this,
                            BookCompActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
    class PickerBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.btn_date_picker:
                    // 6. 데이트피커(타임피커) 리스너 객체를 활용하여 데이터피커(타임피커)
                    // 다이얼로그를 호출
                    new DatePickerDialog(BookActivity.this, datePickerListener,
                            2018,1,1).show();
                    break;
                case R.id.btn_time_picker:
                    new TimePickerDialog(BookActivity.this, timePickerListener,
                            11, 43, false).show();
                    break;
            }
        }
    }
    class DatePickerListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            String temp = i+ "년 "+(i1+1)+"월 "+ i2+"일";
            textview_book_date.setText(temp);
        }
    }
    class TimePickerListener implements  TimePickerDialog.OnTimeSetListener {

        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            String temp = i+"시 "+i1+"분";
            textview_book_time.setText(temp);
        }
    }
}













