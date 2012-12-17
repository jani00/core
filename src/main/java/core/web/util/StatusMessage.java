package core.web.util;

import core.config.WebConfig;
import core.util.CollectionUtil;
import core.util.Validator;

/**
 * A status message of a user action.
 * 
 * @author jani
 * 
 */
public class StatusMessage {

	/**
	 * Possible types.
	 * 
	 */
	public enum Type {
		/**
		 * Status is a success.
		 */
		SUCCESS,
		/**
		 * Status is a fail.
		 */
		FAIL;
	}

	private Type type;
	private String message;

	/**
	 * Constructor
	 */
	public StatusMessage() {
		this("");
	}

	/**
	 * Constructor
	 * 
	 * @param message
	 */
	public StatusMessage(String message) {
		this(Type.SUCCESS, message);
	}

	/**
	 * Constructor
	 * 
	 * @param validator
	 * @param message
	 */
	public StatusMessage(Validator validator, String message) {
		this(message);
		setFromValidator(validator, "");
	}

	/**
	 * Constructor
	 * 
	 * @param type
	 * @param message
	 */
	public StatusMessage(Type type, String message) {
		this.type = type;
		this.message = message;
	}

	/**
	 * Sets a status message from a validator. If validator is valid,
	 * successMessage is set.
	 * 
	 * @param validator
	 * @param successMessage
	 */
	public void setFromValidator(Validator validator, String successMessage) {
		if (validator.isValid()) {
			type = Type.SUCCESS;
			message = successMessage;
		} else {
			type = Type.FAIL;
			this.message = CollectionUtil.join(validator.getErrors()
					.values().toArray(), WebConfig.HTML_NEWLINE);
		}
	}

	/**
	 * Gets the type of the message.
	 * 
	 * @return {@link Type}
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Gets the message.
	 * 
	 * @return String
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the type of the message.
	 * 
	 * @param type
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * Sets the message
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Gets the CSS class of the message
	 * 
	 * @return String
	 */
	public String getCssClass() {
		return getType().toString().toLowerCase();
	}
}
