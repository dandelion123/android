package example.com.myapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView_contact;
    private ArrayAdapter<String> adapter;
    List<String> list = new ArrayList<String>();
    private EditText editText;
    private Button button_call;
    private static final String Tag="MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(Tag,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView_contact = (ListView) findViewById(R.id.lv_contact);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView_contact.setAdapter(adapter);
        editText = (EditText) findViewById(R.id.et_phone);
        button_call = (Button) findViewById(R.id.bt_call);
        button_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = editText.getText().toString();
                if (!(str.equals(""))) {
                    call(str);
                } else {
                    Toast.makeText(MainActivity.this, "请输入电话号码", Toast.LENGTH_SHORT).show();
                }
            }
        });
        readContact();
        final String[] items = new String[]{"拨打电话", "添加该号码到黑名单"};
        listView_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String string = list.get(position).toString();//获得点击处字符串
                final String A[] = string.split("\n");
                Log.d("MainActivity", A[0]);
                Log.d("MainActivity", A[1]);
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).setTitle("请选择").setIcon(R.mipmap.ic_launcher)
                        .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (items[which].equals("拨打电话")) {
                                    call(A[1]);
                                } else if (items[which].equals("添加该号码到黑名单")) {
                                    addToBlackList(A[1]);
                                }
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        });
    }

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

    private void call(String str_number) {
//        Intent intent =new Intent("android.intent.action.CALL", Uri.parse("tel:10086"));
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + str_number));
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addToBlackList(String str) {
        str = str.replaceAll(" ", "");
        Log.d("MainActivity", str);
        Uri uri = Uri.parse("content://example.com.myapp/BlackList");
        ContentResolver contentResolver = getContentResolver();
        String where = "number = ?";//条件查询
        String[] number_args = {str};
        Cursor cursor = contentResolver.query(uri, null, where, number_args, null);
        if (cursor.moveToNext()) {
            Toast.makeText(MainActivity.this, "该号码已在黑名单中存在", Toast.LENGTH_SHORT).show();
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("number", str);
            Uri newUri = getContentResolver().insert(uri, contentValues);
            String newId = newUri.getPathSegments().get(1);
            Log.d("MainActivity", newId);
            Toast.makeText(MainActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
        }
    }

    private void readContact() {
        Cursor cursor = null;

        try {
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                list.add(name + '\n' + number);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }

    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent MyServiceIntent = new Intent(MainActivity.this, MyService.class);  // 要启动的服务
        switch (item.getItemId()) {
            case R.id.item_blacklist:
                Intent intent = new Intent(MainActivity.this, BlackListActivity.class);
                startActivity(intent);
                break;
            case R.id.stop_service:
                stopService(MyServiceIntent);
                break;
            case R.id.start_service:
                startService(MyServiceIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
