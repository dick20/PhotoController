package wifilocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class PredictLocation {
	public static String predictLocation(String message) {
		Map<Integer, String> different = new TreeMap<Integer, String>();
		ArrayList<WifiData> list = DBUtils.getWifiData();
		String res = "";
		String[] wifimessage = message.split(";");
		
		for (int i = 0; i < list.size(); i++) {
			int diff = 0;
			Map<String, Integer> wifi = new HashMap<String, Integer>();
			String[] mes = list.get(i).getWifiMessage().split(";");
			for (String str : mes) {
				String bssid = str.substring(6, 24);
				String level = str.substring(30, 33);
				wifi.put(bssid, Integer.parseInt(level));		
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
			different.put(diff, list.get(i).getAddress());
			//System.out.println(list.get(i).getAddress() + "     " + diff);
		}
		
		for (Integer key:different.keySet()) {
			System.out.println(different.get(key) + "  " + key); 
		}
		
		if (different.isEmpty()) {
			return "not found";
		} else {
			int i = 0;
			int f1 = 0, f2 = 0, f3 = 0;
			AdderssMap adderssMap1 = null, adderssMap2 = null, adderssMap3 = null;
			double resx = 0, resy = 0;
			
			for (Integer key:different.keySet()) {
				i ++;
				if (i == 1) {
					f1 = key;
					adderssMap1 = new AdderssMap(DBUtils.getAddressMap(different.get(key)));
				} else if (i == 2) {
					f2 = key;
					adderssMap2 = new AdderssMap(DBUtils.getAddressMap(different.get(key)));
				} else if (i == 3) {
					f3 = key;
					adderssMap3 = new AdderssMap(DBUtils.getAddressMap(different.get(key)));
				} else {
					break;
				}
				res += different.get(key) + " " ;
			}
			
			double weight = 0;
			weight = 1.0 / (1.0/f1 + 1.0/f2 + 1.0/f3);
			resx = weight / f1 * adderssMap1.getX() + weight / f2 * adderssMap2.getX() + weight / f3 * adderssMap3.getX();
			resy = weight / f1 * adderssMap1.getY() + weight / f2 * adderssMap2.getY() + weight / f3 * adderssMap3.getY();

			res += String.format("%.2f", resx) + " " + String.format("%.2f", resy);
			System.out.println(resx + "  " +resy);
			return res;
		}
	}
	
	public static String classification(String filepath, String path) {
		System.out.println("load start");
		String loadpath = filepath + "WEB-INF\\lib\\Dll1.dll";
		System.load(loadpath);
		//System.load("E:\\AndroidStudioProjects\\PhotoController\\WebAPP\\src\\Dll1.dll");
		System.out.println("load finish");
		TestNative testNative = new TestNative();
		String cmd = "python ./src/classify/DenseNet.py " + path;
        String res = testNative.classify(cmd); 
		
		return res;
	}
	
//	public static void main(String args[]) {
//		try {
//			String message = "bssid:0a:74:9c:6e:32:8e level:-73;bssid:0a:74:9c:6e:3f:1e level:-65;bssid:0a:74:9c:6e:4b:2b level:-89;bssid:0a:74:9c:6e:4d:86 level:-56;bssid:0a:74:9c:6e:4d:87 level:-50;bssid:0a:74:9c:6e:4e:c3 level:-76;bssid:0a:74:9c:6e:9e:ff level:-91;bssid:0a:74:9c:6e:ab:0f level:-90;bssid:0e:74:9c:6e:32:8e level:-72;bssid:0e:74:9c:6e:3f:1e level:-65;bssid:0e:74:9c:6e:3f:1f level:-84;bssid:0e:74:9c:6e:4b:2b level:-89;bssid:0e:74:9c:6e:4d:86 level:-56;bssid:0e:74:9c:6e:4e:c3 level:-76;bssid:0e:74:9c:6e:9e:ff level:-90;bssid:0e:74:9c:6e:ab:0f level:-91;";			
//			System.out.println(PredictLocation.predictLocation(message));
//			System.out.println();
//			
//			String str = PredictLocation.classification("E:\\AndroidStudioProjects\\PhotoController\\WebAPP\\WebContent\\", "./src/household");
//			System.out.println(str);
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
}