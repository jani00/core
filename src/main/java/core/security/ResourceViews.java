package core.security;

import org.codehaus.jackson.map.annotate.JsonView;

/**
 * Classes which are used for Jackson property filtering. Each JSON field can be
 * annotated with {@link JsonView}, which determines whether the field is
 * visible.
 * 
 * @author jani
 * 
 */
public class ResourceViews {

	/**
	 * Used for basic resource view
	 * 
	 */
	public static interface Basic {
		// nothing
	}

	/**
	 * Used for public access.
	 * 
	 */
	public static interface Public extends Basic {
		// nothing
	}

	/**
	 * Used for before-start access.
	 * 
	 */
	public static interface BeforeStart extends Public, Basic {
		// nothing
	}

	/**
	 * Used for after-start access.
	 * 
	 */
	public static interface AfterStart extends Public, BeforeStart, Basic {
		// nothing
	}

	/**
	 * Used for after-start access.
	 * 
	 */
	public static interface AfterEnd extends Public, AfterStart, Basic {
		// nothing
	}

	/**
	 * Used for private access.
	 * 
	 */
	public static interface Private extends Public, BeforeStart, AfterStart,
			AfterEnd, Basic {
		// nothing
	}
}