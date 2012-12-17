<#if resource??>
	<#if hasView>
		<div class="details">
			<h1>Problem details</h1>
			<p>
				<#if action=="edit">
					<button name="post" value="1" type="submit" class="defaultButton">save changes</button>
				</#if>
				<button value="${appRoot}/browse/${resourcePath}?action=download" class="linkbutton">download</button>
				<#if hasViewFull>
					<button value="${appRoot}/browse/${resourcePath}?action=edit" class="linkbutton">edit</button>
					<button value="action|delete|path|${resourcePath}" class="confirmDelete">delete</button>
					<button value="action|validate" class="submitAction">validate</button>
				</#if>
			</p>
			<table class="grid">
				<tr>
					<td>Id</td>
					<td>${resource.id?html}</td>
				</tr>
				<tr>
					<td>Title</td>
					<td>
						<#if action=="edit">
							<input type="text" name="new_title" value="${resource.title?html}" />
							<input type="hidden" name="old_title" value="${current.title?html}" />
						<#else>
							${resource.title?html}
						</#if>
					</td>
				</tr>
				<tr>
					<td>About</td>
					<td>
						<#if action=="edit">
							<textarea rows="5" cols="30" name="new_about">${resource.about?html}</textarea>
							<input type="hidden" name="old_about" value="${current.about?html}" />
						<#else>
							<@util.nl2br text=resource.about?html/>
						</#if>
						
					</td>
				</tr>
				<#if hasViewFull>
				<tr>
					<td>Origin</td>
					<td>
						<#if action=="edit">
							<input type="text" name="new_origin" value="${resource.origin?html}" />
							<input type="hidden" name="old_origin" value="${current.origin?html}" />
						<#else>
							${resource.origin?html}
						</#if>
					</td>
				</tr>
				<tr>
					<td>Authors</td>
					<td>
						<#if action=="edit">
							<input type="text" name="new_authorsString" value="${resource.authorsString?html}" />
							<input type="hidden" name="old_authorsString" value="${current.authorsString?html}" />
						<#else>
							${resource.authorsString?html}
						</#if>
					</td>
				</tr>
				<tr>
					<td>Test Weights</td>
					<td>
						<#if action=="edit">
							<input type="text" name="new_testWeightsString" value="${resource.testWeightsString?html}" />
							<input type="hidden" name="old_testWeightsString" value="${current.testWeightsString?html}" />
						<#else>
							${resource.testWeightsString?html}
						</#if>
					</td>
				</tr>
				<tr>
					<td>Memory Limit</td>
					<td>
						<#if action=="edit">
							<input type="text" name="new_memoryLimit" value="${resource.memoryLimit?c}" />
							<input type="hidden" name="old_memoryLimit" value="${current.memoryLimit?c}" />
						<#else>
							${resource.memoryLimit?c}
						</#if>
					</td>
				</tr>
				<tr>
					<td>Time Limit</td>
					<td>
						<#if action=="edit">
							<input type="text" name="new_timeLimit" value="${resource.timeLimit?c}" />
							<input type="hidden" name="old_timeLimit" value="${current.timeLimit?c}" />
						<#else>
							${resource.timeLimit?string(".###")}
						</#if>
					</td>
				</tr>
				<tr>
					<td>Checker</td>
					<td>
						<#if action=="edit">
							<#list problemCheckers as i>
								<label><input type="radio" class="radio" name="new_checker" value="${i?html}" <#if resource.checker==i>checked="checked"</#if> />${i?html}</label>
							</#list>
							<input type="hidden" name="old_checker" value="${current.checker?html}" />
						<#else>
							${resource.checker?html}
						</#if>
					</td>
				</tr>
				<tr>
					<td>Notes</td>
					<td>
						<#if action=="edit">
							<textarea rows="5" cols="30" name="new_notes">${resource.notes?html}</textarea>
							<input type="hidden" name="old_notes" value="${current.notes?html}" />
						<#else>
							<@util.nl2br text=resource.notes?html/>
						</#if>
					</td>
				</tr>
				</#if>
			</table>
			<#if action=="edit">
				<p>
					<button name="post" value="1" type="submit" class="defaultButton">save changes</button>
					<button value="${appRoot}/browse/${resourcePath}" class="linkbutton confirmCancel">cancel</button>
				</p>
			</#if>
		</div>
		<#include "additional.ftl">
		<div class="spacer"></div>
	<#else>
		<h2>No permission to view this resource!</h2>
	</#if>
<#else>
	<h2>Problem not found...</h2>
</#if>