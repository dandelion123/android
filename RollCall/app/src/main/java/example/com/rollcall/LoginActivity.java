package example.com.rollcall;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText editText_name;
    private EditText editText_pwd;
    private Button button_login;
    private CheckBox checkBox_issave;
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
        boolean issave = sharedPreferences.getBoolean("save", false);
        if (issave) {
            String name = sharedPreferences.getString("name", "");
            editText_name.setText(name);
            String pwd = sharedPreferences.getString("pwd", "");
            editText_pwd.setText(pwd);
            checkBox_issave.setChecked(true);
        }
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_str = editText_name.getText().toString();
                String pwd_str = editText_pwd.getText().toString();
                //// TODO: 2017/4/19 连接服务器条件查询
                if (name_str.equals("admin") && pwd_str.equals("123456")) {
                    editor = sharedPreferences.edit();
                    if (checkBox_issave.isChecked()) {
                        editor.putBoolean("save", true);
                        editor.putString("name", name_str);
                        editor.putString("pwd", pwd_str);
                    } else {
                        editor.clear();
                    }
                    editor.commit();
                    Intent intent = new Intent(LoginActivity.this, RollCallActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    editText_name.setText("");
                    editText_pwd.setText("");
                    Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
