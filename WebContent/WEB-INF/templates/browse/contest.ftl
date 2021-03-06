<#if resource??>
	<#if hasView>
		<div class="details">
			<h1>Contest details</h1>
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
				<tr>
					<td>Start time</td>
					<td>
						<#if action=="edit">
							<input type="text" name="new_startTime" value="${resource.startTime?html}" />
							<input type="hidden" name="old_startTime" value="${current.startTime?html}" />
						<#else>
							${resource.startTime?html}
						</#if>
					</td>
				</tr>
				<tr>
					<td>Duration (minutes)</td>
					<td>
						<#if action=="edit">
							<input type="text" name="new_duration" value="${resource.duration?c}" />
							<input type="hidden" name="old_duration" value="${current.duration?c}" />
						<#else>
							${resource.duration?c}
						</#if>
					</td>
				</tr>
				<tr>
					<td>Grading style</td>
					<td>
						<#if action=="edit">
							<#list gradingStyles as i>
								<label><input type="radio" class="radio" name="new_gradingStyle" value="${i?html}" <#if resource.gradingStyle==i>checked="checked"</#if> />${i?html}</label>
							</#list>
							<input type="hidden" name="old_gradingStyle" value="${current.gradingStyle?html}" />
						<#else>
							${resource.gradingStyle?html}
						</#if>
					</td>
				</tr>
				<#if hasViewFull || resource.hasStarted()>
				<tr>
					<td>Problem order</td>
					<td>
						<#if action=="edit">
							<input type="text" name="new_problemOrderString" value="${resource.problemOrderString?html}" />
							<input type="hidden" name="old_problemOrderString" value="${current.problemOrderString?html}" />
						<#else>
							${resource.problemOrderString?html}
						</#if>
					</td>
				</tr>
				<tr>
					<td>Problem scores</td>
					<td>
						<#if action=="edit">
							<input type="text" name="new_problemScoresString" value="${resource.problemScoresString?html}" />
							<input type="hidden" name="old_problemScoresString" value="${current.problemScoresString?html}" />
						<#else>
							${resource.problemScoresString?html}
						</#if>
					</td>
				</tr>
				</#if>
				<#if hasViewFull>
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
		<#if hasViewFull || resource.hasStarted()>
			<h2>Problems</h2>
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
									<a href="${appRoot}/browse/${resourcePath}/${i.id?html}">${i.id?html}</a>
								<#else>
								${i.id?html}
								</#if>
							</td>
							<td>${i.title?html}</td>
							<td><#if grants[i.id].canViewDetails><@util.nl2br text=i.about?html/></#if></td>
							<td class="action">
								<#if grants[i.id].canViewDetails>
									<a href="${appRoot}/browse/${resourcePath}/${i.id?html}">details</a>
									<a href="${appRoot}/browse/${resourcePath}/${i.id?html}?action=download">download</a>
								</#if>
								<#if grants[i.id].canEdit>
									<a href="${appRoot}/browse/${resourcePath}/${i.id?html}?action=edit">edit</a>
								</#if>
								<#if grants[i.id].canDelete>
									<a href="#" class="confirmDelete" rel="action|delete|path|${resourcePath}/${i.id?html}">delete</a>
								</#if>
							</td>
						</tr>
					</#if>
				</#list>
			</table>
			<#if hasViewFull>
				<fieldset>
					<legend>Add new problem</legend>
					ID: <input name="newresource" value="<#if newresource??>${newresource?html}</#if>" />
					<button value="action|add" class="submitAction defaultButton">add</button>
				</fieldset>
			</#if>
		</#if>
	<#else>
		<h2>No permission to view this resource!</h2>
	</#if>
<#else>
	<h2>Contest not found...</h2>
</#if>