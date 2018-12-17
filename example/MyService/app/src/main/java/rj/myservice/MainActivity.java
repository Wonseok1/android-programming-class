package rj.myservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    Button btn_start, btn_stop, btn_send1, btn_send2;
    TextView tv_rev;
    BroadcastReceiver receiver_form_service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_start = (Button)findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyService2.class);
                startService(intent);
            }
        });

        btn_stop = (Button)findViewById(R.id.btn_stop);
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyService2.class);
                stopService(intent);
            }
        });

        btn_send1 = (Button)findViewById(R.id.btn_send1);
        btn_send1.setOnClickListener(new myButtonListener());

        btn_send2 = (Button)findViewById(R.id.btn_send2);
        btn_send2.setOnClickListener(new myButtonListener());

        tv_rev = (TextView)findViewById(R.id.tv_rev);

        receiver_form_service = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String res = intent.getStringExtra("res");

                if(res != null) {
                    tv_rev.setText(res);
                }
            }
        };

        registerReceiver(receiver_form_service, new IntentFilter("rj.myservice"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver_form_service);

    }

    class myButtonListener implements View.OnClickListener {

        Intent intent;

        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.btn_send1:
                    intent = new Intent("rj.myservice");
                    intent.putExtra("mode", "send1");
                    break;
                case R.id.btn_send2:
                    intent = new Intent("rj.myservice");
                    intent.putExtra("mode", "send2");
                    break;
            }
            sendBroadcast(intent);
        }
    }
}
