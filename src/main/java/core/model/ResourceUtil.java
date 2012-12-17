package core.model;

import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.config.Constants;
import core.config.ResourceConfig;
import core.config.ResourceFileConfig;
import core.security.AccessRight;
import core.util.JsonUtil;
import core.util.Validator;

/**
 * Contains helper methods for resource manipulation.
 * 
 * @author jani
 * 
 */
public class ResourceUtil {

	private static Logger log = LoggerFactory.getLogger(ResourceUtil.class);

	/**
	 * Loads a resource from a given location and kind.
	 * 
	 * @param <T>
	 * @param directory
	 * @param kind
	 * @return Resource.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T loadResource(File directory, final ResourceKind kind) {

		Resource res = null;
		if (directory.isDirectory() && directory.exists()) {
			String filename = kind.getName() + Constants.JSON_FILE_EXTENSION;
			File file = new File(directory, filename);
			if (file.exists() && file.isFile()) {
				res = JsonUtil.readJsonFile(file, kind.getResourceClass());
				if (res != null) {
					res.setId(directory.getName());
					res.setDirectory(directory);
				}
			} else {
				log.info(String.format(
						"ResourceUtil.loadResource(): File %s does not exist",
						file.getPath()));
			}
		} else {
			log.info(String.format(
					"ResourceUtil.loadResource(): directory %s does not exist",
					directory.getPath()));
		}
		return (T) res;
	}

	/**
	 * Loads a resource from a stream, id and kind.
	 * 
	 * @param <T>
	 * @param stream
	 * @param id
	 * @param kind
	 * @return Resource.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T loadResource(InputStream stream, String id,
			final ResourceKind kind) {
		Resource res = JsonUtil.readJsonFile(stream, kind.getResourceClass());
		if (res != null) {
			res.setId(id);
		}
		return (T) res;
	}

	/**
	 * Converts a JSON string to a resource;
	 * 
	 * @param json
	 * @param kind
	 * @param <T>
	 * @return {@link Resource}
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fromJsonString(String json, final ResourceKind kind) {

		Resource res = JsonUtil.readJsonString(json, kind.getResourceClass());

		return (T) res;
	}

	/**
	 * Gets a list of files of child resources.
	 * 
	 * @param directory
	 * @return List of files.
	 */
	public static List<File> getChildResourceList(File directory) {
		List<File> res = new ArrayList<File>();

		if (directory.isDirectory() && directory.exists()) {
			File[] files = directory.listFiles(new FileFilter() {
				@Override
				public boolean accept(File dir) {
					return dir.isDirectory() &&

					dir.getName().matches(ResourceFileConfig.RESOURCE_ID);
				}
			});
			res.addAll(Arrays.asList(files));

		} else {
			log.info(String
					.format("ResourceUtil.getChildResourceList(): directory %s does not exists",
							directory.getPath()));
		}

		return res;
	}

	/**
	 * Loads child resources based on a directory and kind.
	 * 
	 * @param <T>
	 * @param directory
	 * @param kind
	 * @return List of resources.
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> loadChildResources(File directory,
			final ResourceKind kind) {
		List<T> res = new ArrayList<T>();

		List<File> files = getChildResourceList(directory);
		for (File file : files) {
			Resource r = loadResource(file, kind);
			if (r != null) {
				res.add((T) r);
			}
		}

		return res;
	}

	/**
	 * Gets an {@link AccessRight} for a directory and login. It traverses from
	 * the specified directory, up to the repository root. The maximum found
	 * {@link AccessRight} is returned.
	 * 
	 * @param seriesRoot
	 *            Root directory for series.
	 * 
	 * @param directory
	 *            Directory to get {@link AccessRight} for.
	 * @param login
	 *            User login to get {@link AccessRight} for.
	 * @return {@link AccessRight}
	 */
	public static AccessRight getAccessRight(File seriesRoot, File directory,
			String login) {
		AccessRight res = AccessRight.NONE;

		if (login != null && !login.isEmpty()) {
			File start = directory;
			File currentPath = directory;

			if (currentPath.exists()) {
				while (!currentPath.equals(seriesRoot.getParentFile())) {
					AccessRight right = loadAccessRight(currentPath, login);

					if ((right == AccessRight.LIST && currentPath.equals(start))
							|| (right != AccessRight.LIST && (right
									.includes(res)))) {
						res = right;
					}
					currentPath = currentPath.getParentFile();

				}
			}
		}
		return res;
	}

	/**
	 * Loads an {@link AccessRight} for a directory and login. Parses security
	 * file and returns grants.
	 * 
	 * @param directory
	 * @param login
	 * @return {@link AccessRight}
	 */
	@SuppressWarnings("unchecked")
	public static AccessRight loadAccessRight(File directory, String login) {
		AccessRight res = AccessRight.NONE;

		File securityFile = new File(directory,
				ResourceFileConfig.SECURITY_FILENAME);

		if (securityFile.exists()) {
			Map<String, Object> map = JsonUtil.readJsonFile(securityFile,
					Map.class);
			assert map != null;
			Map<String, Object> grants = (Map<String, Object>) map
					.get(ResourceConfig.GRANTS_PROP_NAME);
			if (grants != null) {
				String grant = (String) grants.get(login);
				if (grant != null) {
					res = AccessRight.fromString(grant);
				}
			}
		}

		return res;
	}

	/**
	 * Creates a child resource in the specified directory, id and kind.
	 * 
	 * @param directory
	 *            The parent directory of the new resource.
	 * @param id
	 * @param kind
	 * @return {@link Validator}
	 */
	public static Validator createResource(File directory, String id,
			ResourceKind kind) {
		Validator res = new Validator();

		if (id.matches(ResourceFileConfig.RESOURCE_ID)) {
			File dir = new File(directory, id);
			if (dir.exists()) {
				res.addError("id",
						String.format("Resource with id '%s' exists", id));
			} else {
				dir.mkdir();
				Class<? extends Resource> clazz = kind.getResourceClass();
				try {
					Resource resource = clazz.newInstance();
					resource.setId(id);
					resource.setDirectory(dir);
					resource.save();
				} catch (Exception e) {
					log.info(String
							.format("ResourceUtil.createResource(): Unable to instantiate resource of type %s",
									clazz.getSimpleName()));
				}
			}
		} else {
			res.addError("id", "Invalid resource id");
		}
		return res;
	}

	/**
	 * Gets a list of form parameter names, according to the resource kind.
	 * 
	 * @param kind
	 * @return List of parameter names.
	 */
	public static List<String> getResourceFormFields(ResourceKind kind) {
		String[] res = null;
		switch (kind) {
		case SERIES:
			res = new String[] { "title", "about", "notes" };
			break;
		case CONTEST:
			res = new String[] { "title", "about", "startTime", "duration",
					"gradingStyle", "problemOrderString",
					"problemScoresString", "notes" };
			break;
		case PROBLEM:
			res = new String[] { "title", "about", "origin", "authorsString",
					"testWeightsString", "memoryLimit", "timeLimit", "checker",
					"notes" };
			break;
		}
		return Arrays.asList(res);
	}
}
