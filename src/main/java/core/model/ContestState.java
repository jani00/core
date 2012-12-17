package core.model;

import core.security.ResourceViews;

/**
 * Enumeration of all possible states of a contest.
 * 
 * @author jani
 * 
 */
public enum ContestState {
	/**
	 * The contest has not started yet.
	 */
	NOT_STARTED(ResourceViews.BeforeStart.class),
	/**
	 * The contest has started yet.
	 */
	STARTED(ResourceViews.AfterStart.class),
	/**
	 * The contest has ended.
	 */
	ENDED(ResourceViews.AfterEnd.class);

	private Class<?> resourceView;

	ContestState(Class<?> view) {
		resourceView = view;
	}

	/**
	 * Gets the appropriate {@link ResourceViews} class which corresponds to the
	 * state.
	 * 
	 * @return A ResourceView class.
	 */
	public Class<?> getView() {
		return resourceView;
	}
}
