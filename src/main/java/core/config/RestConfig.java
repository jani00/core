package core.config;

/**
 * Contains definitions of REST service constants.
 * 
 * @author jani
 * 
 */
public class RestConfig {
	/**
	 * The tolerable difference between server and client timestamps in
	 * milliseconds.
	 */
	public static final long TIMESTAMP_TOLERANCE = 1000;
	/**
	 * HTTP OK status code.
	 */
	public static final int HTTP_OK = 200;
	/**
	 * HTTP CREATED status code.
	 */
	public static final int HTTP_CREATED = 201;
	/**
	 * HTTP BAD REQUEST status code.
	 */
	public static final int HTTP_BAD_REQUEST = 400;
	/**
	 * HTTP UMAUTHORIZED status code.
	 */
	public static final int HTTP_UNAUTHORIZED = 401;
	/**
	 * Prefix of the URI, indicating a zip-related request.
	 */
	public static final String ZIP_URI_PREFIX = "_zip";

}
