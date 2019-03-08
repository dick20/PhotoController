package upload;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class IdGenertor {
	
	/**
	 * ����UUID
	 * 
	 * @return UUID
	 */
	public static String generateGUID() {
        return new BigInteger(165, new Random()).toString(36).toUpperCase();
    }
	
	public static String generateOrdersNum() {
		//YYYYMMDD+��ǰʱ�䣨���룩  ��   1��=1000����=1000*1000����
		Date now = new Date();
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String str = df.format(now);
		return str+System.nanoTime();
	}
}