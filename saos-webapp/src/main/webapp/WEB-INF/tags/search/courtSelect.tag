<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<%@ attribute name="items" required="true" description="collection of items" rtexprvalue="true" type="java.util.Collection" %>
<%@ attribute name="id" required="true" description="Id for select" rtexprvalue="true" %>
<%@ attribute name="path" required="true" description="Path for input text" rtexprvalue="true" %>
<%@ attribute name="labelName" required="true" description="Name of the label inside form field" rtexprvalue="true" %>
<%@ attribute name="labelText" required="true" description="Text describing label and text inside input placeholder" rtexprvalue="true" %>
<%@ attribute name="selectedItem" required="true" description="The selected element" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="labelEmptyOption" required="false" description="Name of the label for first(empty) option in select" rtexprvalue="true" %>
<%@ attribute name="courtType" required="false" description="" rtexprvalue="true" %>

<c:set var="label" >
	<spring:message code="${labelText}" />
</c:set>


<div class="form-group" data-court-type="${courtType}" >
	<label for="${labelName}" class="col-lg-2 col-sm-3 col-xs-12 control-label"><c:out value="${label}" />:</label>
   	<div class="col-lg-7 col-sm-8 col-xs-11">
   	    
		<form:select path="${path}" id="${id}" class="form-control" disabled="${fn:length(items)==0}" >
			<c:if test="${fn:length(items)>1}">
				<c:set var="emptyOption"><spring:message code="choose"/></c:set>
				<option value=""><spring:message code="${labelEmptyOption}" text="${emptyOption}" /></option>
			</c:if>
		
			<c:forEach items="${items}" var="item" >
				<option value="${item.id}" <c:if test="${selectedItem == item.id}" >selected="selected"</c:if> >
					<c:out value="${item.name}" />
				</option>
			</c:forEach>
		</form:select>
	</div>
</div>


