package core.model;

import java.io.File;
import core.util.TestBase;

/**
 * Tests {@link Problem}.
 * 
 * @author jani
 * 
 */
public class ProblemTest extends TestBase {
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
	 * Tests {@link Problem#validate()};
	 */
	public void testValidatate() {
		Problem p1 = ResourceUtil.loadResource(new File(seriesRoot,
				"valid_series_2/valid_contest/invalid_problem_1"),
				ResourceKind.PROBLEM);
		assertFalse(p1.validate().isValid());

		Problem p2 = ResourceUtil.loadResource(new File(seriesRoot,
				"valid_series_2/valid_contest/invalid_problem_2"),
				ResourceKind.PROBLEM);
		assertFalse(p2.validate().isValid());

		Problem p3 = ResourceUtil.loadResource(new File(seriesRoot,
				"valid_series_2/valid_contest/invalid_problem_3"),
				ResourceKind.PROBLEM);
		assertFalse(p3.validate().isValid());

		Problem p4 = ResourceUtil.loadResource(new File(seriesRoot,
				"valid_series_2/valid_contest/invalid_problem_4"),
				ResourceKind.PROBLEM);
		assertFalse(p4.validate().isValid());

		Problem p5 = ResourceUtil.loadResource(new File(seriesRoot,
				"series_1/contest_1/problem_1"), ResourceKind.PROBLEM);
		assertTrue(p5.validate().isValid());
	}

	/**
	 * Tests {@link Problem#loadParent()}.
	 */
	public void testLoadParent() {
		Problem p = ResourceUtil.loadResource(new File(seriesRoot,
				"series_1/contest_1/problem_1"), ResourceKind.PROBLEM);
		Contest parent = p.loadParent();
		assertNotNull(parent);
		assertEquals("contest_1", parent.getId());
	}

	/**
	 * Tests {@link Problem#loadChildren()}.
	 */
	public void testLoadChildren() {
		Problem p = ResourceUtil.loadResource(new File(seriesRoot,
				"series_1/contest_1/problem_1"), ResourceKind.PROBLEM);

		assertSame(0, p.loadChildren().size());
	}
}
