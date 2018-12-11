package raejin.mycalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button button_clear, button_divide,
            button_multi, button_minus, button_plus,
            button_equal, button_dot, button_0,
            button_1, button_2, button_3, button_4,
            button_5, button_6, button_7, button_8,
            button_9;
    EditText editText_result;

    String num1 = "", num2 = "", operator = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_clear = (Button)findViewById(R.id.button_clear);
        button_divide = (Button)findViewById(R.id.button_divide);
        button_multi = (Button)findViewById(R.id.button_multi);
        button_minus = (Button)findViewById(R.id.button_minus);
        button_plus = (Button)findViewById(R.id.button_plus);
        button_equal = (Button)findViewById(R.id.button_equal);
        button_dot = (Button)findViewById(R.id.button_dot);
        button_0 = (Button)findViewById(R.id.button_0);
        button_1 = (Button)findViewById(R.id.button_1);
        button_2 = (Button)findViewById(R.id.button_2);
        button_3 = (Button)findViewById(R.id.button_3);
        button_4 = (Button)findViewById(R.id.button_4);
        button_5 = (Button)findViewById(R.id.button_5);
        button_6 = (Button)findViewById(R.id.button_6);
        button_7 = (Button)findViewById(R.id.button_7);
        button_8 = (Button)findViewById(R.id.button_8);
        button_9 = (Button)findViewById(R.id.button_9);

        editText_result = (EditText) findViewById(R.id.editText_result);

        NumberListener numberListener
                = new NumberListener();
        OperatorListener operatorListener
                = new OperatorListener();

        button_0.setOnClickListener(numberListener);
        button_1.setOnClickListener(numberListener);
        button_2.setOnClickListener(numberListener);
        button_3.setOnClickListener(numberListener);
        button_4.setOnClickListener(numberListener);
        button_5.setOnClickListener(numberListener);
        button_6.setOnClickListener(numberListener);
        button_7.setOnClickListener(numberListener);
        button_8.setOnClickListener(numberListener);
        button_9.setOnClickListener(numberListener);
        button_dot.setOnClickListener(numberListener);

        button_clear.setOnClickListener(operatorListener);
        button_divide.setOnClickListener(operatorListener);
        button_multi.setOnClickListener(operatorListener);
        button_minus.setOnClickListener(operatorListener);
        button_plus.setOnClickListener(operatorListener);
        button_equal.setOnClickListener(operatorListener);
    }

    class OperatorListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            switch(view.getId()) {
                case R.id.button_clear:
                    num1 = "";
                    num2 = "";
                    operator = "";
                    editText_result.setText("");
                    break;
                case R.id.button_plus:
                    num1 = editText_result.getText().toString();
                    operator = "+";
                    editText_result.setText("+");
                    break;
                case R.id.button_minus:
                    num1 = editText_result.getText().toString();
                    operator = "-";
                    editText_result.setText("-");
                    break;
                case R.id.button_multi:
                    num1 = editText_result.getText().toString();
                    operator = "*";
                    editText_result.setText("*");
                    break;
                case R.id.button_divide:
                    num1 = editText_result.getText().toString();
                    operator = "/";
                    editText_result.setText("/");
                    break;
                case R.id.button_equal:
                    num2 = editText_result.getText().toString();
                    double result = 0;
                    switch(operator) {
                        case "+":
                            result = Double.parseDouble(num1) + Double.parseDouble(num2);
                            break;
                        case "-":
                            result = Double.parseDouble(num1) - Double.parseDouble(num2);
                            break;
                        case "*":
                            result = Double.parseDouble(num1) * Double.parseDouble(num2);
                            break;
                        case "/":
                            if(!num2.equals("0")) {
                                result = Double.parseDouble(num1) / Double.parseDouble(num2);
                            }
                            break;
                    }
                    editText_result.setText(String.valueOf(result));
                    num1 = "";
                    num2 = "";
                    operator = "";
                    break;
            }
        }
    }

    class NumberListener implements View.OnClickListener {
        // 입력한 값을 임시로 저장하는 변수
        String input = "";

        @Override
        public void onClick(View view) {
            //현재 edittext에 입력된 값(문자열형)을 읽어온다.
            input = editText_result.getText().toString();

            //equals()함수는 소괄호 안의 객체를 비교
            // 같으면 참, 다르면 거짓
            // operator와 num1변수의 값이 공백이 아니면 if문 안의 내용을 수행한다
            if(!operator.equals("") && !num1.equals("")) {
                input = "";
            }
            // view 는 내가 클릭한 버튼 객체
            // view.getId() 내가 클릭한 버튼의 아이디
            switch(view.getId()) {
                case R.id.button_0:
                    input += "0";
                    break;
                case R.id.button_1:
                    input += "1";
                    break;
                case R.id.button_2:
                    input += "2";
                    break;
                case R.id.button_3:
                    input += "3";
                    break;
                case R.id.button_4:
                    input += "4";
                    break;
                case R.id.button_5:
                    input += "5";
                    break;
                case R.id.button_6:
                    input += "6";
                    break;
                case R.id.button_7:
                    input += "7";
                    break;
                case R.id.button_8:
                    input += "8";
                    break;
                case R.id.button_9:
                    input += "9";
                    break;
                case R.id.button_dot:
                    input += ".";
                    break;
            }
            // 입력한 값을 edittext에 적용하기
            editText_result.setText(input);
        }
    }
}
