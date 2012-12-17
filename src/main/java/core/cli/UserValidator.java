package core.cli;

import java.io.File;

import core.security.UserManager;
import core.util.Validator;

/**
 * Command-line tool the administrator can use to validate user data.
 * 
 * @author deni, joro
 */
public class UserValidator {

	/**
	 * Validates the user data in the given file and prints the result.
	 * 
	 * @param args
	 *            the root of the repository file system
	 * @throws IOException
	 */
	public static void main(String[] args) {
		if (args == null || args.length != 1) {
			System.err.println("Usage: java ResourceValidator <users_root>");
			return;
		}

		File usersRoot = new File(args[0]);

		if (usersRoot.isDirectory()) {
			UserManager userManager = new UserManager(usersRoot);
			Validator v = userManager.validateAll();
			if (v.isValid()) {
				System.out.println("All users are valid.");
			} else {
				for (String key : v.getErrors().keySet()) {
					System.out.println(v.getErrors().get(key));
				}
			}
		} else {
			System.err.println("The spcified root directory does not exists.");
		}
	}
}
