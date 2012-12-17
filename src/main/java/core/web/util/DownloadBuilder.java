package core.web.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.util.FileUtil;

/**
 * Helper for building an archive of files (zip). Contains reference and inline
 * files, which are allowed for download. Inline files are one that do not
 * exists on the file system, but are created on the fly (for example, resource
 * JSON files, with limited properties). The reference files are links to files
 * on the file system.
 * 
 * @author jani
 * 
 */
public class DownloadBuilder {
	private Logger log = LoggerFactory.getLogger(DownloadBuilder.class);

	private Map<String, File> refs;

	private Map<String, byte[]> inline;

	/**
	 * Default constructor.
	 */
	public DownloadBuilder() {
		refs = new HashMap<String, File>();
		inline = new HashMap<String, byte[]>();
	}

	/**
	 * Adds an inline file, i.e. one that does not exists on the file system.
	 * 
	 * @param filename
	 * @param file
	 */
	public void addInline(String filename, byte[] file) {
		inline.put(filename, file);
	}

	/**
	 * Gets the inline files of the downloader.
	 * 
	 * @return Inline files.
	 */
	public Map<String, byte[]> getInline() {
		return inline;
	}

	/**
	 * Adds a ref file, i.e. one that does exists on the file system.
	 * 
	 * @param name
	 *            Name of the file.
	 * @param file
	 *            File.
	 */
	public void addRef(String name, File file) {
		refs.put(name, file);
	}

	/**
	 * Adds all ref files, while adding a specific prefix to the name.
	 * 
	 * @param prefix
	 *            Prefix for the files.
	 * @param files
	 *            List of files to add.
	 */
	public void addRefs(String prefix, List<File> files) {
		for (File file : files) {
			addRef(prefix + file.getName(), file);
		}
	}

	/**
	 * Gets all ref files.
	 * 
	 * @return Ref files.
	 */
	public Map<String, File> getRefs() {
		return refs;
	}

	/**
	 * Adds (merges) another {@link DownloadBuilder}. Both ref and inline files
	 * are merged.
	 * 
	 * @param root
	 *            Root, used as a prefix
	 * @param other
	 *            {@link DownloadBuilder} to merge.
	 */
	public void add(String root, DownloadBuilder other) {
		for (String s : other.getRefs().keySet()) {
			refs.put(root + s, other.getRefs().get(s));
		}
		for (String s : other.getInline().keySet()) {
			inline.put(root + s, other.getInline().get(s));
		}
	}

	/**
	 * Converts the {@link DownloadBuilder} to a zip file by combining inline
	 * and ref files. If a files is present in both, the inline is prioritized
	 * and the ref is ignored.
	 * 
	 * @return Byte array representation of the zip.
	 */
	public byte[] toZip() {
		try {
			ByteArrayOutputStream res = new ByteArrayOutputStream();

			ZipOutputStream zip = new ZipOutputStream(res);

			for (String name : getInline().keySet()) {
				zip.putNextEntry(new ZipEntry(name));
				zip.write(getInline().get(name));
				zip.closeEntry();
			}
			for (String name : getRefs().keySet()) {
				if (!getInline().containsKey(name)) {
					FileUtil.addToZip(zip, getRefs().get(name), name);
				}
			}

			zip.flush();
			zip.close();

			return res.toByteArray();
		} catch (IOException e) {
			log.error("DownloadBuilder.toZip(): Error creating zip: "
					+ e.getMessage());
			return null;
		}
	}
}
