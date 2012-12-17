package core.model;

import java.util.Arrays;
import java.util.List;

import core.config.Constants;

/**
 * Enumeration of possible flavors of a resource.
 * 
 * @author jani
 * 
 */
public enum ResourceKind {

	/**
	 * A series kind.
	 */
	SERIES("series", Series.class),

	/**
	 * A contest kind.
	 */
	CONTEST("contest", Contest.class),

	/**
	 * A problem kind.
	 */
	PROBLEM("problem", Problem.class);

	/**
	 * Nesting of different resource kinds.
	 */
	public static final List<ResourceKind> NESTING = Arrays
			.asList(new ResourceKind[] { ResourceKind.SERIES,
					ResourceKind.CONTEST, ResourceKind.PROBLEM });

	private String name;
	private Class<? extends Resource> resourceClass;

	ResourceKind(String name, Class<? extends Resource> resourceClass) {
		this.name = name;
		this.resourceClass = resourceClass;
	}

	/**
	 * Gets the resource file name of the kind.
	 * 
	 * @return Resource file name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the resource file name of the kind with extension.
	 * 
	 * @return Resource file name.
	 */
	public String getResourceFilenameExt() {
		return this.name + Constants.JSON_FILE_EXTENSION;
	}

	/**
	 * Gets the class of the resource.
	 * 
	 * @return Resource class.
	 */
	public Class<? extends Resource> getResourceClass() {
		return this.resourceClass;
	}

	/**
	 * Gets the kind of the successor.
	 * 
	 * @return {@link ResourceKind} of successor.
	 */
	public ResourceKind getSuccessorKind() {
		int index = NESTING.indexOf(this);
		if (index < NESTING.size() - 1) {
			return NESTING.get(index + 1);
		}
		return null;
	}

	/**
	 * Gets the kind of the predecessor.
	 * 
	 * @return {@link ResourceKind} of predecessor.
	 */
	public ResourceKind getPredecessorKind() {
		int index = NESTING.indexOf(this);
		if (index > 0) {
			return NESTING.get(index - 1);
		}
		return null;
	}
}