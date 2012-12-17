package core.config;

import java.util.Locale;

/**
 * Contains definitions of web specific constants.
 * 
 * @author jani
 * 
 */
public class WebConfig {

	/**
	 * Directory containing freemarker templates.
	 */
	public static final String TEMPLATES_LOCATION = "WEB-INF/templates";
	/**
	 * Default master template for freemarker.
	 */
	public static final String DEFAULT_MASTER_TEMPLATE = "master.ftl";

	/**
	 * Locale setting for Freemarker.
	 */
	public static final Locale FTL_LOCALE = Locale.ENGLISH;
	/**
	 * Template encodin for Freemarker.
	 */
	public static final String FTL_TEMPLATE_ENCODING = "utf-8";
	/**
	 * Encoding for Freemarker.
	 */
	public static final String FTL_OUTPUT_ENCODING = "utf-8";

	/**
	 * HTML new line tag.
	 */
	public static final String HTML_NEWLINE = "<br />";
	/**
	 * Default encoding for web request.
	 */
	public static final String DEFAULT_REQUEST_ENCODING = "utf-8";
	/**
	 * Default encoding for web response.
	 */
	public static final String DEFAULT_RESPONSE_ENCODING = "utf-8";
	/**
	 * Default content type for web response.
	 */
	public static final String DEFAULT_RESPONSE_CONTENT_TYPE = "text/html";
	/**
	 * Content type for file download.
	 */
	public static final String DOWNLOAD_CONTENT_TYPE = "application/octet-stream";
	/**
	 * Content type for json.
	 */
	public static final String JSON_CONTENT_TYPE = "application/json";
	/**
	 * Title prefix of HTML pages.
	 */
	public static final String HTML_TITLE = "Contest Repository";

	/**
	 * Session key for storing current login.
	 */
	public static final String SESSION_KEY_LOGIN = "__login";
	
}
