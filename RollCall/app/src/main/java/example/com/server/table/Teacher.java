package example.com.server.table;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * Created by yangjieqiong on 2017/4/19.
 */

public class Teacher extends BmobUser {
    private String Flag;
    private String name;

    private BmobGeoPoint gpsAdd;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BmobGeoPoint getGpsAdd() {
        return gpsAdd;
    }

    public void setGpsAdd(BmobGeoPoint gpsAdd) {
        this.gpsAdd = gpsAdd;
    }

    public String getFlag() {
        return this.Flag;
    }

    public void setFlag(String ID) {
        this.Flag = ID;
    }
}
