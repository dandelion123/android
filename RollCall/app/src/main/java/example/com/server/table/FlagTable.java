package example.com.server.table;

import cn.bmob.v3.BmobObject;

/**
 * Created by yangjieqiong on 2017/4/24.
 */

public class FlagTable extends BmobObject {
    private String T_ID;
    private String C_ID;
    private boolean Flag=false;

    public void setT_ID(String t_ID) {
        T_ID = t_ID;
    }

    public String getT_ID() {
        return this.T_ID;
    }
    public void setC_ID(String c_ID) {
        this.C_ID = c_ID;
    }

    public String getC_ID() {
        return this.C_ID;
    }

    public void setFlag(boolean flag) {
        this.Flag = flag;
    }

    public boolean isFlag() {
        return this.Flag;
    }
}
