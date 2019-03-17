package dick.android.remotecontrol.service;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class DBUtils {
    private static final String TAG = "DBUtils";
    private static String driver = "com.mysql.jdbc.Driver";//数据库驱动
    private static String url = "jdbc:mysql://139.196.79.193:3306/wififingerprint";//数据库路径，找到对应的数据库
    private static String user = "local";//数据库账号
    private static String password = "123456";//数据库密码

    private static Connection getConnection(String dbName) {
        Connection con = null;
        try {
//            driver = "com.mysql.jdbc.Driver";
//            //172.26.86.202    139.196.79.193
//            url = "jdbc:mysql://139.196.79.193:3306/wififingerprint";
//            user = "local";
//            password = "123456";

            Class.forName(driver);//加载数据库驱动，注册到驱动管理
            con=DriverManager.getConnection(url,user,password);
            if (con != null) {
                System.out.println("connect successfully");
                System.out.println("connect successfully");
            } else {
                System.out.println("fail");
                System.out.println("fail");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return con;
    }
    public static HashMap<String, String> getWifiMessage() {
        //HashMap<String, String> map = new HashMap<>();
        Connection conn = getConnection("wififingerprint");
        try {
            Statement st = conn.createStatement();
            String sql = "select wifimessage from message";
            ResultSet res = st.executeQuery(sql);
            if (res == null) {
                return null;
            } else {
                //int cnt = res.getMetaData().getColumnCount();
                //res.last(); int rowCnt = res.getRow(); res.first();
                //res.next();
                while(res.next()) {
                    String wifimessage = res.getString("wifimessage");
                    Log.i("DB",wifimessage);
                }
//                for (int i = 1; i <= cnt; ++i) {
//                    String field = res.getMetaData().getColumnName(i);
//                    map.put(field, res.getString(field));
//                }
                conn.close();
                st.close();
                res.close();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, " 数据操作异常");
            return null;
        }
    }
}