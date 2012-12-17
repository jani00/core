package core.security;

import java.io.File;

import core.model.User;
import core.util.FileUtil;
import core.util.TestBase;

/**
 * Tests {@link UserManager}.
 * 
 * @author jani
 * 
 */
public class UserManagerTest extends TestBase {
	private File tempDir;
	private File usersRoot;
	private UserManager manager;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		tempDir = getTempDir();
		extractTestZip(tempDir, TEST_REPO);
		usersRoot = new File(tempDir, "souls/users");
		manager = new UserManager(usersRoot);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		deleteDir(tempDir);
	}

	/**
	 * Tests {@link UserManager#isLoginTaken(String)}.
	 */
	public void testIsLoginTaken() {
		assertTrue(manager.isLoginTaken("jani"));
		assertFalse(manager.isLoginTaken("other"));
	}

	/**
	 * Tests {@link UserManager#getUserDirectory(String)}.
	 */
	public void testGetUserDirectory() {
		File dir = manager.getUserDirectory("jani");
		File expected = new File(usersRoot, "jani");

		assertEquals(expected, dir);
	}

	/**
	 * Tests {@link UserManager#isAuthorized(String, String)}.
	 */
	public void testIsAuthorized() {
		assertTrue(manager.isAuthorized("jani", "123123"));
		assertFalse(manager.isAuthorized("jani", "wrong"));
	}

	/**
	 * Tests {@link UserManager#loadUser(String)}.
	 */
	public void testLoadUser() {
		User user = manager.loadUser("jani");
		assertNotNull(user);
		assertEquals("jani", user.getLogin());
		assertEquals("Jani", user.getRealName());
	}

	/**
	 * Tests {@link UserManager#save(User)}.
	 */
	public void testSave() {
		User user = new User();
		user.setLogin("test_user");
		user.setRealName("John");

		File file = manager.getUserFile("test_user");
		assertFalse(file.exists());
		boolean res = manager.save(user);
		assertTrue(res);
		assertTrue(file.exists());

		res = manager.save(user);
		assertTrue(res);

		user = manager.loadUser("test_user");
		assertEquals("test_user", user.getLogin());
		assertEquals("John", user.getRealName());

		FileUtil.delete(file.getParentFile());
		assertFalse(file.exists());
	}
}
