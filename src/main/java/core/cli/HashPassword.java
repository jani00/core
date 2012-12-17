package core.cli;

import core.security.UserManager;

/**
 * Command-line tool the administrator can use to hash passwords.
 * 
 * @author deni, joro
 */
public class HashPassword {

	/**
	 * Outputs the hash of the given password.
	 * 
	 * @param args
	 *            the password to hash
	 */
	public static void main(String[] args) {
		if (args == null || args.length != 1) {
			System.err.println("Usage: java HashPassword <password>");
			return;
		}

		String res = UserManager.getPasswordHash(args[0]);
		System.out.println(res);
	}
}
