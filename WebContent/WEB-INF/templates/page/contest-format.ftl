<h1>Contest Format</h1>
<div class="richtext">
	<p>The current document describes the core contests format for the system. <strong>The version of the format described here is 1</strong></p>
	<p>Every contest should be in its own directory with a name matching the pattern <code>[a-z0-9][a-z0-9_-]*</code>. The directory name serves as an identifier for the contest.</p>
	<p>The contest directory should contain the following files (all the files listed below are mandatory, unless stated as optional):</p> 
	 <ul>
		<li>A JSON object, containing the contest metadata, named <code>contest.json</code>. The file should use UTF-8 encoding</li>
		<li>(optional) An arbitrary number of directories for problems for the contest. For more details, see ProblemFormat</li>
		<li>(optional) A subdirectory named <code>_public_files</code>, containing public additional resources for the contest</li>
		<li>(optional, private <code>*</code>) A subdirectory named <code>_files</code>, containing private additional resources for the contest</li>
	</ul>
	<p>The JSON object, describing the contest metadata in <code>contest.json</code> should have the following properties:</p>
	<ul>
		<li><code>"format"</code>: string of the type <code>"core-contest-N"</code>, where <code>N</code> is an integer denoting the version of the contest format used</li>
		<li><code>"title"</code>: string, contest title</li>
		<li><code>"start_time"</code>: UTC formatted date + time, for now only in Zero(Zulu) time</li>
		<li><code>"duration"</code>: integer, duration of the contest (in minutes)</li>
		<li><code>"about"</code>: (optional) string, additional info about the contest</li>
		<li><code>"grading_style"</code>: string, either <code>"acm"</code> or <code>"ioi"</code></li>
		<li><code>"problem_scores"</code>: (optional) array of integers, denoting the scores for each problem specified. Default is 100 for IOI contests and irrelevant for ACM contests (depending on <code>"grading_style"</code>)</li>
		<li><code>"problem_order"</code>: (optional) array of strings, containing the problem identifiers in the order, desired to be presented in the contest. Default is all problems presented as subdirectories to be included in the contest, in their alphabetical order</li>
		<li><code>"notes"</code>: (optional, private) string, additional notes about the contest</li>
	</ul>
	<p><em>*<strong> <code>private</code> </strong> means that these properties aren't visible for all users unless permissions are given</em></p>
	<p>Example:</p>
	<p><code>contest.json</code>:</p>
	<pre><code>{
	 "format": "core-contest-1",
	 "title": "Dummy contest",
	 "start_time": "2012-06-28T09:00:00Z",
	 "duration": 300,
	 "about": "Contains dummy problems",
	 "notes": "Hidden notes",
	 "grading_style": "acm",
	 "problem_order":["problem_fish", "problem_stancho", "another_problem"],
	 "problem_scores": [50, 50, 30]
}
	</code></pre>
	<h3>Additional notes:</h3>
	 <ul>
		<li> If <code>"grading_style"</code> is <code>"acm"</code> in the metadata object, <code>"problem_scores"</code> is ignored (if present)</li>
		<li> <code>"problem_order"</code> describes the problems to be included in the contest, in the order specified. They may be a subset of all the problem sub-directories in the problem directory. If both <code>"problem_order"</code> and <code>"problem_scores"</code> are present, they should be with an equal amount of elements (unless <code>"grading_style"</code> is <code>"acm"</code>, then <code>"problem_scores"</code> is ignored, as pointed above). If only <code>"problem_scores"</code> is present, it should have an amount of elements equal to the all problem sub-directories in the contest directory (a problem sub-directory is one that matches <code>[a-z0-9][a-z0-9_-]*</code>); they would be matched to their points by their alphabetical order</li>
		<li> A contest is considered as valid if its metadata object in <code>contest.json</code> is syntactically and semantically valid (see the above 2 points for semantic validity), and the problems included in the contest are valid (they are identified by either <code>"problem_order"</code> present in the metadata object, or all problem sub-directories in the contest directory)</li>
	 </ul>
</div>