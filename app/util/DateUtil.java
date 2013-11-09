package util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateUtil {

	public static String getFormattedDate(Date date) {
		return new SimpleDateFormat("dd/MM/yyyy").format(date);
	}

	/**
	 * Parse from dd/MM/yyyy
	 * @param str
	 * @return
	 */
	public static Date parseToDate(String str) {
		
		TimeZone GMT = TimeZone.getTimeZone("GMT-03:00");
		
		try {
			int day = Integer.parseInt(str.substring(0, 2));
			int month = Integer.parseInt(str.substring(3, 5)) - 1;
			int year = Integer.parseInt(str.substring(6, 10));
			
			Calendar dateTime = new GregorianCalendar(GMT);
			dateTime.set(year, month, day);
			return dateTime.getTime();

		} catch (StringIndexOutOfBoundsException e) {
			return null;
		}
	}

}
