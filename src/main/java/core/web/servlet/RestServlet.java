package core.web.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.browse.Node;
import core.browse.NodeType;
import core.config.ConfigurationManager;
import core.config.RestConfig;
import core.config.WebConfig;
import core.model.Resource;
import core.model.ResourceKind;
import core.model.User;
import core.security.UserManager;
import core.util.CollectionUtil;
import core.util.JsonUtil;
import core.util.Parser;
import core.util.StringUtil;
import core.util.Validator;
import core.util.WebUtil;

/**
 * Entry point for REST service.
 * 
 * Authentication is as follows. All requests should contains the following
 * mandatory parameters: login, timestamp, token:
 * 
 * <li>"login" should be the username of the user.</li>
 * 
 * <li>"timestamp" should be the timestamp of the client, which should be in a
 * tolerable difference from the server's timestamp. Otherwise, authentication
 * will fail.</li>
 * 
 * <li>"token" should be a sha256, which is calculated like token = sha256(ip +
 * timestamp + sha256(password)). IP is the ip address, the way the server sees
 * the client.</li>
 * 
 * The servlet compares the sent token to the expected one, and if the timestamp
 * is within the given tolerance, then authentication will succeed.
 * 
 * Serves on the following URLs and HTTP methods:
 * 
 * GET method:
 * 
 * <li>[any path] e.g. /series1/contest2 - if a directory returns a JSON,
 * describing the resource, its children and files; otherwise returns the file
 * as a download.</li>
 * 
 * <li>/_zip/[any_path] e.g. /_zip/series1 - if a directory, returns a zip,
 * otherwise nothing.</li>
 * 
 * PUT method:
 * 
 * <li>[any_file_path] e.g. /series1/contest2/contest.json - if path is a file,
 * puts a file on the server in the destination, replacing the existing file (if
 * any).</li>
 * 
 * <li>/_zip/[any_resource_path] e.g. /_zip/series1 - if path is a directory,
 * unzips the given file to the server in the directory, replacing everything in
 * it.</li>
 * 
 * POST method:
 * 
 * <li>[resource_path] e.g. /series1/contest2 - creates a new sub-resource.</li>
 * 
 * <li>[resource_subdirectory_path] e.g. /series1/contest2/_files/test - creates
 * a subdirectory.</li>
 * 
 * DELETE method:
 * 
 * <li>[any_path] e.g. /series1/contest2 - deletes the given path (recursive if
 * directory).
 * 
 * @author jani
 * 
 */
@WebServlet("/rest")
public class RestServlet extends HttpServlet {
	private Logger log = LoggerFactory.getLogger(JsonUtil.class);
	private static final long serialVersionUID = -6732935281351769883L;
	private ConfigurationManager config = new ConfigurationManager();
	private UserManager userManager = new UserManager(config.getUsersRoot());

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Validator validator = isAuthenticated(request);

		if (validator.isValid()) {
			super.service(request, response);
		} else {
			sendInfo(request, response, RestConfig.HTTP_UNAUTHORIZED,
					validator.toString());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// Long stamp = new Date().getTime();
		// response.getWriter().println(stamp);
		// response.getWriter().println(sha256(request.getRemoteAddr() + stamp +
		// sha256("123123")));

		String login = getLogin(request);

		String path = (String) request.getAttribute("path");
		path = (path == null ? "" : path);
		List<String> parts = Node.splitPath(path);
		if (parts.size() > 0 && RestConfig.ZIP_URI_PREFIX.equals(parts.get(0))) {
			// contains _zip prefix
			parts.remove(0);
			path = CollectionUtil.join(parts, Node.PATH_SEPARATOR);
			Node node = new Node(config.getSeriesRoot(), path, login);
			if (node.getFile().isDirectory()) {
				byte[] file = node.getDownload();
				sendFile(response, node.getDownloadFilename(), file);
			} else {
				sendUnknown(response);
			}
		} else {
			Node node = new Node(config.getSeriesRoot(), path, login);

			if (node.getFile().isFile()) {
				byte[] file = node.getDownload();
				sendFile(response, node.getDownloadFilename(), file);
			} else {
				// building response json object
				Map<String, Object> res = new HashMap<String, Object>();
				if (node.getResouceKind() != ResourceKind.PROBLEM) {
					// fetching children
					List<Map<String, String>> children = new ArrayList<Map<String, String>>();
					for (Resource r : node.loadChildResources()) {
						Map map = JsonUtil.readJsonString(r.getBasicInfoJson(),
								HashMap.class);
						map.put("id", r.getId());
						children.add(map);
					}
					res.put("children", children);
				}
				if (!node.isRoot()) {
					// fetching resource descriptor
					Map resourceMap = new HashMap();
					Resource resource = node.getResouce();
					if (resource != null) {
						String json = resource.getJsonString(node.getAccess());
						resourceMap = JsonUtil.readJsonString(json, Map.class);
					}
					res.put(node.getResouceKind().getName(), resourceMap);

					// fetching additional files
					List<Map<String, Object>> files = new ArrayList<Map<String, Object>>();
					for (File file : node.getFiles()) {
						Map<String, Object> f = new HashMap<String, Object>();
						f.put("name", file.getName());
						f.put("size", file.length());
						f.put("is_dir", file.isDirectory());
						files.add(f);
					}

					res.put("files", files);
				}

				sendJson(response, RestConfig.HTTP_OK, res);
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String login = getLogin(request);

		String path = (String) request.getAttribute("path");
		path = (path == null ? "" : path);

		Node node = new Node(config.getSeriesRoot(), path, login);

		String name = request.getParameter("name");
		if (name == null) {
			sendError(response, "Name not supplied.");
		} else {
			Validator res;
			if (node.getType() == NodeType.SYSTEM_DIRECTORY) {
				res = node.addDirectory(name);
			} else {
				res = node.addResource(name);
			}
			if (res.isValid()) {
				sendCreated(response);
			} else {
				sendError(response, res.toString());
			}
		}
	}

	@Override
	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String login = getLogin(request);

		String path = (String) request.getAttribute("path");
		path = (path == null ? "" : path);
		List<String> parts = Node.splitPath(path);
		if (parts.size() > 0 && RestConfig.ZIP_URI_PREFIX.equals(parts.get(0))) {
			// contains _zip prefix
			parts.remove(0);
			path = CollectionUtil.join(parts, Node.PATH_SEPARATOR);
			Node node = new Node(config.getSeriesRoot(), path, login);

			Validator res = node.addZip(request.getInputStream());
			if (res.isValid()) {
				sendOk(response);
			} else {
				sendError(response, res.toString());
			}
		} else {
			Node node = new Node(config.getSeriesRoot(), path, login);
			if (node.getFile().isFile() || !node.getFile().exists()) {
				// last part contains the file name.
				String name = node.getParts().get(node.getParts().size() - 1);
				node = node.getParent();
				Validator res = node.addFile(request.getInputStream(), name,
						true);
				if (res.isValid()) {
					sendCreated(response);
				} else {
					sendError(response, res.toString());
				}
			} else {
				sendUnknown(response);
			}
		}
	}

	@Override
	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String login = getLogin(request);

		String path = (String) request.getAttribute("path");
		path = (path == null ? "" : path);

		Node node = new Node(config.getSeriesRoot(), path, login);

		Validator res = node.delete();
		if (res.isValid()) {
			sendOk(response);
		} else {
			sendError(response, res.toString());
		}
	}

	private void sendInfo(HttpServletRequest request,
			HttpServletResponse response, int status, String message) {
		Map<String, String> o = new HashMap<String, String>();
		o.put("server_stamp", "" + new Date().getTime());
		o.put("remote_ip", request.getRemoteAddr());
		o.put("message", message);
		sendJson(response, status, o);
	}

	private void sendFile(HttpServletResponse response, String filename,
			byte[] file) {
		response.setStatus(RestConfig.HTTP_OK);
		try {
			WebUtil.sendFile(file, filename, response);
		} catch (IOException e) {
			log.error("RestServlet.sendFile(): Unable to send file to output");
		}
	}

	private void sendJson(HttpServletResponse response, int status,
			Object object) {
		sendJson(response, status, JsonUtil.objectToJsonString(object));
	}

	private void sendJson(HttpServletResponse response, int status, String json) {
		response.setContentType(WebConfig.JSON_CONTENT_TYPE);
		response.setStatus(status);
		try {
			response.getWriter().println(json);
		} catch (IOException e) {
			log.error("RestServlet.sendJson(): Unable to send json to output");
		}
	}

	private void sendOk(HttpServletResponse response) {
		response.setStatus(RestConfig.HTTP_OK);
	}

	private void sendCreated(HttpServletResponse response) {
		response.setStatus(RestConfig.HTTP_CREATED);
	}

	private void sendUnknown(HttpServletResponse response) {
		response.setStatus(RestConfig.HTTP_BAD_REQUEST);
	}

	private void sendError(HttpServletResponse response, String error) {
		Map<String, String> json = new HashMap<String, String>();
		json.put("error", error);
		sendJson(response, RestConfig.HTTP_BAD_REQUEST, json);
	}

	private Validator isAuthenticated(HttpServletRequest request) {
		Long stamp = Parser.readLong(request.getParameter("stamp"));
		String login = getLogin(request);
		String inputToken = request.getParameter("token");
		User user = null;
		Validator res = new Validator();
		if (Math.abs(stamp - new Date().getTime()) > RestConfig.TIMESTAMP_TOLERANCE) {
			res.addError("stamp", "Invalid timestamp.");
		} else if (login == null) {
			res.addError("login", "Login not supplied.");
		} else if (inputToken == null) {
			res.addError("auth", "Auth token not supplied.");
		} else {
			user = userManager.loadUser(login);
			if (user == null) {
				res.addError("auth", "Invalid auth token.");
			} else {
				String token = calcAuthToken(request.getRemoteAddr(), stamp,
						user.getPassword());
				if (!token.equals(inputToken)) {
					res.addError("auth", "Invalid auth token.");
				}
			}
		}
		return res;
	}

	private String calcAuthToken(String ip, long stamp, String passwordHash) {
		String input = ip + stamp + passwordHash;
		return StringUtil.sha256(input);
	}

	private String getLogin(HttpServletRequest request) {
		return request.getParameter("login");
	}
}
