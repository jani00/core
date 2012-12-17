package core.web.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import core.config.ConfigurationManager;
import core.config.WebConfig;
import core.model.User;
import core.security.UserManager;
import core.web.controller.Controller;
import core.web.controller.BrowseController;
import core.web.controller.ControllerName;
import core.web.controller.LoginController;
import core.web.controller.LogoutController;
import core.web.controller.PageController;
import core.web.controller.ProfileController;
import core.web.controller.RegistrationController;
import core.web.view.View;

/**
 * Base servlet. Contains basic functionality.
 * 
 * @author jani
 * 
 */
public abstract class BaseServlet extends HttpServlet {

	private long servletStartTime;
	private long requestStartTime;
	private String httpRoot;
	private String appRoot;
	private boolean debug;
	private ConfigurationManager config = new ConfigurationManager();
	private UserManager userManager = new UserManager(config.getUsersRoot());

	private static final long serialVersionUID = 7715904627406855736L;

	private final Map<String, Class<? extends Controller>> controllerMap = new HashMap<String, Class<? extends Controller>>();

	private View view;

	private User currentUser = null;

	@Override
	public void init() {
		servletStartTime = System.nanoTime();

		registerController(PageController.class);
		registerController(RegistrationController.class);
		registerController(LoginController.class);
		registerController(LogoutController.class);
		registerController(BrowseController.class);
		registerController(ProfileController.class);
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		requestStartTime = System.nanoTime();
		String requestUrl = request.getRequestURL().toString();
		httpRoot = requestUrl.substring(0, requestUrl.length()
				- request.getServletPath().length());
		appRoot = request.getContextPath();
		debug = config.getDebug();
		view = new View(getServletContext());

		String username = (String) request.getSession().getAttribute(
				WebConfig.SESSION_KEY_LOGIN);
		currentUser = userManager.loadUser(username);

		view.put("debug", debug);
		view.put("httpRoot", httpRoot);
		view.put("appRoot", appRoot);

		super.service(request, response);
	}

	protected void registerController(Class<? extends Controller> clazz) {
		ControllerName annotation = clazz.getAnnotation(ControllerName.class);
		assert annotation != null;
		controllerMap.put(annotation.value(), clazz);
	}

	protected Controller getController(HttpServletRequest request,
			HttpServletResponse response) {
		String controllerName = (String) request.getAttribute("controller");

		Controller res = getController(controllerName, request, response);

		return res;
	}

	protected Controller getController(String name, HttpServletRequest request,
			HttpServletResponse response) {
		Class<? extends Controller> clazz = controllerMap.get(name);
		Controller res = null;
		try {
			res = clazz.newInstance();
		} catch (Exception e) {
			return null;
		}
		res.init(name, this, request, response, getView());
		return res;
	}

	/**
	 * Gets the time since the request started.
	 * 
	 * @return Time in seconds
	 */
	public double requestElapsedTime() {
		return (System.nanoTime() - requestStartTime) / Math.pow(10, 9);
	}

	/**
	 * Gets the total time the servlet has been alive for.
	 * 
	 * @return Time in seconds
	 */
	public double servletElapsedTime() {
		return (System.nanoTime() - servletStartTime) / Math.pow(10, 9);
	}

	/**
	 * Gets the request schema (e.g. "http://")
	 * 
	 * @return String
	 */
	public String getHttpRoot() {
		return httpRoot;
	}

	/**
	 * Gets the application root.
	 * 
	 * @return String
	 */
	public String getAppRoot() {
		return appRoot;
	}

	/**
	 * Gets the view.
	 * 
	 * @return {@link View}
	 */
	public View getView() {
		return view;
	}

	/**
	 * Gets if application is in debug mode.
	 * 
	 * @return Whether in debug.
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * Gets the currently logged user.
	 * 
	 * @return {@link User}.
	 */
	public User getCurrentUser() {
		return currentUser;
	}

	/**
	 * Gets the {@link ConfigurationManager}.
	 * 
	 * @return {@link ConfigurationManager}.
	 */
	public ConfigurationManager getConfig() {
		return config;
	}

	/**
	 * Gets the {@link UserManager}.
	 * 
	 * @return {@link UserManager}.
	 */
	public UserManager getUserManager() {
		return userManager;
	}

	/**
	 * Gets the currently logged user login.
	 * 
	 * @return String
	 */
	public String getCurrentLogin() {
		return currentUser == null ? null : currentUser.getLogin();
	}

	/**
	 * Gets a value from the session (if exists) and immediately removes it.
	 * 
	 * @param <T>
	 * @param session
	 * @param name
	 * @return Found value or null.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getFromSession(HttpSession session, String name) {
		T res = (T) session.getAttribute(name);
		session.removeAttribute(name);
		return res;
	}

	/**
	 * Performs an HTTP redirect.
	 * 
	 * @param request
	 * @param response
	 * @param target
	 * @throws IOException
	 */
	public void redirect(HttpServletRequest request,
			HttpServletResponse response, String target) throws IOException {
		response.sendRedirect(request.getContextPath() + target);
	}
}
