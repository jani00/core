package core.web.controller;

import core.config.WebConfig;
import core.util.Validator;
import core.web.util.PostState;

/**
 * Controller for user login.
 * 
 * @author jani
 * 
 */
@ControllerName("login")
public class LoginController extends Controller {
	@Override
	public void get() {
		getView().put("template", "login");

		PostState state = PostState.get(getSession());
		getView().put(state.getAll());
	}

	@Override
	public void post() {
		if (getRequest().getParameter("post") != null) {
			String login = getRequest().getParameter("login");
			String password = getRequest().getParameter("password");
			boolean isAuthorized = getServlet().getUserManager()
					.isAuthorized(login, password);
			if (isAuthorized) {
				getSession().setAttribute(WebConfig.SESSION_KEY_LOGIN, login);
				setRedirect("/browse");
				return;
			} else {
				Validator validator = new Validator();
				validator.addError("login", "Invalid username or password");
				PostState state = new PostState();
				state.put("validator", validator);
				state.put("login", login);
				state.save(getSession());
			}
		}
		setRedirect("/login");
	}

	@Override
	public String getPageTitle() {
		return "Login";
	}
}
