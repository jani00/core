<h1>Available series</h1>
<table class="grid">
	<tr>
		<th>Id</th>
		<th>Title</th>
		<th>About</th>
		<th class="action">Actions</th>
	</tr>
	<#list list as i>
		<#if grants[i.id].canViewBasic>
			<tr>
				<td>
					<#if grants[i.id].canViewDetails>
						<a href="${appRoot}/browse/${i.id?html}">${i.id?html}</a>
					<#else>
					${i.id?html}
					</#if>
				</td>
				<td>${i.title?html}</td>
				<td><#if grants[i.id].canViewDetails><@util.nl2br text=i.about?html/></#if></td>
				<td class="action">
					<#if grants[i.id].canViewDetails>
						<a href="${appRoot}/browse/${i.id?html}">details</a>
						<a href="${appRoot}/browse/${i.id?html}?action=download">download</a>
					</#if>
					<#if grants[i.id].canEdit>
						<a href="${appRoot}/browse/${i.id?html}?action=edit">edit</a>
					</#if>
					<#if grants[i.id].canDelete>
						<a href="#" class="confirmDelete" rel="action|delete|path|${i.id?html}">delete</a>
					</#if>
				</td>
			</tr>
		</#if>
	</#list>
</table>
<#if hasViewFull>
	<fieldset>
		<legend>Add new series</legend>
		ID: <input name="newresource" value="<#if newresource??>${newresource?html}</#if>" />
		<button value="action|add" class="submitAction">add</button>
	</fieldset>
</#if>