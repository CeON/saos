<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>
<%@ tag display-name="monthSelect" description="Prints select form element with year options" %>

<%@ attribute name="id" required="true" description="Select element id" rtexprvalue="true" %>
<%@ attribute name="path" required="true" description="Select element path" rtexprvalue="true" %>
<%@ attribute name="yearRange" required="true" description="Year range. Use dash '-' or comma ',' to define ranges of years. E.g. 1990-2000, 2006, 2008. The order of year options corresponds directly to the order of years in yearRange." rtexprvalue="true" %>


<c:set var="commaSplittedYears" value="${fn:split(yearRange, ',')}"/>


<form:select id="${id}" path="${path}" class="form-control">

<c:forEach items="${commaSplittedYears}" var="year">

    <c:set var="year" value="${fn:trim(year)}"/>

    <c:choose>
        
        <c:when test="${!fn:contains(year, '-')}">
        
            <form:option value="${year}">${year}</form:option>
        
        </c:when>
        
        
        
        <c:otherwise>
            
            <c:set var="dashSplittedYears" value="${fn:split(year, '-')}"/>
            
            <%-- forEach loop can not iterate down and step cannot be < 1. Nevertheless the solution below deals with it and
            can print the years up or down, depending on the range --%>
            
            <c:set var="beginYear" value="${fn:trim(dashSplittedYears[0])}"/>
            <c:set var="endYear" value="${fn:trim(dashSplittedYears[1])}"/>
            <c:set var="order" value="asc"/>
            <c:if test="${beginYear>endYear}">
                <c:set var="beginYear" value="${endYear}"/>
                <c:set var="endYear" value="${fn:trim(dashSplittedYears[0])}"/>
                <c:set var="order" value="desc"/>
            </c:if>
            
            
            <c:forEach begin="${beginYear}" end="${endYear}" step="1" varStatus="loop">
                <c:if test="${order=='asc'}">
                    <form:option value="${beginYear+loop.count-1}">${beginYear+loop.count-1}</form:option>
                </c:if>
                <c:if test="${order=='desc'}">
                    <form:option value="${endYear-loop.count+1}">${endYear-loop.count+1}</form:option>
                </c:if>
            </c:forEach>
        </c:otherwise>
    
    </c:choose>

</c:forEach>


</form:select>
