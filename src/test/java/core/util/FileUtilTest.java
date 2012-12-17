package core.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * Unit test for {@link FileUtil}.
 * 
 * @author jani
 * 
 */
public class FileUtilTest extends TestBase {

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
	 * Tests {@link FileUtil#getDirectoryFiles(File)}.
	 */
	public void testGetDirectoryFiles() {
		List<File> res;
		File dir = new File(tempDir, "nonempty");
		res = FileUtil.getDirectoryFiles(dir);
		assertTrue(res.contains(new File(dir, "dir1")));
		assertTrue(res.contains(new File(dir, "file1")));
		assertTrue(res.contains(new File(dir, "file2")));
		assertFalse(res.contains(new File(dir, "wrong")));

		res = FileUtil.getDirectoryFiles(dir, "file.*");
		assertFalse(res.contains(new File(dir, "dir1")));
		assertTrue(res.contains(new File(dir, "file1")));
		assertTrue(res.contains(new File(dir, "file2")));

		res = FileUtil.getDirectoryFiles(dir, new FileFilter() {

			@Override
			public boolean accept(File file) {
				return file.isDirectory();
			}
		});
		assertTrue(res.contains(new File(dir, "dir1")));
		assertFalse(res.contains(new File(dir, "file1")));
		assertFalse(res.contains(new File(dir, "file2")));
	}

	/**
	 * Tests {@link FileUtil#getDirectoryFileNames(File)}.
	 */
	public void testGetDirectoryFilenames() {
		List<String> res;
		File dir = new File(tempDir, "nonempty");
		res = FileUtil.getDirectoryFileNames(dir);

		assertTrue(res.contains("dir1"));
		assertTrue(res.contains("file1"));
		assertTrue(res.contains("file2"));
		assertFalse(res.contains("wrong"));
	}

	/**
	 * Tests {@link FileUtil#createZip(File)}.
	 */
	public void testCreateZip() {
		File dir = new File(tempDir, "nonempty");

		byte[] zip;
		try {
			zip = FileUtil.createZip(dir);
			assertNotNull(zip);
		} catch (IOException e) {
			assert false;
		}

	}

	/**
	 * Tests {@link FileUtil#unzipArchive(File, File)}.
	 */
	public void testUnzip() {
		File zip = new File(tempDir, "test.zip");
		File destionation = new File(tempDir, "unzipped");
		assertFalse(destionation.exists());

		FileUtil.unzipArchive(zip, destionation);
		assertTrue(destionation.exists());

		List<File> files = FileUtil.getDirectoryFiles(destionation);
		assertEquals(files.size(), 2);

		FileUtil.delete(destionation);
		assertFalse(destionation.exists());
	}

	/**
	 * Tests {@link FileUtil#copyDirectory(File, File)} and
	 * {@link FileUtil#delete(File)}.
	 */
	public void testCopyDelete() {
		File dir = new File(tempDir, "nonempty");
		File copy = new File(tempDir, "copy");

		assertFalse(copy.exists());
		FileUtil.copyDirectory(dir, copy);
		assertTrue(copy.exists());

		FileUtil.delete(copy);
		assertFalse(copy.exists());
	}

	/**
	 * Tests {@link FileUtil#createDirectory(File, String)}.
	 */
	public void testCreateDirectory() {
		Validator res;

		res = FileUtil.createDirectory(tempDir, "newdir");
		assertTrue(res.isValid());
		File f = new File(tempDir, "newdir");
		assertTrue(f.exists());
		FileUtil.delete(f);
		assertFalse(f.exists());

		res = FileUtil.createDirectory(tempDir, "nonempty");
		assertFalse(res.isValid());
	}

	/**
	 * Tests {@link FileUtil#saveStream(InputStream, File, boolean)}.
	 */
	public void testSaveStream() {
		File testFile = new File(tempDir, "binary.png");
		File destinationFile = new File(tempDir, "copy.png");

		assertFalse(destinationFile.exists());

		InputStream stream;
		try {
			stream = new FileInputStream(testFile);

			Validator res = FileUtil.saveStream(stream, destinationFile, false);

			assertTrue(res.isValid());
			assertTrue(destinationFile.exists());

			assertTrue(FileUtils.contentEquals(testFile, destinationFile));

			res = FileUtil.saveStream(stream, destinationFile, true);
			assertTrue(res.isValid());
			assertTrue(destinationFile.exists());

			res = FileUtil.saveStream(stream, destinationFile, false);
			assertFalse(res.isValid());

			FileUtil.delete(destinationFile);
			assertFalse(destinationFile.exists());

			stream.close();
		} catch (IOException e) {
			assert false;
		}

	}

	/**
	 * Tests {@link FileUtil#read(File)}.
	 */
	public void testRead() {
		File testFile = new File(tempDir, "binary.png");
		byte[] res = FileUtil.read(testFile);

		assertNotNull(res);

		res = FileUtil.read(new File(tempDir, "wrong"));
		assertNull(res);
	}

	/**
	 * Tests {@link FileUtil#getTempDirectory()}.
	 */
	public void testGetTempDirectory() {
		File temp = FileUtil.getTempDirectory();
		assertTrue(temp.isDirectory());
		assertTrue(temp.exists());
		FileUtil.delete(temp);
		assertFalse(temp.exists());
	}
}
