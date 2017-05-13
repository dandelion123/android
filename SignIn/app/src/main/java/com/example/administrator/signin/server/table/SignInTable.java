package com.example.administrator.signin.server.table;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/5/7 0007.
 */

public class SignInTable extends BmobObject {
    private String studentID;
    private String CourseID;
    private String T_ID;
    private String MAC;

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public void setCourseID(String courseID) {
        CourseID = courseID;
    }

    public void setT_ID(String t_ID) {
        T_ID = t_ID;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getCourseID() {
        return CourseID;
    }

    public String getT_ID() {
        return T_ID;
    }

    public String getMAC() {
        return MAC;
    }
}
