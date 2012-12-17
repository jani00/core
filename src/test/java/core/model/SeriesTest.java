package core.model;

import java.io.File;
import java.util.List;

import core.util.TestBase;

/**
 * Tests {@link Series}.
 * 
 * @author jani
 * 
 */
public class SeriesTest extends TestBase {
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
	 * Tests {@link Series#validate()};
	 */
	public void testValidatate() {
		Series s1 = ResourceUtil.loadResource(new File(seriesRoot,
				"invalid_series_1"), ResourceKind.SERIES);
		assertFalse(s1.validate().isValid());

		Series s2 = ResourceUtil.loadResource(new File(seriesRoot,
				"invalid_series_2"), ResourceKind.SERIES);
		assertFalse(s2.validate().isValid());

		Series s3 = ResourceUtil.loadResource(new File(seriesRoot, "series_1"),
				ResourceKind.SERIES);
		assertTrue(s3.validate().isValid());
	}

	/**
	 * Tests {@link Series#loadParent()}.
	 */
	public void testLoadParent() {
		Series s = ResourceUtil.loadResource(new File(seriesRoot, "series_1"),
				ResourceKind.SERIES);
		assertNull(s.loadParent());
	}

	/**
	 * Tests {@link Series#loadChildren()}.
	 */
	public void testLoadChildren() {
		Series s = ResourceUtil.loadResource(new File(seriesRoot, "series_1"),
				ResourceKind.SERIES);
		List<Contest> children = s.loadChildren();
		assertSame(2, children.size());
	}

	/**
	 * Tests {@link Series#getSeriesRoot()}.
	 */
	public void testGetSeriesRoot() {
		Series s = ResourceUtil.loadResource(new File(seriesRoot, "series_1"),
				ResourceKind.SERIES);
		assertEquals(seriesRoot, s.getSeriesRoot());
	}
}
