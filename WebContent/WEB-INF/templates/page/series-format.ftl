<h1>Series Format </h1>
<div class="richtext">
	<p>The current documents describes the core series format for the system. <strong>The version of the format described here is 1</strong>.</p>
	<p>Each series should be in a separate directory with a name matching the pattern <code>[a-z0-9][a-z0-9_-]*</code>. The directory name serves as an identifier for the series. </p>
	<p>The problem directory should contain the following files (all the files listed below are mandatory, unless stated as optional):</p> 
	 <ul>
		<li>A JSON object, describing the series metadata, in a file named <code>series.json</code>. The file should use UTF-8 encoding</li>
		<li>(optional) An arbitrary number of directories for contests for the series. For more details, see <a href="${appRoot}/contest-format">Contest Format</a></li>
		<li>(optional) A subdirectory named <code>_public_files</code>, containing public additional resources for the series</li>
		<li>(optional, private <code>*</code>) A subdirectory named <code>_files</code>, containing private additional resources for the series</li>
	</ul>
	<p>The JSON object, describing the contest metadata in <code>series.json</code> should have the following properties:</p>
	 <ul>
		<li> <code>"format"</code>: string of the type <code>"core-series-N"</code>, where <code>N</code> is an integer denoting the version of the series format used</li>
		<li> <code>"title"</code>: string, series title</li>
		<li> <code>"about"</code>: (optional) string, additional info about the series</li>
		<li> <code>"notes"</code>: (optional, private) string, additional notes about the series</li>
	</ul>
	<p><em><code>*</code><strong>private</strong> means that these properties aren't visible for all users unless permissions are given</em></p>
	<p>Example:</p>
	<pre><code>{
	 "format": "core-series-1",
	 "title": "Dummy series #1",
	 "about": "Contains dummy contests",
	 "notes": "Hidden notes",
}
	</code></pre>
	<h3>Additional notes </h3>
	 <ul>
		<li> The series is considered as valid if its metadata object in <code>series.json</code> is syntactically valid, and the contests included in the series are valid</li>
	 </ul>
</div>