package core.model;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import core.security.AccessRight;
import core.util.TestBase;
import core.util.Validator;

/**
 * Tests {@link ResourceUtil}.
 * 
 * @author jani
 * 
 */
public class ResourceUtilTest extends TestBase {
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
	 * Tests {@link ResourceUtil#loadResource(File, ResourceKind)}.
	 */
	public void testLoadResource() {
		File dir = new File(seriesRoot, "series_1");
		Series s = ResourceUtil.loadResource(dir, ResourceKind.SERIES);
		assertNotNull(s);
		assertEquals("series_1", s.getId());

		assertNull(ResourceUtil.loadResource(dir, ResourceKind.CONTEST));

		assertNotNull(ResourceUtil.loadResource(new File(seriesRoot,
				"series_1/contest_1"), ResourceKind.CONTEST));

		assertNotNull(ResourceUtil.loadResource(new File(seriesRoot,
				"series_1/contest_1/problem_1"), ResourceKind.PROBLEM));
	}

	/**
	 * Tests {@link ResourceUtil#fromJsonString(String, ResourceKind)}.
	 */
	public void testFromJsonString() {
		File file = new File(seriesRoot, "series_1/series.json");
		try {
			String json = FileUtils.readFileToString(file);
			Series s = ResourceUtil.fromJsonString(json, ResourceKind.SERIES);
			assertNotNull(s);
		} catch (IOException e) {
			assert false;
		}
	}

	/**
	 * Tests {@link ResourceUtil#getChildResourceList(File)}.
	 */
	public void testGetChildResourceList() {
		File file = new File(seriesRoot, "series_1");
		List<File> list = ResourceUtil.getChildResourceList(file);

		assertTrue(list.contains(new File(file, "contest_1")));
		assertTrue(list.contains(new File(file, "contest_2")));
	}

	/**
	 * Tests {@link ResourceUtil#loadChildResources(File, ResourceKind)}.
	 */
	public void testLoadChildResources() {
		List<Resource> res = ResourceUtil.loadChildResources(seriesRoot,
				ResourceKind.SERIES);
		assertEquals(5, res.size());

		res = ResourceUtil.loadChildResources(new File(seriesRoot, "series_1"),
				ResourceKind.CONTEST);
		assertEquals(2, res.size());
	}

	/**
	 * Tests {@link ResourceUtil#getAccessRight(File, File, String)}.
	 */
	public void testGetAccessRight() {
		assertSame(AccessRight.VIEW,
				ResourceUtil.getAccessRight(seriesRoot, seriesRoot, "jani"));
		assertSame(AccessRight.VIEW_FULL,
				ResourceUtil.getAccessRight(seriesRoot, seriesRoot, "milo"));

		assertSame(AccessRight.VIEW_FULL, ResourceUtil.getAccessRight(
				seriesRoot, new File(seriesRoot, "series_1"), "jani"));
		assertSame(AccessRight.VIEW_FULL, ResourceUtil.getAccessRight(
				seriesRoot, new File(seriesRoot, "series_1/contest_1"), "jani"));
		assertSame(AccessRight.VIEW_FULL, ResourceUtil.getAccessRight(
				seriesRoot,
				new File(seriesRoot, "series_1/contest_1/problem_1"), "jani"));
	}

	/**
	 * Tests {@link ResourceUtil#loadAccessRight(File, String)}.
	 */
	public void testLoadAccessRight() {
		assertSame(AccessRight.VIEW,
				ResourceUtil.loadAccessRight(seriesRoot, "jani"));
		assertSame(AccessRight.VIEW_FULL, ResourceUtil.loadAccessRight(
				new File(seriesRoot, "series_1"), "jani"));
		assertSame(AccessRight.NONE, ResourceUtil.loadAccessRight(new File(
				seriesRoot, "series_1/contest_1"), "jani"));
		assertSame(AccessRight.NONE, ResourceUtil.loadAccessRight(new File(
				seriesRoot, "series_1/contest_2"), "jani"));
		assertSame(AccessRight.NONE, ResourceUtil.loadAccessRight(new File(
				seriesRoot, "series_1/contest_1/problem_1"), "jani"));
	}

	/**
	 * Tests {@link ResourceUtil#createResource(File, String, ResourceKind)}.
	 */
	public void testCreateResource() {
		Validator res;

		res = ResourceUtil.createResource(seriesRoot, "series_1",
				ResourceKind.SERIES);
		assertFalse(res.isValid());

		res = ResourceUtil.createResource(seriesRoot, "series_2",
				ResourceKind.SERIES);
		assertTrue(res.isValid());

		Series s = ResourceUtil.loadResource(new File(seriesRoot, "series_2"),
				ResourceKind.SERIES);
		assertNotNull(s);
		assertEquals("series_2", s.getId());
	}
}
