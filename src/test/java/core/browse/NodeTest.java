package core.browse;

import java.io.File;
import java.util.List;

import core.model.Resource;
import core.model.ResourceKind;
import core.util.TestBase;

/**
 * Tests {@link Node}.
 * 
 * @author jani
 * 
 */
public class NodeTest extends TestBase {
	private File tempDir;
	private File seriesRoot;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		tempDir = getTempDir();
		extractTestZip(tempDir, TEST_REPO);
		seriesRoot = new File(tempDir, "series");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		deleteDir(tempDir);
	}

	/**
	 * Tests {@link Node#getType()}.
	 */
	public void testGetNodeType() {
		Node node = new Node(seriesRoot, "/", "");
		assertSame(NodeType.ROOT, node.getType());

		node = new Node(seriesRoot, "/series_1", "");
		assertSame(NodeType.RESOURCE, node.getType());

		node = new Node(seriesRoot, "/series_1/contest", "");
		assertSame(NodeType.RESOURCE, node.getType());

		node = new Node(seriesRoot, "/series_1/contest_1/problem_1", "");
		assertSame(NodeType.RESOURCE, node.getType());

		node = new Node(seriesRoot, "/series_1/_files", "");
		assertSame(NodeType.SYSTEM_DIRECTORY, node.getType());

		node = new Node(seriesRoot, "/series_1/_public_files", "");
		assertSame(NodeType.SYSTEM_DIRECTORY, node.getType());

		node = new Node(seriesRoot, "/series_1/_security.json", "");
		assertSame(NodeType.SYSTEM_FILE, node.getType());

		node = new Node(seriesRoot, "/series_1/series.json", "");
		assertSame(NodeType.SYSTEM_FILE, node.getType());

		node = new Node(seriesRoot,
				"/series_1/contest_1/problem_1/description.txt", "");
		assertSame(NodeType.PROBLEM_FILE, node.getType());

		node = new Node(seriesRoot,
				"/series_1/contest_1/problem_1/checker.cpp", "");
		assertSame(NodeType.PROBLEM_FILE, node.getType());
	}

	/**
	 * Tests {@link Node#getResouceKind()}.
	 */
	public void testGetResourceKind() {
		Node node = new Node(seriesRoot, "/", "");
		assertSame(null, node.getResouceKind());

		node = new Node(seriesRoot, "/series_1", "");
		assertSame(ResourceKind.SERIES, node.getResouceKind());

		node = new Node(seriesRoot, "/series_1/contest", "");
		assertSame(ResourceKind.CONTEST, node.getResouceKind());

		node = new Node(seriesRoot, "/series_1/contest_1/problem_1", "");
		assertSame(ResourceKind.PROBLEM, node.getResouceKind());

		node = new Node(seriesRoot, "/series_1/_files", "");
		assertSame(ResourceKind.SERIES, node.getResouceKind());

		node = new Node(seriesRoot, "/series_1/_public_files", "");
		assertSame(ResourceKind.SERIES, node.getResouceKind());

		node = new Node(seriesRoot, "/series_1/_security.json", "");
		assertSame(ResourceKind.SERIES, node.getResouceKind());

		node = new Node(seriesRoot, "/series_1/series.json", "");
		assertSame(ResourceKind.SERIES, node.getResouceKind());

		node = new Node(seriesRoot,
				"/series_1/contest_1/problem_1/description.txt", "");
		assertSame(ResourceKind.PROBLEM, node.getResouceKind());

		node = new Node(seriesRoot,
				"/series_1/contest_1/problem_1/checker.cpp", "");
		assertSame(ResourceKind.PROBLEM, node.getResouceKind());
	}

	/**
	 * Tests {@link Node#getResourceDirectory()}.
	 */
	public void testGetResourceDirectory() {
		Node node = new Node(seriesRoot, "/series_1/contest_1", "");
		assertEquals(new File(seriesRoot, "/series_1/contest_1"),
				node.getResourceDirectory());

		node = new Node(seriesRoot,
				"/series_1/contest_1/problem_1/description", "");

		assertEquals(new File(seriesRoot, "/series_1/contest_1/problem_1"),
				node.getResourceDirectory());
	}

	/**
	 * Tests {@link Node#loadChildResources()}.
	 */
	public void testLoadChildResource() {
		Node node = new Node(seriesRoot, "/", "milo");
		List<Resource> list = node.loadChildResources();
		assertSame(5, list.size());
	}

	/**
	 * Tests {@link Node#getParent()}.
	 */
	public void testGetParent() {
		Node node = new Node(seriesRoot, "/series_1/contest_1", "");
		Node parent = node.getParent();
		assertEquals(new File(seriesRoot, "series_1"), parent.getFile());
	}

	/**
	 * Tests {@link Node#getChild(String)}.
	 */
	public void testGetChild() {
		Node node = new Node(seriesRoot, "/series_1", "");
		Node child = node.getChild("contest_1");
		assertEquals(new File(seriesRoot, "series_1/contest_1"),
				child.getFile());
	}
}
