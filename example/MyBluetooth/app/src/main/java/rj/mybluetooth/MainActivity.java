package rj.mybluetooth;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import rj.mybluetooth.adapter.DeviceAdapter;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter bluetoothAdapter;
    Button btn_scan, btn_discover;
    ArrayList<BluetoothDevice> device_list;
    boolean bPerm;
    ListView lv_device;
    DeviceAdapter deviceAdapter;
    boolean bConn = false;
    boolean bSelect = false;
    AlertDialog selectDialog;
    BluetoothServerSocket serverSocket;
    static BluetoothSocket connSocket;
    BluetoothDevice targetDevice;
    final UUID MY_UUID = UUID.fromString("00001111-1010-1010-1010-12345678ABCD");
    ServerThread serverThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setPermission(new String[] {
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN
        });

        btn_discover = (Button) findViewById(R.id.btn_discover);
        btn_scan     = (Button) findViewById(R.id.btn_scan);
        lv_device    = (ListView) findViewById(R.id.lv_device);

        btn_discover.setOnClickListener(new BtnListener());
        btn_scan.setOnClickListener(new BtnListener());

        device_list = new ArrayList<BluetoothDevice>();
        deviceAdapter = new DeviceAdapter(
                MainActivity.this, R.layout.item_device,
                device_list);

        lv_device.setAdapter(deviceAdapter);
        lv_device.setOnItemClickListener(new ItemListener());

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // 지원 여부 검사
        if(bluetoothAdapter != null) {
            // 블루투스 활성화
            bluetoothAdapter.enable();

            Toast.makeText(MainActivity.this, "서버 동작",Toast.LENGTH_SHORT).show();

            // 앱이 시작되면 서버 동작을 하기 위해 스레드를 구동한다.
            serverThread = new ServerThread();
            serverThread.start();
        } else {
            Toast.makeText(getApplicationContext(), "블루투스를 지원하지 않음",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    class ItemListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            targetDevice = device_list.get(i);

            Toast.makeText(MainActivity.this, "클라이언트 동작", Toast.LENGTH_SHORT).show();
            ClientThread clientThread = new ClientThread();
            clientThread.start();
        }
    }

    
    class ServerThread extends Thread {
        @Override
        public void run() {
            while(true) {
                if(!bConn && bluetoothAdapter.isEnabled()) {
                    try {

                        serverSocket = bluetoothAdapter
                                .listenUsingRfcommWithServiceRecord("MyBluetooth", MY_UUID);

                        connSocket = serverSocket.accept();

                        if(connSocket != null) {
                            Intent intent = new Intent(MainActivity.this,
                                    MessageActivity.class);
                            startActivity(intent);
                            bConn = true;
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    class ClientThread extends Thread {

        @Override
        public void run() {
            try {
                bluetoothAdapter.cancelDiscovery();
                connSocket = targetDevice.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                connSocket.connect();

                bConn = true;

                Intent intent = new Intent(MainActivity.this,
                        MessageActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what) {
                case 1:
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(getApplicationContext());
                    builder.setMessage("연결합니끼?");
                    builder.setPositiveButton("허가", dialogListener);
                    builder.setNegativeButton("거부", dialogListener);
                    selectDialog = builder.create();
                    selectDialog.show();
                    break;
            }
        }
    };

    DialogInterface.OnClickListener dialogListener =
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(dialogInterface == selectDialog &&
                            i == DialogInterface.BUTTON_POSITIVE) {



                    } else if (dialogInterface == selectDialog &&
                            i == DialogInterface.BUTTON_NEGATIVE) {
                        try {
                            serverSocket.close();
                            bSelect = false;
                            bConn = false;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };


    class BtnListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.btn_discover:     // 기기 검색 허용
                    Toast.makeText(getApplicationContext(), "다른 기기가 스마트폰을 검색하여 인지할 수 있음",
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 1000);
                    startActivity(intent);
                    break;
                case R.id.btn_scan:     // 페어링 기기 검색
                    Toast.makeText(getApplicationContext(), "페어링된 기기를 검색함",
                            Toast.LENGTH_SHORT).show();

                    Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
                    device_list.clear();

                    if(devices.size() > 0) {
                        Iterator<BluetoothDevice> iter = devices.iterator();
                        while(iter.hasNext()) {
                            BluetoothDevice d = iter.next();
                            // 디바이스의 목록을 저장
                            device_list.add(d);
                            Log.d("BLUETEST", "name : " + d.getName() + " addr : " + d.getAddress());
                        }

                        deviceAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "검색된 기기가 없습니다.",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    private void setPermission(String[] perm) {
        boolean bPerm = false;

        for (int i = 0; i < perm.length; i++) {
            if(ContextCompat.checkSelfPermission(getApplicationContext(), perm[i])
                    != PackageManager.PERMISSION_GRANTED) {
                bPerm = false;
            }
        }

        if(!bPerm) {
            ActivityCompat.requestPermissions(
                    this, perm, 200);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean bPerm = true;
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 200 && grantResults.length > 0) {
            for(int i = 0; i < grantResults.length; i++) {
                if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    bPerm = false;
                }
            }
        }
        this.bPerm = bPerm;
    }
}
