package raejin.mybrowser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText editText_url;
    WebView webView_main;
    Button button_back, button_forward, button_go;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);

        editText_url = (EditText)findViewById(R.id.editText_url);
        webView_main = (WebView)findViewById(R.id.webView_main);
        button_back = (Button)findViewById(R.id.button_back);
        button_forward = (Button)findViewById(R.id.button_forward);
        button_go = (Button)findViewById(R.id.button_go);

        final WebSettings webSettings = webView_main.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView_main.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        button_forward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                webView_main.goForward();
            }
        });

        button_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                webView_main.goBack();
            }
        });

        button_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView_main.loadUrl(editText_url.getText().toString());
            }
        });

    }

    class webListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.button_forward:
                    webView_main.goForward();
                    break;
                case R.id.button_back:
                    webView_main.goBack();
                    break;
                case R.id.button_go:
                    webView_main.loadUrl(editText_url.getText().toString());
                    break;
            }
        }
    }

}
