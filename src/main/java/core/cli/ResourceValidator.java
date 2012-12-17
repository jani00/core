package core.cli;

import java.io.File;
import java.util.Map;

import core.model.Resource;
import core.model.ResourceKind;
import core.model.ResourceUtil;

/**
 * Command-line tool for validating resources.
 * 
 * @author joro, petko
 * 
 */
public class ResourceValidator {

	private static final String SERIES = "-s";
	private static final String CONTEST = "-c";
	private static final String PROBLEM = "-p";

	/**
	 * Validates the resource file structure and prints the result. Use the -s
	 * option to validate a series, -c to validate a contest and -p to validate
	 * a problem.
	 * 
	 * @param args
	 *            args[0] = "-p | -c | -s"; args[1] = resource path
	 */
	public static void main(String[] args) {
		if (args == null || args.length != 2) {
			System.err
					.println("Usage: java ResourceValidator -option <resource_path>");
			System.err.println("valid options:");
			System.err.println("\t -s\t\t indicates a series");
			System.err.println("\t -c\t\t indicates a contest");
			System.err.println("\t -p\t\t indicates a problem");
			return;
		}

		String option = args[0];
		String resourcePath = args[1];
		ResourceKind kind = getResourceKind(option);

		if (kind == null) {
			System.err.println("Invalid option: use -s, -c or -p.");
		} else {
			File file = new File(resourcePath);
			Resource resource = ResourceUtil.loadResource(file, kind);
			if (resource == null) {
				System.err.println("Unable to load the specified resource.");
				return;
			}
			Map<String, String> errors = resource.validate().getErrors();
			if (errors.size() == 0) {
				System.out.println("The specified resource is valid.");
			} else {
				System.out.println("The specified resource is NOT valid:");
				for (String key : errors.keySet()) {
					System.out.println(errors.get(key));
				}
			}
		}
	}

	/**
	 * Gets the ResourceKind that corresponds to the cli tool option
	 * 
	 * @param option
	 *            The input option.
	 * @return The corresponding ResourceKind.
	 */
	private static ResourceKind getResourceKind(String option) {
		ResourceKind res = null;

		if (SERIES.equalsIgnoreCase(option)) {
			res = ResourceKind.SERIES;
		} else if (CONTEST.equalsIgnoreCase(option)) {
			res = ResourceKind.CONTEST;
		} else if (PROBLEM.equalsIgnoreCase(option)) {
			res = ResourceKind.PROBLEM;
		}

		return res;
	}
}
