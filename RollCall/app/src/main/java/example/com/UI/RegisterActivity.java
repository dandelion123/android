package example.com.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import example.com.rollcall.R;
import example.com.server.table.Teacher;

public class RegisterActivity extends AppCompatActivity {
    private EditText editText_r_account;
    private EditText editText_r_pwd;
    private EditText editText_r_name;
    private Button button_register;
    private final static String tag = "RegisterActivity";
    private LocationClient locationClient = null;
    public BDLocationListener myListener = new MyBdlocationListener();
    private double longitude = 0.0;
    private double latitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editText_r_account = (EditText) findViewById(R.id.et_r_account);
        editText_r_pwd = (EditText) findViewById(R.id.et_r_pwd);
        editText_r_name = (EditText) findViewById(R.id.et_r_name);
        button_register = (Button) findViewById(R.id.bt_register);
        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(myListener);
        initLocation();//初始化LocationgClient
        locationClient.start();
        Toast.makeText(RegisterActivity.this, "longitude:经度"+longitude+"latitude:纬度"+latitude, Toast.LENGTH_SHORT).show();
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
                Teacher teacher = new Teacher();
                teacher.setUsername(account_str);
                teacher.setPassword(pwd_str);
                teacher.setName(name_str);
                Log.d(tag,"longitude"+longitude+"\nlatitude"+latitude);
                BmobGeoPoint gpsAdd = new BmobGeoPoint(longitude, latitude);
                teacher.setGpsAdd(gpsAdd);
                teacher.setFlag("T");//表明这是老师。
                teacher.signUp(new SaveListener<Teacher>() {
                    @Override
                    public void done(Teacher s, BmobException e) {
                        if (e == null) {
                            Toast.makeText(RegisterActivity.this, "注册成功"+longitude+"\n"+latitude, Toast.LENGTH_SHORT).show();
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

    // TODO: 2017/5/4 0004 注册时可以不用获取经纬度

    public class MyBdlocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //positionTextView.setText(location.getAddrStr()+location.getLatitude()+"  "+location.getLongitude());
            Log.d(tag,"location.getLatitude():"+location.getLatitude()+"\nlocation.getLongitude()"+location.getLongitude());
            latitude=location.getLatitude();
            longitude=location.getLongitude();
            Log.d(tag,"longitude"+longitude+"\nlatitude"+latitude);
            if (latitude!=4.9E-324&&longitude!=4.9E-324){
                locationClient.stop();
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        locationClient.setLocOption(option);
    }
}
