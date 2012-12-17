package core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Helper methods for parsing.
 * 
 * @author jani
 * 
 */
public class Parser {
	/**
	 * Regex for datetime
	 */
	public static final String DATETIME_REGEX = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	/**
	 * Time zone
	 */
	public static final String TIMEZONE = "Zulu";

	/**
	 * Converts string to date.
	 * 
	 * @param input
	 * @return {@link Date}
	 */
	public static Date readDate(String input) {
		DateFormat dateFormat = new SimpleDateFormat(DATETIME_REGEX);
		dateFormat.setTimeZone(TimeZone.getTimeZone(TIMEZONE));
		Date res = null;
		try {
			res = dateFormat.parse(input);
		} catch (ParseException e) {
			// nothing
		}
		return res;
	}

	/**
	 * Converts string to integer.
	 * 
	 * @param input
	 * @return {@link Integer}, 0 on fail.
	 */
	public static Integer readInteger(String input) {
		try {
			return Integer.parseInt(input);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * Converts string to long.
	 * 
	 * @param input
	 * @return {@link Integer}, 0 on fail.
	 */
	public static Long readLong(String input) {
		try {
			return Long.parseLong(input);
		} catch (Exception e) {
			return 0l;
		}
	}

	/**
	 * Converts string to double.
	 * 
	 * @param input
	 * @return {@link Double}.
	 */
	public static Double readDouble(String input) {
		try {
			return Double.parseDouble(input);
		} catch (Exception e) {
			return 0d;
		}
	}

	/**
	 * Parses an object, according to the specified class.
	 * 
	 * @param <T>
	 * @param input
	 * @param clazz
	 * @return Converted value.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T readObject(Object input, Class<T> clazz) {
		if (clazz == String.class) {
			return (T) input.toString();
		}
		if (clazz == Integer.class || clazz == int.class) {
			return (T) Parser.readInteger(input.toString());
		}
		if (clazz == Double.class || clazz == double.class) {
			return (T) Parser.readDouble(input.toString());
		}
		return (T) input;
	}

	/**
	 * Converts date to string.
	 * 
	 * @param date
	 * 
	 * @return {@link String}
	 */
	public static String dateToString(Date date) {
		DateFormat dateFormat = new SimpleDateFormat(DATETIME_REGEX);
		dateFormat.setTimeZone(TimeZone.getTimeZone(TIMEZONE));
		return dateFormat.format(date);
	}
}
