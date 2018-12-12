package rj.callme;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    Button btn_call;
    EditText et_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_call = (Button)findViewById(R.id.btn_call);
        et_input = (EditText)findViewById(R.id.et_input);

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numbers = "tel:" + et_input.getText().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(numbers));
                startActivity(intent);
            }
        });
    }
}
