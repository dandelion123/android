package example.com.rollcall;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RollCallActivity extends AppCompatActivity implements View.OnClickListener{
    private Button button_start;
    private Button button_stop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_call);
        button_start=(Button)findViewById(R.id.bt_start);
        button_stop=(Button)findViewById(R.id.bt_stop);
        button_start.setOnClickListener(this);
        button_stop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_start://开始点名响应函数
                new Thread(new Runnable() {//开启线程
                    @Override
                    public void run() {
                        String result=new HttpConnectToServer(1).connect();//连接服务器，并发送开始点名指令给服务器
                        Toast.makeText(RollCallActivity.this,result,Toast.LENGTH_SHORT);
                    }
                }).start();
                break;
            case R.id.bt_stop://停止点名响应函数
                new Thread(new Runnable() {
                    @Override
                    public void run() {//开启线程
                        new HttpConnectToServer(2).connect();//开启线程，并发送停止点名指令给服务器
                    }
                }).start();
                break;
            default:
                break;
        }
    }
}
