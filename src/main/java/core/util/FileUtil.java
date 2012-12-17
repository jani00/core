package core.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.config.Constants;
import core.web.util.FileUpload;

/**
 * Helper methods for file manipulation.
 * 
 * @author jani
 * 
 */
public class FileUtil {
	private static Logger log = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * Possible file types
	 * 
	 */
	public enum FileType {
		/**
		 * File
		 */
		FILE,
		/**
		 * Directory
		 */
		DIRECTORY
	}

	/**
	 * Gets a list of files in a directory, according to a filter.
	 * 
	 * @param directory
	 * @param filter
	 * @return List of files.
	 */
	public static List<File> getDirectoryFiles(File directory, FileFilter filter) {
		List<File> res = new ArrayList<File>();

		if (directory.isDirectory() && directory.exists()) {
			File[] files = directory.listFiles(filter);
			res = Arrays.asList(files);
		}
		Collections.sort(res, new Comparator<File>() {
			@Override
			public int compare(File f1, File f2) {
				if (f1.isDirectory() && !f2.isDirectory()) {
					return -1;
				}

				if (f2.isDirectory() && !f1.isDirectory()) {
					return 1;
				}

				return f1.getName().compareTo(f2.getName());
			}
		});
		return res;
	}

	/**
	 * Gets a list of files in directory, according to a regular expression.
	 * 
	 * @param directory
	 * @param match
	 * @return List of files.
	 */
	public static List<File> getDirectoryFiles(File directory,
			final String match) {
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.getName().matches(match);
			}
		};
		return getDirectoryFiles(directory, filter);
	}

	/**
	 * Gets all children of a directory.
	 * 
	 * @param directory
	 * @return List of files.
	 */
	public static List<File> getDirectoryFiles(File directory) {
		FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return true;
			}
		};
		return getDirectoryFiles(directory, filter);
	}

	/**
	 * Gets list of file names of a directory.
	 * 
	 * @param directory
	 * @return List of file names.
	 */
	public static List<String> getDirectoryFileNames(File directory) {
		List<String> res = new ArrayList<String>();
		for (File file : getDirectoryFiles(directory)) {
			res.add(file.getName());
		}
		return res;
	}

	/**
	 * Creates a zip file from a directory.
	 * 
	 * @param directory
	 * @return Byte array.
	 * @throws IOException
	 */
	public static byte[] createZip(File directory) throws IOException {
		ByteArrayOutputStream res = new ByteArrayOutputStream();

		ZipOutputStream zip = new ZipOutputStream(res);

		addToZip(zip, directory, directory.getName());

		zip.flush();
		zip.close();

		return res.toByteArray();
	}

	/**
	 * Adds recursively files to a zip.
	 * 
	 * @param zip
	 * @param file
	 * @param name
	 *            Root name.
	 * @throws IOException
	 */
	public static void addToZip(ZipOutputStream zip, File file, String name)
			throws IOException {
		if (file.isFile()) {
			byte[] buffer = new byte[1024];
			FileInputStream in = new FileInputStream(file);
			try {
				zip.putNextEntry(new ZipEntry(name));
				int len;
				while ((len = in.read(buffer)) > 0) {
					zip.write(buffer, 0, len);
				}
			} finally {
				in.close();
			}
			zip.closeEntry();
		} else {
			String[] list = file.list();
			if (list.length == 0) {
				zip.putNextEntry(new ZipEntry(name
						+ Constants.ZIP_ENTRY_SEPARATOR));
				zip.closeEntry();
			} else {
				for (String filename : list) {
					File f = new File(file, filename);
					String newDest = f.getName();
					if (!name.isEmpty()) {
						newDest = name + Constants.ZIP_ENTRY_SEPARATOR
								+ newDest;
					}
					addToZip(zip, f, newDest);
				}
			}
		}

	}

	/**
	 * Deletes a file / directory (recursively).
	 * 
	 * @param file
	 * @return Whether successful.
	 */
	public static boolean delete(File file) {
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				boolean success = delete(child);
				if (!success) {
					return false;
				}
			}
		}
		return file.delete();
	}

	/**
	 * Copies a directory to another location.
	 * 
	 * @param source
	 * @param destination
	 * @return success
	 */
	public static boolean copyDirectory(File source, File destination) {
		try {
			FileUtils.copyDirectory(source, destination);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Creates a directory in a directory.
	 * 
	 * @param parent
	 * @param name
	 * @return {@link Validator}
	 */
	public static Validator createDirectory(File parent, String name) {
		Validator res = new Validator();

		if (parent.isDirectory()) {
			File newdir = new File(parent, name);
			if (newdir.exists()) {
				res.addError("newdir", "Directory already exists");
			} else {
				newdir.mkdir();
			}
		} else {
			res.addError("newdir", "Parent directory does not exists");
		}

		return res;
	}

	/**
	 * Saves an uploaded file to a directory.
	 * 
	 * @param destinationDirectory
	 * @param file
	 * @param overwrite
	 * @return {@link Validator}
	 */
	public static Validator uploadFile(File destinationDirectory,
			FileUpload file, boolean overwrite) {
		Validator res = new Validator();

		if (destinationDirectory.isDirectory()) {
			File destinationFile = new File(destinationDirectory,
					file.getFilename());
			if (destinationFile.exists() && !overwrite) {
				res.addError("newdir", "File already exists");
			} else {
				file.save(destinationFile);
			}
		} else {
			res.addError("newdir", "Destination directory does not exists");
		}

		return res;
	}

	/**
	 * Saves an input stream to the file system.
	 * 
	 * @param input
	 * @param destinationFile
	 * @param overwrite
	 * 
	 * @return {@link Validator}
	 */
	public static Validator saveStream(InputStream input, File destinationFile,
			boolean overwrite) {
		Validator res = new Validator();

		if (destinationFile.exists() && !overwrite) {
			res.addError("newdir", "File already exists");
		} else {
			try {
				FileOutputStream output = new FileOutputStream(destinationFile);
				try {
					IOUtils.copy(input, output);
				} finally {
					output.close();
				}
			} catch (IOException e) {
				res.addError("error", e.getMessage());
			}
		}

		return res;
	}

	/**
	 * Reads a file to byte array. If a directory is passed, then it is
	 * converted to a zip.
	 * 
	 * @param file
	 * @return Byte array.
	 */
	public static byte[] read(File file) {
		byte[] res = null;

		if (file.exists()) {
			if (file.isFile()) {
				try {
					RandomAccessFile f = new RandomAccessFile(file, "r");
					res = new byte[(int) f.length()];
					f.read(res);
					f.close();
				} catch (IOException e) {
					log.error(String.format(
							"FileUtil.read(): Unable to read file %s", file));
				}
			} else if (file.isDirectory()) {
				try {
					res = createZip(file);
				} catch (IOException e) {
					log.error(String.format(
							"FileUtil.read(): Unable to read directory %s",
							file));
				}
			}
		}
		return res;
	}

	/**
	 * Creates a new temp directory with a random name at a specified location
	 * and with a certain prefix.
	 * 
	 * @return The created directory.
	 */
	public static File getTempDirectory() {
		File tempRoot = new File(System.getProperty("java.io.tmpdir"));
		String uuid = UUID.randomUUID().toString();
		File res = new File(tempRoot, uuid);
		assert !res.exists();
		res.mkdirs();
		assert res.exists();
		return res;
	}

	/**
	 * Unzips an archive to the specified directory.
	 * 
	 * @param archive
	 * @param outputDir
	 * @return success
	 */
	public static boolean unzipArchive(File archive, File outputDir) {
		try {
			ZipFile zip = new ZipFile(archive);
			for (Enumeration<?> e = zip.entries(); e.hasMoreElements();) {
				ZipEntry entry = (ZipEntry) e.nextElement();

				if (entry.isDirectory()) {
					new File(outputDir, entry.getName()).mkdirs();
					continue;
				}

				File outputFile = new File(outputDir, entry.getName());
				if (!outputFile.getParentFile().exists()) {
					outputFile.getParentFile().mkdirs();
				}

				BufferedInputStream inputStream = new BufferedInputStream(
						zip.getInputStream(entry));
				BufferedOutputStream outputStream = new BufferedOutputStream(
						new FileOutputStream(outputFile));

				try {
					IOUtils.copy(inputStream, outputStream);
				} finally {
					outputStream.close();
					inputStream.close();
				}

			}
			zip.close();
		} catch (Exception e) {
			log.error("FileUtil.unzipArchive(): Error while extracting file "
					+ archive, e);
			return false;
		}
		return true;
	}
}
