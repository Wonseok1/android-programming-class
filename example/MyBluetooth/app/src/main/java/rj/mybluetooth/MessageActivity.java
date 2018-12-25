package rj.mybluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.OutputStream;

public class MessageActivity extends AppCompatActivity {
    TextView tv_log;
    Button btn_send;
    EditText et_msg;
    BluetoothSocket connSocket;
    boolean bRead = true;
    Handler writeHandler;

    MsgThread msgThread;
    WriteThread writeThread;
    ReadThread readThread;
    StringBuffer sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        tv_log = (TextView)findViewById(R.id.tv_log);
        btn_send = (Button)findViewById(R.id.btn_send);
        et_msg = (EditText)findViewById(R.id.et_msg);

        connSocket = MainActivity.connSocket;

        sb = new StringBuffer();

        msgThread = new MsgThread();
        msgThread.start();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String send_msg = et_msg.getText().toString() + "\n";
                if(writeHandler != null) {
                    Message msg = new Message();
                    msg.obj = send_msg;
                    writeHandler.sendMessage(msg);
                }
            }
        });
    }

    class MsgThread extends Thread {

        @Override
        public void run() {
            try {
                if(readThread != null) {
                    bRead = false;
                }
                readThread = new ReadThread(connSocket);
                readThread.start();

                if(writeThread != null) {
                    writeHandler.getLooper().quit();
                }
                writeThread = new WriteThread(connSocket);
                writeThread.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    Handler msgHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what) {
                case 100:
                    sb.append("me > "+(String)msg.obj);
                    tv_log.setText(sb);
                    break;
                case 200:
                    sb.append("you > " + (String)msg.obj);
                    tv_log.setText(sb);
                    break;
            }
        }
    };

    class WriteThread extends Thread {
        BluetoothSocket socket;
        OutputStream os = null;

        public WriteThread(BluetoothSocket socket) {
            this.socket = socket;
            try {
                os = socket.getOutputStream();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        public void run() {
            Looper.prepare();
            writeHandler = new Handler() {

                @Override
                public void handleMessage(Message msg) {
                    try {
                        os.write(((String)msg.obj).getBytes());
                        os.flush();

                        Message msg_to_acti = new Message();
                        msg_to_acti.what = 200;
                        msg_to_acti.obj = msg.obj;
                        msgHandler.sendMessage(msg_to_acti);
                    } catch (Exception e) {
                        e.printStackTrace();
                        writeHandler.getLooper().quit();
                    }
                }
            };
            Looper.loop();
        }
    }

    class ReadThread extends Thread {
        BluetoothSocket socket;
        BufferedInputStream bis = null;

        public ReadThread(BluetoothSocket socket) {
            this.socket = socket;
            try {
                bis = new BufferedInputStream(
                        socket.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while(bRead) {
                try {
                    byte[] buf = new byte[1024];
                    int bytes = bis.read(buf);
                    String read_str = new String(buf, 0, bytes);

                    Message msg = new Message();
                    msg.what = 100;
                    msg.obj = read_str;
                    msgHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    bRead = false;
                }
            }
        }
    }
}
