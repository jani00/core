package core.web.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.fileupload.FileItem;

/**
 * A wrapper for file upload.
 * 
 * @author jani
 * 
 */
public class FileUpload {

	private String filename;
	private String contentType;
	private long size;
	private FileItem item;

	/**
	 * Gets the filename of the file.
	 * 
	 * @return String
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * Gets the content type of the file
	 * 
	 * @return String
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Gets the size of the file
	 * 
	 * @return long
	 */
	public long getSize() {
		return size;
	}

	/**
	 * Constructor.
	 * 
	 * @param filename
	 * @param contentType
	 * @param size
	 * @param item
	 */
	public FileUpload(String filename, String contentType, long size,
			FileItem item) {
		super();
		this.filename = filename;
		this.contentType = contentType;
		this.size = size;
		this.item = item;
	}

	/**
	 * Saves the uploaded file to a destination.
	 * 
	 * @param file
	 * @return Whether successful.
	 */
	public boolean save(File file) {
		try {
			item.write(file);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Gets the inputstream of the file.
	 * 
	 * @return {@link InputStream}
	 */
	public InputStream getStream() {
		try {
			return item.getInputStream();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Checks if a valid file is uploaded.
	 * 
	 * @return Whether file is valid.
	 */
	public boolean isUploaded() {
		return size > 0;
	}

}
