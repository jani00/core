package core.config;

import java.util.Arrays;

import core.util.CollectionUtil;

/**
 * Contains definitions of specific constants related to resource files .
 * 
 * @author jani
 * 
 */
public class ResourceFileConfig {

	/**
	 * Regex for resource id / directory names.
	 */
	public static final String RESOURCE_ID = "[a-z0-9][a-z0-9_-]*";

	/**
	 * File name of security filename.
	 */
	public static final String SECURITY_FILENAME = "_security.json";

	/**
	 * Regex for security filename.
	 */
	public static final String SECURITY_FILENAME_REGEX = "_security\\.json";

	/**
	 * File name of user json schema.
	 */
	public static final String USER_JSON_FILENAME = "user.json";

	/**
	 * Directory name for public files directory of resources.
	 */
	public static final String RESOURCE_PUBLIC_DIRECTORY_NAME = "_public_files";
	/**
	 * Directory name for private files directory of resources.
	 */
	public static final String RESOURCE_PRIVATE_DIRECTORY_NAME = "_files";

	/**
	 * Regex for public and private directory of resources.
	 */
	public static final String ADDITIONAL_FILES = String.format("%s|%s",
			RESOURCE_PUBLIC_DIRECTORY_NAME, RESOURCE_PRIVATE_DIRECTORY_NAME);

	/**
	 * Regex for problem test files.
	 */
	public static final String PROBLEM_TEST_FILE_REGEX = "(test\\.([0-9]{2}\\.)?(in|ans))";
	/**
	 * Regex for problem description files.
	 */
	public static final String PROBLEM_DESCRIPTION_FILE_REGEX = "(description(.*))";
	/**
	 * Regex for problem checker files.
	 */
	public static final String PROBLEM_CHECKER_FILE_REGEX = "(checker(.*))";
	/**
	 * Regex for problem solution files.
	 */
	public static final String PROBLEM_SOLUTION_FILE_REGEX = "(solution(.*))";
	/**
	 * Regex for a single test.in file.
	 */
	public static final String PROBLEM_TEST_IN_FILE_SINGLE_REGEX = "test\\.in";
	/**
	 * Regex for a single test.ans file.
	 */
	public static final String PROBLEM_TEST_ANS_FILE_SINGLE_REGEX = "test\\.ans";
	/**
	 * Regex for multiple single test.in files.
	 */
	public static final String PROBLEM_TEST_IN_FILE_MULTI_REGEX = "test\\.([0-9]{2})\\.in";
	/**
	 * Regex for multiple single test.ans files.
	 */
	public static final String PROBLEM_TEST_ANS_FILE_MULTI_REGEX = "test\\.([0-9]{2})\\.ans";

	/**
	 * Accumulation of regexes for problem extra files.
	 */
	public static final String PROBLEM_EXTRA_FILES = CollectionUtil.join(
			Arrays.asList(new String[] { PROBLEM_TEST_FILE_REGEX,
					PROBLEM_DESCRIPTION_FILE_REGEX, PROBLEM_CHECKER_FILE_REGEX,
					PROBLEM_SOLUTION_FILE_REGEX }), "|");

	/**
	 * Regex for new directory.
	 */
	public static final String NEW_DIRECTORY = "[a-z0-9][a-z0-9_ -]*";
	/**
	 * Match for any file.
	 */
	public static final String ANY_FILE = ".*";
}
