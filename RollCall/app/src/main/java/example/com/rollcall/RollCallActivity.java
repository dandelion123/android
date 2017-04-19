package example.com.rollcall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RollCallActivity extends AppCompatActivity implements View.OnClickListener{
    private Button button_start;
    private Button button_stop;
    private Button button_result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_call);
        button_start=(Button)findViewById(R.id.bt_start);
        button_stop=(Button)findViewById(R.id.bt_stop);
        button_start.setOnClickListener(this);
        button_stop.setOnClickListener(this);
        button_result=(Button)findViewById(R.id.bt_result);
        button_result.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_start://开始点名响应函数
                //// TODO: 2017/4/19
                break;
            case R.id.bt_stop://停止点名响应函数
                //// TODO: 2017/4/19
                break;
            case R.id.bt_result://查看签到结果
                Intent intent_result = new Intent(RollCallActivity.this, ResultActivity.class);
                startActivity(intent_result);
                break;
            default:
                break;
        }
    }
}
