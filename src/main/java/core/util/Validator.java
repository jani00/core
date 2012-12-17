package core.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * 
 * Utility class for validation. Contains a map of errors.
 * 
 * @author jani
 * 
 */
public class Validator {
	private Map<String, String> errors;

	/**
	 * Default constructor.
	 */
	public Validator() {
		errors = new HashMap<String, String>();
	}

	/**
	 * Checks if an error with the specified key exists
	 * 
	 * @param key
	 * @return whether the error exists
	 */
	public boolean hasError(String key) {
		return errors.containsKey(key);
	}

	/**
	 * Gets all errors.
	 * 
	 * @return All errors.
	 */
	public Map<String, String> getErrors() {
		return errors;
	}

	/**
	 * Adds an error.
	 * 
	 * @param key
	 * @param message
	 */
	public void addError(String key, String message) {
		errors.put(key, message);
	}

	/**
	 * Adds all errors while prefixing their keys.
	 * 
	 * @param prefix
	 * @param messages
	 */
	public void addErrors(String prefix, List<String> messages) {
		int index = 0;
		for (String message : messages) {
			addError(prefix + index, message);
			index++;
		}
	}

	/**
	 * Gets an error.
	 * 
	 * @param key
	 * @return Error
	 */
	public String getError(String key) {
		return errors.get(key);

	}

	/**
	 * Clears all errors.
	 */
	public void reset() {
		errors.clear();
	}

	/**
	 * Checks if validator is valid, i.e. contains no errors.
	 * 
	 * @return Whether is valid.
	 */
	public boolean isValid() {
		return errors.size() == 0;
	}

	/**
	 * Validates a required field.
	 * 
	 * @param input
	 *            Input string.
	 * @param key
	 *            Error key.
	 * @param message
	 *            Error message.
	 * @return Whether is valid.
	 */
	public boolean validateRequired(String input, String key, String message) {
		if (isEmpty(input)) {
			addError(key, message);
			return false;
		}
		return true;
	}

	/**
	 * Validates a field against a regular expression.
	 * 
	 * @param input
	 *            Input string.
	 * @param regex
	 *            Regular expression.
	 * @param key
	 *            Error key.
	 * @param message
	 *            Error message.
	 * @return Whether is valid.
	 */
	public boolean validateRegex(String input, String regex, String key,
			String message) {
		if (!isMatch(input, regex)) {
			addError(key, message);
			return false;
		}
		return true;
	}

	/**
	 * Validates a minimum length of a field.
	 * 
	 * @param input
	 *            Input string.
	 * @param minLength
	 *            Minimum length, inclusive.
	 * @param key
	 *            Error key.
	 * @param message
	 *            Error message.
	 * @return Whether is valid.
	 */
	public boolean validateLength(String input, int minLength, String key,
			String message) {
		if (!hasLength(input, minLength)) {
			addError(key, message);
			return false;
		}
		return true;
	}

	/**
	 * Validates an email address.
	 * 
	 * @param input
	 *            Input string.
	 * @param key
	 *            Error key.
	 * @param message
	 *            Error message.
	 * @return Whether is valid.
	 */
	public boolean validateEmail(String input, String key, String message) {
		if (!isEmail(input)) {
			addError(key, message);
			return false;
		}
		return true;
	}

	/**
	 * Validates an equality.
	 * 
	 * @param input1
	 *            Input string.
	 * @param input2
	 *            Input string.
	 * @param key
	 *            Error key.
	 * @param message
	 *            Error message.
	 * @return Whether is valid.
	 */
	public boolean validateEqual(String input1, String input2, String key,
			String message) {
		if (!areEqual(input1, input2)) {
			addError(key, message);
			return false;
		}
		return true;
	}

	/**
	 * Helper method to check of string is empty.
	 * 
	 * @param input
	 * @return Whether is empty.
	 */
	public static boolean isEmpty(String input) {
		return input == null || input.isEmpty();
	}

	/**
	 * Helper method to check if input is a match to a regex.
	 * 
	 * @param input
	 * @param regex
	 * @return Whether is a match.
	 */
	public static boolean isMatch(String input, String regex) {
		return input != null && input.matches(regex);
	}

	/**
	 * Helper method to check if a string has a required length.
	 * 
	 * @param input
	 * @param minLength
	 *            Inclusive.
	 * @return Whether has a required length.
	 */
	public static boolean hasLength(String input, int minLength) {
		return input != null && input.length() >= minLength;
	}

	/**
	 * Helper method to check if a string is a valid email address.
	 * 
	 * @param input
	 * @return Whether is a valid email.
	 */
	public static boolean isEmail(String input) {
		try {
			InternetAddress emailAddr = new InternetAddress(input);
			emailAddr.validate();
			return true;
		} catch (AddressException ex) {
			return false;
		}
	}

	/**
	 * Helper method to check equality.
	 * 
	 * @param input1
	 * @param input2
	 * @return Whether equal.
	 */
	public static boolean areEqual(String input1, String input2) {
		return input1 != null && input1.equals(input2);
	}

	/**
	 * Merges with another validator.
	 * 
	 * @param other
	 */
	public void merge(Validator other) {
		errors.putAll(other.getErrors());
	}

	/**
	 * Merges with another validator, while prefixing the keys.
	 * 
	 * @param other
	 * @param keyPrefix
	 */
	public void merge(Validator other, String keyPrefix) {
		for (String key : other.getErrors().keySet()) {
			addError(keyPrefix + key, other.getError(key));
		}
	}

	@Override
	public String toString() {
		return CollectionUtil.join(errors.values().toArray(), ", ");
	}
}
