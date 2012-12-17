package core.util;

/**
 * Unit test for {@link StringUtil}.
 * 
 * @author jani
 * 
 */
public class StringUtilTest extends TestBase {

	/**
	 * Tests {@link StringUtil#sha256(String)}.
	 */
	public void testSha256() {
		String input = "test";
		String sha256 = StringUtil.sha256(input);
		assertEquals(
				"9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08",
				sha256);
	}

}
