package example.com.myapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by yangjieqiong on 2017/4/10.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    private static final String Tag="BootBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(Tag,"onReceive");
        if(intent.getAction().equals(ACTION)){
            Intent MyServiceIntent = new Intent(context, MyService.class);  // 要启动的服务
            MyServiceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startService(MyServiceIntent);
        }
    }
}
