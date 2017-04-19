package example.com.rollcall;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.bmob.v3.Bmob;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button_curriculum;
    private Button button_RollCall;
    private Button button_Exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this, "e7eacece0f855e4049efbd1013fd2f17");
        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);
        button_curriculum = (Button) findViewById(R.id.bt_curriculum);
        button_curriculum.setOnClickListener(this);
        button_RollCall = (Button) findViewById(R.id.bt_RollCall);
        button_RollCall.setOnClickListener(this);
        button_Exit = (Button) findViewById(R.id.bt_exit);
        button_Exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_curriculum:
                Intent intent_curriculum = new Intent(MainActivity.this, CurriculumActivity.class);
                startActivity(intent_curriculum);
                break;
            case R.id.bt_RollCall:
//                Intent intent_RollCall = new Intent(MainActivity.this, RollCallActivity.class);
//                startActivity(intent_RollCall);
                Intent intent_Login = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent_Login);
                break;
            case R.id.bt_exit:
                finish();
                break;
            default:
                break;
        }
    }
}
