package core.web.controller;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for information pages.
 * 
 * @author jani
 * 
 */
@ControllerName("page")
public class PageController extends Controller {
	private static Map<String, String> title;
	private String page;

	@Override
	public void get() {
		page = (String) getRequest().getAttribute("page");
		String template = String.format("page/%s", page);
		getView().put("template", template);
	}

	@Override
	public void post() {
		// nothing
	}

	static {
		title = new HashMap<String, String>();
		title.put("index", "");
		title.put("about", "About");
		title.put("problem-format", "Problem Format");
		title.put("contest-format", "Contest Format");
		title.put("series-format", "Series Format");
		title.put("user-format", "User Format");
		title.put("security-policy", "Security Policy");
		title.put("rest-service", "REST Service");
	}

	@Override
	public String getPageTitle() {
		return title.get(page);
	}
}
