<h1>Security Policy</h1>
<div class="richtext">
	<p>This document describes the access rights users have for viewing resources.</p>
	<h3>Default visibility</h3>
	<p>When a new resource is created, by default it is invisible for all users of the system. In order to make it visible and control who can view what kind of information about it, the administrator, the creator or other authorized users can specify explicit access rights.</p>
	<p>This is done in a special json file named "_security.json" and located in the main directory of the resource. It has the following structure: </p>
	<pre><code>{
  "grants": {
    "milo": "view_full",
    "ivan": "view"
   }
}
	</code></pre>
	<h3>Access Rights</h3>
	<p>There are 4 access rights types: </p>
	<ul>
		<li><strong>none</strong>: <p>The user doesn’t know the resource exists. </p></li>
		<li><strong>list</strong>: <p>The user can view the id and title of the resource, i.e. he/she will simply know the resource exists. This means that the user will see the resource when browsing its parent's content, but will not be provided with any additional information.</p></li>
		<li><strong>view</strong>: <p>The user can view the public content of the resource (public according to the visibility settings described below). This right will usually be given to users in order to participate in a contest. The information they need depends on whether the contest has started (cannot see problem statements before that) or finished (cannot see author solutions and tests before that).</p></li>
		<li><strong>view_full</strong>: <p>The user can view both the public and private content of the resource. This includes notes, author solutions, tests and the private directory. This right will usually be given to the team that prepares the contest.</p></li>
	</ul>
	<p>For more information about the difference between view and view_full, see below.</p>
	<p>All rights except list are recursive. This means that if a user has view_full right on a contest, he/she has the same right on the problems in it.</p>
	<p>Access rights are nested, i.e. view_full includes view, which in turn includes list.</p>
	<h3>Public and private content</h3>
	<p>Every resource has public and private part. Users with view right have access to the public part only, while users with view_full right can also see the private part. </p> 
	<h4>Common for all resources:</h4>
	<ul>
		<li>public content:
			<ul>
				<li>title</li>
				<li>about</li>
			</ul>
		</li>
		<li>private content:
			<ul>
			  	<li>notes</li>
			  	<li>_files directory</li>
			  	<li>_security.json</li> 
			</ul>
		</li>
	</ul>
	<h4>Series:</h4>
	<p><em>common to all resources</em></p> 
	<h4>Contest:</h4>
	<ul>
		<li>public content:
			<ul>
				<li>before the contest starts:
					<ul>
			            <li>common public content (title, about, etc.)</li>
			            <li>start-time</li>
			            <li>duration</li>
			            <li>grading-style</li> 
					</ul>
				</li>
        		<li>after the contest starts:
        			<ul>
			            <li>all of the above</li>
			            <li>the public content of the problems</li>
			            <li>problem-order (names of the problems)</li>
			            <li>problem-scores</li>
			            <li>_public_files directory</li>
					</ul>
				</li>
			</ul>
		</li>
		<li>after the content ends:</li>
	</ul>
	<h4>Problem:</h4>
	<ul>
		<li>public content:
			<ul>
        		<li>before the contest starts:
        			<ul>
            			<li>common public content (title, about, etc.)</li>
            		</ul>
            	</li>
            	<li>after the contest starts:
            		<ul>
            			<li>description* files</li>
            			<li>_public_files directory</li>
            		</ul>
            	 </li>
            	 <li>after the contest ends:
            	 	<ul>
            	 		<li>test weights</li>
            	 	</ul>
            	 </li>
            </ul>
		</li>
		<li>private content:
			<ul>
        		<li>time-limit</li>
		        <li>memory-limit</li>
		        <li>authors</li>
		        <li>solution*</li>
		        <li>checker</li>
		        <li>tests</li>
		        <li>origin </li>
			</ul>
		</li>
	</ul>
</div>