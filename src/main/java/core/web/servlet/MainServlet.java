package core.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.config.WebConfig;
import core.web.controller.Controller;
import core.web.view.ViewException;

/**
 * Main servlet for web application.
 * 
 * @author jani
 * 
 */
@WebServlet("/main")
public class MainServlet extends BaseServlet {

	private static final long serialVersionUID = 2487482383666670835L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Controller controller = getController(request, response);

		assert (controller != null);

		controller.get();

		if (!controller.getRedirect().isEmpty()) {
			redirect(request, response, controller.getRedirect());
		} else if (controller.renderView()) {

			response.setCharacterEncoding(WebConfig.DEFAULT_RESPONSE_ENCODING);
			response.setContentType(WebConfig.DEFAULT_RESPONSE_CONTENT_TYPE);

			String htmlTitle = WebConfig.HTML_TITLE;
			if (controller.getPageTitle() != null
					&& !controller.getPageTitle().isEmpty()) {
				htmlTitle = String.format("%s - %s", htmlTitle,
						controller.getPageTitle());
			}
			getView().put("htmlTitle", htmlTitle);

			getView().put("controllerName", controller.getName());

			getView().put("requestElapsedTime", requestElapsedTime());
			getView().put("servletElapsedTime", servletElapsedTime());

			getView().put("currentUser", getCurrentUser());

			try {
				String res = getView().parse();
				response.getWriter().print(res);
			} catch (ViewException e) {
				response.getWriter().print(e.getMessage());
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Controller controller = getController(request, response);

		assert (controller != null);

		controller.post();

		if (!controller.getRedirect().isEmpty()) {
			redirect(request, response, controller.getRedirect());
		}
	}

}
