package com.example.administrator.signin.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.administrator.signin.CoursePackge.CourseTableView;
import com.example.administrator.signin.R;
import com.example.administrator.signin.server.table.Course;
import com.example.administrator.signin.server.table.Student;
import com.example.administrator.signin.server.table.StudentCheckList;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class CurriculumActivity extends AppCompatActivity {
    private CourseTableView courseTableView;
    private static final String tag = "CurriculumActivity";
    private Student userInfo;
    private LocationClient locationClient = null;
    public BDLocationListener myListener = new MyBdlocationListener();
    private double longitude = 0.0;
    private double latitude = 0.0;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "e7eacece0f855e4049efbd1013fd2f17");
        userInfo = BmobUser.getCurrentUser(Student.class);
        if (userInfo == null) {
            Log.d(tag, "userInfo == null");
            Intent intent = new Intent(CurriculumActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        setContentView(R.layout.activity_curriculum);

        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(myListener);
        initLocation();//初始化LocationgClient
        locationClient.start();

        courseTableView = (CourseTableView) findViewById(R.id.ctv);
        flash();
        courseTableView.setOnCourseItemClickListener(new CourseTableView.OnCourseItemClickListener() {
            @Override
            public void onCourseItemClick(TextView tv, int jieci, int day, final String des, final String CourseID, final String ObjectID) {
                Log.d(tag, jieci + des + day + CourseID);
                //TODO: 2017/4/21
                final String[] items = new String[]{"签到", "删除该课程"};
                AlertDialog dialog = new AlertDialog.Builder(CurriculumActivity.this).setTitle("请选择功能").setIcon(R.mipmap.ic_launcher)
                        .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (items[which].equals("签到")) {

                                    final List<String> listTeacherName = new ArrayList<String>();//保存查询到的教师姓名
                                    final List<String> listTeacherUserName = new ArrayList<String>();//保存查询到的教师姓名

                                    final BmobGeoPoint gpsAdd = new BmobGeoPoint(longitude, latitude);
                                    BmobQuery bmobQuery = new BmobQuery();
                                    Log.d(tag, "经度：" + longitude);
                                    Log.d(tag, "纬度：" + latitude);
                                    bmobQuery.addWhereEqualTo("Flag", "T");//接近用户地点50米以内的老师
                                    bmobQuery.addWhereWithinKilometers("gpsAdd", gpsAdd, 0.05);

                                    bmobQuery.findObjects(new FindListener<Student>() {
                                        @Override
                                        public void done(List<Student> list, BmobException e) {
                                            if (e == null) {
                                                for (Student str : list) {
                                                    Log.d(tag, "距离" + gpsAdd.distanceInMilesTo(str.getGpsAdd()));
                                                    Log.d(tag, "名字" + str.getName() + "用户名" + str.getUsername());
                                                    listTeacherName.add(str.getName());
                                                    listTeacherUserName.add(str.getUsername());
                                                }
                                                // TODO: 2017/5/7 0007 选择教师姓名,将教师ID与课程号一起带到下一个activity

                                            } else {
                                                Log.d(tag, "查询出错");
                                            }
                                        }
                                    });
                                } else if (items[which].equals("删除该课程")) {
                                    // TODO: 2017/5/7 0007 从对照表删掉自己的课程信息

                                }
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        });
    }

    /**
     * 刷新界面
     */

    private void flash() {
        String S_ID = userInfo.getUsername();
        Log.d(tag, "flash: userInfo.getUsername" + S_ID);
        BmobQuery<StudentCheckList> q1 = new BmobQuery<StudentCheckList>();
        q1.addWhereEqualTo("StudentID", S_ID);
        final List<String> lista = new ArrayList<String>();
        q1.findObjects(new FindListener<StudentCheckList>() {
            @Override
            public void done(List<StudentCheckList> list, BmobException e) {
                if (e == null) {
                    Log.d(tag, "done: " + list + lista);
                    for (StudentCheckList checkList : list) {
                        lista.add(checkList.getCourseID());
                        Log.d(tag, "done: flagTable.getC_ID" + checkList.getCourseID());
                    }
                    for (String str : lista) {
                        Log.d(tag, "done  进入for");
                        Log.d(tag, "done  STR" + str);
                    }
                    BmobQuery<Course> query = new BmobQuery<Course>();
                    query.addWhereContainedIn("CourseID", lista);
                    query.findObjects(new FindListener<Course>() {
                        @Override
                        public void done(List<Course> list, BmobException e) {
                            if (e == null) {
                                for (Course course : list) {
                                    Log.d(tag, "地点Course course : list" + course.getPlace());
                                }
                                courseTableView.updateCourseViews(list);
                            } else {
                                Log.e(tag, "done: 出错", e);
                                // TODO: 2017/4/24 添加查询错误之后所做事件
                            }
                        }
                    });
                } else {
                    // TODO: 2017/4/24 添加查询错误之后所做事件
                    Log.e(tag, "done: 出错", e);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //// TODO: 2017/4/21
        switch (item.getItemId()) {
            case R.id.add_course:
                Intent intent = new Intent(CurriculumActivity.this, AddCourseActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.quit_item:
                BmobUser.logOut();
                Intent intent_Login = new Intent(CurriculumActivity.this, LoginActivity.class);
                startActivity(intent_Login);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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
