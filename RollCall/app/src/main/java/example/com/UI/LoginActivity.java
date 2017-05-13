package example.com.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import example.com.rollcall.R;
import example.com.server.table.Teacher;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editText_name;
    private EditText editText_pwd;
    private Button button_login;
    private CheckBox checkBox_issave;
    private TextView textView_register;
    private TextView textView_exit;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editText_name = (EditText) findViewById(R.id.et_name);
        editText_pwd = (EditText) findViewById(R.id.et_pwd);
        button_login = (Button) findViewById(R.id.bt_login);
        checkBox_issave = (CheckBox) findViewById(R.id.ch_save);
        textView_register=(TextView)findViewById(R.id.tv_register);
        textView_exit=(TextView)findViewById(R.id.tv_exit);
        textView_exit.setOnClickListener(this);
        textView_register.setOnClickListener(this);
        button_login.setOnClickListener(this);
        boolean issave = sharedPreferences.getBoolean("save", false);
        if (issave) {
            String name = sharedPreferences.getString("name", "");
            editText_name.setText(name);
            String pwd = sharedPreferences.getString("pwd", "");
            editText_pwd.setText(pwd);
            checkBox_issave.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                final String name_str = editText_name.getText().toString();
                final String pwd_str = editText_pwd.getText().toString();
                if (TextUtils.isEmpty(name_str) || TextUtils.isEmpty(pwd_str)) {
                    Toast.makeText(LoginActivity.this, "用户名、密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                BmobUser bmobUser = new BmobUser();
                bmobUser.setUsername(name_str);
                bmobUser.setPassword(pwd_str);
                /*************登陆判断***********/
                bmobUser.login(new SaveListener<BmobUser>() {
                    @Override
                    public void done(BmobUser bmobUser, BmobException e) {
                        if (e == null) {
                            editor = sharedPreferences.edit();
                            if (checkBox_issave.isChecked()) {
                                editor.putBoolean("save", true);
                                editor.putString("name", name_str);
                                editor.putString("pwd", pwd_str);
                            } else {
                                editor.clear();
                            }
                            editor.commit();
                            Intent intent = new Intent(LoginActivity.this, CurriculumActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            editText_name.setText("");
                            editText_pwd.setText("");
                            // TODO: 2017/5/4 0004 登录失败判断给出正确的提示
                            Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                            Log.e("LoginActivity",e.toString());
                        }
                    }
                });
                break;
            case R.id.tv_register:
                Intent intent_Login = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent_Login);
                break;
            case R.id.tv_exit:
                finish();
                break;
            default:
                break;
        }
    }
}