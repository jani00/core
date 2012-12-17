package core.web.util;

/**
 * Wrapper for a breadcrumb item.
 * 
 * @author jani
 * 
 */
public class BreadcrumbItem {
	private String name;
	private String url;

	/**
	 * Constructor.
	 * 
	 * @param name
	 * @param url
	 */
	public BreadcrumbItem(String name, String url) {
		this.name = name;
		this.url = url;
	}

	/**
	 * Gets the name of the item.
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the url of the item.
	 * 
	 * @return String
	 */
	public String getUrl() {
		return url;
	}
}
