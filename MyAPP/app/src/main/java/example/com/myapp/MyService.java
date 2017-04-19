package example.com.myapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.telephony.*;

import java.lang.reflect.Method;

/**
 * Created by yangjieqiong on 2017/4/10.
 */

public class MyService extends Service {
    private TelephonyManager tm;
    private MyListener listener;
    private static final String Tag="MyService";

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(Tag,"onBind");
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(Tag,"onCreate");
        super.onCreate();
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new MyListener();
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Tag,"onStartCommand");
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext()); //获取一个Notification构造器
        Intent nfIntent = new Intent(this, MainActivity.class);
        builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher)) // 设置下拉列表中的图标(大图标)
                .setContentTitle("我是一个服务") // 设置下拉列表里的标题
                .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
                .setContentText("我正在监听通话状态") // 设置上下文内容
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间
        Notification notification = builder.build(); // 获取构建好的Notification
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
        startForeground(110, notification);// 开始前台服务
        Log.d("MyService", "服务启动");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(Tag,"onDestroy");
        stopForeground(true);
        super.onDestroy();
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);//取消电话状态监听
    }

    /**
     * 用于监听电话的通话状态
     */
    private class MyListener extends PhoneStateListener {
        public void onCallStateChanged(int state, final String incomingNumber) {
            Log.d("MyService", "电话状态改变");
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING://响铃状态
                    Log.d("MyService", "响铃状态,来电号码为：" + incomingNumber);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            /**
                             * 判断黑名单中是否存在这个号码，存在则挂断电话。
                             */
                            Uri uri = Uri.parse("content://example.com.myapp/BlackList");
                            ContentResolver contentResolver = getContentResolver();
                            String where = "number = ?";//条件查询
                            String[] number_args = {incomingNumber};
                            Cursor cursor = contentResolver.query(uri, null, where, number_args, null);
                            if (cursor.moveToNext()) {//黑名单中存在该号码，挂断电话
                                Log.d("MyService", incomingNumber + "在黑名单中");
                                endCall();
                            }
                        }
                    }).start();
                    break;
                default:
                    Log.d("MyService", "其他状态");
                    break;
            }
        }
    }

    public void endCall() {
        try {
            Method method = Class.forName("android.os.ServiceManager")
                    .getMethod("getService", String.class);
            IBinder binder = (IBinder) method.invoke(null, new Object[]{Context.TELEPHONY_SERVICE});
            ITelephony telephony = ITelephony.Stub.asInterface(binder);
            telephony.endCall();
        } catch (NoSuchMethodException e) {
            Log.d("MyService", "", e);
        } catch (ClassNotFoundException e) {
            Log.d("MyService", "", e);
        } catch (Exception e) {
        }
    }
}
