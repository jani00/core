package core.web.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

/**
 * Structure for preserving values between POST request. Uses a map and session.
 * 
 * @author jani
 * 
 */
public class PostState {
	/**
	 * The session key to store the map.
	 */
	public static String SESSION_KEY = "__state";

	private Map<String, Object> map;

	/**
	 * Constructor
	 */
	public PostState() {
		map = new HashMap<String, Object>();
	}

	/**
	 * Puts a value
	 * 
	 * @param key
	 * @param val
	 */
	public void put(String key, Object val) {
		map.put(key, val);
	}

	/**
	 * Gets a value
	 * 
	 * @param <T>
	 * @param key
	 * @return Value or null.
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		if (map.containsKey(key)) {
			return (T) map.get(key);
		}
		return null;
	}

	/**
	 * Fetches the instance from the session, if any, otherwise create an empty
	 * one.
	 * 
	 * @param session
	 * @return {@link PostState}
	 */
	public static PostState get(HttpSession session) {
		PostState res;
		if (session.getAttribute(SESSION_KEY) == null) {
			res = new PostState();
		} else {
			res = (PostState) session.getAttribute(SESSION_KEY);
			session.removeAttribute(SESSION_KEY);
		}
		return res;
	}

	/**
	 * Save the instance in the session.
	 * 
	 * @param session
	 */
	public void save(HttpSession session) {
		session.setAttribute(SESSION_KEY, this);
	}

	/**
	 * Gets all values.
	 * 
	 * @return Map
	 */
	public Map<String, Object> getAll() {
		return new HashMap<String, Object>(map);
	}
}
