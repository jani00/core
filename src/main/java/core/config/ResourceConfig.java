package core.config;

/**
 * Contains definitions of resource specific constants.
 * 
 * @author jani
 * 
 */
public class ResourceConfig {
	/**
	 * Series format used in this version.
	 */
	public static final String SERIES_FORMAT = "core-series-1";
	/**
	 * Contest format used in this version.
	 */
	public static final String CONTEST_FORMAT = "core-contest-1";
	/**
	 * Problem format used in this version.
	 */
	public static final String PROBLEM_FORMAT = "core-problem-1";

	/**
	 * Path to the schema files that are used for json validation.
	 */
	public static final String RESOURCE_SCHEMA_PATH = "/schema";

	/**
	 * Possible grading styles options.
	 */
	public static final String[] GRADING_STYLES = { "acm", "ioi" };
	/**
	 * Possible checker options.
	 */
	public static final String[] PROBLEM_CHECKERS = { "diff", "custom" };
	/**
	 * Default checker for a problem
	 */
	public static final String PROBLEM_CUSTOM_CHECKERS = PROBLEM_CHECKERS[1];

	/**
	 * Property name that is used for security grant.
	 */
	public static final String GRANTS_PROP_NAME = "grants";
}
