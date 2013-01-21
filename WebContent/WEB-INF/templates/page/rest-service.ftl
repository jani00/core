<h1>REST Service</h1>
<div class="richtext">
	<p>This document describes the REST service for the repository.</p>
	<p>The Contest Repository provides access via a REST service. Its purpose is to help with automation while accessing and manipulating resources.</p>
	<p>The REST service is available at url <a href="${appRoot}/rest">${httpRoot}/rest</a></p>
	<h3>Authentication</h3>
	<p>In order to access the REST service, an authentication is required. The following parameters should be passed on each request in order for successful authentication:</p>
	<ul>
		<li>login - The user login.</li>
		<li>stamp - The timestamp on the user's machine. It is the time in milliseconds since January 1, 1970, 00:00:00 GMT</li>
		<li>token - which should be calculated like sha256(ip + timestamp + sha256(password)), where ip is the user's ip address.</li>
	</ul>
	<h3>Methods</h3>
	<p>The REST service provides the following HTTP methods:</p>
	<ul>
		<li><strong>GET</strong>:
			<ul>
				<li>[any URI, <strong>not</strong> starting with "_zip"], e.g. "/series1/contest2":
					<ul>
						<li>If the URI corresponds to a directory, returns a JSON, describing the resource, its children and files.</li>
						<li>If the URI corresponds to a file, returns the file as a download.</li>
					</ul>
				</li>
				<li>[any URI, starting with "_zip"]:
					<ul>
						<li>If the URI corresponds to a directory, returns a zip for it.</li>
						<li>If the URI corresponds to a file, returns nothing.</li>
					</ul>
				</li> 
			</ul>
		</li>
		<li><strong>PUT</strong>:
			<ul>
				<li>[any URI, <strong>not</strong> starting with "_zip"], e.g. "/series1/contest2/contest.json":
					<ul>
						<li>If the URI corresponds to a directory, does nothing.</li>
						<li>If the URI corresponds to a file, saves the posted file on the server to the URI, replacing the existing file, if any.</li>
					</ul>
				</li>
				<li>[any URI, starting with "_zip"]:
					<ul>
						<li>If the URI corresponds to a directory, unzips the given file to the server in the directory, replacing everything in it.</li>
						<li>If the URI corresponds to a file, does nothing</li>
					</ul>
				</li>
			</ul>
		</li>
		<li><strong>POST</strong>:
			<ul>
				<li>[any URI], e.g. "/series1/contest2/":
					<ul>
						<li>If the URI corresponds to a resource, creates a new subresource (in this case - a problem). Expects a parameter "name" which contains the name of the new resource.</li>
						<li>If the URI corresponds to an additional resource directory, e.g. "/series1/_files/dir", creates a new subdirectory. Expects a parameter "name" which contains the name of the new directory.</li>
					</ul>
				</li>
			</ul>
		</li>
		<li><strong>DELETE</strong>:
			<ul>
				<li>[any URI]:
					<p>Deletes the file, corresponding to the URI, recursive if a directory</p>
				</li>
			</ul>
		</li>
	</ul>
	<p>The REST service returns either a JSON, describing the result or a download of a file.</p>
	<h3>Return Codes</h3>
	<p>The REST service utilizes HTTP codes in the response in order to indicate the result of the request.</p>
	<ul>
		<li><strong>200</strong>: Returned in any successful request, except when something is created (see 201).</li>
		<li><strong>201</strong>: Returned if a new resource or directory is created, or if a file is successfully uploaded.</li>
		<li><strong>400</strong>: Returned if the service does not know how to handle the request or if a required parameter failed validation.</li>
		<li><strong>401</strong>: Returned if authentication failed for the request.</li>
	</ul>
</div>