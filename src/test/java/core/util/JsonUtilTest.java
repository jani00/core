package core.util;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unit test for {@link JsonUtil}.
 * 
 * @author jani
 * 
 */
public class JsonUtilTest extends TestBase {

	private File tempDir;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		tempDir = getTempDir();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		deleteDir(tempDir);
	}

	/**
	 * Tests {@link JsonUtil#readJsonString(String, Class)}.
	 */
	@SuppressWarnings("unchecked")
	public void testReadJsonString() {
		String json = "{\"key\":\"value\"}";
		Map<String, String> map = JsonUtil.readJsonString(json, Map.class);
		assertTrue(map.containsKey("key"));
		assertEquals(map.get("key"), "value");
		String arr = "[1, 2, 3, 5]";
		List<Integer> list = JsonUtil.readJsonString(arr, List.class);
		assertEquals(list.size(), 4);
	}

	/**
	 * Tests {@link JsonUtil#writeJsonFile(File, Object)} and
	 * {@link JsonUtil#readJsonString(String, Class)}.
	 */
	@SuppressWarnings("unchecked")
	public void testJsonFile() {
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("key", "value");
		json.put("list", new Integer[] { 1, 2, 3, 5 });
		File dest = new File(tempDir, "test.json");
		JsonUtil.writeJsonFile(dest, json);
		assertTrue(dest.exists());

		Map<String, Object> read = JsonUtil.readJsonFile(dest, Map.class);
		assertEquals(read.get("key"), json.get("key"));
		assertEquals(((List<Integer>) read.get("list")).size(), 4);
	}

	/**
	 * Tests {@link JsonUtil#objectToJsonString(Object)}.
	 */
	public void testObjectToString() {
		Map<String, Object> o = new HashMap<String, Object>();
		o.put("key", "value");
		String json = JsonUtil.objectToJsonString(o);
		assertEquals("{\"key\":\"value\"}", json);
	}
}
