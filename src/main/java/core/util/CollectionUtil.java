package core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper methods for collections.
 * 
 * @author jani
 * 
 */
public class CollectionUtil {
	/**
	 * Concatenates a list to a string, using glue.
	 * 
	 * @param <T>
	 * @param input
	 * @param glue
	 * @return String
	 */
	public static <T> String join(List<T> input, String glue) {

		String res = "";

		if (input.size() > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append(input.get(0));

			for (int i = 1; i < input.size(); i++) {
				sb.append(glue);
				sb.append(input.get(i));
			}

			res = sb.toString();
		}

		return res;
	}

	/**
	 * Concatenates an array to a string, using glue.
	 * 
	 * @param <T>
	 * @param input
	 * @param glue
	 * @return String
	 */
	public static <T> String join(T[] input, String glue) {
		return join(Arrays.asList(input), glue);
	}

	/**
	 * Filters a map, according to a list of keys and prefix.
	 * 
	 * @param map
	 * @param keys
	 * @param prefix
	 * @return Filtered values.
	 */
	public static Map<String, Object> filterMap(Map<String, String> map,
			List<String> keys, String prefix) {

		HashMap<String, Object> res = new HashMap<String, Object>();

		for (String s : keys) {
			String key = prefix + s;
			if (map.containsKey(key)) {
				res.put(s, map.get(key));
			}
		}
		return res;
	}

	/**
	 * Filters a list with a regex.
	 * 
	 * @param list
	 * @param regex
	 * @return Filtered values.
	 */
	public static List<String> filterRegex(List<String> list, String regex) {
		List<String> res = new ArrayList<String>();
		for (String s : list) {
			if (s.matches(regex)) {
				res.add(s);
			}
		}
		return res;
	}

	/**
	 * Gets a list of matches of elements of a list.
	 * 
	 * @param list
	 * @param regex
	 * @param group
	 * @return Matched values.
	 */
	public static List<String> getMatches(List<String> list, String regex,
			int group) {
		Pattern pattern = Pattern.compile(regex);
		List<String> res = new ArrayList<String>();
		for (String s : list) {
			Matcher m = pattern.matcher(s);
			if (m.matches()) {
				res.add(m.group(group));
			}
		}
		return res;
	}

	/**
	 * Checks if there is a match in a list.
	 * 
	 * @param list
	 * @param regex
	 * @return Whether matches.
	 */
	public static boolean hasMatch(List<String> list, String regex) {
		for (String s : list) {
			if (s.matches(regex)) {
				return true;
			}
		}
		return false;
	}
}
