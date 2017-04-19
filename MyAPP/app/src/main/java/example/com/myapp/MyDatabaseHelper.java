package example.com.myapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yangjieqiong on 2017/4/10.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private String create_table="create table BlackList("+
            "id integer primary key autoincrement," +
            "number text unique)";
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_table);
    }

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version) {
        super(context,name,factory,version);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
