package core.web.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * A collection of {@link BreadcrumbItem}s.
 * 
 * @author jani
 * 
 */
public class Breadcrumbs {

	private List<BreadcrumbItem> items;

	/**
	 * Separator for string representation.
	 */
	public static final String PATH_SEPARATOR = "/";

	/**
	 * Constructor.
	 */
	public Breadcrumbs() {
		items = new ArrayList<BreadcrumbItem>();
	}

	/**
	 * Adds an item.
	 * 
	 * @param name
	 * @param url
	 */
	public void add(String name, String url) {
		items.add(new BreadcrumbItem(name, url));
	}

	/**
	 * Adds all items.
	 * 
	 * @param list
	 */
	public void add(List<String> list) {
		for (String item : list) {
			add(item, item);
		}
	}

	/**
	 * Builds web-ready list of breadcrumbs. If items are ["a", "b", "c"], the result
	 * will be ["a", "a/b", "a/b/c"].
	 * 
	 * @return List of {@link BreadcrumbItem}
	 */
	public List<BreadcrumbItem> build() {
		List<BreadcrumbItem> res = new ArrayList<BreadcrumbItem>();
		String url = "";
		for (BreadcrumbItem item : items) {
			url += (url.isEmpty() ? "" : PATH_SEPARATOR);
			url += item.getUrl();
			res.add(new BreadcrumbItem(item.getName(), url));
		}
		return res;
	}
}
