<#if (breadcrumbs?size > 1)>
<ul class="breadcrumbs">
	<#list breadcrumbs.build() as i>
		<li><a href="${appRoot}/${i.url?html}">${i.name?html}</a><#if i_has_next><span>/</span></#if></li>
	</#list>
</ul>
</#if>