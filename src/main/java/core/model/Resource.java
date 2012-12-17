package core.model;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.config.Constants;
import core.config.ResourceConfig;
import core.config.ResourceFileConfig;
import core.security.AccessRight;
import core.security.ResourceViews;
import core.util.JsonUtil;
import core.util.Validator;
import core.web.util.DownloadBuilder;

/**
 * Represents a resource entity from the system. Provides getters and setters
 * for the corresponding attributes in the json.
 * 
 * @author Svetla Marinova, M23322, joro, petko, jani
 */

public abstract class Resource {

	private Logger log = LoggerFactory.getLogger(Resource.class);

	@JsonIgnore
	@JsonView(ResourceViews.Basic.class)
	private String id;

	@JsonView(ResourceViews.Basic.class)
	private String format;

	@JsonView(ResourceViews.Basic.class)
	private String title;

	@JsonView(ResourceViews.Basic.class)
	private String about;

	@JsonView(ResourceViews.Private.class)
	private String notes;

	private Validator validator;

	private File directory;

	/**
	 * Constructs an instance of the resource.
	 */
	public Resource() {
		format = "";
		title = "";
		about = "";
		notes = "";

		validator = new Validator();

		setDirectory(null);
	}

	/**
	 * Gets the resource identifier.
	 * 
	 * @return the resource id
	 */
	@JsonIgnore
	public String getId() {
		return id;
	}

	/**
	 * Sets the resource identifier.
	 * 
	 * @param id
	 *            the new resource id
	 */
	@JsonIgnore
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the resource json file format.
	 * 
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * Sets the resource json file format.
	 * 
	 * @param format
	 *            the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * Gets the resource title.
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the resource title.
	 * 
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the resource about.
	 * 
	 * @return the about
	 */
	public String getAbout() {
		return about;
	}

	/**
	 * Sets the resource about.
	 * 
	 * @param about
	 *            the about to set
	 */
	public void setAbout(String about) {
		this.about = about;
	}

	/**
	 * Gets the resource notes.
	 * 
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * Sets the resource notes.
	 * 
	 * @param notes
	 *            the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Override
	public String toString() {
		return String.format("Resource: type=%s, id=%s", this.getClass()
				.getSimpleName(), this.id);
	}

	/**
	 * Get the validator for the resource
	 * 
	 * @return Validator validator of resource
	 */
	@JsonIgnore
	public Validator getValidator() {
		return validator;
	}

	/**
	 * Validates the resource.
	 * 
	 * @return Validator validator of resource
	 */
	public Validator validate() {
		Validator res = JsonUtil.validateJson(this, getSchemaFilename());
		if (getDirectory() != null
				&& !(getDirectory().exists() && getDirectory().isDirectory())) {
			res.addError("directory_exists", String.format(
					"Directory for resource does not exists: %s",
					getDirectory()));
		}
		if (getFile() != null && !(getFile().exists() && getFile().isFile())) {
			res.addError("file", String.format(
					"JSON file for resource does not exists: %s", getFile()));
		}

		if (getDirectory() != null) {
			String directoryName = getDirectory().getName();
			if (!directoryName.equals(getId())) {
				res.addError("directory_id", String.format(
						"Resource ID and directory name mismatch: %s - %s",
						directoryName, getId()));
			}
			if (!directoryName.matches(ResourceFileConfig.RESOURCE_ID)) {
				res.addError("directory", String.format(
						"Directory name is invalid: %s", directoryName));
			}
		}
		if (!getId().matches(ResourceFileConfig.RESOURCE_ID)) {
			res.addError("id",
					String.format("Resource ID is invalid: %s", getId()));
		}

		return res;
	}

	/**
	 * Sets the directory in which the resource resides.
	 * 
	 * @param directory
	 *            Directory of the resource.
	 */
	public void setDirectory(File directory) {
		this.directory = directory;
	}

	/**
	 * Gets the directory in which the resource resides.
	 * 
	 * @return The directory in which the resource resides.
	 */
	@JsonIgnore
	public File getDirectory() {
		return directory;
	}

	/**
	 * Gets the JSON file for the resource.
	 * 
	 * @return JSON file for the resource.
	 */
	@JsonIgnore
	public File getFile() {
		if (directory == null) {
			return null;
		}

		return new File(directory, getFilename());

	}

	/**
	 * Calculates the root directory for the series.
	 * 
	 * @return {@link File}
	 */
	@JsonIgnore
	public File getSeriesRoot() {
		File res = getDirectory();
		ResourceKind kind = getKind();
		while (kind != null) {
			res = res.getParentFile();
			kind = kind.getPredecessorKind();
		}
		return res;
	}

	/**
	 * Gets the JSON file name for the resource.
	 * 
	 * @return JSON file name for the resource.
	 */
	@JsonIgnore
	public String getFilename() {
		return String.format("%s%s", getKind().getName(),
				Constants.JSON_FILE_EXTENSION);
	}

	/**
	 * Gets the corresponding JSON schema for this resource.
	 * 
	 * @return JSON schema for this resource.
	 */
	@JsonIgnore
	public String getSchemaFilename() {
		return String.format("%s/%s%s", ResourceConfig.RESOURCE_SCHEMA_PATH,
				getKind().getName(), Constants.JSON_FILE_EXTENSION);
	}

	/**
	 * Loads the children of the resource. The children are resources. The kind
	 * of the children is calculated, depending on the nesting of the resource.
	 * 
	 * @param <T>
	 *            Type of children.
	 * @return A list of all resource children resources.
	 */
	public <T> List<T> loadChildren() {
		ResourceKind childKind = getKind().getSuccessorKind();
		return ResourceUtil.loadChildResources(getDirectory(), childKind);
	}

	/**
	 * Loads the parent resource of the current resource.
	 * 
	 * @param <T>
	 *            The type of the parent.
	 * @return Parent resource.
	 */
	public <T> T loadParent() {
		T res = null;

		ResourceKind parentKind = getKind().getPredecessorKind();

		if (parentKind != null && getDirectory().isDirectory()
				&& getDirectory().exists()) {
			res = ResourceUtil.loadResource(getDirectory().getParentFile(),
					parentKind);
		} else {
			log.info(String.format(
					"Resource.loadParent(): directory %s does not exists",
					directory.getPath()));
		}

		return res;
	}

	/**
	 * Gets the access right for specified login against the current resource.
	 * 
	 * @param login
	 *            Login to get AccessRight for.
	 * @return AccessRight for specified login.
	 */
	public AccessRight getAccessRight(String login) {
		return ResourceUtil.getAccessRight(getSeriesRoot(), getDirectory(),
				login);
	}

	/**
	 * Gets the basic resource info as JSON string.
	 * 
	 * @return JSON string
	 */
	@JsonIgnore
	public String getBasicInfoJson() {
		return JsonUtil.objectToJsonString(this, ResourceViews.Basic.class);
	}

	/**
	 * Saves (persists) the resource to the file system.
	 */
	public void save() {
		if (directory.isDirectory() && directory.exists()) {
			String filename = getKind().getName()
					+ Constants.JSON_FILE_EXTENSION;

			File jsonFile = new File(directory, filename);
			JsonUtil.writeJsonFile(jsonFile, this);
			createCommonFiles();
		} else {
			log.info(String.format(
					"Resource.save(): directory %s does not exists",
					directory.getPath()));
		}
	}

	private void createCommonFiles() {
		File security = new File(directory,
				ResourceFileConfig.SECURITY_FILENAME);
		if (!security.exists()) {
			if (security.isDirectory()) {
				security.delete();
			}
			HashMap<String, HashMap<String, String>> hash = new HashMap<String, HashMap<String, String>>();
			hash.put(ResourceConfig.GRANTS_PROP_NAME,
					new HashMap<String, String>());
			JsonUtil.writeJsonFile(security, hash);
		}
		String[] dirs = { ResourceFileConfig.RESOURCE_PUBLIC_DIRECTORY_NAME,
				ResourceFileConfig.RESOURCE_PRIVATE_DIRECTORY_NAME };
		for (String dir : dirs) {
			File newdir = new File(directory, dir);
			if (newdir.isFile()) {
				newdir.delete();
			}
			if (!newdir.exists()) {
				newdir.mkdir();
			}
		}
	}

	/**
	 * Gets the ResourceKind of the resource.
	 * 
	 * @return ResourceKind
	 */
	@JsonIgnore
	public abstract ResourceKind getKind();

	/**
	 * Creates a downloader for a specific user for the resource, according to
	 * the security policy
	 * 
	 * @param login
	 *            Login to create a downloader for.
	 * @param access
	 *            Starting AccessRight.
	 * @return Downloader for the resource.
	 */
	@JsonIgnore
	public abstract DownloadBuilder getDownloader(String login,
			AccessRight access);

	/**
	 * Get the JSON object, describing the resource as a string, according to
	 * the security policy
	 * 
	 * @param access
	 *            active {@link AccessRight}.
	 * @return JSON string
	 */
	@JsonIgnore
	public abstract String getJsonString(AccessRight access);

	/**
	 * Gets a list of files which a visible for the specified access right.
	 * 
	 * @param access
	 *            AccessRight
	 * @return List of files.
	 */
	@JsonIgnore
	public abstract List<File> getFilesList(AccessRight access);
}
