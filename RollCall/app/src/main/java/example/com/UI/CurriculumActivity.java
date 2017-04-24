package example.com.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import example.com.CoursePackge.CourseTableView;
import example.com.rollcall.R;
import example.com.server.table.Course;

public class CurriculumActivity extends AppCompatActivity {
    private CourseTableView courseTableView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curriculum);
        courseTableView = (CourseTableView) findViewById(R.id.ctv);
        flash();
//        courseTableView.setOnCourseItemClickListener(new CourseTableView.OnCourseItemClickListener() {
//            @Override
//            public void onCourseItemClick(TextView tv, int jieci, int day, String des) {
//                Log.d("my", jieci + des + day);
                // TODO: 2017/4/21
//            }
//        });
    }
    /**刷新界面*/
    private void flash(){
        BmobQuery<Course>query=new BmobQuery<>();
        query.setLimit(50);
        query.findObjects(new FindListener<Course>() {
            @Override
            public void done(List<Course> list, BmobException e) {
                if(e==null){
                    for(Course course:list){
                        Log.d("AddCourseActivity","地点"+course.getPlace());
                    }
                    courseTableView.updateCourseViews(list);
                }else {
                    // TODO: 2017/4/24 添加查询错误之后所做事件
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
            case R.id.delete_course:
                courseTableView.clearViewsIfNeeded();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
