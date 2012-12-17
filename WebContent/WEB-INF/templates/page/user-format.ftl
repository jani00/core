<h1>User Format</h1>
<div class="richtext">
	<p>This document describes the main user information format for the system. The repository uses the user information to check authentication in order to control the access to itself.</p>
	<p>Each system user should be described by a file named user.json which is located in the user's directory. Thus, there will not be any naming conflicts. For example, the full path to a user description file may be /souls/users/joro/user.json. It is possible for each user's directory to contain additional information (such as a user's photo).</p>
	<p>The user information file ("user.json") should contain the following data items (an item is considered mandatory unless explicitly stated otherwise):</p>
	<ul>
    	<li>"login" : the name by which the user logs onto the system. This is also the name of the user's directory.</li>
    	<li>"password" : the hash code of the user's password. The password is never stored in plain text.</li>
    	<li>"email" : the user's e-mail address.</li>
    	<li>"real_name" : the user's first and last name.</li>
    	<li>"city" : (optional) the place where the user resides.</li>
    	<li>"organizations" : (optional) a list of organizations the user belongs to (such as schools, universities, etc.).</li>
    	<li>"about" : (optional) additional piece of information about the user.</li>
    </ul> 
	<p>Example:</p>
	<pre><code>{
    "login": "john",
    "real_name": "John Smith",
    "email": "johnsmith@example.com",
    "city": "City",
    "organizations": [
        "Work", "Office"
    ],
    "password": "8622fa22edd747bca30db20e98bedc1ad8ed64d6735a25bab837063dc4be2d6"

}
	</code></pre>
	<h3>Additional notes </h3>
	 <ul>
		<li>As already explained, a user's password is never stored in plain text. Instead, its hash code is saved in the user.json file. The repository administrator will be provided with tools that enable him/her to automatically compute the hash code of a given password (or more generally - a string).</li>
		<li>In addition, another command line tool that provides means for user information validation will also be present.</li>
	 </ul>
</div>