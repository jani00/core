package core.util;

import java.util.Date;

/**
 * Unit test for {@link Parser}.
 * 
 * @author jani
 * 
 */
public class ParserTest extends TestBase {

	/**
	 * Tests {@link Parser#readDate(String)}
	 */
	public void testReadDate() {
		Date res = Parser.readDate("2012-06-07T13:55:12Z");
		assertNotNull(res);

		res = Parser.readDate("malformed2012-06-07T13:55:12Z");
		assertNull(res);
	}

	/**
	 * Tests P{@link Parser#readDouble(String)}
	 */
	public void testReadDouble() {
		String input = "12.3";

		Double res = Parser.readDouble(input);

		assertEquals(12.3, res);

		res = Parser.readDouble("string");
		assertEquals(0.0, res);
	}

	/**
	 * Tests {@link Parser#readInteger(String)}
	 */
	public void testReadInteger() {
		String input = "56";

		Integer res = Parser.readInteger(input);

		assertEquals("" + input, res.toString());

		res = Parser.readInteger("string");
		assertEquals("" + 0, res.toString());
	}

	/**
	 * Tests {@link Parser#readLong(String)}
	 */
	public void testReadLong() {
		String input = "67";

		Long res = Parser.readLong(input);

		assertEquals("" + input, res.toString());

		res = Parser.readLong("string");
		assertEquals("" + 0, res.toString());
	}

	/**
	 * Tests {@link Parser#readObject(Object, Class)}
	 */
	public void testObject() {
		assertEquals(new Integer(12), Parser.readObject("12", Integer.class));
		assertEquals(new Integer(12), Parser.readObject("12", int.class));
		assertNotSame(new Integer(13), Parser.readObject("12", Integer.class));
		assertEquals(new Double(12.3), Parser.readObject("12.3", Double.class));
		assertEquals(new Double(12.3), Parser.readObject("12.3", double.class));
		assertEquals("string", Parser.readObject("string", String.class));
	}

}
