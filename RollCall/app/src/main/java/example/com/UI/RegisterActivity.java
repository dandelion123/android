package example.com.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import example.com.rollcall.R;
import example.com.server.table.Teacher;

public class RegisterActivity extends AppCompatActivity {
    private EditText editText_r_name;
    private EditText editText_r_pwd;
    private Button button_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editText_r_name = (EditText) findViewById(R.id.et_r_account);
        editText_r_pwd = (EditText) findViewById(R.id.et_r_pwd);
        button_register = (Button) findViewById(R.id.bt_register);
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name_str = editText_r_name.getText().toString();
                final String pwd_str = editText_r_pwd.getText().toString();
                if (TextUtils.isEmpty(name_str) || TextUtils.isEmpty(pwd_str)) {
                    Toast.makeText(RegisterActivity.this, "用户名、密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                Teacher teacher = new Teacher();
                teacher.setUsername(name_str);
                teacher.setPassword(pwd_str);
                teacher.setFlag("T");
                teacher.signUp(new SaveListener<Teacher>() {
                    @Override
                    public void done(Teacher s, BmobException e) {
                        if (e == null) {
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
