<h1>Profile</h1>
<#if status?? && status.message != "">
	<@util.status status=status />
</#if>
<form method="post" action="">
	<p>
		<label><span class="title"><em>*</em>Username:</span> <input type="text" readonly="readonly" value="${user.login?html}" /></label>
	</p> 
	<p>
		<label><span class="title"><em>*</em>Password:</span> <input type="password" name="password" /></label>
		<@util.error input=user.validator.getError("password")/> 
	</p>
	<p>
		<label><span class="title"><em>*</em>Repeat password:</span> <input type="password" name="confirmPassword" /></label>
		<@util.error input=user.validator.getError("confirmPassword")/> 
	</p>
	<p>
		<label><span class="title"><em>*</em>E-mail:</span> <input type="text" name="email" value="${user.email?html}" /></label>
		<@util.error input=user.validator.getError("email")/> 
	</p>
	<p>		
		<label><span class="title"><em>*</em>Real name:</span> <input type="text" name="realName" value="${user.realName?html}" /></label>
		<@util.error input=user.validator.getError("realName")/> 
	</p>
	<p>		
		<label><span class="title">City:</span> <input type="text" name="city" value="${user.city?html}" /></label>
		
	</p>
	<p> 
		<label><span class="title">Organization (comma separated):</span> <input type="text" name="organizations" value="${user.organizationsString?html}" /></label>
	</p>
	<p>
		<label><span class="title">About:</span><textarea rows="3" cols="40" name="about" >${user.about?html}</textarea></label>
	</p>
	<p><button name="post" value="1" class="submit">update</button></p>
</form>
