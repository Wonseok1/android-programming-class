package rj.smswords;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText et_msg;
    TextView tv_word;
    Button btn_send, btn_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_msg = (EditText)findViewById(R.id.et_msg);
        tv_word = (TextView)findViewById(R.id.tv_words);
        btn_send = (Button)findViewById(R.id.btn_send);
        btn_close = (Button)findViewById(R.id.btn_close);

        et_msg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                tv_word.setText(editable.length() + " /  80 바이트");
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, et_msg.getText(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
