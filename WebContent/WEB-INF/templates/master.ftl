<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${htmlTitle}</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script src="${appRoot}/assets/javascript/jquery-1.8.3_min.js" type="text/javascript"></script>
<script src="${appRoot}/assets/javascript/main.js" type="text/javascript"></script>
<link href="${appRoot}/assets/style/main.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<ul id="menu">
		<li><a href="${appRoot}/index">home</a></li>
	<#if currentUser??>
		<li><a href="${appRoot}/browse">browse</a></li>
		<li><a href="${appRoot}/profile">Profile</a></li>
		<li class="last"><a href="${appRoot}/logout">Logout (${currentUser.login?html})</a></li>
	<#else>
		<li><a href="${appRoot}/login">login</a></li>
		<li class="last"><a href="${appRoot}/registration">register</a></li>
	</#if>
	</ul>
	<#import "/lib/util.ftl" as util>
	<div id="content">
		<#if template??>
			<#include "${template}.ftl">
		</#if>
	</div>
	<div id="footer">
		Contest Repository CORE ver 2.0
	</div>
	<#if debug>
		<p class="debug">R: ${requestElapsedTime?string("0.0000")}s; S: ${servletElapsedTime?string("0.00")}s</p>
	</#if>
</body>
</html>