package core.browse;

/**
 * Enumeration of types of nodes in the repository.
 * 
 * @author jani
 * 
 */
public enum NodeType {

	/**
	 * The root node.
	 */
	ROOT,

	/**
	 * A resource node.
	 */
	RESOURCE,

	/**
	 * A system directory, e.g. _files or _public_files.
	 */
	SYSTEM_DIRECTORY,

	/**
	 * A system file, like series.json, _security.json, etc.
	 */
	SYSTEM_FILE,

	/**
	 * A problem-specific file.
	 */
	PROBLEM_FILE;

}