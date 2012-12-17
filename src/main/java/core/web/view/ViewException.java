package core.web.view;

/**
 * An exception that occurred while parsing the view of a web page.
 * 
 * @author jani
 * 
 */
public class ViewException extends Exception {
	private static final long serialVersionUID = 6895517053493356250L;
	private String message;
	private Throwable cause;

	/**
	 * @param message
	 * @param cause
	 */
	public ViewException(String message, Throwable cause) {
		this.message = message;
		this.cause = cause;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	@Override
	public Throwable getCause() {
		return this.cause;
	}
}
