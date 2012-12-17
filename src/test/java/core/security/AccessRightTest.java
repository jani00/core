package core.security;

import java.io.File;
import core.model.ResourceUtil;
import core.util.TestBase;

/**
 * Tests behaviour of {@link AccessRight} - nesting and recursive inclusion.
 * 
 * @author jani
 * 
 */
public class AccessRightTest extends TestBase {
	private File tempDir;
	private File seriesRoot;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		tempDir = getTempDir();
		extractTestZip(tempDir, TEST_REPO);
		seriesRoot = new File(tempDir, "series");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		
		deleteDir(tempDir);
	}

	/**
	 * Tests that greater {@link AccessRight} includes a lesser one.
	 */
	public void testAccessRightInclusion() {
		assertTrue(hasRight(seriesRoot, "milo", AccessRight.VIEW_FULL));
		assertTrue(hasRight(seriesRoot, "milo", AccessRight.VIEW));
		assertTrue(hasRight(seriesRoot, "milo", AccessRight.LIST));
		assertTrue(hasRight(seriesRoot, "milo", AccessRight.NONE));
	}

	/**
	 * Tests that {@link AccessRight} are recursively included, with the
	 * exception of LIST.
	 */
	public void testAccessRightRecursive() {
		assertTrue(hasRight(seriesRoot, "milo", AccessRight.VIEW_FULL));
		assertTrue(hasRight(getResourceDirectory("series_1"), "milo",
				AccessRight.VIEW_FULL));
		assertTrue(hasRight(getResourceDirectory("series_1/contest_1"), "milo",
				AccessRight.VIEW_FULL));
		assertTrue(hasRight(
				getResourceDirectory("series_1/contest_1/problem_1"), "milo",
				AccessRight.VIEW_FULL));

		assertTrue(hasRight(seriesRoot, "jani", AccessRight.VIEW));
		assertFalse(hasRight(seriesRoot, "jani", AccessRight.VIEW_FULL));
		assertTrue(hasRight(getResourceDirectory("series_1"), "jani",
				AccessRight.VIEW_FULL));
		assertTrue(hasRight(getResourceDirectory("series_1/contest_1"), "jani",
				AccessRight.VIEW_FULL));
		assertTrue(hasRight(
				getResourceDirectory("series_1/contest_1/problem_1"), "jani",
				AccessRight.VIEW_FULL));

		assertTrue(hasRight(seriesRoot, "petko", AccessRight.LIST));
		assertFalse(hasRight(seriesRoot, "petko", AccessRight.VIEW));
		assertFalse(hasRight(seriesRoot, "petko", AccessRight.VIEW_FULL));
		assertFalse(hasRight(getResourceDirectory("series_1"), "petko",
				AccessRight.VIEW_FULL));
		assertTrue(hasRight(getResourceDirectory("series_1/contest_1"),
				"petko", AccessRight.VIEW_FULL));
		assertTrue(hasRight(
				getResourceDirectory("series_1/contest_1/problem_1"), "petko",
				AccessRight.VIEW_FULL));

		assertTrue(hasRight(seriesRoot, "eli", AccessRight.NONE));
		assertFalse(hasRight(seriesRoot, "eli", AccessRight.LIST));
		assertFalse(hasRight(getResourceDirectory("series_1"), "eli",
				AccessRight.LIST));
		assertFalse(hasRight(getResourceDirectory("series_1/contest_1"), "eli",
				AccessRight.LIST));
		assertFalse(hasRight(
				getResourceDirectory("series_1/contest_1/problem_1"), "eli",
				AccessRight.LIST));

		// LIST is not recursive
		assertTrue(hasRight(getResourceDirectory("series_1"), "deni",
				AccessRight.LIST));
		assertFalse(hasRight(getResourceDirectory("series_1/contest_1"),
				"deni", AccessRight.LIST));
	}

	private File getResourceDirectory(String path) {
		return new File(seriesRoot, path);
	}

	private boolean hasRight(File path, String login, AccessRight required) {
		AccessRight access = ResourceUtil.getAccessRight(seriesRoot, path,
				login);
		return access.includes(required);
	}
}
