package com.example.administrator.signin.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.signin.R;
import com.example.administrator.signin.server.table.Course;
import com.example.administrator.signin.server.table.Student;
import com.example.administrator.signin.server.table.StudentCheckList;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class AddCourseActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> arrayList;
    private Button buttonAdd;
    private static final String tag = "AddCourseActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        buttonAdd = (Button) findViewById(R.id.bt_get);
        listView = (ListView) findViewById(R.id.lv_Course);
        arrayList = new ArrayList<>();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                final String string = arrayList.get(position).toString();
                Log.d(tag, string);
                String [] names = string.split("\\n");
                for (int i = 0; i < names.length; i++) {
                    Log.d(tag,names[i]);
                }
                final String c_id=names[0].substring(5);
                Log.d(tag,"Cid:"+c_id);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddCourseActivity.this);
                alertDialog.setTitle("");
                alertDialog.setMessage("添加到课程表？");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StudentCheckList studentList=new StudentCheckList();
                        studentList.setCourseID(c_id);
                        studentList.setStudentID(BmobUser.getCurrentUser(Student.class).getUsername());
                        studentList.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if(e==null){
                                    Intent intent = new Intent(AddCourseActivity.this, CurriculumActivity.class);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(AddCourseActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                                }else{
                                    // TODO: 2017/5/12 0012
                                }
                            }
                        });
                    }
                });
                alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {//取消删除
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        flush();
    }

    public void flush() {
        BmobQuery<Course> query = new BmobQuery<Course>();
        query.findObjects(new FindListener<Course>() {
            @Override
            public void done(List<Course> list, BmobException e) {
                if (e == null) {
                    for (Course course : list) {
                        Log.d(tag, course.getCourseID());
                        arrayList.add("课程ID：" + course.getCourseID() + "\n课程名:" + course.getCourseName() +
                                "\n上课时间:" + getCourseTime(course) + "\n上课地点:" + course.getPlace());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddCourseActivity.this, android.R.layout.simple_list_item_1, arrayList);
                    listView.setAdapter(adapter);
                } else {
                    // TODO: 2017/5/12 0012
                }
            }
        });
    }

    public String getCourseTime(Course course) {
        String day;
        String time;
        switch (course.getDay()) {
            case 1:
                day = "周一";
                break;
            case 2:
                day = "周二";
                break;
            case 3:
                day = "周三";
                break;
            case 4:
                day = "周四";
                break;
            case 5:
                day = "周五";
                break;
            case 6:
                day = "周六";
                break;
            case 7:
                day = "周日";
                break;
            default:
                day = null;
        }
        switch (course.getTime()) {
            case 1:
                time = "一二节";
                break;
            case 3:
                time = "三四节";
                break;
            case 5:
                time = "五六节";
                break;
            case 7:
                time = "七八节";
                break;
            default:
                time = "晚自习";
        }
        return day + time;
    }
}
