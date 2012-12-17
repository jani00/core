package core.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonView;

import core.config.ResourceConfig;
import core.config.ResourceFileConfig;
import core.security.AccessRight;
import core.security.ResourceViews;
import core.util.CollectionUtil;
import core.util.FileUtil;
import core.util.JsonUtil;
import core.util.Parser;
import core.util.Validator;
import core.web.util.DownloadBuilder;

/**
 * A representation of a problem.
 * 
 * @author joro, petko, jani
 * 
 */
public class Problem extends Resource {
	@JsonView(ResourceViews.Private.class)
	private List<String> authors;

	@JsonProperty("time_limit")
	@JsonView(ResourceViews.Private.class)
	private Double timeLimit;

	@JsonProperty("memory_limit")
	@JsonView(ResourceViews.Private.class)
	private Integer memoryLimit;

	@JsonView(ResourceViews.Private.class)
	private String origin;

	@JsonProperty("test_weights")
	@JsonView(ResourceViews.AfterEnd.class)
	private List<Integer> testWeights;

	@JsonView(ResourceViews.Private.class)
	private String checker;

	/***
	 * Gets a default instance of a problem resource.
	 */
	public Problem() {
		setFormat(ResourceConfig.PROBLEM_FORMAT);
		checker = "diff";
		origin = "";
		authors = new ArrayList<String>();
		testWeights = new ArrayList<Integer>();
		memoryLimit = 0;
		timeLimit = 0d;
	}

	@Override
	public ResourceKind getKind() {
		return ResourceKind.PROBLEM;
	}

	/**
	 * Gets the problem authors.
	 * 
	 * @return the authors
	 */
	public List<String> getAuthors() {
		return authors;
	}

	/**
	 * Sets the problem authors.
	 * 
	 * @param authors
	 *            the authors to set
	 */
	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	/**
	 * Gets the problem time limit.
	 * 
	 * @return the timeLimit
	 */
	public double getTimeLimit() {
		return timeLimit;
	}

	/**
	 * Sets the problem time limit.
	 * 
	 * @param timeLimit
	 *            the timeLimit to set
	 */
	public void setTimeLimit(double timeLimit) {
		this.timeLimit = timeLimit;
	}

	/**
	 * Gets the problem memory limit.
	 * 
	 * @return the memoryLimit
	 */
	public Integer getMemoryLimit() {
		return memoryLimit;
	}

	/**
	 * Sets the problem memory limit.
	 * 
	 * @param memoryLimit
	 *            the memoryLimit to set
	 */
	public void setMemoryLimit(Integer memoryLimit) {
		this.memoryLimit = memoryLimit;
	}

	/**
	 * Gets the problem origin.
	 * 
	 * @return the origin
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * Sets the problem origin.
	 * 
	 * @param origin
	 *            the origin to set
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	/**
	 * Sets the problem test weights.
	 * 
	 * @return the testWeights
	 */
	public List<Integer> getTestWeights() {
		return testWeights;
	}

	/**
	 * Sets the problem test weights.
	 * 
	 * @param testWeights
	 *            the testWeights to set
	 */
	public void setTestWeights(List<Integer> testWeights) {
		this.testWeights = testWeights;
	}

	/**
	 * Gets the problem checker file name.
	 * 
	 * @return the checker
	 */
	public String getChecker() {
		return checker;
	}

	/**
	 * Sets the problem checker file name.
	 * 
	 * @param checker
	 *            the checker to set
	 */
	public void setChecker(String checker) {
		this.checker = checker;
	}

	/**
	 * Gets a comma separated list of authors
	 * 
	 * @return String representation of authors.
	 */
	@JsonIgnore
	public String getAuthorsString() {
		return CollectionUtil.join(authors, ", ");
	}

	/**
	 * Sets the authors from a string. The string is comma split and empty
	 * values are omitted.
	 * 
	 * @param input
	 *            Comma separated authors.
	 */
	public void setAuthorsString(String input) {
		this.authors.clear();
		String[] split = input.split(",");
		for (String s : split) {
			s = s.trim();
			if (!s.isEmpty()) {
				this.authors.add(s);
			}
		}
	}

	/**
	 * Gets a comma separated list of test weights
	 * 
	 * @return String representation of test weights.
	 */
	@JsonIgnore
	public String getTestWeightsString() {
		return CollectionUtil.join(testWeights, ", ");
	}

	/**
	 * Sets the test weights from a string. The string is comma split and empty
	 * values are omitted.
	 * 
	 * @param input
	 *            Comma separated test weights.
	 */
	public void setTestWeightsString(String input) {
		this.testWeights.clear();
		String[] split = input.split(",");
		for (String s : split) {
			s = s.trim();
			if (!s.isEmpty()) {
				this.testWeights.add(Parser.readInteger(s));
			}
		}
	}

	@Override
	public Validator validate() {
		Validator res = super.validate();

		if (getDirectory() != null) {
			res.merge(validateFiles());
		}
		return res;
	}

	private Validator validateFiles() {
		Validator res = new Validator();

		assert getDirectory() != null;

		List<String> files = FileUtil.getDirectoryFileNames(getDirectory());

		List<String> descriptions = CollectionUtil.filterRegex(files,
				ResourceFileConfig.PROBLEM_DESCRIPTION_FILE_REGEX);
		if (descriptions.size() < 1) {
			res.addError("file_description", "description file is missing");
		}

		if (checker.equals(ResourceConfig.PROBLEM_CUSTOM_CHECKERS)) {
			List<String> checkers = CollectionUtil.filterRegex(files,
					ResourceFileConfig.PROBLEM_CHECKER_FILE_REGEX);
			if (checkers.size() == 0) {
				res.addError("file_checker", "checker file is missing");
			}
			if (checkers.size() > 1) {
				res.addError("file_checker", "multiple checker files");
			}
		}

		String[] pattern = new String[] { ResourceFileConfig.ADDITIONAL_FILES,
				ResourceFileConfig.PROBLEM_EXTRA_FILES, getFilename(),
				ResourceFileConfig.SECURITY_FILENAME_REGEX };

		List<String> allowed = CollectionUtil.filterRegex(files,
				CollectionUtil.join(pattern, "|"));
		if (allowed.size() < files.size()) {
			res.addError("file_extra", "contains unknown files");
		}

		boolean hasSingleTestIn = CollectionUtil.hasMatch(files,
				ResourceFileConfig.PROBLEM_TEST_IN_FILE_SINGLE_REGEX);
		boolean hasSingleTestAns = CollectionUtil.hasMatch(files,
				ResourceFileConfig.PROBLEM_TEST_ANS_FILE_SINGLE_REGEX);
		List<String> matchTestIn = CollectionUtil.getMatches(files,
				ResourceFileConfig.PROBLEM_TEST_IN_FILE_MULTI_REGEX, 1);
		List<String> matchTestAns = CollectionUtil.getMatches(files,
				ResourceFileConfig.PROBLEM_TEST_ANS_FILE_MULTI_REGEX, 1);
		int testWeightsSize = testWeights.size();

		boolean singleTest = hasSingleTestIn && hasSingleTestAns;
		boolean multiTest = matchTestIn.size() > 0 && matchTestAns.size() > 0;

		boolean testsValid = true;
		if (!(singleTest ^ multiTest)) {
			testsValid = false;
			res.addError("file_tests", "tests files are missing");
		}
		if (multiTest) {
			Collections.sort(matchTestIn);
			Collections.sort(matchTestAns);
			if (matchTestIn.size() != matchTestAns.size()) {
				testsValid = false;
				res.addError("file_tests",
						"some test.xx.in or test.xx.ans file is missing.");
			} else {
				for (int i = 0; i < matchTestIn.size(); i++) {
					int t1 = Parser.readInteger(matchTestIn.get(i));
					int t2 = Parser.readInteger(matchTestAns.get(i));
					if (t1 != i || t2 != i) {
						testsValid = false;
						res.addError("file_tests",
								"some test.xx.in or test.xx.ans file is missing.");
						break;
					}
				}
			}

		}

		if (multiTest && testsValid && matchTestIn.size() != testWeightsSize) {
			res.addError("test_weights", "test files and test weights mismatch");
		}
		return res;
	}

	@Override
	public DownloadBuilder getDownloader(String login, AccessRight access) {
		String root = getDirectory().getName() + "/";

		String json = getJsonString(access);

		List<File> files = getFilesList(access);

		DownloadBuilder res = new DownloadBuilder();
		res.addInline(root + getFilename(), json.getBytes());
		res.addRefs(root, files);

		return res;
	}

	@Override
	public String getJsonString(AccessRight access) {
		Class<?> view;
		Contest parent = loadParent();
		ContestState state = ContestState.NOT_STARTED;
		if (parent != null) {
			state = parent.getState();
		}
		if (access.includes(AccessRight.VIEW_FULL)) {
			view = ResourceViews.Private.class;
		} else {
			view = state.getView();
			List<String> regex = new ArrayList<String>();
			switch (state) {
			case ENDED:
			case STARTED:
				regex.add(ResourceFileConfig.RESOURCE_PUBLIC_DIRECTORY_NAME);
				regex.add(ResourceFileConfig.PROBLEM_DESCRIPTION_FILE_REGEX);
			case NOT_STARTED:
				break;
			}
		}

		return JsonUtil.objectToJsonString(this, view);
	}

	@Override
	public List<File> getFilesList(AccessRight access) {
		List<File> res = new ArrayList<File>();
		List<String> regex = new ArrayList<String>();
		if (access.includes(AccessRight.VIEW_FULL)) {
			regex.add(ResourceFileConfig.PROBLEM_DESCRIPTION_FILE_REGEX);
			regex.add(ResourceFileConfig.PROBLEM_TEST_FILE_REGEX);
			regex.add(ResourceFileConfig.PROBLEM_CHECKER_FILE_REGEX);
			regex.add(ResourceFileConfig.PROBLEM_SOLUTION_FILE_REGEX);
			regex.add(ResourceFileConfig.SECURITY_FILENAME_REGEX);
			regex.add(ResourceFileConfig.RESOURCE_PUBLIC_DIRECTORY_NAME);
			regex.add(ResourceFileConfig.RESOURCE_PRIVATE_DIRECTORY_NAME);
			regex.add(getFilename());
		} else if (access.includes(AccessRight.VIEW)) {
			Contest contest = loadParent();
			if (contest != null) {
				if (contest.hasStarted()) {
					regex.add(ResourceFileConfig.PROBLEM_DESCRIPTION_FILE_REGEX);
					regex.add(ResourceFileConfig.RESOURCE_PUBLIC_DIRECTORY_NAME);
				}
			}
			regex.add(getFilename());
		}
		String match = CollectionUtil.join(regex, "|");
		res = FileUtil.getDirectoryFiles(getDirectory(), match);
		return res;
	}

}
