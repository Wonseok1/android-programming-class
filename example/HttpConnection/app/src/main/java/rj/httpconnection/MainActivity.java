package rj.httpconnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.spec.ECField;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    TextView tv_data;
    ImageView iv_poster;
    String host = "http://192.168.1.150:3000";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_data = (TextView)findViewById(R.id.tv_data);
        iv_poster = (ImageView)findViewById(R.id.iv_poster);

        String url = this.host;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("number","1");

        MyHttpTask myHttpTask = new MyHttpTask(url, map);
        myHttpTask.execute();

        String url_img = this.host+"/files";
        HashMap<String, String> map_img = new HashMap<String, String>();
        map_img.put("number","1");

        MyImageHttpTask myImageHttpTask = new MyImageHttpTask(url_img, map_img);
        myImageHttpTask.execute();

    }

    class MyImageHttpTask extends AsyncTask<Void, Void, Bitmap> {

        String url_str;
        HashMap<String, String> map;

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
            // do something
            iv_poster.setImageBitmap(s);
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
                    bufferedReader = new BufferedReader(new InputStreamReader(
                            http.getInputStream(), "UTF-8"));
                    StringBuffer stringBuffer = new StringBuffer();
                    String line;

                    while((line = bufferedReader.readLine()) != null) {
                        stringBuffer.append(line);
                    }

                    result = stringBuffer.toString();
                }
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                try{
                    if(printWriter != null) printWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if(bufferedReader != null) bufferedReader.close();
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
                Log.d("HttpConnectionLog", root.getString("title"));
                Log.d("HttpConnectionLog", String.valueOf(root.getInt("runningTime")));
                Log.d("HttpConnectionLog", root.getString("openDate"));

                JSONArray director = root.getJSONArray("director");
                JSONArray actor = root.getJSONArray("actor");
                JSONArray category = root.getJSONArray("category");

                for(int i = 0; i < director.length(); i++) {
                    Log.d("HttpConnectionLog", director.getString(i));
                }

                for(int j = 0; j < actor.length(); j++) {
                    Log.d("HttpConnectionLog", actor.getString(j));
                }
                for(int t = 0; t < category.length(); t++) {
                    Log.d("HttpConnectionLog", category.getString(t));
                }

            } catch(Exception e) {
                e.printStackTrace();
            }

            tv_data.setText(s);

            this.cancel(true);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}



