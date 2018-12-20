package com.example.raejin.mymovieapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.example.raejin.mymovieapp.adapters.ListViewAdapter;
import com.example.raejin.mymovieapp.adapters.ListViewAdapter2;
import com.example.raejin.mymovieapp.form.ListViewItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MovieListActivity extends AppCompatActivity {

    ListView lv_movieList;
    String url = "http://192.168.1.150:3000";
    String url_for_img = "http://192.168.1.150:3000/files";

    final int NUMBER_FOR_MOVIE = 6;
    ArrayList<ListViewItem> arrayList;
    ArrayList<ListViewItem> arrayList_t;
    ListViewAdapter2 listViewAdapter2;
    Button re;

    boolean fileReadPermission;
    boolean fileWritePermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        setPermission();

        re = (Button)findViewById(R.id.btn_re);
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), arrayList.size()+"개", Toast.LENGTH_SHORT).show();
                arrayList_t.addAll(arrayList);
                listViewAdapter2.notifyDataSetChanged();
            }
        });
        // 2. 리스트뷰 객체 만들기
        lv_movieList = (ListView) findViewById(R.id.lv_movieList);




        // Adapter 에게 전달할 데이터 구성하기

        arrayList = new ArrayList<ListViewItem>();
        arrayList_t = new ArrayList<ListViewItem>();

        // 서버로부터 영화 정보 6개를 받는다.
        MyHttpTask[] myHttpTaskArr = new MyHttpTask[6];
        HashMap<String, String> temp_map;

        if(arrayList.size() < NUMBER_FOR_MOVIE) {
            for (int i = 0; i < NUMBER_FOR_MOVIE; i++) {
                temp_map = new HashMap<String, String>();
                temp_map.put("number", String.valueOf(i + 1));
                myHttpTaskArr[i] = new MyHttpTask(url, temp_map);
                myHttpTaskArr[i].execute();
            }
        }



        // 3. 리스트뷰에 Adapter 등록하기
        /**
         * 첫번째 매개변수 : 액티비티 정보 (context 객체)
         * 두번째 매개변수 : 리스트뷰 항목의 레이아웃(안드로이드 제공)
         * 세번째 매개변수 : 표시할 데이터들
         */

        // 3-1.새로 만든 어답터를 등록한다.

        // 한 항목에 2개 영화 출력
//*
        listViewAdapter2 = new ListViewAdapter2(
                MovieListActivity.this,
                R.layout.listview_item2,
                arrayList_t);
//*/

        // 한 항목에 1개 영화 출력
/*
        ListViewAdapter listViewAdapter = new ListViewAdapter(
                MovieListActivity.this,
                R.layout.listview_item,
                arrayList);
        lv_movieList.setAdapter(listViewAdapter);
//*/

        lv_movieList.setAdapter(listViewAdapter2);

        // 4. 리스트 뷰에 OnItemClickListener 등록하기
        lv_movieList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(
                            AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(MovieListActivity.this,
                                i + " 선택함", Toast.LENGTH_LONG).show();
                    }
                });
    }

    class MyImageHttpTask extends AsyncTask<Void, Void, Bitmap> {

        String url_str;
        HashMap<String, String> map;
        int pos;

        public MyImageHttpTask(String url_str, HashMap<String, String> map) {
            super();

            this.url_str = url_str;
            this.map = map;
        }


        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap result = null;
            String post_query = "";
            PrintWriter printWriter = null;

            try {
                URL text = new URL(url_str);
                HttpURLConnection http = (HttpURLConnection)text.openConnection();
                http.setRequestProperty("Content-type",
                        "application/x-www-form-urlencoded;charset=UTF-8");
                http.setConnectTimeout(10000);
                http.setReadTimeout(10000);
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);

                if(map != null && map.size() > 0) {

                    Iterator<String> keys = map.keySet().iterator();

                    boolean first_query_part = true;
                    while(keys.hasNext()) {

                        if(!first_query_part) {
                            post_query += "&";
                        }

                        String key = keys.next();
                        post_query += (key + "=" + URLEncoder.encode(map.get(key), "UTF-8"));

                        first_query_part = false;
                    }

                    // sending to server
                    printWriter = new PrintWriter(new OutputStreamWriter(
                            http.getOutputStream(), "UTF-8"));
                    printWriter.write(post_query);
                    printWriter.flush();

                    // receive from server
                    result = BitmapFactory.decodeStream(http.getInputStream());

                }
            } catch(Exception e) {
                e.printStackTrace();
                result = null;
            } finally {
                try{
                    if(printWriter != null) printWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(Bitmap s) {
            // 비트맵을 sd카드의 파일로 만든다.
            String filePath;
            filePath = makeImageFile(s, map.get("number"));
            for(int i = 0; i < arrayList.size(); i++) {
                if(arrayList.get(i).getNumber() == Integer.valueOf(map.get("number")) &&
                       !filePath.equals("")) {
                    arrayList.get(i).setImgFileName(filePath);
                }
            }
            // do something
            this.cancel(true);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    class MyHttpTask extends AsyncTask<Void, Void, String> {

        String url_str;
        HashMap<String, String> map;

        public MyHttpTask(String url_str, HashMap<String, String> map) {
            super();

            this.url_str = url_str;
            this.map = map;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = null;
            String post_query = "";
            PrintWriter printWriter = null;
            BufferedReader bufferedReader = null;

            try {
                URL text = new URL(url_str);
                HttpURLConnection http = (HttpURLConnection) text.openConnection();
                http.setRequestProperty("Content-type",
                        "application/x-www-form-urlencoded;charset=UTF-8");
                http.setConnectTimeout(10000);
                http.setReadTimeout(10000);
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);

                if (map != null && map.size() > 0) {

                    Iterator<String> keys = map.keySet().iterator();

                    boolean first_query_part = true;
                    while (keys.hasNext()) {

                        if (!first_query_part) {
                            post_query += "&";
                        }

                        String key = keys.next();
                        post_query += (key + "=" + URLEncoder.encode(map.get(key), "UTF-8"));

                        first_query_part = false;
                    }

                    // sending to server
                    printWriter = new PrintWriter(new OutputStreamWriter(
                            http.getOutputStream(), "UTF-8"));
                    printWriter.write(post_query);
                    printWriter.flush();

                    // receive from server
                    bufferedReader = new BufferedReader(new InputStreamReader(
                            http.getInputStream(), "UTF-8"));
                    StringBuffer stringBuffer = new StringBuffer();
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuffer.append(line);
                    }

                    result = stringBuffer.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (printWriter != null) printWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if (bufferedReader != null) bufferedReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            // do something
            try {
                JSONObject root = new JSONObject(s);

                Log.d("HttpConnectionLog", root.getString("number"));
                Log.d("HttpConnectionLog", root.getString("title"));
                Log.d("HttpConnectionLog", String.valueOf(root.getInt("runningTime")));
                Log.d("HttpConnectionLog", root.getString("openDate"));

                JSONArray director = root.getJSONArray("director");
                JSONArray actor = root.getJSONArray("actor");
                JSONArray category = root.getJSONArray("category");

                ArrayList<String> director_arr = new ArrayList<String>();
                for(int i = 0; i < director.length(); i++) {
                    director_arr.add(director.getString(i));
                    Log.d("HttpConnectionLog", director.getString(i));
                }

                ArrayList<String> actor_arr = new ArrayList<String>();
                for(int j = 0; j < actor.length(); j++) {
                    actor_arr.add(actor.getString(j));
                    Log.d("HttpConnectionLog", actor.getString(j));
                }

                ArrayList<String> category_arr = new ArrayList<String>();
                for(int t = 0; t < category.length(); t++) {
                    category_arr.add(category.getString(t));
                    Log.d("HttpConnectionLog", category.getString(t));
                }

                arrayList.add(new ListViewItem(
                        Integer.valueOf(root.getString("number")),
                        root.getString("title"),
                        root.getString("openDate"),
                        root.getInt("runningTime"),
                        director_arr,
                        actor_arr,
                        category_arr,
                        ""
                        ));

                listViewAdapter2.notifyDataSetChanged();


                MyImageHttpTask myImageHttpTask =
                        new MyImageHttpTask(url_for_img, this.map);
                myImageHttpTask.execute();

            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                this.cancel(true);
            }

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
    private String makeImageFile(Bitmap bitmap, String fileName) {

        // 파일을 쓰기 위한 outputstream 객체
        OutputStream outputStream = null;
        String filePath = "";

        try {
            // 파일을 저장할 디렉토리 이름 & 경로를 지정
            String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/MyMovieApp";
            File dir = new File(dirPath);

            // 해당 이름과 경로에 디렉토리가 있다면 새로 만들지 않는다.
            // 없다면, 새로 디렉토리를 만든다.
            if(!dir.exists()) {
                dir.mkdir();
            }

            filePath = dirPath+"/"+fileName+".png";
            // 앞서 만든 디렉토리에 파일 이름을 지정한다.
            // 만약 같은 파일이 있다면, 새로 만들지 않는다.
            // 없다면 새로 파일을 만든다.
            File file = new File(filePath);

            if(!file.exists()) {
                file.createNewFile();
            }

            // 파일 안에 데이터를 넣을 stream을 만든다. (예시 : 만두에 소를 넣음)
            outputStream = new FileOutputStream(file);

            // 비트맵을 png 파일 형태로 데이터에 넣는다.
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            // 전송 후에 stream을 닫는다.
            outputStream.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }
    private void setPermission() {
        // 권한 검사 - sd 카드 읽기
        if(ContextCompat.checkSelfPermission(MovieListActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            fileReadPermission = true;
        }

        // 권한 검사 - SD 카드 쓰기
        if(ContextCompat.checkSelfPermission(MovieListActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            fileWritePermission = true;
        }

        // 권한이 허용되지 않으면, 사용자에게 요청한다.
        if(!fileReadPermission && !fileWritePermission) {
            ActivityCompat.requestPermissions(MovieListActivity.this,
                    new String[] {
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, 200);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 200 && grantResults.length > 0) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fileReadPermission = true;
            }
            if(grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                fileWritePermission = true;
            }
        }
    }
}