package example.com.UI;

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

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import example.com.CoursePackge.CourseTableView;
import example.com.rollcall.R;
import example.com.server.table.Course;
import example.com.server.table.FlagTable;
import example.com.server.table.Teacher;
import rx.Subscription;

public class CurriculumActivity extends AppCompatActivity {
    private CourseTableView courseTableView;
    private static final String tag = "CurriculumActivity";
    private Teacher userInfo;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "e7eacece0f855e4049efbd1013fd2f17");
        userInfo  = BmobUser.getCurrentUser(Teacher.class);
        if (userInfo == null) {
            Log.d(tag,"userInfo == null");
            Intent intent = new Intent(CurriculumActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        setContentView(R.layout.activity_curriculum);
        courseTableView = (CourseTableView) findViewById(R.id.ctv);
        flash();
        courseTableView.setOnCourseItemClickListener(new CourseTableView.OnCourseItemClickListener() {
            @Override
            public void onCourseItemClick(TextView tv, int jieci, int day, final String des, final String CourseID, final String ObjectID) {
                Log.d(tag, jieci + des + day + CourseID);
                //TODO: 2017/4/21
                final String[] items = new String[]{"点名", "删除该课程"};
                AlertDialog dialog = new AlertDialog.Builder(CurriculumActivity.this).setTitle("请选择功能").setIcon(R.mipmap.ic_launcher)
                        .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (items[which].equals("点名")) {
                                    BmobQuery<FlagTable> bmobQuery1 = new BmobQuery<FlagTable>();
                                    bmobQuery1.addWhereEqualTo("T_ID", userInfo.getName());
                                    BmobQuery<FlagTable> bmobQuery2 = new BmobQuery<FlagTable>();
                                    bmobQuery2.addWhereEqualTo("C_ID", CourseID);
                                    List<BmobQuery<FlagTable>> andQuerys = new ArrayList<BmobQuery<FlagTable>>();
                                    andQuerys.add(bmobQuery1);
                                    andQuerys.add(bmobQuery2);
                                    BmobQuery<FlagTable> query = new BmobQuery<FlagTable>();
                                    query.and(andQuerys);
                                    query.findObjects(new FindListener<FlagTable>() {
                                        @Override
                                        public void done(List<FlagTable> object, BmobException e) {
                                            if (e == null) {
                                                Intent intent = new Intent(CurriculumActivity.this, RollCallActivity.class);
                                                Log.d(tag, object.get(0).getObjectId());
                                                intent.putExtra("ObjectID", object.get(0).getObjectId());
                                                startActivity(intent);
                                            } else {
                                                Log.e(tag, "失败：" + e.getMessage() + "," + e.getErrorCode());
                                            }
                                        }
                                    });

                                } else if (items[which].equals("删除该课程")) {
                                    // TODO: 2017/5/7 0007 删除方式有误
                                    Course course = new Course();
                                    course.delete(ObjectID, new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                Toast.makeText(CurriculumActivity.this, "课程" + des + "删除成功", Toast.LENGTH_SHORT).show();
                                                Log.d(tag, "成功");
                                                Intent intent = new Intent(CurriculumActivity.this, CurriculumActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Log.e(tag, "失败：" + e.getMessage() + "," + e.getErrorCode());
                                            }
                                        }
                                    });
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
        String T_ID = userInfo.getUsername();
        Log.d(tag, "flash: userInfo.getUsername"+T_ID);
        BmobQuery<FlagTable> q1 = new BmobQuery<FlagTable>();
        q1.addWhereEqualTo("T_ID", T_ID);
        final List<String> lista = new ArrayList<String>();
        q1.findObjects(new FindListener<FlagTable>() {
            @Override
            public void done(List<FlagTable> list, BmobException e) {
                if (e == null) {
                    for (FlagTable flagTable : list) {
                        lista.add(flagTable.getC_ID());
                        Log.d(tag, "done: flagTable.getC_ID"+flagTable.getC_ID());
                    }
                    for (String str : lista) {
                        Log.d(tag,"done  进入for");
                        Log.d(tag,"done  STR"+ str);
                    }
                    BmobQuery<Course> query = new BmobQuery<Course>();
                    query.addWhereContainedIn("CourseID",lista);
                    query.findObjects(new FindListener<Course>() {
                        @Override
                        public void done(List<Course> list, BmobException e) {
                            if (e == null) {
                                for (Course course : list) {
                                    Log.d(tag, "地点Course course : list" + course.getPlace());
                                }
                                courseTableView.updateCourseViews(list);
                            } else {
                                Log.e(tag, "done: 出错",e);
                                // TODO: 2017/4/24 添加查询错误之后所做事件
                            }
                        }
                    });
                } else {
                    // TODO: 2017/4/24 添加查询错误之后所做事件
                    Log.e(tag, "done: 出错",e);
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


}
