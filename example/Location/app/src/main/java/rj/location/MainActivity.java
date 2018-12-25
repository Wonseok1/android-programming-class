package rj.location;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    boolean bPerm;
    LocationListener locationListener;
    String selected_provider;
    StringBuffer sb;
    TextView tv_provider, tv_lat, tv_lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sb = new StringBuffer();
        tv_provider = (TextView)findViewById(R.id.tv_provider);
        tv_lat = (TextView)findViewById(R.id.tv_lat);
        tv_lng = (TextView)findViewById(R.id.tv_lng);
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        List<String> providers = locationManager.getAllProviders();
        sb.append("All provider\n");
        for(int i = 0; i < providers.size(); i++) {
            sb.append(providers.get(i)+"\n");
        }
        sb.append("\n\n");

        List<String> enable_providers = locationManager.getProviders(true);
        sb.append("Enable provider\n");
        for(int j = 0; j < enable_providers.size(); j++) {
            sb.append(enable_providers.get(j)+"\n");
        }
        sb.append("\n\n");

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        selected_provider = locationManager.getBestProvider(criteria, true);

        sb.append("Best provider\n");
        sb.append(selected_provider);
        sb.append("\n\n");

        tv_provider.setText(sb);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // 위치 정보 전달할 때 호출
                // 위치 정보를 얻을 수 있다.
                tv_lat.setText("위도 : " + String.valueOf(location.getLatitude()));
                tv_lng.setText("경도 : " + String.valueOf(location.getLongitude()));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                // provider의 상태가 변경될 때마다 호출
            }

            @Override
            public void onProviderEnabled(String s) {
                // provider가 사용 가능한 상황이 되는 순간 호출
            }

            @Override
            public void onProviderDisabled(String s) {
                // provider가 사용 불가능한 상황이 되는 순간 호출
            }
        };

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION )
                == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(selected_provider);
            if(location != null) {
                tv_lat.setText("위도 : " + String.valueOf(location.getLatitude()));
                tv_lng.setText("경도 : " + String.valueOf(location.getLongitude()));

            }
            locationManager.requestLocationUpdates(selected_provider, 10000, 10, locationListener);
        } else {
            ActivityCompat.requestPermissions(
                    this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 200);
            Toast.makeText(MainActivity.this, "권한을 허용한 후 재시작 해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
    }
}
