package core.web.controller;

import core.model.User;
import core.util.Validator;
import core.web.util.PostState;
import core.web.util.StatusMessage;
import core.web.util.StatusMessage.Type;

/**
 * Controller for user profile.
 * 
 * @author jani
 * 
 */
@ControllerName("profile")
public class ProfileController extends Controller {
	@Override
	public void get() {
		getView().put("template", "profile");

		PostState state = PostState.get(getSession());
		getView().put(state.getAll());

		if (getServlet().getCurrentUser() == null) {
			setRedirect("/registration");
		} else {
			User current = state.get("user");
			User user = (current == null ? getServlet().getCurrentUser()
					: current);

			getView().put("user", user);
		}

	}

	@Override
	public void post() {
		User user = getServlet().getCurrentUser();

		if (user == null) {
			setRedirect("/registration");
		} else {
			getSession().setAttribute("user", user);

			if (getRequest().getParameter("post") != null) {

				PostState state = new PostState();
				if (getRequest().getParameter("password").isEmpty()) {
					user.setConfirmPassword(user.getPassword());
				} else {
					user.setPassword(getRequest().getParameter("password"));
					user.setConfirmPassword(getRequest().getParameter(
							"confirmPassword"));
				}
				user.setEmail(getRequest().getParameter("email"));
				user.setRealName(getRequest().getParameter("realName"));
				user.setCity(getRequest().getParameter("city"));
				user.setOrganizationsString(getRequest().getParameter(
						"organizations"));
				user.setAbout(getRequest().getParameter("about"));

				Validator validator = user.validate(getServlet()
						.getUserManager(), true);
				StatusMessage status = new StatusMessage(Type.SUCCESS, "");
				status.setFromValidator(validator,
						"Profile successfully updated");
				if (validator.isValid()) {
					if (!getRequest().getParameter("password").isEmpty()) {
						user.hashPassword();
					}
					getServlet().getUserManager().save(user);
				} else {
					state.put("user", user);
				}
				state.put("status", status);
				state.save(getSession());
			}
			setRedirect("/profile");
		}

	}

	@Override
	public String getPageTitle() {
		return "Profile";
	}
}
