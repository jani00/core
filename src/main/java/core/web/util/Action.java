package core.web.util;

/**
 * Enumeration of possible web actions.
 * 
 * @author jani
 * 
 */
public enum Action {
	/**
	 * View action
	 */
	VIEW("view"),
	/**
	 * Add action
	 */
	ADD("add"),
	/**
	 * Edit action
	 */
	EDIT("edit"),
	/**
	 * Delete action
	 */
	DELETE("delete"),
	/**
	 * Validate action
	 */
	VALIDATE("validate"),
	/**
	 * Download action
	 */
	DOWNLOAD("download"),

	/**
	 * Add file action
	 */
	ADD_FILE("add-file"),

	/**
	 * Add directory action
	 */
	ADD_DIR("add-dir");

	private String name;

	Action(String name) {
		this.name = name;
	}

	/**
	 * Gets the string name.
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Parses string to {@link Action}
	 * 
	 * @param val
	 * @return {@link Action}
	 */
	public static Action fromString(String val) {
		for (Action a : Action.values()) {
			if (a.name.equalsIgnoreCase(val)) {
				return a;
			}
		}
		return VIEW;
	}
}
