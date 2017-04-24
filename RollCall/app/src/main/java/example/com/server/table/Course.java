package example.com.server.table;

/**
 * Created by yangjieqiong on 2017/4/21.
 */

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class Course extends BmobObject implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -9121734039844677432L;
    private String CourseID;//课程号
    private String CourseName;//课程名
    private int day;//上课时间//星期几
    private int Time;//第几节课
    private String Place;
    private int spanNum = 2;

    private String ClassRoomName;
    private String ClassTypeName;

    public Course(String CourseID, String CourseName, int day, int Time, String Place) {
        this.CourseID = CourseID;
        this.CourseName = CourseName;
        this.day = day;
        this.Time = Time;
        this.Place = Place;
    }

    public Course() {
    }

    public String getCourseID() {
        return this.CourseID;
    }

    public void setCourseID(String courseID) {
        this.CourseID = courseID;
    }

    public String getCourseName() {
        return this.CourseName;
    }

    public void setCourseName(String courseName) {
        this.CourseName = courseName;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getTime() {
        return Time;
    }

    public void setTime(int Time) {
        this.Time = Time;
    }

    public String getPlace() {
        return this.Place;
    }

    public void setPlace(String place) {
        this.Place = place;
    }

    public int getSpanNum() {
        return spanNum;
    }

    public void setSpanNum(int spanNum) {
        this.spanNum = spanNum;
    }

    @Override
    public String toString() {
        return "Course [id=" + CourseID + "，name=" + CourseName + ", day=" + day + ", Time=" + Time + "，Places=" + Place
                + ", spanNun=" + spanNum + "]";
    }

    public String getClassRoomName() {
        return this.ClassRoomName;
    }

    public void setClassRoomName(String classRoomName) {
        this.ClassRoomName = classRoomName;
    }

    public String getClassTypeName() {
        return this.ClassTypeName;
    }

    public void setClassTypeName(String classTypeName) {
        this.ClassTypeName = classTypeName;
    }

}
