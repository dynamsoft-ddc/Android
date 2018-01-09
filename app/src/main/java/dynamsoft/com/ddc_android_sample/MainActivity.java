package dynamsoft.com.ddc_android_sample;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.dynamsoft.restful.ocr.Program;

import java.io.PrintStream;

public class MainActivity extends AppCompatActivity {

    TextView consoleView;
    Handler consoleViewHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            String str = (String)msg.obj;
            if(null != str){
                consoleView.append(str);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        consoleView = (TextView)findViewById(R.id.consoleView);
    }
    @Override
    protected void onResume(){
        super.onResume();
        new Thread(){
            public void run(){
                PrintStream consoleViewOut = new PrintStream(System.out){
                    @Override
                    public void println(String str){
                        super.println(str);
                        Message message = new Message();
                        if(null != str){
                            message.obj = str + "\n";
                        }else{
                            message.obj = "\n";
                        }
                        consoleViewHandler.sendMessage(message);
                    }
                };
                PrintStream oriOut = System.out;
                System.setOut(consoleViewOut);
                Program.main(null);
                System.setOut(oriOut);
            }
        }.start();
    }
}
