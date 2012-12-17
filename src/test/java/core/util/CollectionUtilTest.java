package core.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unit test for {@link CollectionUtil}.
 * 
 * @author jani
 * 
 */
public class CollectionUtilTest extends TestBase {

	/**
	 * Tests {@link CollectionUtil#filterMap(Map, List, String)}.
	 */
	public void testFilterMap() {
		Map<String, String> testMap = new HashMap<String, String>();
		testMap.put("number_One", "1");
		testMap.put("number_Two", "2");
		testMap.put("number_Three", "3");
		testMap.put("number_Five", "5");

		Map<String, Object> res = CollectionUtil
				.filterMap(testMap,
						Arrays.asList(new String[] { "One", "Two", "Four" }),
						"number_");

		assertTrue(res.containsKey("One"));
		assertTrue(res.containsKey("Two"));
		assertFalse(res.containsKey("Three"));
		assertFalse(res.containsKey("Four"));

		assertEquals(res.get("One"), "1");
		assertEquals(res.get("Two"), "2");

		res = CollectionUtil.filterMap(testMap,
				Arrays.asList(new String[] { "Four" }), "number_");
		assertTrue(res.isEmpty());

		res = CollectionUtil.filterMap(testMap,
				Arrays.asList(new String[] { "One", "Two", "Four" }), "wrong");
		assertTrue(res.isEmpty());
	}

	/**
	 * Tests {@link CollectionUtil#filterRegex(List, String)}.
	 */
	public void testFilterRegex() {
		List<String> testList = Arrays.asList(new String[] { "word_123", "345",
				"567_word" });
		List<String> res = CollectionUtil.filterRegex(testList, "\\w+");
		assertTrue(res.contains("word_123"));
		assertTrue(res.contains("345"));
		assertTrue(res.contains("567_word"));

		res = CollectionUtil.filterRegex(testList, "\\d+");
		assertEquals(res.size(), 1);
		assertFalse(res.contains("word_123"));
		assertTrue(res.contains("345"));
		assertFalse(res.contains("567_word"));

		res = CollectionUtil.filterRegex(testList, "word_123|345");
		assertEquals(res.size(), 2);
		assertTrue(res.contains("word_123"));
		assertTrue(res.contains("345"));
		assertFalse(res.contains("567_word"));

		res = CollectionUtil.filterRegex(testList, "wrong");
		assertTrue(res.isEmpty());
	}

	/**
	 * Tests {@link CollectionUtil#getMatches(List, String, int)}.
	 */
	public void testGetMatches() {
		List<String> testList = Arrays.asList(new String[] { "entry_00",
				"other_01", "entry_02" });

		List<String> res = CollectionUtil.getMatches(testList,
				"entry_([0-9]{2})", 1);

		assertEquals(res.size(), 2);
		assertTrue(res.contains("00"));
		assertFalse(res.contains("01"));
		assertTrue(res.contains("02"));

		res = CollectionUtil.getMatches(testList, "wrong_([0-9]{2})", 1);
		assertTrue(res.isEmpty());
	}

	/**
	 * Tests {@link CollectionUtil#join(List, String)}.
	 */
	public void testJoin() {
		String[] testArray = new String[] { "One", "Two", "Three" };
		String res = CollectionUtil.join(testArray, ",");
		assertEquals(res, "One,Two,Three");

		List<String> testList = Arrays.asList(testArray);
		res = CollectionUtil.join(testList, "|");
		assertEquals(res, "One|Two|Three");

	}
}
