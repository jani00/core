package core.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.config.Constants;
import core.config.WebConfig;
import core.web.util.FileUpload;

/**
 * Web related helper methods.
 * 
 * @author jani
 * 
 */
public class WebUtil {

	private static Logger log = LoggerFactory.getLogger(WebUtil.class);

	/**
	 * Sends a file to a response.
	 * 
	 * @param file
	 * @param response
	 * @throws IOException
	 */
	public static void sendFile(File file, HttpServletResponse response)
			throws IOException {
		FileInputStream stream = new FileInputStream(file);
		try {
			sendFile(stream, file.getName(), response);
		} finally {
			stream.close();
		}
	}

	/**
	 * Sends a file to a response.
	 * 
	 * @param file
	 * @param response
	 * @return Whether successful.
	 */
	public static boolean send(File file, HttpServletResponse response) {
		if (file.exists()) {
			if (file.isFile()) {
				try {
					WebUtil.sendFile(file, response);
					return true;
				} catch (IOException e) {
					log.info(String.format("WebUtil.send(): Unable to send file %s", file));
					return false;
				}
			} else if (file.isDirectory()) {
				try {
					WebUtil.sendDirectory(file, response);
					return true;
				} catch (IOException e) {
					log.info(String.format("WebUtil.send(): Unable to send directory %s", file));
					return false;
				}
			}
		}
		return false;

	}

	/**
	 * Sends a directory as zip to a response.
	 * 
	 * @param file
	 * @param response
	 * @throws IOException
	 */
	public static void sendDirectory(File file, HttpServletResponse response)
			throws IOException {
		byte[] zip = FileUtil.createZip(file);
		sendFile(zip, file.getName() + Constants.ZIP_FILE_EXTENSION, response);
	}

	/**
	 * Sends a byte array to a response as a download.
	 * 
	 * @param file
	 * @param filename
	 * @param response
	 * @throws IOException
	 */
	public static void sendFile(byte[] file, String filename,
			HttpServletResponse response) throws IOException {
		InputStream stream = new ByteArrayInputStream(file);
		sendFile(stream, filename, response);
		stream.close();
	}

	/**
	 * Sends an input stream as a file download to a response.
	 * 
	 * @param input
	 * @param filename
	 * @param response
	 * @throws IOException
	 */
	public static void sendFile(InputStream input, String filename,
			HttpServletResponse response) throws IOException {

		response.setContentType(WebConfig.DOWNLOAD_CONTENT_TYPE);
		response.setHeader("Content-Disposition",
				String.format("attachment;filename=\"%s\"", filename));

		final int bufferSize = 2 ^ 12;

		ServletOutputStream output = response.getOutputStream();
		byte[] outputByte = new byte[bufferSize];
		int read;
		while ((read = input.read(outputByte, 0, bufferSize)) != -1) {
			output.write(outputByte, 0, read);
		}
		output.close();

	}

	/**
	 * Gets a map of request parameters.
	 * 
	 * @param request
	 *            Request to parse.
	 * @param paramNames
	 *            Parameter names.
	 * @param prefix
	 *            Prefix of names.
	 * @return Map.
	 */
	public static Map<String, Object> getRequestParameters(
			HttpServletRequest request, List<String> paramNames, String prefix) {

		HashMap<String, Object> res = new HashMap<String, Object>();

		for (String s : paramNames) {
			String paramName = prefix + s;
			res.put(s, request.getParameter(paramName));
		}

		return res;
	}

	/**
	 * Parses a multipart form request.
	 * 
	 * @param request
	 * @param post
	 *            Post parameters are returned here.
	 * @param files
	 *            File parameters are returned here.
	 */
	public static void parseRequest(HttpServletRequest request,
			Map<String, String> post, Map<String, FileUpload> files) {
		post.clear();
		files.clear();

		if (ServletFileUpload.isMultipartContent(request)) {
			FileItemFactory factory = new DiskFileItemFactory();

			ServletFileUpload upload = new ServletFileUpload(factory);

			try {
				@SuppressWarnings("unchecked")
				List<DiskFileItem> items = upload.parseRequest(request);

				Iterator<DiskFileItem> iter = items.iterator();
				while (iter.hasNext()) {
					FileItem item = iter.next();

					if (item.isFormField()) {
						String name = item.getFieldName();
						String value = item.getString(WebConfig.DEFAULT_REQUEST_ENCODING);
						post.put(name, value);
					} else {
						String fieldname = item.getFieldName();
						String filename = item.getName();
						String contentType = item.getContentType();
						long size = item.getSize();
						files.put(fieldname, new FileUpload(filename,
								contentType, size, item));

					}
				}
			} catch (FileUploadException e) {
				log.error("Error while parsing file upload request");
			} catch (UnsupportedEncodingException e) {
				log.error("Error while parsing file upload request");
			}
		} else {
			log.error("Trying to parse a non-multipart form");
		}

	}

}
