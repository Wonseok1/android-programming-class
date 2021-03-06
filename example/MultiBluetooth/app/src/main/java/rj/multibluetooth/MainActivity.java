package rj.multibluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import rj.multibluetooth.adapter.DeviceAdapter;

import static android.media.CamcorderProfile.get;

public class MainActivity extends AppCompatActivity {
    /**
     * 페어링 되어 있는 블루투스 모듈 2개를 활용하여 데이터를 전송한다는 가정하에 예시를 작성함
     *
     * 주변 기기를 검색하는 부분은 제외함
     * 아두이노 코드 주소 : https://github.com/jrj8819/arduino-programming-class/blob/master/example/easybluetooth2Android/easybluetooth2Android.ino
     */
    boolean bPerm;
    final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    ArrayList<BluetoothDevice> device_list;
    DeviceAdapter deviceAdapter;
    BluetoothAdapter bluetoothAdapter;
    Button btn_blue1On, btn_blue1Off, btn_blue2On, btn_blue2Off;
    ListView lv_device;
    TextView tv_blue1, tv_blue2;
    boolean bBlue1 = false, bBlue2 = false;
    BluetoothDevice target1, target2;
    BluetoothSocket target1Socket, target2Socket;
    Toolbar tb_main;
    ReadThread read1Thread, read2Thread;
    Write1Thread write1Thread;
    Write2Thread write2Thread;
    Handler write1Handler, write2Handler;

    final int SEND_1_MESSAGE = 100;
    final int RECEIVED_1_MESSAGE = 200;
    final int SEND_2_MESSAGE = 300;
    final int RECEIVED_2_MESSAGE = 400;

    StringBuffer sb;

    boolean bTarget1Read = true;
    boolean bTarget2Read = true;
    MsgThread msgTarget1Thread, msgTarget2Thread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setViews();
        Toast.makeText(MainActivity.this,
                "페어링된 기기를 선택하세요",
                Toast.LENGTH_LONG).show();

        // 전달할 메시지를을 저장할 Stringbuffer 객체
        sb = new StringBuffer();

        // 권한 관련 설정
        setPermission(new String[] {
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN
        });

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // 블루투스 지원 여부 검사
        if(bluetoothAdapter != null) {
            // 블루투스 활성화
            bluetoothAdapter.enable();

            // 검색된 블루투스 기기 정보를 저장하기 위한 Arraylist
            device_list = new ArrayList<BluetoothDevice>();

            // 검색된 블루투스 기기 정보를 listview에 표시하기 위한 Adapter 생성 및 등록
            deviceAdapter = new DeviceAdapter(
                    MainActivity.this, R.layout.item_device,
                    device_list);
            lv_device.setAdapter(deviceAdapter);
            lv_device.setOnItemClickListener(new ListClickListener());

            // 스마트폰과 페어링 되어 있는 기기들의 목록을 만든다
            scanPairing();



        } else {
            Toast.makeText(getApplicationContext(), "블루투스를 지원하지 않음",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    // 이전에 페어링된 기기 검색
    private void scanPairing() {
        Toast.makeText(getApplicationContext(), "이전 페어링된 기기를 검색함",
                Toast.LENGTH_SHORT).show();

        if(bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();
        }

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
    }

    // 페어링된 기존 기기와 연결을 위해 리스트뷰 항목을 눌렀을 때 동작하는 리스너
    class ListClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Toast.makeText(MainActivity.this, "블루투스 모듈의 깜빡임이 멈출때 까지 잠시 기다려 주세요", Toast.LENGTH_SHORT).show();
            TargetThread targetThread;

            if(!bBlue1) {
                target1 = device_list.get(i);
                targetThread = new TargetThread(target1);
                tv_blue1.setText(target1.getName() + "선택 완료");
            } else {
                target2 = device_list.get(i);
                targetThread = new TargetThread(target2);
                tv_blue2.setText(target2.getName() + "선택 완료");
            }
            targetThread.start();

        }
    }

    // 기기와 블루투스 통신을 위한 소캣을 생성하고 데이터 송신을 위한 스레드를 생성 및 실행하는 스레드
    class TargetThread extends Thread {
        BluetoothSocket socket;
        BluetoothDevice device;

        public TargetThread(BluetoothDevice device) {
            this.device = device;
        }

        @Override
        public void run() {
            try {
                socket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                socket.connect();

                if(!bBlue1) {
                    target1Socket = socket;
                    if(msgTarget1Thread == null) {
                        msgTarget1Thread = new MsgThread(1);
                        msgTarget1Thread.start();
                        bBlue1 = true;
                    }

                } else {
                    target2Socket = socket;
                    if(msgTarget2Thread == null) {
                        msgTarget2Thread = new MsgThread(2);
                        msgTarget2Thread.start();
                        bBlue2 = true;
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 데이터 송신 수신을 담당하는 스레드들을 실행하는 스레드
    class MsgThread extends Thread {
        int target_number;

        public MsgThread(int target_number) {
            this.target_number = target_number;
        }

        @Override
        public void run() {
            try {
                switch (target_number) {
                    case 1:
                        // 기존에 readThread 가 있다면 중지한다.
                        if(read1Thread != null) {
                            read1Thread.interrupt();
                        }
                        // 데이터를 수신하기 위한 readThread 생성
                        read1Thread = new ReadThread(target1Socket, 1);
                        read1Thread.start();

                        // 기존에 writeThread 가 있다면 중지한다.
                        if(write1Thread != null) {
                            // writeThread 내부에서 looper를 활용하고 있으므로
                            // looper를 종료해 주어야 한다.
                            write1Handler.getLooper().quit();
                        }

                        // 데이터를 송신하기 위한 writeThread 생성
                        write1Thread = new Write1Thread(target1Socket, 1);
                        write1Thread.start();
                        break;
                    case 2:

                        // 기존에 readThread 가 있다면 중지한다.
                        if(read2Thread != null) {
                            read2Thread.interrupt();
                        }
                        // 데이터를 수신하기 위한 readThread 생성
                        read2Thread = new ReadThread(target2Socket, 2);
                        read2Thread.start();

                        // 기존에 writeThread 가 있다면 중지한다.
                        if(write2Thread != null) {
                            // writeThread 내부에서 looper를 활용하고 있으므로
                            // looper를 종료해 주어야 한다.
                            write2Handler.getLooper().quit();
                        }

                        // 데이터를 송신하기 위한 writeThread 생성
                        write2Thread = new Write2Thread(target2Socket, 2);
                        write2Thread.start();

                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 데이터를 아두이노에게 송신하는 스레드 1
    class Write1Thread extends Thread {
        BluetoothSocket socket;
        OutputStream os = null;
        int target_number;

        public Write1Thread(BluetoothSocket socket, int target_number) {
            // 통신을 위한 bluetoothSocket 객체를 받는다.
            this.socket = socket;
            this.target_number = target_number;
            try {
                // bluetootsocket객체어서 OutputStream을 생성한다.
                os = socket.getOutputStream();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            Looper.prepare();
            // 메시지를 받으면, 처리하는 핸들러
            write1Handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    try {
                        // 주어진 데이터를 OutputStream에 전달하여 상대측에 송신한다.
                        os.write(((String)msg.obj).getBytes());
                        os.flush();

                        // 전송한 데이터를 MessageActivity안의 TextView에 출력하기 위해 메시지를 전달한다.
                        Message msg_to_acti = new Message();
                        msg_to_acti.what = SEND_1_MESSAGE;
                        msg_to_acti.obj = msg.obj;
                        msgHandler.sendMessage(msg_to_acti);
                    } catch (Exception e) {
                        e.printStackTrace();
                        write1Handler.getLooper().quit();
                    }
                }
            };



            Looper.loop();
        }
    }

    // 데이터를 아두이노에게 송신하는 스레드 2
    class Write2Thread extends Thread {
        BluetoothSocket socket;
        OutputStream os = null;
        int target_number;

        public Write2Thread(BluetoothSocket socket, int target_number) {
            // 통신을 위한 bluetoothSocket 객체를 받는다.
            this.socket = socket;
            this.target_number = target_number;
            try {
                // bluetootsocket객체어서 OutputStream을 생성한다.
                os = socket.getOutputStream();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            Looper.prepare();
            // 메시지를 받으면, 처리하는 핸들러

            write2Handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    try {
                        // 주어진 데이터를 OutputStream에 전달하여 상대측에 송신한다.
                        os.write(((String)msg.obj).getBytes());
                        os.flush();

                        // 전송한 데이터를 MessageActivity안의 TextView에 출력하기 위해 메시지를 전달한다.
                        Message msg_to_acti = new Message();
                        msg_to_acti.what = SEND_2_MESSAGE;
                        msg_to_acti.obj = msg.obj;
                        msgHandler.sendMessage(msg_to_acti);
                    } catch (Exception e) {
                        e.printStackTrace();
                        write2Handler.getLooper().quit();
                    }
                }
            };

            Looper.loop();
        }
    }

    // 아두이노의 시리얼로 부터 메시지를 읽는 동작하는 스레드(스레드 1개로 구성)
    class ReadThread extends Thread {
        BluetoothSocket socket;
        BufferedInputStream bis = null;
        int target_number;
        boolean bRead;

        public ReadThread(BluetoothSocket socket, int target_number) {
            this.socket = socket;
            this.target_number = target_number;
            try {
                // bluetoothSocket에서 bufferedInputStream을 생성한다.
                bis = new BufferedInputStream(
                        socket.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            if(target_number == 1) {
                bRead = bTarget1Read;
            } else if(target_number == 2) {
                bRead = bTarget2Read;
            }

            while(!Thread.currentThread().isInterrupted() && bRead) {

                try {
                    // 데이터를 임시로 저장할 버퍼를 만든다.
                    byte[] buf = new byte[1024];
                    // 버퍼에 데이터를 읽어온다.
                    int bytes = bis.read(buf);
                    // 읽어온 문자열 형태로 저장한다.
                    String read_str = new String(buf, 0, bytes);

                    // 읽어온 MessageActivity 안의 listview에 적용하기 위해 헨들러에 메시지를 전달한다
                    Message msg = new Message();
                    if(target_number == 1) {
                        msg.what = RECEIVED_1_MESSAGE;
                    } else if(target_number == 2) {
                        msg.what = RECEIVED_2_MESSAGE;
                    }
                    msg.obj = read_str;
                    msgHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    bRead = false;
                }
            }
        }
    }

    // 송수신된 메시지를 화면에 TextView에 출력하기 위한 Handler
    Handler msgHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what) {
                case SEND_1_MESSAGE:
                    sb.setLength(0);
                    sb.append("send 1 > "+(String)msg.obj);
                    tv_blue1.setText(sb);
                    break;
                case RECEIVED_1_MESSAGE:
                    sb.setLength(0);
                    sb.append("receive 1 > " + (String)msg.obj);
                    tv_blue1.setText(sb);
                    break;
                case SEND_2_MESSAGE:
                    sb.setLength(0);
                    sb.append("send 2 > "+(String)msg.obj);
                    tv_blue2.setText(sb);
                    break;
                case RECEIVED_2_MESSAGE:
                    sb.setLength(0);
                    sb.append("receive 2 > " + (String)msg.obj);
                    tv_blue2.setText(sb);
                    break;
            }
        }
    };

    // 연결 확립후 버튼을 누르면 명령(a, b)를 전송하는 리스너
    class BtnListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {


            switch(view.getId()) {
                case R.id.btn_blue1On:
                    if(write1Handler != null) {
                        Message msg = new Message();
                        msg.obj = "a";
                        write1Handler.sendMessage(msg);
                    }
                    break;
                case R.id.btn_blue1Off:
                    if(write1Handler != null) {
                        Message msg = new Message();
                        msg.obj = "b";
                        write1Handler.sendMessage(msg);
                    }
                    break;
                case R.id.btn_blue2On:
                    if(write2Handler != null) {
                        Message msg = new Message();
                        msg.obj = "a";
                        write2Handler.sendMessage(msg);
                    }
                    break;
                case R.id.btn_blue2Off:
                    if(write2Handler != null) {
                        Message msg = new Message();
                        msg.obj = "b";
                        write2Handler.sendMessage(msg);
                    }
                    break;
            }
        }
    }

    private void setViews() {
        btn_blue1On = (Button)findViewById(R.id.btn_blue1On);
        btn_blue1Off = (Button)findViewById(R.id.btn_blue1Off);
        btn_blue2On = (Button)findViewById(R.id.btn_blue2On);
        btn_blue2Off = (Button)findViewById(R.id.btn_blue2Off);

        lv_device = (ListView)findViewById(R.id.lv_device);
        tv_blue1 = (TextView)findViewById(R.id.tv_blue1);
        tv_blue2 = (TextView)findViewById(R.id.tv_blue2);

        BtnListener btnListener = new BtnListener();

        btn_blue1On.setOnClickListener(btnListener);
        btn_blue1Off.setOnClickListener(btnListener);
        btn_blue2On.setOnClickListener(btnListener);
        btn_blue2Off.setOnClickListener(btnListener);

    }


    private void setPermission(String[] perm) {
        boolean bPerm = false;

        if(ContextCompat.checkSelfPermission(getApplicationContext(), perm[0])
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), perm[1])
                        == PackageManager.PERMISSION_GRANTED) {
            bPerm = true;
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
            if(bPerm) {

            }
        }
        this.bPerm = bPerm;
    }
}
