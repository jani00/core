package core.web.controller;

import core.config.WebConfig;

/**
 * Controller for user logout.
 * 
 * @author jani
 * 
 */
@ControllerName("logout")
public class LogoutController extends Controller {

	@Override
	public void get() {
		getSession().setAttribute(WebConfig.SESSION_KEY_LOGIN, null);
		setRedirect("/index");

	}

	@Override
	public void post() {
		// nothing
	}

	@Override
	public String getPageTitle() {
		return "";
	}
}
