<#if status?? && status.message != "">
	<@util.status status=status />
</#if>
<#if validator??>
	<@util.errorSummary errors=validator.errors/>
</#if>
<#include "breadcrumbs.ftl">
<form method="post" action="" enctype="multipart/form-data">
	<#include "browse/${resourceTemplate}.ftl">
</form>
<div class="clear"></div>
<#include "breadcrumbs.ftl">