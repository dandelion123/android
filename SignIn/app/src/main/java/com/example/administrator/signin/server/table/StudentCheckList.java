package com.example.administrator.signin.server.table;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/5/7 0007.
 */

public class StudentCheckList extends BmobObject {
    private String CourseID;
    private String StudentID;
    private String TeacherID;

    public void setCourseID(String courseID) {
        CourseID = courseID;
    }

    public void setStudentID(String studenID) {
        StudentID = studenID;
    }

    public String getCourseID() {
        return CourseID;
    }

    public String getStudentID() {
        return StudentID;
    }


}
