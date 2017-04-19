package example.com.rollcall;

import cn.bmob.v3.BmobObject;

/**
 * Created by yangjieqiong on 2017/4/19.
 */

public class Teacher extends BmobObject {
    private String T_ID;
    private String pwd;

    public void setT_ID(String ID) {
        this.T_ID = ID;
    }

    public String getT_ID() {
        return T_ID;
    }

    public void setPwd(String password) {
        this.pwd = password;
    }

    public String getPwd() {
        return pwd;
    }
}
