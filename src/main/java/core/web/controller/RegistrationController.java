package core.web.controller;

import core.config.WebConfig;
import core.model.User;

/**
 * Controller for user registration.
 * 
 * @author jani
 * 
 */
@ControllerName("registration")
public class RegistrationController extends Controller {
	@Override
	public void get() {
		getView().put("template", "registration");

		User user = getServlet().getFromSession(getSession(), "user");
		if (user == null) {
			user = new User();
		}
		getView().put("user", user);
	}

	@Override
	public void post() {
		User user = new User();
		getSession().setAttribute("user", user);

		if (getRequest().getParameter("post") != null) {
			user.setLogin(getRequest().getParameter("login"));
			user.setPassword(getRequest().getParameter("password"));
			user.setConfirmPassword(getRequest()
					.getParameter("confirmPassword"));
			user.setEmail(getRequest().getParameter("email"));
			user.setRealName(getRequest().getParameter("realName"));
			user.setCity(getRequest().getParameter("city"));
			user.setOrganizationsString(getRequest().getParameter(
					"organizations"));
			user.setAbout(getRequest().getParameter("about"));

			if (user.validate(getServlet().getUserManager()).isValid()) {
				user.hashPassword();
				if (getServlet().getUserManager().save(user)) {
					getSession().setAttribute(WebConfig.SESSION_KEY_LOGIN,
							user.getLogin());
					setRedirect("/browse");
					return;
				}
			}
		}
		setRedirect("/registration");
	}

	@Override
	public String getPageTitle() {
		return "Registration";
	}
}
