package example.com.myapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class BlackListActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editText;
    private Button button;
    private ListView listView;
    private ArrayList<String> arrayList;
    private String number = null;//保存输入的电话号
    private static final String Tag="BlackListActivity";

    @Override
    protected void onResume() {
        Log.d(Tag,"onResume");
        super.onPostResume();
    }

    @Override
    protected void onPause() {
        Log.d(Tag,"onPause");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        Log.d(Tag,"onRestart");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        Log.d(Tag,"onStart");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(Tag,"onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(Tag,"onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(Tag,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list);
        button = (Button) findViewById(R.id.bt_add);
        button.setOnClickListener(this);
        editText = (EditText) findViewById(R.id.et_number);
        listView = (ListView) findViewById(R.id.lv_blacklist);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                final String string = arrayList.get(position).toString();//获得电话号码
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(BlackListActivity.this);
                alertDialog.setTitle("");
                alertDialog.setMessage("是否删除此黑名单号码");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定删除
                        Uri uri = Uri.parse("content://example.com.myapp/BlackList");
                        getContentResolver().delete(uri, "number='" + string + "'", null);
//                        Log.d("BlackListActivity", String.valueOf(newId));
                        flush();
                    }
                });
                alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {//取消删除
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }
        });
        flush();
    }

    public void flush() {
        editText.setText("");
        arrayList = new ArrayList<String>();
        Uri uri = Uri.parse("content://example.com.myapp/BlackList");
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String number = cursor.getString(cursor.getColumnIndex("number"));
                Log.d("BlackListActivity", "number name is " + number);
                arrayList.add(number);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);
        cursor.close();
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(BlackListActivity.this, "该号码已在黑名单中存在", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    flush();//更新
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add:
                number = editText.getText().toString();
                if (!number.equals("")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Uri uri = Uri.parse("content://example.com.myapp/BlackList");
                            /**
                             * 如果黑名单里面有了该电话号码，则不插入
                             * */
                            ContentResolver contentResolver = getContentResolver();
                            String where = "number = ?";//条件查询
                            String[] number_args = {number};
                            Cursor cursor = contentResolver.query(uri, null, where, number_args, null);
                            if (cursor.moveToNext()) {
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message); // 将Message对象发送出去
                                cursor.close();
                            } else {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("number", number);
                                Uri newUri = getContentResolver().insert(uri, contentValues);
                                String newId = newUri.getPathSegments().get(1);
                                Log.d("BlackListActivity", newId);
                                Message message = new Message();
                                message.what = 2;
                                handler.sendMessage(message); // 将Message对象发送出去
                            }
                        }
                    }).start();
                } else {
                    Toast.makeText(this, "请输入电话号码", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
