package example.com.rollcall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button button_curriculum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_curriculum=(Button)findViewById(R.id.bt_curriculum);
        button_curriculum.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_curriculum:
                Intent intent=new Intent(MainActivity.this,CurriculumActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_rollcall:
                break;
            case R.id.bt_result:
                break;
        }
    }
}
