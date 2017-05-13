package example.com.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import example.com.rollcall.R;
import example.com.server.table.FlagTable;
import example.com.server.table.Teacher;

public class RollCallActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button_start;
    private Button button_stop;
    private Button button_result;
    private static final String tag = "RollCallActivity";

    private LocationClient locationClient = null;
    public BDLocationListener myListener = new MyBdlocationListener();
    private double longitude = 0.0;
    private double latitude = 0.0;
    private String ObjectID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_call);
        button_start = (Button) findViewById(R.id.bt_start);
        button_stop = (Button) findViewById(R.id.bt_stop);
        button_start.setOnClickListener(this);
        button_stop.setOnClickListener(this);
        button_result = (Button) findViewById(R.id.bt_result);
        button_result.setOnClickListener(this);
        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(myListener);
        initLocation();//初始化LocationgClient
        locationClient.start();
        Intent intent = getIntent();
        ObjectID = intent.getStringExtra("ObjectID");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_start://开始点名响应函数
                /**选择自己在点名的课程*/
                // TODO: 2017/4/19  先通过用户名更新位置信息
                final BmobGeoPoint gpsAdd = new BmobGeoPoint(longitude, latitude);
                Teacher userInfo = BmobUser.getCurrentUser(Teacher.class);
                userInfo.setGpsAdd(gpsAdd);
                userInfo.update(userInfo.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.d(tag, "经度：" + longitude);
                            Log.d(tag, "纬度：" + latitude);
                            Toast.makeText(RollCallActivity.this, "上传地址成功\n"+"经度：" + longitude+"纬度：" + latitude, Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(tag, e.getMessage());
                        }
                    }
                });

                FlagTable flag = new FlagTable();
                flag.setFlag(true);
                Log.d(tag, ObjectID);
                flag.update(ObjectID, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(RollCallActivity.this, "学生可以签到", Toast.LENGTH_SHORT).show();
                            Log.d(tag, "开始点名");
                        } else {
                            Toast.makeText(RollCallActivity.this, "失败，学生无法签到", Toast.LENGTH_SHORT).show();
                            Log.d(tag, e.getMessage());
                        }
                    }
                });
                break;
            case R.id.bt_stop://停止点名响应函数
                //// TODO: 2017/4/19  需要用户名和课程ID号
                FlagTable flag1 = new FlagTable();
                flag1.setFlag(false);
                Log.d(tag, ObjectID);
                flag1.update(ObjectID, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(RollCallActivity.this, "学生现在无法签到", Toast.LENGTH_SHORT).show();
                            Log.d(tag, "开始点名");
                        } else {
                            Toast.makeText(RollCallActivity.this, "停止失败，学生依然可以签到", Toast.LENGTH_SHORT).show();
                            Log.d(tag, e.getMessage());
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


    public class MyBdlocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //positionTextView.setText(location.getAddrStr()+location.getLatitude()+"  "+location.getLongitude());
            Log.d(tag, "location.getLatitude():" + location.getLatitude() + "\nlocation.getLongitude()" + location.getLongitude());
            if (latitude != 4.9E-324 && longitude != 4.9E-324) {
                locationClient.stop();
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Log.d(tag, "longitude" + longitude + "\nlatitude" + latitude);
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
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
