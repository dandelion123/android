package example.com.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import example.com.rollcall.R;
import example.com.server.table.Course;
import example.com.server.table.FlagTable;
import example.com.server.table.Teacher;

public class AddCourseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText editTextID;
    private EditText editTextName;
    private EditText editTextPlace;
    private Spinner spinner_day;
    private Spinner spinner_time;
    private Button buttonAdd;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<Integer> adapter1;
    private List<String> list;
    private List<Integer> list1;
    private String ID;
    private String Name;
    private String Place;
    /**
     * 上课时间
     */
    private int day = 1;
    private int time = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        list = new ArrayList<String>();
        list.add("周一");
        list.add("周二");
        list.add("周三");
        list.add("周四");
        list.add("周五");
        list.add("周六");
        list.add("周日");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        spinner_day = (Spinner) findViewById(R.id.sp_day);
        spinner_day.setAdapter(adapter);
        spinner_day.setOnItemSelectedListener(this);
        list1 = new ArrayList<Integer>();
        for (int i = 1; i <= 12; i++) {
            list1.add(i);
        }
        adapter1 = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, list1);
        spinner_time = (Spinner) findViewById(R.id.sp_time);
        spinner_time.setAdapter(adapter1);
        spinner_time.setOnItemSelectedListener(this);

        buttonAdd = (Button) findViewById(R.id.bt_add);
        editTextID = (EditText) findViewById(R.id.et_addCourseID);
        editTextName = (EditText) findViewById(R.id.et_addCourseName);
        editTextPlace = (EditText) findViewById(R.id.et_addCoursePlace);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 判断用户输入
                ID = editTextID.getText().toString();
                Name = editTextName.getText().toString();
                Place = editTextPlace.getText().toString();
                if (ID.equals("") || Name.equals("") || Place.equals("")) {
                    Toast.makeText(AddCourseActivity.this, "请输入课程ID、课程名、上课地点", Toast.LENGTH_SHORT).show();
                    return;
                }
                final Course course = new Course();
                course.setDay(day);
                course.setTime(time);
                course.setCourseID(ID);
                course.setCourseName(Name);
                course.setPlace(Place);
                //保存到服务器
                course.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            /**向签到表添加一行数据 若失败则删除课程表的数据*/
                            insert_to_flag_table(s);
                        } else {
                            Log.e("AddCourseActivity", e.getMessage());
                        }
                    }
                });
            }
        });
    }

    /**
     * 向签到标志表插入一行
     */
    public void insert_to_flag_table(final String str) {
        Teacher userInfo = BmobUser.getCurrentUser(Teacher.class);
        FlagTable flag = new FlagTable();
        flag.setT_ID(userInfo.getUsername());
        flag.setC_ID(ID);
        flag.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Intent intent = new Intent(AddCourseActivity.this, CurriculumActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(AddCourseActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                } else {
                    //删除课程表数据
                    Log.d("AddCourseActivity", s);
                    Course course = new Course();
                    course.delete(str, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Log.e("AddCourseActivity", "成功删除课程表");
                            } else {
                                Log.e("AddCourseActivity", e.getMessage());
                            }
                        }
                    });
                    Toast.makeText(AddCourseActivity.this, "添加失败，请重试", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sp_day:
                day = position + 1;
                Log.d("AddCourseActivity", "sp_day选中" + position);
                break;
            case R.id.sp_time:
                time = position + 1;
                Log.d("AddCourseActivity", "sp_time选中" + position);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
