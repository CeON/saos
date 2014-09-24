<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<%@ attribute name="items" required="true" description="collection of courts" rtexprvalue="true" type="java.util.Collection" %>
<%@ attribute name="path" required="true" description="Path for input text" rtexprvalue="true" %>
<%@ attribute name="labelName" required="true" description="Name of the label inside form field" rtexprvalue="true" %>
<%@ attribute name="labelText" required="true" description="Text describing label and text inside input placeholder" rtexprvalue="true" %>
<%@ attribute name="selectedCourt" required="true" description="The selected institution" rtexprvalue="true" type="java.lang.String" %>

<c:set var="label" >
	<spring:message code="${labelText}" />
</c:set>



<div class="form-group">
	<label for="${labelName}" class="col-sm-2 control-label"><c:out value="${label}" />:</label>
   	<div class="col-sm-7">
   	
		<form:select path="${path}" >
		
			<c:if test="${fn:length(items)>1}">
			   <option value=""></option>
			</c:if>
		
			<c:forEach items="${items}" var="court" >
				<option value="${court.id}" <c:if test="${selectedCourt == court.id}" >selected="selected"</c:if> >
					<c:out value="${court.name}" />
				</option>
			</c:forEach>
		</form:select>
	</div>
</div>



