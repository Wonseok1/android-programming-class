package com.example.raejin.mymovieapp;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raejin.mymovieapp.format.MovieInfo;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

/**
 * Created by Raejin on 2018-03-05.
 */

public class InfoActivity extends AppCompatActivity {

    String[] movie_title, movie_director, movie_actor,
            movie_type, movie_star_point, photo_package;
    TypedArray movie_img, photo_little, photo_match, photo_monday, photo_black;
    ArrayList<MovieInfo> movieInfo;

    int movie_index;
    TextView textview_movie_title, textview_director, textview_actor, textview_type;
    ImageView imageview_poster;
    RatingBar ratingbar_star_point;
    Button btn_go_book;


    final static int ERROR = -1;
    final static int MOVIE_INFO_REQUEST_CODE = 1;

    //* Scroll view code
    LinearLayout linearLayout_info;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent intent = getIntent();        // 전달받은 인텐트를 수신
        movie_index = intent.getIntExtra("movie_index", -1);    // 인텐트에서 전달된 데이터 읽음

        movieInfo = new ArrayList<MovieInfo>();
        setMovieData();

        if (intent != null && movie_index != ERROR) {

            // 컴포넌트의 객체를 생성
            textview_movie_title = (TextView) findViewById(R.id.textview_movie_title);
            textview_director = (TextView) findViewById(R.id.textview_director);
            textview_actor = (TextView) findViewById(R.id.textview_actor);
            ratingbar_star_point = (RatingBar) findViewById(R.id.ratingbar_star_point);
            btn_go_book = (Button) findViewById(R.id.btn_go_book);
            //* Scroll view code
            linearLayout_info = (LinearLayout) findViewById(R.id.linearLayout_info);
            //*/

            textview_movie_title.setText(movieInfo.get(movie_index).getTitle());
            textview_director.setText(movieInfo.get(movie_index).getDirector());
            textview_actor.setText(movieInfo.get(movie_index).getActors());

            linearLayout_info.removeAllViews();

            for (int j = 0; j < movieInfo.get(movie_index).getImages().size(); j++) {
                ImageView temp = new ImageView(InfoActivity.this);
                temp.setImageResource(movieInfo.get(movie_index).getImages().get(j));
                temp.setLayoutParams(new LinearLayout.LayoutParams(500, 600));
                temp.setScaleType(ImageView.ScaleType.FIT_XY);
                linearLayout_info.addView(temp);
            }

            // 버튼에 대한 리스너 객체 만들기
            GoToBookListener goToBookListener = new GoToBookListener();

            // 버튼 객체에 리스너 객체 등록
            btn_go_book.setOnClickListener(goToBookListener);

        } else {
            Toast.makeText(InfoActivity.this,
                    "동작중에 오류가 발생하였습니다.",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }


    private void setMovieData() {
        ArrayList<Integer> images = new ArrayList<Integer>();

        images.add(R.drawable.black);
        images.add(R.drawable.black1);
        images.add(R.drawable.black2);
        images.add(R.drawable.black3);
        movieInfo.add(new MovieInfo("블랙펜서", "라이언 쿠글러", "채드윅 보스만, 마이클 B. 조던, 루피타 뇽, 다나이 구리라", images));

        images = new ArrayList<Integer>();
        images.add(R.drawable.match);
        images.add(R.drawable.match1);
        images.add(R.drawable.match2);
        images.add(R.drawable.match3);
        movieInfo.add(new MovieInfo("궁합", "홍창표", "심은경, 이승기", images));


        images = new ArrayList<Integer>();
        images.add(R.drawable.match);
        images.add(R.drawable.match1);
        images.add(R.drawable.match2);
        images.add(R.drawable.match3);
        movieInfo.add(new MovieInfo("궁합", "홍창표", "심은경, 이승기", images));


        images = new ArrayList<Integer>();
        images.add(R.drawable.little);
        images.add(R.drawable.little1);
        images.add(R.drawable.little2);
        images.add(R.drawable.little3);
        movieInfo.add(new MovieInfo("리틀 포레스트", "임순례", "김태리, 류준열, 문소리, 진기주", images));


        images = new ArrayList<Integer>();
        images.add(R.drawable.monday);
        images.add(R.drawable.monday1);
        images.add(R.drawable.monday2);
        images.add(R.drawable.monday3);
        movieInfo.add(new MovieInfo("월요일이 사라졌다.", "토미 위르콜라", "누미 라파스, 윌렘 데포, 글렌 클로즈", images));


    }

    class GoToBookListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent indent = new Intent(InfoActivity.this, BookActivity.class);
            indent.putExtra("movie_index", movie_index);
            startActivityForResult(indent, MOVIE_INFO_REQUEST_CODE);
        }
    }
}
