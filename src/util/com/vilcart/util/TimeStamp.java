package util.com.vilcart.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStamp {

	public static String CurTimeStamp() {

		// method 1
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		//System.out.println(timestamp); // 2021-03-24 16:34:26.666
		return timestamp.toString();
	}

}
