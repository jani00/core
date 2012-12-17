package core.model;

import java.io.File;

import core.model.User;
import core.security.UserManager;
import core.util.TestBase;

/**
 * Tests {@link User}.
 * 
 * @author jani
 * 
 */
public class UserTest extends TestBase {
	private File tempDir;
	private File usersRoot;
	private UserManager manager;
	User user = new User();

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		tempDir = getTempDir();
		extractTestZip(tempDir, TEST_REPO);
		usersRoot = new File(tempDir, "souls/users");
		manager = new UserManager(usersRoot);

		user.setLogin("test_user");
		user.setAbout("about");
		user.setCity("city");
		user.setEmail("email@domain.tld");
		user.setOrganizationsString("orga1, orga2");
		user.setPassword("password");
		user.setConfirmPassword("password");
		user.setRealName("realname");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		deleteDir(tempDir);
	}

	/**
	 * Tests creation of new {@link User}.
	 */
	public void testNewUser() {
		assertSame(2, user.getOrganizations().size());

		user.hashPassword();
		assertTrue(manager.save(user));

		assertTrue(manager.isAuthorized("test_user", "password"));
	}

	/**
	 * Tests User validation.
	 */
	public void testValidUser() {
		assertTrue(user.validate(manager).isValid());
	}

	/**
	 * Tests login validation.
	 */
	public void testInvalidLogin() {
		user.setLogin("!user");
		assertFalse(user.validate(manager).isValid());
	}

	/**
	 * Tests login validation.
	 */
	public void testInvalidLoginTaken() {
		user.setLogin("jani");
		assertFalse(user.validate(manager).isValid());
	}

	/**
	 * Tests email validation.
	 */
	public void testInvalidEmail() {
		user.setEmail("email");
		assertFalse(user.validate(manager).isValid());
	}

	/**
	 * Tests password validation.
	 */
	public void testInvalidPassword() {
		user.setPassword("1");
		assertFalse(user.validate(manager).isValid());
	}

	/**
	 * Tests password validation.
	 */
	public void testInvalidPasswordMismatch() {
		user.setPassword("password1");
		user.setConfirmPassword("password2");
		assertFalse(user.validate(manager).isValid());
	}
}
