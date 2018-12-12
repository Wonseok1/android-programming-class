package raejin.listviewexample;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //2단계 : 리스트뷰의 객체를 저장할 변수를 만든다.
    ListView listView_custom;
    ArrayList<ItemDate> data_list;
    Button button_call;

    ListView listView_greatman;
    ProgressBar progressBar_circle;

    // 어답터에서 리스트뷰에게 전달할 자료들
    final String[] list = {
            "이순신", "광개토대왕", "신사임당", "세종대왕",
            "이성계", "윤봉길", "안중근"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar_circle = (ProgressBar)findViewById(R.id.progressBar_cirle);

        button_call = (Button)findViewById(R.id.button_callSub);
        button_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 객체를 화면에 나타내려면 setVisibility()함수를 쓴다.
                // 매개변수는 보이려면 View.VISIBLE
                // 안보이려면 View.INVISIBLE
                // 공간을 차지하지 않고 완전히 숨김 View.GONE
                progressBar_circle.setVisibility(View.VISIBLE);

                /*
                // 다른 엑티비티로 이동하는 방법
                // 1. 인텐트 객체를 만든다.
                // 인텐트 객체 만들때 필요한 2가지는 MainActivity의 정보 = MainActivity.this
                // SubActivity.class
                Intent intent = new Intent(MainActivity.this, SubActivity.class);

                // 2. startActivity()함수로 엑티비티를 이동한다.
                startActivity(intent);

                //*/

                /* 인텐트의 다른 쓰임세 1 > 웹페이지 보이기
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.naver.com"));
                startActivity(intent);
                //*/

                /* 인텐트의 다른 쓰임세 2 > 전화 걸기
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("tel:010-1234-5678"));
                startActivity(intent);
                //*/


                /* 인텐트의 다른 쓰임세 3 > 지도 보기
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("geo:36.6349120, 127.4869820"));
                startActivity(intent);
                //*/
            }
        });


        //2단계 : 리스트뷰의 객체를 만든다.
        listView_custom = (ListView)findViewById(R.id.listView_custom);

        data_list = new ArrayList<ItemDate>();
        data_list.add(new ItemDate(R.drawable.leesoonsin, "이순신", "임진왜란의 영웅"));
        data_list.add(new ItemDate(R.drawable.gwanggaeto, "광개토대왕", "고구려의 대왕"));
        data_list.add(new ItemDate(R.drawable.saimdang, "신사임당", "조선의 여류시인"));
        data_list.add(new ItemDate(R.drawable.sejong, "세종대왕", "한글의 창시자"));
        data_list.add(new ItemDate(R.drawable.leesungkye, "이성계", "조선의 태조"));
        data_list.add(new ItemDate(R.drawable.yoon, "윤봉길", "한국의 독립운동가"));
        data_list.add(new ItemDate(R.drawable.ann, "안중근", "한국의 독립운동가"));


        listView_greatman = (ListView)findViewById(
                R.id.listView_greatman);

        // 커스텀 리스트뷰 예외 : 만든 어답터의 객체를 생성한다.
        MyAdapter myAdapter = new MyAdapter(MainActivity.this,
                R.layout.item_layout, R.layout.item_layout2, data_list);

        listView_custom.setAdapter(myAdapter);


        // 리스트뷰 예외
        // 배열 자료를 리스트뷰로 전달해주는 어답터 : ArrayAdapter<T>
        // T대신에 자료형을 넣는다.
        // 생성자 ArrayAdapter<String>()의 매개변수
        // - MainActivity.this = 엑티비티의 정보, 리스트뷰 객체가
        // 있는 엑티비티 이름.this
        // - android.R.layout.simple_list_item_1 = 리스트뷰 안에
        // 들어가는 항목의 레이아웃 파일(안드로이드에서 기본 제공)
        // - list : 리스트 뷰에게 전달할 배열
        ArrayAdapter<String> adapter
                = new ArrayAdapter<String>(MainActivity.this,
                    android.R.layout.simple_list_item_1, list);

        listView_greatman.setAdapter(adapter);

        //4단계 : 리스너의 객체 만들기
        ItemListener itemListener = new ItemListener();

        //5단계 : 리스너의 객체를 리스트뷰에 등록하기
        listView_greatman.setOnItemClickListener(itemListener);

        listView_custom.setOnItemClickListener(itemListener);
    }

    //3단계 : 리스너 객체의 클래스 만들기
    class ItemListener implements AdapterView.OnItemClickListener {

        /**
         *
         * @param adapterView 리스트 뷰의 객체
         * @param view 리스트 뷰 안에 포함된 항목의 객체
         * @param i 리스트 뷰에서 항목의 순서번호(0부터 시작)
         * @param l 리스트 뷰에서 항목의 순서번호(long 자료형)
         */
        @Override
        public void onItemClick(AdapterView<?> adapterView,
                                View view, int i, long l) {
            String str = "내가 선택한 위인은? :" + list[i];
            Toast.makeText(getApplicationContext(),
                    str, Toast.LENGTH_SHORT).show();
        }
    }
}
