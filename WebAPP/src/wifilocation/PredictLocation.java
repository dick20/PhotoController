package wifilocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PredictLocation {
	public static String predictLocation(String message) {
		ArrayList<WifiData> list = DBUtils.getWifiData();
		String res = "";
		int difference = Integer.MAX_VALUE;
		String[] wifimessage = message.split(";");
//		for(String str : wifimessage) {
//			System.out.println(str);
//		}
		
		for (int i = 0; i < list.size(); i++) {
			int diff = 0;
			Map<String, Integer> wifi = new HashMap<String, Integer>();
			String[] mes = list.get(i).getWifiMessage().split(";");
			for (String str : mes) {
				String bssid = str.substring(6, 24);
				String level = str.substring(30, 33);
				wifi.put(bssid, Integer.parseInt(level));
//				System.out.println(bssid + "  " + level);			
			}
			
			for(String str : wifimessage) {
				String bssid = str.substring(6, 24);
				String level = str.substring(30, 33);
				int levelI = Integer.parseInt(level);
				
				if(wifi.containsKey(bssid)){
					int wifileval = wifi.get(bssid);
					diff += (levelI-wifileval) * (levelI-wifileval);
				}else{
					diff += 20*20;
				}
			}
			
			System.out.println(list.get(i).getAddress() + "     " + diff);
			if (difference > diff) {
				difference = diff;
				res = list.get(i).getAddress();
			}
		}
		if (res.isEmpty()) {
			return "not found";
		}
		return res;
	}
	
	public static void main(String args[]) {
		try {
			String message = "bssid:0a:74:9c:6e:32:8e level:-73;bssid:0a:74:9c:6e:3f:1e level:-65;bssid:0a:74:9c:6e:4b:2b level:-89;bssid:0a:74:9c:6e:4d:86 level:-56;bssid:0a:74:9c:6e:4d:87 level:-50;bssid:0a:74:9c:6e:4e:c3 level:-76;bssid:0a:74:9c:6e:9e:ff level:-91;bssid:0a:74:9c:6e:ab:0f level:-90;bssid:0e:74:9c:6e:32:8e level:-72;bssid:0e:74:9c:6e:3f:1e level:-65;bssid:0e:74:9c:6e:3f:1f level:-84;bssid:0e:74:9c:6e:4b:2b level:-89;bssid:0e:74:9c:6e:4d:86 level:-56;bssid:0e:74:9c:6e:4e:c3 level:-76;bssid:0e:74:9c:6e:9e:ff level:-90;bssid:0e:74:9c:6e:ab:0f level:-91;";			
			System.out.println(PredictLocation.predictLocation(message));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}