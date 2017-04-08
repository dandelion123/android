package example.com.rollcall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button_curriculum;
    private Button button_rollcall;
    private Button button_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_curriculum = (Button) findViewById(R.id.bt_curriculum);
        button_curriculum.setOnClickListener(this);
        button_rollcall=(Button)findViewById(R.id.bt_rollcall);
        button_rollcall.setOnClickListener(this);
        button_result=(Button)findViewById(R.id.bt_result);
        button_result.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_curriculum:
                Intent intent_curriculum = new Intent(MainActivity.this, CurriculumActivity.class);
                startActivity(intent_curriculum);
                break;
            case R.id.bt_rollcall:
                Intent intent_rollcall = new Intent(MainActivity.this, RollCallActivity.class);
                startActivity(intent_rollcall);
                break;
            case R.id.bt_result:
                Intent intent_result = new Intent(MainActivity.this, ResultActivity.class);
                startActivity(intent_result);
                break;
            default:
                break;
        }
    }
}
