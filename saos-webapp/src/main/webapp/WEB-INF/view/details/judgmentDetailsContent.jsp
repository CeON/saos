<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<%-- Judgment content --%>            
<h1 ><spring:message code="judgmentDetails.judgmentFullText" /></h1>

<div class="info-box" >
    <div class="info-box-nav">
        <spring:message code="judgmentDetails.judgmentFullText.hint.title" var="fullTextHintTitle" />
        <spring:message code="judgmentDetails.judgmentFullText.hint" var="fullTextHint" />
        <saos:hint title="${fullTextHintTitle}" content="${fullTextHint}" placement="left" />
    </div>
</div>
        
<c:choose>
    <c:when test="${judgment.textContent.contentInFile}">
        <c:set var="filePath" value="${contextPath}/files/judgments/${judgment.textContent.filePath}" />
    </c:when>
    <c:otherwise>
        <c:set var="filePath" value="${contextPath}/judgments/content/${judgmentId}.html" />
    </c:otherwise>
</c:choose>

<c:set var="filetypeIconPath" value="${contextPath}/static/image/icons/filetype/${fn:toLowerCase(judgment.textContent.type)}.png" />
<c:set var="filetypeIconAlt"><saos:enum value="${judgment.textContent.type}" /></c:set>
<c:set var="downloadFileTextMessage"><spring:message code="judgmentDetails.judgmentFullText.download.${fn:toLowerCase(judgment.textContent.type)}" /></c:set>
            
<div class="judgment-content-buttons">
    <a class="icon-${fn:toLowerCase(judgment.textContent.type)}" href="${filePath}" title="${downloadFileTextMessage}" >
        
    </a>
</div>

<div class="body">
    <c:out value="${formattedTextContent}" escapeXml="false" />
</div>


