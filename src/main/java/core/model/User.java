package core.model;

import java.util.ArrayList;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import core.config.ResourceConfig;
import core.config.ResourceFileConfig;
import core.security.UserManager;
import core.util.CollectionUtil;
import core.util.JsonUtil;
import core.util.Validator;

/**
 * A user of the system.
 * 
 * @author deni, joro, jani
 */
public class User {

	private String login;
	private String password;
	private String confirmPassword;
	private String email;

	@JsonProperty("real_name")
	private String realName;
	private String city;
	private ArrayList<String> organizations;
	private String about;

	private Validator validator;

	/**
	 * Creates a new user.
	 */
	public User() {
		login = "";
		password = "";
		confirmPassword = "";
		email = "";
		realName = "";
		city = "";
		about = "";
		organizations = new ArrayList<String>();
		validator = new Validator();
	}

	/**
	 * Gets the login.
	 * 
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * Gets the password of the user.
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password of the user.
	 * 
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the email of the user.
	 * 
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email of the user.
	 * 
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the real name of the user.
	 * 
	 * @return the realName
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * Sets the real name of the user.
	 * 
	 * @param realName
	 *            the realName to set
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 * Gets the city of the user.
	 * 
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Sets the city of the user.
	 * 
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Gets the organizations the user belongs to.
	 * 
	 * @return the organizations
	 */
	public ArrayList<String> getOrganizations() {
		return organizations;
	}

	/**
	 * Gets a comma separated list of organizations
	 * 
	 * @return String representation of organizations.
	 */
	@JsonIgnore
	public String getOrganizationsString() {
		return CollectionUtil.join(organizations, ", ");
	}

	/**
	 * Adds organizations the user belongs to.
	 * 
	 * @param organizations
	 *            the organizations to set
	 */
	public void setOrganizations(ArrayList<String> organizations) {
		this.organizations = organizations;
	}

	/**
	 * Sets the organizations from a string. The string is comma split and empty
	 * values are omitted.
	 * 
	 * @param organizations
	 *            Comma separated organizations.
	 */
	public void setOrganizationsString(String organizations) {
		this.organizations.clear();
		String[] split = organizations.split(",");
		for (String s : split) {
			s = s.trim();
			if (!s.isEmpty()) {
				this.organizations.add(s);
			}
		}
	}

	/**
	 * Gets the additional information about the user.
	 * 
	 * @return the about
	 */
	public String getAbout() {
		return about;
	}

	/**
	 * Sets the additional information about the user.
	 * 
	 * @param about
	 *            the about to set
	 */
	public void setAbout(String about) {
		this.about = about;
	}

	/**
	 * Sets the login.
	 * 
	 * @param login
	 *            the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * Gets the confirmPassword field.
	 * 
	 * @return the confirmPassword
	 */
	@JsonIgnore
	public String getConfirmPassword() {
		return confirmPassword;
	}

	/**
	 * Sets the confirmPassword field.
	 * 
	 * @param confirmPassword
	 *            the confirmPassword to set
	 */
	@JsonIgnore
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	/**
	 * Validated the user, assuming the it is being created.
	 * 
	 * @param userManager
	 *            {@link UserManager} for the user.
	 * @return {@link Validator}, containing the result.
	 */
	@JsonIgnore
	public Validator validate(UserManager userManager) {
		return validate(userManager, false);
	}

	/**
	 * Validates the user.
	 * 
	 * @param userManager
	 *            {@link UserManager} for the user.
	 * 
	 * @param update
	 *            Indicated whether an update or a creation is performed. Login
	 *            is not checked for availability when updating.
	 * 
	 * @return {@link Validator}, containing the result.
	 */
	@JsonIgnore
	public Validator validate(UserManager userManager, boolean update) {
		Validator v = JsonUtil.validateJson(this, getSchemaFilename());

		validator.merge(v);

		validator.validateRegex(login, ResourceFileConfig.RESOURCE_ID, "login",
				"Valid username is required");
		validator.validateEmail(email, "email", "Valid email is required");
		validator.validateRequired(realName, "realName",
				"Real name is required");
		validator
				.validateRequired(password, "password", "Password is required");
		validator.validateEqual(password, confirmPassword, "confirmPassword",
				"Password do not match");

		if (!update) {
			if (userManager.isLoginTaken(login)) {
				validator.addError("login", "Username is not available.");
			}
		}

		return validator;
	}

	/**
	 * Gets the JSON schema file name, used for validation.
	 * 
	 * @return JSON schema file name.
	 */
	@JsonIgnore
	public String getSchemaFilename() {
		return String.format("%s/%s", ResourceConfig.RESOURCE_SCHEMA_PATH,
				ResourceFileConfig.USER_JSON_FILENAME);
	}

	/**
	 * Gets the validator of the user.
	 * 
	 * @return {@link Validator}.
	 */
	@JsonIgnore
	public Validator getValidator() {
		return validator;
	}

	/**
	 * Hashes the user password.
	 */
	public void hashPassword() {
		this.password = UserManager.getPasswordHash(this.password);
	}
}
