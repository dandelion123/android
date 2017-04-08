package example.com.rollcall;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by yangjieqiong on 2017/4/8.
 * 连接服务器的类，调用connect连接服务器，通过构造函数来确定需要服务器做什么
 */

public class HttpConnectToServer {
    private int Command;//1代表开始点名；2代表停止点名；3代表查看点名结果
    private static final String IP = "192.168.0.1";//服务器的IP地址
    private static final int PORT = 6002;//服务器的端口号

    public HttpConnectToServer(int command) {
        this.Command = command;
    }

    public String connect() {
        Socket socket = null;
        String str = null;
        try {
            socket = new Socket(IP, PORT);
            socket.setSoTimeout(10000);//超时时间
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.write(Command);//向服务器发送指令
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Log.d("Read:", str = br.readLine());//从服务器读数据
            br.close();
            out.close();
        } catch (Exception e) {
            str = "连接服务器失败" + e.toString();
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return str;
    }
}
