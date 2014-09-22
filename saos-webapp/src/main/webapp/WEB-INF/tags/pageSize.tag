<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>
<%@ tag display-name="pageInfo" description="Search result page info" small-icon=""%>

<%@ attribute name="page" required="true" description="Current page no" rtexprvalue="true" type="org.springframework.data.domain.Page"%>

<span></span>
<select id="searchPageSize" name="size">
    <option value="20" <c:if test="${page.size==20}"> selected="selected"</c:if>>20</option>
    <option value="50" <c:if test="${page.size==50}"> selected="selected"</c:if>>50</option>
    <option value="100" <c:if test="${page.size==100}"> selected="selected"</c:if>>100</option>
</select>