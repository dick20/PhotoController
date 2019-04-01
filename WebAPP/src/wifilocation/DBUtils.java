package wifilocation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBUtils {
    private static String driver = "com.mysql.jdbc.Driver";//数据库驱动
    private static String url = "jdbc:mysql://139.196.79.193:3306/wififingerprint";//数据库路径，找到对应的数据库
    private static String user = "local";//数据库账号
    private static String password = "123456";//数据库密码
    
    private static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName(driver);//加载数据库驱动，注册到驱动管理
            con = DriverManager.getConnection(url,user,password);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return con;
    }
    
    public static ArrayList<WifiData> getWifiData() {
    	ArrayList<WifiData> list = new ArrayList<>();
        Connection con = DBUtils.getConnection();
        try {
            java.sql.Statement st = con.createStatement();
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
                    //System.out.println(wifiData.getAddress() + "  " + wifiData.getWifiMessage());
                }
                con.close();
                st.close();
                res.close();
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("数据获取异常");
            return null;
        }
    }
    
    public static AdderssMap getAddressMap(String address) {
    	Connection con = DBUtils.getConnection();
    	PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            String sql = "select * from addressmap where address=?";
            ps=con.prepareStatement(sql);
            ps.setString(1, address);
            rs=ps.executeQuery();
            
            if(rs.next()){
            	AdderssMap adderssMap = new AdderssMap();
                adderssMap.setAddress(address);
                adderssMap.setX(rs.getFloat("addressMapX"));
                adderssMap.setY(rs.getFloat("addressMapY"));            
                
                return adderssMap;
            }else{
                return null;
            }           
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("数据获取异常");
            return null;
        }
    }
    
    public static void main(String args[]) {
		try {
			DBUtils.getWifiData();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
