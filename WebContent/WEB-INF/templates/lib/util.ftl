<#macro error input="">
	<#if input??>
		<span class="error">${input?html}</span>
	</#if>
</#macro>  
	
	
<#macro errorSummary errors>
	<#if errors?has_content>
		<ul class="validator-summary">
		<#list errors?keys as i>
			<li>${errors[i]?html}</li>
		</#list>
		</ul>
	</#if>
</#macro>  
	

<#macro nl2br text>
	${text?replace("\n", "<br />")}
</#macro>


<#macro status status>
	<div class="status">
		<div class="${status.cssClass}">${status.message}</div>
	</div>
</#macro> 