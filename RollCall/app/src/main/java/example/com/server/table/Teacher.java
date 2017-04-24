package example.com.server.table;

import cn.bmob.v3.BmobUser;

/**
 * Created by yangjieqiong on 2017/4/19.
 */

public class Teacher extends BmobUser {
    private String Flag;

    public void setFlag(String ID) {
        this.Flag = ID;
    }

    public String getFlag() {
        return this.Flag;
    }
}
