package rj.edittext;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    EditText et_name, et_pw, et_mail, et_time, et_number;
    TextView tv_typeof;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_name = (EditText)findViewById(R.id.et_name);
        et_pw = (EditText)findViewById(R.id.et_pw);
        et_mail = (EditText)findViewById(R.id.et_mail);
        et_time = (EditText)findViewById(R.id.et_time);
        et_number = (EditText)findViewById(R.id.et_number);
        tv_typeof = (TextView)findViewById(R.id.tv_typeof);

        et_name.addTextChangedListener(new MyTextWatchar());
    }

    class MyTextWatchar implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //Toast.makeText(MainActivity.this, "수정되기 전 : "+charSequence.toString(), Toast.LENGTH_LONG).show();
            tv_typeof.setText(tv_typeof.getText().toString() + "\n수정되기 전 : "+charSequence.toString());
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //Toast.makeText(MainActivity.this, "수정된는 중간 : "+charSequence.toString(), Toast.LENGTH_LONG).show();
            tv_typeof.setText(tv_typeof.getText().toString() + "\n수정되기 중간 : "+charSequence.toString());

        }

        @Override
        public void afterTextChanged(Editable editable) {
            //Toast.makeText(MainActivity.this, "수정되기 후 : "+editable.toString(), Toast.LENGTH_LONG).show();
            tv_typeof.setText(tv_typeof.getText().toString() + "\n수정되기 후 : "+editable.toString());

        }
    }
}
