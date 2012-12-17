package core.util;

import java.io.File;

import core.web.util.DownloadBuilder;

/**
 * Unit test for {@link DownloadBuilder}.
 * 
 * @author jani
 * 
 */
public class DownloadBuilderTest extends TestBase {

	private File tempDir;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		tempDir = getTempDir();
		extractTestZip(tempDir, TestBase.TEST_FILES);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		deleteDir(tempDir);
	}

	/**
	 * Tests adding entries to {@link DownloadBuilder}.
	 */
	public void testCreation() {
		DownloadBuilder res = new DownloadBuilder();
		res.addRef("dir", new File(tempDir, "nonempty"));
		res.addInline("inline.txt", "inline content".getBytes());
		
		assertSame(1, res.getInline().size());
		assertSame(1, res.getRefs().size());
	}

	/**
	 * Tests merging of {@link DownloadBuilder}s.
	 */
	public void testMerge() {
		DownloadBuilder res1 = new DownloadBuilder();
		res1.addRef("dir", new File(tempDir, "nonempty"));
		
		assertSame(0, res1.getInline().size());
		assertSame(1, res1.getRefs().size());
		
		DownloadBuilder res2 = new DownloadBuilder();
		res2.addInline("inline.txt", "inline content".getBytes());
		assertSame(1, res2.getInline().size());
		assertSame(0, res2.getRefs().size());
		
		res2.add("", res1);
		assertSame(1, res2.getInline().size());
		assertSame(1, res2.getRefs().size());
	}
	/**
	 * Tests converting to zip.
	 */
	public void testToZip() {
		DownloadBuilder res = new DownloadBuilder();
		res.addRef("dir", new File(tempDir, "nonempty"));
		res.addInline("inline.txt", "inline content".getBytes());
		
		byte[] zip = res.toZip();
		assertNotNull(zip);
	}
}
