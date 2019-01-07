package rj.bearingp1top2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText et_lat1, et_lat2, et_lon1, et_lon2;
    Button btn_go;
    ImageView iv_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_lat1 = (EditText)findViewById(R.id.et_lat1);
        et_lat2 = (EditText)findViewById(R.id.et_lat2);
        et_lon1 = (EditText)findViewById(R.id.et_lon1);
        et_lon2 = (EditText)findViewById(R.id.et_lon2);
        btn_go = (Button)findViewById(R.id.btn_go);
        iv_img = (ImageView)findViewById(R.id.iv_img);

        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double lat1 = Double.valueOf(et_lat1.getText().toString());
                double lat2 = Double.valueOf(et_lat2.getText().toString());
                double lon1 = Double.valueOf(et_lon1.getText().toString());
                double lon2 = Double.valueOf(et_lon2.getText().toString());

                short result = bearingP1toP2(lat1, lon1, lat2, lon2);

                Toast.makeText(MainActivity.this,
                        "result : " + result, Toast.LENGTH_SHORT).show();

                iv_img.setRotation((float)result);
            }
        });
    }

    public static short bearingP1toP2(
            double P1_latitude, double P1_longitude,
            double P2_latitude, double P2_longitude)
    {
        // 현재 위치 : 위도나 경도는 지구 중심을 기반으로 하는 각도이기 때문에 라디안 각도로 변환한다.
        double Cur_Lat_radian = P1_latitude * (3.141592 / 180);
        double Cur_Lon_radian = P1_longitude * (3.141592 / 180);


        // 목표 위치 : 위도나 경도는 지구 중심을 기반으로 하는 각도이기 때문에 라디안 각도로 변환한다.
        double Dest_Lat_radian = P2_latitude * (3.141592 / 180);
        double Dest_Lon_radian = P2_longitude * (3.141592 / 180);

        // radian distance
        double radian_distance = 0;
        radian_distance = Math.acos(
                Math.sin(Cur_Lat_radian) * Math.sin(Dest_Lat_radian)
                        + Math.cos(Cur_Lat_radian) * Math.cos(Dest_Lat_radian)
                        * Math.cos(Cur_Lon_radian - Dest_Lon_radian));

        // 목적지 이동 방향을 구한다.(현재 좌표에서 다음 좌표로 이동하기 위해서는 방향을 설정해야 한다. 라디안값이다.
        double radian_bearing = Math.acos(
                (Math.sin(Dest_Lat_radian) - Math.sin(Cur_Lat_radian)
                        * Math.cos(radian_distance)) / (Math.cos(Cur_Lat_radian)
                        * Math.sin(radian_distance)));
        // acos의 인수로 주어지는 x는 360분법의 각도가 아닌 radian(호도)값이다.

        double true_bearing = 0;
        if (Math.sin(Dest_Lon_radian - Cur_Lon_radian) < 0)
        {
            true_bearing = radian_bearing * (180 / 3.141592);
            true_bearing = 360 - true_bearing;
        }
        else
        {
            true_bearing = radian_bearing * (180 / 3.141592);
        }

        return (short)true_bearing;
    }
}
