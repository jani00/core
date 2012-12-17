package core.security;

import java.io.File;
import core.config.ResourceFileConfig;
import core.model.User;
import core.util.JsonUtil;
import core.util.StringUtil;
import core.util.Validator;

/**
 * Manages getting, editing and creation of users.
 * 
 * @author deni, joro, jani
 */
public class UserManager {
	private File usersRoot;

	/**
	 * Creates and instance of the user manager.
	 * 
	 * @param usersRoot
	 *            Root directory for users.
	 */
	public UserManager(File usersRoot) {
		this.usersRoot = usersRoot;
	}

	/**
	 * Checks whether a user with the specified login already exists.
	 * 
	 * @param login
	 *            the login of the user
	 * @return true if a user with the given login name already exists, false
	 *         otherwise
	 */
	public boolean isLoginTaken(String login) {
		File userJsonFile = getUserFile(login);
		return userJsonFile.exists();
	}

	/**
	 * Gets the directory for the specified username.
	 * 
	 * 
	 * @param username
	 * @return Directory for the specified username.
	 */
	public File getUserDirectory(String username) {
		return new File(usersRoot, username);
	}

	/**
	 * Gets the JSON file for the specified username.
	 * 
	 * 
	 * @param username
	 * @return File for the specified username
	 */
	public File getUserFile(String username) {
		return new File(getUserDirectory(username),
				ResourceFileConfig.USER_JSON_FILENAME);
	}

	/**
	 * Checks authorization credentials of the user.
	 * 
	 * @param username
	 * @param password
	 * @return true if the credentials are valid, false otherwise
	 */
	public boolean isAuthorized(String username, String password) {
		User user = loadUser(username);
		if (user != null) {
			return getPasswordHash(password).equals(user.getPassword());
		}
		return false;
	}

	/**
	 * Loads and parses the specified JSON file.
	 * 
	 * @param jsonFile
	 * @return The user, corresponding to the JSON file.
	 */
	public User loadUser(File jsonFile) {
		User res = JsonUtil.readJsonFile(jsonFile, User.class);
		if (res != null) {
			res.setConfirmPassword(res.getPassword());
		}
		return res;
	}

	/**
	 * Loads the {@link User} object for the specified username.
	 * 
	 * @param username
	 * @return The user, corresponding to the username.
	 */
	public User loadUser(String username) {
		User res = null;
		if (Validator.isMatch(username, ResourceFileConfig.RESOURCE_ID)) {
			File file = getUserFile(username);
			res = loadUser(file);
		}
		return res;
	}

	/**
	 * Saves (persists) the user to the file system.
	 * 
	 * @param user
	 *            The {@link User} object to save.
	 * 
	 * 
	 * @return if successful.
	 */
	public boolean save(User user) {
		File directory = getUserDirectory(user.getLogin());
		if (!directory.exists()) {
			directory.mkdir();
		}
		return JsonUtil.writeJsonFile(getUserFile(user.getLogin()), user);
	}

	/**
	 * Gets the SHA-256 hash code of the given password.
	 * 
	 * @param password
	 *            the password whose hash code should be returned.
	 * @return the SHA-256 hash code of the given password
	 */
	public static String getPasswordHash(String password) {
		return StringUtil.sha256(password);
	}

	/**
	 * Validates all users in the repository with the given root and outputs the
	 * result in the console.
	 * 
	 * @param usersRoot
	 *            Root directory for users.
	 * 
	 * @return {@link Validator}
	 */
	public Validator validateAll() {
		Validator res = new Validator();
		if (!usersRoot.exists() || !usersRoot.isDirectory()) {
			res.addError("directory", "Could not find users directory");
			return res;
		}

		File[] list = usersRoot.listFiles();
		for (File dir : list) {
			if (dir.isDirectory()) {
				User user = loadUser(dir.getName());
				Validator sub = user.validate(this, true);
				if (!sub.isValid()) {
					res.addError("user_" + user.getLogin(), user.getLogin()
							+ " is invalid");
				}
			}
		}
		return res;
	}
}