package core.config;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides configuration properties for the system.
 * 
 * @author deni, joro, jani
 */
public class ConfigurationManager {

	private static final String CONFIG_FILENAME = "core.properties";
	private static final String REPO_ROOT_PROP_NAME = "repo_root";
	private static final String USERS_ROOT_PROP_NAME = "users_root";
	private static final String SERIES_DIRNAME_PROP_NAME = "series_dirname";
	private static final String DEBUG_PROP_NAME = "debug";

	private final static Logger log = LoggerFactory
			.getLogger(ConfigurationManager.class);

	private Properties properties;

	/**
	 * Creates an instance of the configuration manager.
	 */
	public ConfigurationManager() {
		properties = new Properties();
		try {
			properties.load(getClass().getResourceAsStream(
					"/" + CONFIG_FILENAME));
		} catch (IOException e) {
			log.error(String.format(
					"ConfigurationManager(): Could not load config file %s",
					CONFIG_FILENAME));
		}

		if (!(getRepoRoot().exists() && getRepoRoot().isDirectory())) {
			log.error(String
					.format("ConfigurationManager(): Repo root directory %s does not exists!",
							getRepoRoot().getAbsoluteFile()));
		}
	}

	/**
	 * Gets the property with the specified key.
	 * 
	 * @param key
	 *            the property key
	 * @return the property value
	 */
	private String getProperty(String key) {
		return properties.getProperty(key);
	}

	/**
	 * Gets the main directory in which repository is stored.
	 * 
	 * @return the main directory in which repository is stored
	 */
	public File getRepoRoot() {
		return new File(getProperty(REPO_ROOT_PROP_NAME));
	}

	/**
	 * Gets the relative path to the directory containing all the series.
	 * 
	 * @return the relative path to the directory containing all the series.
	 */
	public String getSeriesDirName() {
		return getProperty(SERIES_DIRNAME_PROP_NAME);
	}

	/**
	 * Gets the main directory in which users are stored.
	 * 
	 * @return the main directory in which users are stored
	 */
	public File getUsersRoot() {
		return new File(getProperty(USERS_ROOT_PROP_NAME));
	}

	/**
	 * Gets the debug mode of the app.
	 * 
	 * @return The debug mode.
	 */
	public boolean getDebug() {
		return "true".equals(getProperty(DEBUG_PROP_NAME));
	}

	/**
	 * Gets the full path to the directory containing all the series.
	 * 
	 * @return the full path to the directory containing all the series.
	 */
	public File getSeriesRoot() {
		return new File(getRepoRoot(), getSeriesDirName());
	}
}
