<h1>Login</h1>
<#if validator??>
	<@util.errorSummary errors=validator.errors/>
</#if>
<form method="post" action="">
	<p>
		<label><span class="title"><em>*</em>Username:</span> <input type="text" name="login" value="<#if login??>${login?html}</#if>" /></label>
	</p> 
	<p>
		<label><span class="title"><em>*</em>Password:</span> <input type="password" name="password" /></label>
	</p>
	<p><button name="post" value="1" class="submit">Login</button></p>
</form>