<div class="additional">
	<h2>Additional resources</h2>
	<ul class="breadcrumbs">
		<li><a href="${appRoot}/browse/${node.resourcePath}">[root]</a></li>
		<#if (node.additionalParts?size > 0)>
			<#list 0..(node.additionalParts?size)-1 as i>
			  <li><span>/</span><a href="${appRoot}/browse/${node.resourcePath}/${node.getPath(node.resourceParts?size, 1 + node.resourceParts?size + i)}">${node.additionalParts[i]?html}</a></li>
			</#list> 
		</#if>
	</ul>
	<table class="grid">
		<th>Name</th>
		<th>Size</th>
		<th class="action">Actions</th>
		<#if (node.additionalParts?size > 0)>
			<tr>
				<td><a href="${appRoot}/browse/${node.parentPath?html}">[parent]</a>
				<td></td>
				<td></td>
			</tr>
		</#if>
		<#list files as i>
			<tr>
				<td>
					<#if i.isDirectory()>
					<a href="${appRoot}/browse/${node.path?html}/${i.name?html}">${i.name?html}</a>
					<#else>
						${i.name?html}
					</#if>
				</td>
				<td>
					<#if i.isFile()>
					${i.length()}B
					</#if>
				</td>
				<td class="action">
					<a href="${appRoot}/browse/${node.path?html}/${i.name?html}?action=download">download</a>
					<#if hasViewFull && (node.additionalParts?size > 0 || i.file)>
						<a href="#" class="confirmDelete" rel="action|delete|path|${node.path?html}/${i.name?html}">delete</a>
					</#if>
				</td>
			</tr>
		</#list>
	</table>
	<#if (node.additionalParts?size > 0) && hasViewFull>
		<fieldset>
			<legend>Add directory</legend>
			Name: <input name="newdir" value="<#if newdir??>${newdir?html}</#if>" />
			<button value="action|add-dir" class="submitAction defaultButton">create</button>
		</fieldset>
	</#if>
	<#if ((node.additionalParts?size > 0) && hasViewFull) || (isProblem && (node.additionalParts?size == 0))>
		<fieldset>
			<legend>Add file</legend>
			<input type="file" name="newfile" class="file" />
			<button value="action|add-file" class="submitAction defaultButton">upload</button>
		</fieldset>
	</#if>
</div>