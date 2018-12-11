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
    int movie_index;
    TextView textview_movie_title, textview_director, textview_actor, textview_type;
    ImageView imageview_poster;
    RatingBar ratingbar_star_point;
    Button btn_go_book;
    final static int ERROR = -1;
    final static int MOVIE_INFO_REQUEST_CODE = 1;

    //* Scroll view code
    LinearLayout linearLayout_info;

    ArrayList<String> img_file_path;
    //*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent intent = getIntent();        // 전달받은 인텐트를 수신
        movie_index = intent.getIntExtra("movie_index", -1);    // 인텐트에서 전달된 데이터 읽음

        if (movie_index != ERROR) {
            // 파일에 저장된 데이터를 읽어옴
            movie_title = getResources().getStringArray(R.array.movie_title);
            movie_director = getResources().getStringArray(R.array.movie_director);
            movie_actor = getResources().getStringArray(R.array.movie_actor);
            movie_type = getResources().getStringArray(R.array.movie_type);
            movie_img = getResources().obtainTypedArray(R.array.movie_img);
            movie_star_point = getResources().getStringArray(R.array.movie_star_point);
            //* Scroll view code
            photo_little = getResources().obtainTypedArray(R.array.movie_little_img);
            photo_match = getResources().obtainTypedArray(R.array.movie_match_img);
            photo_black = getResources().obtainTypedArray(R.array.movie_black_img);
            photo_monday = getResources().obtainTypedArray(R.array.movie_monday_img);
            //*/
            photo_package = getResources().getStringArray(R.array.photo_package);


            // 컴포넌트의 객체를 생성
            textview_movie_title = (TextView) findViewById(R.id.textview_movie_title);
            textview_director = (TextView) findViewById(R.id.textview_director);
            textview_actor = (TextView) findViewById(R.id.textview_actor);
            textview_type = (TextView) findViewById(R.id.textview_type);
            imageview_poster = (ImageView) findViewById(R.id.imageview_poster);
            ratingbar_star_point = (RatingBar) findViewById(R.id.ratingbar_star_point);
            btn_go_book = (Button) findViewById(R.id.btn_go_book);
            //* Scroll view code
            linearLayout_info = (LinearLayout) findViewById(R.id.linearLayout_info);
            //*/


            switch (movie_index) {
                case 0: // 블랙펜서를 선택한 경우

                    // 이미지 이름 읽어오기
                    String temp_str = photo_package[movie_index];
                    String[] split_str;
                    split_str = temp_str.split("/");

                    for (int j = 0; j < split_str.length; j++) {
                            /*
                            이미지 이름으로 이미지 아이디 찾기
                             */
                        // 이름 구성하기
                        String file_name = "@drawable/" + split_str[j];
                        // 이미지 아이디 찾기
                        int img_id = getResources().getIdentifier(
                                file_name,
                                "drawable",
                                this.getPackageName());
                        ImageView temp = new ImageView(InfoActivity.this);
                        temp.setImageResource(img_id);
                        temp.setLayoutParams(new LinearLayout.LayoutParams(500, 600));
                        temp.setScaleType(ImageView.ScaleType.FIT_XY);
                        linearLayout_info.addView(temp);
                    }
                    break;
                case 1: // 궁합을 선택한 경우
                    img_file_path = findFileByExt("jpg");

                    for(int i = 0; i < img_file_path.size(); i++) {
                        ImageView temp = new ImageView(InfoActivity.this);


                        // 경로로 부터 파일을 읽어옴 (그냥 파일)
                        File img_file = new File(img_file_path.get(i));
                        // 파일을 이미지 파일로 변환 (이미지 파일)
                        Bitmap bitmap = BitmapFactory.decodeFile(img_file.getAbsolutePath());

                        Bitmap bitmap_resize = Bitmap.createScaledBitmap(bitmap, 500, 600, true);

                        // 이미지 뷰에 이미지 파일 적용
                        temp.setImageBitmap(bitmap_resize);

                        temp.setLayoutParams(new LinearLayout.LayoutParams(500, 600));
                        temp.setScaleType(ImageView.ScaleType.FIT_XY);
                        linearLayout_info.addView(temp);
                    }
                    /*
                    for (int i = 0; i < photo_match.length(); i++) {
                        ImageView temp = new ImageView(InfoActivity.this);
                        temp.setImageResource(
                                photo_match.getResourceId(i, -1));
                        temp.setLayoutParams(new LinearLayout.LayoutParams(500, 600));
                        temp.setScaleType(ImageView.ScaleType.FIT_XY);
                        linearLayout_info.addView(temp);
                    }
                    */
                    break;
                case 2: // 리틀 포레스트를 선택한 경우
                    for (int i = 0; i < photo_little.length(); i++) {
                        ImageView temp = new ImageView(InfoActivity.this);
                        temp.setImageResource(
                                photo_little.getResourceId(i, -1));
                        temp.setLayoutParams(new LinearLayout.LayoutParams(500, 600));
                        temp.setScaleType(ImageView.ScaleType.FIT_XY);
                        linearLayout_info.addView(temp);
                    }
                    break;
                case 3: //월요일이 사라졌다를 선택한 경우
                    for (int i = 0; i < photo_monday.length(); i++) {
                        ImageView temp = new ImageView(InfoActivity.this);
                        temp.setImageResource(
                                photo_monday.getResourceId(i, -1));
                        temp.setLayoutParams(new LinearLayout.LayoutParams(500, 600));
                        temp.setScaleType(ImageView.ScaleType.FIT_XY);
                        linearLayout_info.addView(temp);
                    }
                    break;
            }


            // 데이터를 컴포넌트에 적용
            textview_movie_title.setText(movie_title[movie_index]);
            textview_director.setText(movie_director[movie_index]);
            textview_actor.setText(movie_actor[movie_index]);
            textview_type.setText(movie_type[movie_index]);
            imageview_poster.setImageResource(movie_img.getResourceId(movie_index, ERROR));
            ratingbar_star_point.setRating(Float.valueOf(movie_star_point[movie_index]));

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

    private ArrayList<String> findFileByExt(String ext) {
        ArrayList<String> temp_array = new ArrayList<String>();
        final String file_ext = ext;
        // 1. SD카드가 인식되어 있는지 확인한다.
        // 인식이 된경우에는 getExternalStorageState() 함수가 Environment.MEDIA_MOUNT 값을 리턴
        String state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)) {
            //2 sd카드 폴더의 주소를 가져옴
            String sd_path = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();

            //3. 찾고자 하는 파일의 확장자를 검색해주는 필더 객체를 생성
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
                    return s.endsWith(file_ext);
                }
            };
            //4. 파일 필터 객체를 활용하여 파일을 검색
            File sdRoot = new File(sd_path);
            String[] jpg_list = sdRoot.list(filter);
            // 5. 검색 한 파일 리스트를 출력
            String temp = "";
            for(int i = 0; i < jpg_list.length; i++) {
                temp += jpg_list[i] +"\n";
                temp_array.add(sd_path+"/"+jpg_list[i]);
            }
            return temp_array;
        } else {
            Toast.makeText(getApplicationContext(),
                    "SD 카드 인식이 안됨",
                    Toast.LENGTH_LONG).show();
            return null;
        }

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
