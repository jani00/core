package core.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;

/**
 * Base class for unit tests. Provides access to file resources and temp files.
 * 
 * @author jani
 * 
 */
public class TestBase extends TestCase {

	/**
	 * ZIP, containing the test repository.
	 */
	public static final String TEST_REPO = "/repo.zip";
	/**
	 * ZIP, containing various test files.
	 */
	public static final String TEST_FILES = "/files.zip";

	protected File getTempDir() {
		File tempRoot = new File(System.getProperty("java.io.tmpdir"));
		String uuid = UUID.randomUUID().toString();
		File res = new File(tempRoot, uuid);
		assertFalse(res.exists());
		res.mkdirs();
		assertTrue(res.exists());
		return res;
	}

	protected void deleteDir(File file) {
		assertTrue(file.exists());

		try {
			FileUtils.deleteDirectory(file);
		} catch (IOException e) {
			assert false;
		}

		assertFalse(file.exists());
	}

	protected void extractTestZip(File tempDir, String pathToZip) {
		InputStream input = getClass().getResourceAsStream(pathToZip);
		File zip = new File(tempDir, "temp.zip");
		FileUtil.saveStream(input, zip, true);
		FileUtil.unzipArchive(zip, tempDir);
	}

}
