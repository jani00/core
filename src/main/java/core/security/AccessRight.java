package core.security;

/**
 * Enumeration of all view access rights the system uses.
 * 
 * @author deni, petko
 */
public enum AccessRight {

	/**
	 * The user does not have access to the resource this right applies to.
	 */
	NONE,

	/**
	 * The user can view the name and title of a resource, i.e. he/she will
	 * simply know that the resource exists. This means that the user will see
	 * the resource when browsing its parent's content, but will not be provided
	 * with any additional information.
	 */
	LIST,

	/**
	 * The user can view the public information about the resource.
	 */
	VIEW,

	/**
	 * The user can view the whole information about the resource.
	 */
	VIEW_FULL;

	/**
	 * Checks whether the current right includes (is stronger than) the given
	 * one.
	 * 
	 * @param other
	 *            the right for which to check
	 * @return true if the current right includes the given one, false otherwise
	 */
	public boolean includes(AccessRight other) {
		return ordinal() >= other.ordinal();
	}

	/**
	 * Parses a string to the appropriate AccessRight
	 * 
	 * @param s
	 *            Input string to parse.
	 * @return The parsed AccessRight.
	 */
	public static AccessRight fromString(String s) {
		try {
			return Enum.valueOf(AccessRight.class, s.toUpperCase());
		} catch (Exception e) {
			return NONE;
		}
	}

	/**
	 * Returns the greater of the two AccessRights, i.e. the one with greater
	 * rights.
	 * 
	 * @param r1
	 *            AccessRight1
	 * @param r2
	 *            AccessRight1
	 * @return The greater AccessRight.
	 */
	public static AccessRight max(AccessRight r1, AccessRight r2) {
		return values()[Math.max(r1.ordinal(), r2.ordinal())];
	}
}
