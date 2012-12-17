package core.model;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import core.util.Parser;
import core.util.TestBase;

/**
 * Tests {@link Contest}.
 * 
 * @author jani
 * 
 */
public class ContestTest extends TestBase {
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
	 * Tests {@link Contest#validate()};
	 */
	public void testValidatate() {
		Contest c1 = ResourceUtil.loadResource(new File(seriesRoot,
				"valid_series_1/invalid_contest_1"), ResourceKind.CONTEST);
		assertFalse(c1.validate().isValid());

		Contest c2 = ResourceUtil.loadResource(new File(seriesRoot,
				"series_1/contest_1"), ResourceKind.CONTEST);
		assertTrue(c2.validate().isValid());
	}

	/**
	 * Tests {@link Contest#loadParent()}.
	 */
	public void testLoadParent() {
		Contest c = ResourceUtil.loadResource(new File(seriesRoot,
				"series_1/contest_1"), ResourceKind.CONTEST);
		Series parent = c.loadParent();
		assertNotNull(parent);
		assertEquals("series_1", parent.getId());
	}

	/**
	 * Tests {@link Contest#loadChildren()}.
	 */
	public void testLoadChildren() {
		Contest c = ResourceUtil.loadResource(new File(seriesRoot,
				"series_1/contest_1"), ResourceKind.CONTEST);
		List<Problem> children = c.loadChildren();
		assertSame(1, children.size());
	}

	/**
	 * Tests {@link Contest} state.
	 */
	public void testContestState() {
		Contest contest = new Contest();
		contest.setDuration(120);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, 1);
		contest.setStartTime(Parser.dateToString(cal.getTime()));
		assertSame(ContestState.NOT_STARTED, contest.getState());

		cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -1);
		contest.setStartTime(Parser.dateToString(cal.getTime()));
		assertSame(ContestState.STARTED, contest.getState());

		cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -3);
		contest.setStartTime(Parser.dateToString(cal.getTime()));
		assertSame(ContestState.ENDED, contest.getState());
	}
}
