package dick.android.remotecontrol;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dick.android.remotecontrol.service.DBUtils;

public class WifiCollector extends AppCompatActivity {
    WifiManager wifi;
    public static WifiManager wifimanger;
    TextView show;
    Button button;
    Button get;
    Button set;
    Button clear_but;
    Button jump;
    EditText positionMark;
    private static final int REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;

    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            // set
            if(msg.what==101) {
                Toast.makeText(WifiCollector.this, "成功上传wifi信息", Toast.LENGTH_SHORT).show();
                positionMark.setText("");
            }
            // get
            else if(msg.what==102){
                Toast.makeText(WifiCollector.this, "成功获取wifi信息", Toast.LENGTH_SHORT).show();
                positionMark.setText("");
            }
            // clear
            else if(msg.what==103){
                Toast.makeText(WifiCollector.this, "未打开清除功能，防止操作失误", Toast.LENGTH_SHORT).show();
                positionMark.setText("");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_data);
        show = findViewById(R.id.wifi_ssid);
        button = findViewById(R.id.fresh);
        get = findViewById(R.id.get);
        set = findViewById(R.id.set);
        clear_but = findViewById(R.id.clear_but);
        jump = findViewById(R.id.jump);
        positionMark = findViewById(R.id.positionMark);

        // 判断wifi是否开启
        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(! wifi.isWifiEnabled()){
            if(wifi.getWifiState() != WifiManager.WIFI_STATE_ENABLING)
                wifi.setWifiEnabled(true);
        }
        wifimanger = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//如果 API level 是大于等于 23(Android 6.0) 时
            //判断是否具有权限
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //判断是否需要向用户解释为什么需要申请该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    //showToast("自Android 6.0开始需要打开位置权限");
                }
                //请求权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_CODE_ACCESS_COARSE_LOCATION);
            }
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 获取当前连接上的wifi相关信息
                 */
                wifimanger.startScan();
                WifiInfo info = wifi.getConnectionInfo();
                int strength = info.getRssi();
                int speed = info.getLinkSpeed();
                String bssid = info.getBSSID();
                String ssid = info.getSSID();
                String units = WifiInfo.LINK_SPEED_UNITS;
                StringBuilder wifiinformation = new StringBuilder("ScanResults is: \n");
                /**
                 * 获取扫描到的所有wifi相关信息
                 */
                List<ScanResult> results = wifi.getScanResults();
                /**
                 *这里添加了先验知识，仅保留中大的wifi信息
                 * 这里为了方便比较，新增排序功能
                 */
                List<String> wifiinfo = new ArrayList<>();
                for(ScanResult result:results){
                    if(result.BSSID.substring(0, 12).equals("0e:74:9c:6e:") ||
                            result.BSSID.substring(0, 12).equals("0a:74:9c:6e:")) {
                        String wifitemp = "bssid：" + result.BSSID + "   ssid：" + result.SSID + " level：" + result.level + "\n";
                        wifiinfo.add(wifitemp);
                    }
                }
                Collections.sort(wifiinfo);
                for (String str:wifiinfo){
                    // Log.i("test",str);
                    wifiinformation.append(str);
                }
                String text = "正连接的WiFi\nssid为：" + ssid + "\nbssid为：" +bssid + "\n连接速度为：" + String.valueOf(speed) + "  " + String.valueOf(units) + "\n强度为：" + strength;
                wifiinformation.append("\n\n");
                wifiinformation.append(text);

                show.setText(wifiinformation.toString());
            }
        });

        clear_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // DBUtils.clearWifiData();
                        Message message = Message.obtain(handler);
                        message.what = 103;
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });

        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(WifiCollector.this,MainActivity.class);
                startActivity(intent);
            }
        });

        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DBUtils.getWifiData();
                        Message message = Message.obtain(handler);
                        message.what = 102;
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String address = positionMark.getText().toString();
                if(address == null || address.equals("")) {
                    Toast.makeText(WifiCollector.this,"输入地址不能为空",Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            wifimanger = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                            String wifiMessage = getWifiMessage();
                            DBUtils.insertWifiData(new WifiData(address, wifiMessage));
                            Message message = Message.obtain(handler);
                            message.what = 101;
                            handler.sendMessage(message);
                        }
                    }).start();
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_ACCESS_COARSE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户允许改权限，0表示允许，-1表示拒绝 PERMISSION_GRANTED = 0， PERMISSION_DENIED = -1
                //permission was granted, yay! Do the contacts-related task you need to do.
                //这里进行授权被允许的处理
            } else {
                //permission denied, boo! Disable the functionality that depends on this permission.
                //这里进行权限被拒绝的处理
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public static String getWifiMessage() {
        //WifiInfo info = wifimanger.getConnectionInfo();
        StringBuilder wifiinformation = new StringBuilder();
        List<String> wifiinfo = new ArrayList<>();
        /**
         * 获取扫描到的所有wifi相关信息
         * 按照mac地址排序
         */
        wifimanger.startScan();
        List<ScanResult> results = wifimanger.getScanResults();
        for(ScanResult result:results){
            if(result.BSSID.substring(0, 12).equals("0e:74:9c:6e:") ||
                    result.BSSID.substring(0, 12).equals("0a:74:9c:6e:")) {
                String wifitemp = "bssid:" + result.BSSID + " level:" + result.level + ";";
                wifiinfo.add(wifitemp);
            }
        }
        Collections.sort(wifiinfo);
        for (String str:wifiinfo){
            wifiinformation.append(str);
        }
        return wifiinformation.toString();
    }
}
