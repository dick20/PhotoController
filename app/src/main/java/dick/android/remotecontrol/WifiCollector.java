package dick.android.remotecontrol;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import dick.android.remotecontrol.service.DBUtils;

public class WifiCollector extends AppCompatActivity {
    WifiManager wifi;
    public static WifiManager wifimanger;
    TextView show;
    Button button;
    Button get;
    Button set;
    EditText positionMark;
    private static final int REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_data);
        show = (TextView) findViewById(R.id.wifi_ssid);
        button = findViewById(R.id.fresh);
        get = findViewById(R.id.get);
        set = findViewById(R.id.set);
        positionMark = findViewById(R.id.position);

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
                WifiInfo info = wifi.getConnectionInfo();
                int strength = info.getRssi();
                int speed = info.getLinkSpeed();
                String bssid = info.getBSSID();
                String ssid = info.getSSID();
                String units = WifiInfo.LINK_SPEED_UNITS;
                String wifiinformation = "ScanResults is: \n";
                /**
                 * 获取扫描到的所有wifi相关信息
                 */
                List<ScanResult> results = wifi.getScanResults();
                for(ScanResult result:results){
                    if(result.BSSID.substring(0, 12).equals("0e:74:9c:6e:") ||
                            result.BSSID.substring(0, 12).equals("0a:74:9c:6e:")) {
                        wifiinformation += "bssid为：" + result.BSSID + "   ssid为：" + result.SSID + "   强度为：" + result.level + "\n";
                    }
                }

                String text = "正连接的WiFi\nssid为：" + ssid + "\nbssid为：" +bssid + "\n连接速度为：" + String.valueOf(speed) + "  " + String.valueOf(units) + "\n强度为：" + strength;
                wifiinformation += "\n\n";
                wifiinformation += text;

                show.setText(wifiinformation);
            }
        });


        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DBUtils.getWifiData();
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
                            String wifiMessage = getWifiMessage();
                            DBUtils.insertWifiData(new WifiData(address, wifiMessage));

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
        WifiInfo info = wifimanger.getConnectionInfo();
        String wifiinformation = "";
        /**
         * 获取扫描到的所有wifi相关信息
         */
        List<ScanResult> results = wifimanger.getScanResults();
        for(ScanResult result:results){
            if(result.BSSID.substring(0, 12).equals("0e:74:9c:6e:") ||
                    result.BSSID.substring(0, 12).equals("0a:74:9c:6e:")) {
                wifiinformation += "bssid：" + result.BSSID + " level：" + result.level + ";";
            }
        }

        return wifiinformation;
    }
}
