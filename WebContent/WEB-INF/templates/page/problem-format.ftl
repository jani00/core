<h1>Problem Format </h1>
<div class="richtext">
	<p>The current documents describes the core problem format for the system. <b>The version of the format described here is 1.</b></p>
	<p>Every problem should be in a separate directory with a name matching the pattern <code>[a-z0-9][a-z0-9_-]*</code>. The directory name serves as an identifier for the problem.</p> 
	<p>The problem directory should contain the following files (all the files listed below are mandatory for the problem to be valid, unless stated as optional):</p> 
	<ul>
	 <li>A JSON object, describing the problem metadata, in a file called <code>problem.json</code>. The file should use UTF-8 encoding</li>
	 <li>An arbitrary number of files (but at least one) with names matching the pattern <code>description*</code> (e.g. <code>description.pdf</code>). Those serve as description files for the problem</li>
	 <li> 
		<ol>
			<li>For problems with one test, a file named <code>test.in</code> - the test input file, and a corresponding <code>test.ans</code> - the test answer file</li>
			<li>For multi-test problems, files named <code>test.00.in</code>, <code>test.01.in</code>, etc - test input files, and <code>test.00.ans</code>, <code>test.01.ans</code>, etc - respectively test answer files
				<b>Note: test pairs should have consecutive numbers, starting from 00, with no numbers skipped</b></li>
		</ol>
	</li>
	 <li>(optional) Source file with a name matching <code>checker*</code> - a checker for the problem. The checker should accept 3 command line parameters - <code>&lt;input_file&gt;, &lt;answer_file&gt;, &lt;output_file&gt;</code>, which are respectively the test input file, test answer file and the contestant output file</li>
	 <li>(optional) Any number of files named <code>solution*</code> (e.g. <code>solution_manev.cpp</code>) - author solutions for the problem</li>
	 <li>(optional) A subdirectory named <code>_public_files</code>, containing public additional resources for the problem</li>
	 <li>(optional, private <code>*</code>) A subdirectory named <code>_files</code>, containing private additional resources for the problem</li>
	</ul>
	<p>The JSON object describing the problem metadata in `problem.json` should have the following properties:</p>
	<ul>
	 <li><code>"format"</code> : string of the type <code>"core-problem-N"</code>, where N is an integer denoting the version of the problem format used</li>
	 <li><code>"title"</code> : string, problem title</li>
	 <li><code>"about"</code> : (optional) string, containing additional info about the problem</li>
	 <li><code>"time_limit"</code> : (private) floating-point number, denotes the time limit of the problem, in seconds</li>
	 <li><code>"memory_limit"</code> : (optional, private) integer, denotes the memory limit of the problem, in mebibytes (2^20 bytes)</li>
	 <li><code>"origin"</code> : (optional, private) string, containing info about the origin</li>
	 <li><code>"notes"</code> : (optional, private) string, additional notes about the problem</li>
	 <li><code>"authors"</code>: (optional, private) An array of strings, listing problem authors</li>
	 <li><code>"test_weights"</code>: (optional, private) An array of integers, denoting what is the relative weight of each test (if applicable)</li>
	 <li><code>"checker"</code>: string, either <code>"diff"</code> or <code>"custom"</code>. <code>"diff"</code> means that the contestant output for each test should be compared directly to the answer file, and <code>"custom"</code> means that a checker should be used</li>
	</ul>
	<p><i>`*` <b>private</b> means that these properties aren't visible for all users unless permissions are given</i></p>
	<p>Example:</p>
	<pre><code>{ 
	 "format": "core-problem-1", 
	 "title": "Dummy problem", 
	 "about": "",
	 "time_limit": 4.5, 
	 "memory_limit": 20, 
	 "test_weights": [100, 50],
	 "origin": "taken from contest-x", 
	 "notes": "DP", 
	 "authors":["author1", "author2"], 
	 "checker": "diff" 
} 
	</code></pre>
	<h3>Additional notes:</h3>
	<ul>
	 <li>If there is only one test file and <code>"test_weights"</code> is present in the metadata object, it is ignored.</li>
	 <li>If there are multiple tests, the number of elements in <code>"test_weights"</code> must be equal to the test files count</li>
	 <li>If <code>"checker"</code> is <code>"custom"</code> in the metadata object, there must be a checker source file in the problem directory</li>
	</ul>
</div>