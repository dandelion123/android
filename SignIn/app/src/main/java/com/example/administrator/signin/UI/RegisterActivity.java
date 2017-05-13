package com.example.administrator.signin.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.signin.R;
import com.example.administrator.signin.server.table.Student;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity {
    private EditText editText_r_account;
    private EditText editText_r_pwd;
    private EditText editText_r_name;
    private Button button_register;
    private final static String tag = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editText_r_account = (EditText) findViewById(R.id.et_r_account);
        editText_r_pwd = (EditText) findViewById(R.id.et_r_pwd);
        editText_r_name = (EditText) findViewById(R.id.et_r_name);
        button_register = (Button) findViewById(R.id.bt_register);
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String account_str = editText_r_account.getText().toString();
                final String name_str = editText_r_name.getText().toString();
                final String pwd_str = editText_r_pwd.getText().toString();
                if (TextUtils.isEmpty(account_str) || TextUtils.isEmpty(pwd_str) || TextUtils.isEmpty(name_str)) {
                    Toast.makeText(RegisterActivity.this, "用户名、密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                Student student = new Student();
                student.setUsername(account_str);
                student.setPassword(pwd_str);
                student.setName(name_str);
//                Log.d(tag,"longitude"+longitude+"\nlatitude"+latitude);
//                BmobGeoPoint gpsAdd = new BmobGeoPoint(longitude, latitude);
//                teacher.setGpsAdd(gpsAdd);
                student.setFlag("S");//表明这是学生。
                student.signUp(new SaveListener<Student>() {
                    @Override
                    public void done(Student s, BmobException e) {
                        if (e == null) {
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // TODO: 2017/5/7 0007 告诉用户失败原因 
                            Toast.makeText(RegisterActivity.this, "注册失败，请重新注册", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
