package core.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
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
 * A representation of a contest.
 * 
 * @author joro, petko, jani
 * 
 */
public class Contest extends Resource {

	@JsonProperty("start_time")
	@JsonView(ResourceViews.BeforeStart.class)
	private String startTime;

	@JsonView(ResourceViews.BeforeStart.class)
	private Integer duration;

	@JsonView(ResourceViews.BeforeStart.class)
	@JsonProperty("grading_style")
	private String gradingStyle;

	@JsonView(ResourceViews.AfterStart.class)
	@JsonProperty("problem_scores")
	private List<Integer> problemScores;

	@JsonView(ResourceViews.AfterStart.class)
	@JsonProperty("problem_order")
	private List<String> problemOrder;

	/**
	 * Default constructor
	 */
	public Contest() {
		setFormat(ResourceConfig.CONTEST_FORMAT);
		startTime = "";
		duration = 0;
		gradingStyle = ResourceConfig.GRADING_STYLES[0];
		problemScores = new ArrayList<Integer>();
		problemOrder = new ArrayList<String>();
	}

	@Override
	public ResourceKind getKind() {
		return ResourceKind.CONTEST;
	}

	/**
	 * Gets the contest starting time.
	 * 
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * Sets the contest starting time.
	 * 
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * Gets the contest duration.
	 * 
	 * @return the duration
	 */
	public Integer getDuration() {
		return duration;
	}

	/**
	 * Sets the contest duration.
	 * 
	 * @param duration
	 *            the duration to set
	 */
	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	/**
	 * Gets the contest grading style.
	 * 
	 * @return the gradingStyle
	 */
	public String getGradingStyle() {
		return gradingStyle;
	}

	/**
	 * Sets the contest grading style.
	 * 
	 * @param gradingStyle
	 *            the gradingStyle to set
	 */
	public void setGradingStyle(String gradingStyle) {
		if (Arrays.asList(ResourceConfig.GRADING_STYLES).contains(gradingStyle)) {
			this.gradingStyle = gradingStyle;
		}
	}

	/**
	 * Gets the problem scores.
	 * 
	 * @return the problemScores
	 */
	public List<Integer> getProblemScores() {
		return problemScores;
	}

	/**
	 * Sets the problem scores.
	 * 
	 * @param problemScores
	 *            the problemScores to set
	 */
	public void setProblemScores(List<Integer> problemScores) {
		this.problemScores = problemScores;
	}

	/**
	 * Gets the problem order.
	 * 
	 * @return the problemOrder
	 */
	public List<String> getProblemOrder() {
		return problemOrder;
	}

	/**
	 * Sets the problem order.
	 * 
	 * @param problemOrder
	 *            the problemOrder to set
	 */
	public void setProblemOrder(List<String> problemOrder) {
		this.problemOrder = problemOrder;
	}

	/**
	 * Gets a comma separated list of problem order
	 * 
	 * @return String representation of problem order.
	 */
	@JsonIgnore
	public String getProblemOrderString() {
		return CollectionUtil.join(problemOrder, ", ");
	}

	/**
	 * Sets the problem order from a string. The string is comma split and empty
	 * values are omitted.
	 * 
	 * @param input
	 *            Comma separated problem order.
	 */
	public void setProblemOrderString(String input) {
		this.problemOrder.clear();
		String[] split = input.split(",");
		for (String s : split) {
			s = s.trim();
			if (!s.isEmpty()) {
				this.problemOrder.add(s);
			}
		}
	}

	/**
	 * Gets a comma separated list of problem scores
	 * 
	 * @return String representation of problem scores.
	 */
	@JsonIgnore
	public String getProblemScoresString() {
		return CollectionUtil.join(problemScores, ", ");
	}

	/**
	 * Sets the problem scores from a string. The string is comma split and
	 * empty values are omitted.
	 * 
	 * @param input
	 *            Comma separated problem scores.
	 */
	public void setProblemScoresString(String input) {
		this.problemScores.clear();
		String[] split = input.split(",");
		for (String s : split) {
			s = s.trim();
			if (!s.isEmpty()) {
				this.problemScores.add(Parser.readInteger(s.trim()));
			}
		}
	}

	/**
	 * Checks if the contest has started.
	 * 
	 * @return whether the contest has started
	 */
	@JsonIgnore
	public boolean hasStarted() {
		return isPassed(0);
	}

	/**
	 * Checks if the contest has ended.
	 * 
	 * @return whether the contest has ended
	 */
	@JsonIgnore
	public boolean hasEnded() {
		return isPassed(duration);
	}

	@JsonIgnore
	private boolean isPassed(int minutes) {
		Date now = new Date();
		Date date = Parser.readDate(startTime);
		return date != null
				&& now.getTime() >= date.getTime() + (long) minutes * 60 * 1000;
	}

	/**
	 * Calculated the {@link ContestState} of the contest.
	 * 
	 * @return {@link ContestState} of the contest.
	 */
	@JsonIgnore
	public ContestState getState() {
		if (hasEnded()) {
			return ContestState.ENDED;
		}
		if (hasStarted()) {
			return ContestState.STARTED;
		}
		return ContestState.NOT_STARTED;
	}

	@Override
	public Validator validate() {
		Validator res = super.validate();

		List<Problem> children = loadChildren();

		if (getDirectory() != null) {
			res.merge(validateFiles(children));
		}

		if (Parser.readDate(getStartTime()) == null) {
			res.addError("startTime", "Invalid start time");
		}

		if (children.size() != problemScores.size()) {
			res.addError("mismatch_problem_scores",
					"problems and scores mismatch");
		}

		if (children.size() != problemOrder.size()) {
			res.addError("mismatch_problem_order",
					"problems and order  mismatch");
		} else {
			List<String> problemNames = new ArrayList<String>();
			for (Problem problem : children) {
				problemNames.add(problem.getId());
			}
			List<String> sortedOrder = new ArrayList<String>(problemOrder);
			Collections.sort(sortedOrder);
			Collections.sort(problemNames);
			assert sortedOrder.size() == problemNames.size();
			for (int i = 0; i < problemNames.size(); i++) {
				if (!(problemNames.get(i).equals(sortedOrder.get(i)))) {
					res.addError("mismatch_problem_order",
							"problems and order mismatch");
				}
			}
		}

		for (int i = 0; i < children.size(); i++) {
			Validator v = children.get(i).validate();
			res.merge(v, "child_" + i);
		}

		return res;

	}

	private Validator validateFiles(List<Problem> children) {
		Validator res = new Validator();

		assert getDirectory() != null;

		List<String> files = FileUtil.getDirectoryFileNames(getDirectory());

		String[] pattern = new String[] { ResourceFileConfig.ADDITIONAL_FILES,
				getFilename(), ResourceFileConfig.SECURITY_FILENAME_REGEX };

		List<String> allowed = CollectionUtil.filterRegex(files,
				CollectionUtil.join(pattern, "|"));

		if (allowed.size() + children.size() < files.size()) {
			res.addError("file_extra", "contains unknown files");
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

		if (access.includes(AccessRight.VIEW_FULL) || hasStarted()) {
			List<Resource> children = loadChildren();
			for (Resource child : children) {
				AccessRight childAccess = child.getAccessRight(login);
				DownloadBuilder d = child.getDownloader(login, childAccess);
				res.add(root, d);
			}
		}

		return res;
	}

	@Override
	public String getJsonString(AccessRight access) {
		ContestState state = getState();
		Class<?> view;

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
			regex.add(ResourceFileConfig.SECURITY_FILENAME_REGEX);
			regex.add(ResourceFileConfig.RESOURCE_PUBLIC_DIRECTORY_NAME);
			regex.add(ResourceFileConfig.RESOURCE_PRIVATE_DIRECTORY_NAME);
			regex.add(getFilename());
		} else if (access.includes(AccessRight.VIEW)) {
			if (hasStarted()) {
				regex.add(ResourceFileConfig.RESOURCE_PUBLIC_DIRECTORY_NAME);
			}
			regex.add(getFilename());
		}
		String match = CollectionUtil.join(regex, "|");
		res = FileUtil.getDirectoryFiles(getDirectory(), match);
		return res;
	}
}
