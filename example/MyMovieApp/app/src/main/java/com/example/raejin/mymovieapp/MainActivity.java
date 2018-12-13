package com.example.raejin.mymovieapp;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView_movie;        // 리스트뷰 객체를 저장하는 변수
    ArrayAdapter movie_adapter;     // 어답터 객체를 저장하는 변수
    final static int MOVIE_INDEX_REQUEST_CODE = 1;
    ArrayList<movie_list_item> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * 툴바 객체 만들기
         */
        Toolbar toolbar_main = (Toolbar)findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar_main);
        /**
         * END OF 툴바 객체 만들기
         */

        arrayList = new ArrayList<movie_list_item>();

        String[] movie_title = getResources().getStringArray(R.array.movie_title);
        TypedArray movie_img = getResources().obtainTypedArray(R.array.movie_img);

        for(int i = 0; i < movie_title.length; i++ ) {
            arrayList.add(new movie_list_item(
                    movie_img.getResourceId(i, -1),
                    movie_title[i]));
        }
        listView_movie = (ListView)findViewById(R.id.listview_movie);   // 리스트뷰 객체 생성
        listView_movie.setAdapter(new MyMovieAdapter(
                MainActivity.this,
                arrayList,
                R.layout.item_movie_list
        ));

        MyMovieListListener myMovieListListener =
                new MyMovieListListener();          // 리스너 객체 생성

        listView_movie.setOnItemClickListener(myMovieListListener); // 리스트뷰 객체에 어답터 객체 등록


        movie_adapter = ArrayAdapter.createFromResource(
                MainActivity.this,
                R.array.movie_title,
                android.R.layout.simple_list_item_1);   // 어답터 객체 생성
        listView_movie.setAdapter(movie_adapter);       // 리스트 뷰 객체에 어답터 객체 등록



    }

    /**
     * 메뉴를 툴바에 연결시켜주는 함수
     * @param aMenu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu aMenu) {
        getMenuInflater().inflate(R.menu.menu, aMenu);
        return true;
    }

    /**
     * 메뉴를 선택했을 때 동작을 구섷아는 함수
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.item_go_to_join:
                Intent intent = new Intent(
                        MainActivity.this, JoinActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    class MyMovieListListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(
                    MainActivity.this,
                    InfoActivity.class);                    // 인텐트 객체 생성
            intent.putExtra("movie_index", i);        // 인텐트 객체에 데이터 첨부
            startActivityForResult(intent, MOVIE_INDEX_REQUEST_CODE);   // 액티비티 호출
        }
    }

}
