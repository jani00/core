package core.model;

import core.util.TestBase;

/**
 * Tests {@link ResourceKind}.
 * 
 * @author jani
 * 
 */
public class ResourceKindTest extends TestBase {

	/**
	 * Tests {@link ResourceKind#getSuccessorKind()}.
	 */
	public void testGetSuccessorKind() {
		assertEquals(ResourceKind.CONTEST,
				ResourceKind.SERIES.getSuccessorKind());
		assertEquals(ResourceKind.PROBLEM,
				ResourceKind.CONTEST.getSuccessorKind());
		assertNull(ResourceKind.PROBLEM.getSuccessorKind());
	}

	/**
	 * Tests {@link ResourceKind#getPredecessorKind()}.
	 */
	public void testGetPredecessorKind() {
		assertEquals(ResourceKind.CONTEST,
				ResourceKind.PROBLEM.getPredecessorKind());
		assertEquals(ResourceKind.SERIES,
				ResourceKind.CONTEST.getPredecessorKind());
		assertNull(ResourceKind.SERIES.getPredecessorKind());
	}
}
