package core.util;

import java.util.Arrays;
import java.util.List;

/**
 * Unit test for {@link Validator}.
 * 
 * @author jani
 * 
 */
public class ValidatorTest extends TestBase {

	/**
	 * Tests {@link Validator#addError(String, String)}.
	 */
	public void testAddError() {
		Validator validator = new Validator();
		assertTrue(validator.isValid());
		validator.addError("error", "error");
		assertFalse(validator.isValid());
		assertTrue(validator.hasError("error"));
	}

	/**
	 * Tests {@link Validator#addErrors(String, List)}.
	 */
	public void testAddErrors() {
		Validator validator = new Validator();

		List<String> messages = Arrays.asList(new String[] { "m1", "m2" });
		validator.addErrors("pre_", messages);
		assertFalse(validator.isValid());
		assertTrue(validator.hasError("pre_0"));
		assertTrue(validator.hasError("pre_1"));
	}

	/**
	 * Tests {@link Validator#validateRequired(String, String, String)}.
	 */
	public void testValidateRequired() {
		Validator validator = new Validator();

		validator.validateRequired("input", "key", "message");
		assertTrue(validator.isValid());

		validator.validateRequired("", "key", "message");
		assertFalse(validator.isValid());
	}

	/**
	 * Tests {@link Validator#validateRegex(String, String, String, String)}.
	 */
	public void testValidateRegex() {
		Validator validator = new Validator();

		validator.validateRegex("1234", "\\d+", "key", "message");
		assertTrue(validator.isValid());

		validator.validateRegex("string", "\\d+", "key", "message");
		assertFalse(validator.isValid());
	}

	/**
	 * Tests {@link Validator#validateLength(String, int, String, String)}.
	 */
	public void testValidateLength() {
		Validator validator = new Validator();

		validator.validateLength("string", 3, "key", "message");
		assertTrue(validator.isValid());

		validator.validateLength("s", 3, "key", "message");
		assertFalse(validator.isValid());
	}

	/**
	 * Tets {@link Validator#validateEmail(String, String, String)}.
	 */
	public void testValidateEmail() {
		Validator validator = new Validator();

		validator.validateEmail("email@domain.tld", "key", "message");
		assertTrue(validator.isValid());

		validator.validateEmail("false", "key", "message");
		assertFalse(validator.isValid());
	}

	/**
	 * Tests {@link Validator#validateEqual(String, String, String, String)}.
	 */
	public void testValidateEqual() {
		Validator validator = new Validator();

		validator.validateEqual("input", "input", "key", "message");
		assertTrue(validator.isValid());

		validator.validateEqual("input", "wrong", "key", "message");
		assertFalse(validator.isValid());
	}
}
