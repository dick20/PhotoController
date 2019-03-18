package dick.android.remotecontrol.service;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dick.android.remotecontrol.WifiData;

public class DBUtils {
    private static final String TAG = "DBUtils";
    private static String driver = "com.mysql.jdbc.Driver";//数据库驱动
    private static String url = "jdbc:mysql://139.196.79.193:3306/wififingerprint";//数据库路径，找到对应的数据库
    private static String user = "local";//数据库账号
    private static String password = "123456";//数据库密码

    private static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName(driver);//加载数据库驱动，注册到驱动管理
            con=DriverManager.getConnection(url,user,password);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return con;
    }
    public static List<WifiData> getWifiData() {
        List<WifiData> list = new ArrayList<>();
        Connection con = getConnection();
        try {
            Statement st = con.createStatement();
            String sql = "select * from message";
            ResultSet res = st.executeQuery(sql);
            if (res == null) {
                return null;
            } else {
                while(res.next()) {
                    WifiData wifiData = new WifiData();
                    wifiData.setAddress(res.getString("address"));
                    wifiData.setWifiMessage(res.getString("wifimessage"));
                    list.add(wifiData);
                    Log.i("DB",wifiData.getAddress() + "  " + wifiData.getWifiMessage());
                }

                con.close();
                st.close();
                res.close();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, " 数据获取异常");
            return null;
        }
    }

    public static void insertWifiData(WifiData wifiData) {
        Connection con = getConnection();
        PreparedStatement ps = null;
        try{
            String sql = "INSERT INTO message (address,wifimessage) VALUES (?,?)";
            String address = wifiData.getAddress();
            String wifimessage = wifiData.getWifiMessage();
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setString(1, address);
            ps.setString(2, wifimessage);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, " 数据插入异常");
        }
    }
}