package example.com.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.UpdateListener;
import example.com.rollcall.R;
import example.com.server.table.FlagTable;
import example.com.server.table.Teacher;

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
        Teacher userInfo = BmobUser.getCurrentUser(Teacher.class);
        FlagTable flag = new FlagTable();
        switch (v.getId()){
            case R.id.bt_start://开始点名响应函数
                // TODO: 2017/4/19  需要用户名和课程ID号、查询objectID，再通过objectID更新
                flag.setFlag(true);
                flag.update(userInfo.getUsername(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e==null){
                            Toast.makeText(RollCallActivity.this, "学生可以签到", Toast.LENGTH_SHORT).show();
                            Log.d("RollCallActivity","开始点名");
                        }else {
                            Toast.makeText(RollCallActivity.this, "失败，学生无法签到", Toast.LENGTH_SHORT).show();
                            Log.d("RollCallActivity",e.getMessage());
                        }
                    }
                });

                break;
            case R.id.bt_stop://停止点名响应函数
                //// TODO: 2017/4/19  需要用户名和课程ID号
                flag.setFlag(false);
                flag.update(userInfo.getUsername(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e==null){
                            Toast.makeText(RollCallActivity.this, "学生无法签到", Toast.LENGTH_SHORT).show();
                            Log.d("RollCallActivity","开始点名");
                        }else {
                            Toast.makeText(RollCallActivity.this, "失败，学生依然可以签到", Toast.LENGTH_SHORT).show();
                            Log.d("RollCallActivity",e.getMessage());
                        }
                    }
                });
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
