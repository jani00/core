package core.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import core.web.servlet.BaseServlet;
import core.web.view.View;

/**
 * Base web controller. Contains basic controller logic.
 * 
 * @author jani
 * 
 */
public abstract class Controller {
	private BaseServlet servlet;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private View view;

	private String name;
	private String redirect;
	private boolean renderView;

	/**
	 * Constructor
	 */
	public Controller() {
		redirect = "";
		renderView = true;
		name = "";
	}

	/**
	 * Processes the GET method.
	 */
	public abstract void get();

	/**
	 * Processes the POST method.
	 */
	public abstract void post();

	/**
	 * Gets the page title
	 * 
	 * @return String
	 */
	public abstract String getPageTitle();

	/**
	 * Initializes the controller.
	 * 
	 * @param name
	 * @param servlet
	 * @param request
	 * @param response
	 * @param view
	 */
	public void init(String name, BaseServlet servlet,
			HttpServletRequest request, HttpServletResponse response, View view) {

		this.name = name;
		this.servlet = servlet;
		this.request = request;
		this.response = response;
		this.view = view;

		init();
	}

	/**
	 * Initializes the controller.
	 */
	public void init() {
		// nothing
	}

	/**
	 * Sets a url for redirection.
	 * 
	 * @param url
	 */
	public void setRedirect(String url) {
		this.redirect = url;
	}

	/**
	 * Gets the redirection url.
	 * 
	 * @return String
	 */
	public String getRedirect() {
		return redirect;
	}

	/**
	 * Sets whether to render the view. Should be set to false when a binary
	 * response is expected.
	 * 
	 * @param renderView
	 */
	public void setRenderView(boolean renderView) {
		this.renderView = renderView;
	}

	/**
	 * Gets whether to render the view.
	 * 
	 * @return Whether to render the view.
	 */
	public boolean renderView() {
		return renderView;
	}

	/**
	 * Gets the name of the controller.
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the servlet, owning the controller.
	 * 
	 * @return {@link BaseServlet}
	 */
	public BaseServlet getServlet() {
		return servlet;
	}

	/**
	 * Gets the request
	 * 
	 * @return {@link HttpServletRequest}
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * Gets the response
	 * 
	 * @return {@link HttpServletResponse}
	 */
	public HttpServletResponse getResponse() {
		return response;
	}

	/**
	 * Gets the view
	 * 
	 * @return {@link View}
	 */
	public View getView() {
		return view;
	}

	/**
	 * Gets the session.
	 * 
	 * @return {@link HttpSession}
	 */
	public HttpSession getSession() {
		return getRequest().getSession();
	}
}
