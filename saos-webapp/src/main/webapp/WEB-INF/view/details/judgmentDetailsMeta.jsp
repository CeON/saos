<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<spring:message code="judgmentDetails.meta.keywords" var="detailsMetaKeywords" />

<%-- JudgmentType --%>
<c:if test="${!empty judgment.judgmentType}" >

    <c:choose>
        <c:when test="${judgment.courtType == 'SUPREME'}" >
        
            <c:set var="metaJudgmentType" value="${judgment.scJudgmentForm.name}" />
            <c:set var="detailsMetaKeywords" value="${detailsMetaKeywords}, ${metaJudgmentType}" />
        </c:when>
        <c:otherwise>
        
            <c:set var="metaJudgmentType" >
                <saos:enum value="${judgment.judgmentType}" ></saos:enum>
            </c:set>
            <c:set var="detailsMetaKeywords" value="${detailsMetaKeywords}, ${metaJudgmentType}" />
        </c:otherwise>
    </c:choose>
</c:if>

<%-- CaseNumbers --%>
<c:if test="${!empty judgment.caseNumbers}" >
    
    <c:set var="metaCaseNumbers" >
        <saos:caseNumber items="${judgment.caseNumbers}"/>
    </c:set>
    <c:set var="detailsMetaKeywords" value="${detailsMetaKeywords}, ${metaCaseNumbers}" />
</c:if>

<%-- CourtType --%>
<c:if test="${!empty judgment.courtType}" >

    <spring:message code="pl.edu.icm.saos.persistence.model.CourtType.${judgment.courtType}" var="metaCourtType" />
    <c:set var="detailsMetaKeywords" value="${detailsMetaKeywords}, ${metaCourtType}" />
</c:if>

<%-- CommonCourtJudgment properties --%>
<c:if test="${judgment.courtType == 'COMMON'}" >

	<%-- CommonCourt --%>
    <c:if test="${!empty judgment.courtDivision.court.name}" >
        <c:set var="detailsMetaKeywords" value="${detailsMetaKeywords}, ${judgment.courtDivision.court.name}" />
	</c:if>
    
	<%-- CommonCourtDivision --%>
    <c:if test="${!empty judgment.courtDivision.name}" >   
        <c:set var="detailsMetaKeywords" value="${detailsMetaKeywords}, ${judgment.courtDivision.name}" />
    </c:if>

</c:if>	

<%-- SupremeCourtJudgment properties --%>
<c:if test="${judgment.courtType == 'SUPREME'}" >

    <c:if test="${!empty judgment.scChambers}" >
        <c:set var="metaScChambers" value="" />
        <c:forEach items="${judgment.scChambers}" var="chamber" varStatus="status">
            <c:if test="!status.first" >
                <c:set var="metaScChambers" value="${metaScChambers}, " />
            </c:if>
            <c:set var="metaScChambers" value="${metaScChambers}${chamber.name }" />
        </c:forEach>
        
        <c:set var="detailsMetaKeywords" value="${detailsMetaKeywords}, ${metaScChambers}" />
    </c:if>

    <c:if test="${!empty judgment.scChamberDivision.name}" >
        <c:set var="detailsMetaKeywords" value="${detailsMetaKeywords}, ${judgment.scChamberDivision.name}" />
    </c:if>

</c:if>

<%-- Judges --%>
<c:if test="${!empty judgment.judges}" >
    <c:set var="metaJudges" value="" ></c:set>
    <c:forEach items="${judgment.judges}" var="judge" varStatus="status" >
        
        <c:set var="metaJudges" value="${metaJudges}${judge.name}" />
        
        <c:if test="${!status.last}">
            <c:set var="metaJudges" value="${metaJudges}, " />
        </c:if>
    </c:forEach>
    
    <c:set var="detailsMetaKeywords" value="${detailsMetaKeywords}, ${metaJudges}" />
</c:if>

<%-- Keywords --%> 
<c:if test="${!empty judgment.keywords}" >

    <c:set var="metaKeywords" value="" ></c:set>
    <c:forEach items="${judgment.keywords}" var="keyword" varStatus="status" >
        
        <c:set var="metaKeywords" value="${metaKeywords}${keyword.phrase }" />
        
        <c:if test="${!status.last}">
            <c:set var="metaKeywords" value="${metaKeywords}, " />
        </c:if>
    </c:forEach>
    
    <c:set var="detailsMetaKeywords" value="${detailsMetaKeywords}, ${metaKeywords}" />
</c:if>

<%-- LegalBases --%> 
<c:if test="${!empty judgment.legalBases}" >

    <c:set var="metaLegalBases" value="" ></c:set>
    <c:forEach items="${judgment.legalBases}" var="legalBase" varStatus="status" >
        
        <c:set var="metaLegalBases" value="${metaLegalBases}${legalBase }" />
        
        <c:if test="${!status.last}">
            <c:set var="metaLegalBases" value="${metaLegalBases}, " />
        </c:if>
    </c:forEach>
    
    <c:set var="detailsMetaKeywords" value="${detailsMetaKeywords}, ${metaLegalBases}" />
</c:if>


<meta name="keywords" content="${detailsMetaKeywords}" />


<%-- Meta description --%> 
<spring:message var="detailsMetaDescription" code="details.meta.judgment" />

<c:choose>
    <c:when test="${judgment.judgmentType == 'RESOLUTION' }" >
        <spring:message var="releasedBy" code="details.meta.releasedBy.feminine" />
    </c:when>
    <c:when test="${judgment.judgmentType == 'SENTENCE' }" >
        <spring:message var="releasedBy" code="details.meta.releasedBy.masculine" />
    </c:when>
    <c:otherwise>
        <spring:message var="releasedBy" code="details.meta.releasedBy.neuter" />
    </c:otherwise>
</c:choose>


<c:if test="${!empty metaJudgmentType}" >
    <c:if test="${judgment.courtType == 'SUPREME'}" >
        <c:set var="metaJudgmentType" value="${fn:toUpperCase(fn:substring(metaJudgmentType, 0, 1))}${fn:substring(metaJudgmentType, 1, fn:length(metaJudgmentType))}" />    
    </c:if>

    <c:set var="detailsMetaDescription" value="${metaJudgmentType}" />
</c:if>

<c:if test="${!empty metaCaseNumbers}" >
    <spring:message code="details.meta.withSignature" var="withSignature" /> 
    <c:set var="detailsMetaDescription" value="${detailsMetaDescription} ${withSignature} ${metaCaseNumbers}" />
</c:if>

<c:choose>
    <c:when test="${judgment.courtType == 'COMMON'}" >
        <c:if test="${!empty judgment.courtDivision.court.name}" >
            <c:set var="detailsMetaDescription" value="${detailsMetaDescription} ${releasedBy} ${judgment.courtDivision.court.name}" />
        </c:if>
    </c:when>
    <c:when test="${judgment.courtType == 'SUPREME'}" >
        <c:if test="${!empty metaScChambers}" >
            <c:set var="detailsMetaDescription" value="${detailsMetaDescription} ${releasedBy} ${metaScChambers}" />
        </c:if>
    </c:when>
    <c:when test="${judgment.courtType == 'NATIONAL_APPEAL_CHAMBER'}" >
        <spring:message code="details.meta.nationalAppealChamber" var="metaCourtType" />
        <c:set var="detailsMetaDescription" value="${detailsMetaDescription} ${releasedBy} ${metaCourtType}" />
    </c:when>
    <c:otherwise>
        <c:if test="${!empty metaCourtType}" >
		    <c:set var="detailsMetaDescription" value="${detailsMetaDescription} ${releasedBy} ${metaCourtType}" />
		</c:if>
    </c:otherwise>
</c:choose>


<c:if test="${!empty metaJudges}" >
    <spring:message code="details.meta.judges" var="containingJudges" />
    <c:set var="detailsMetaDescription" value="${detailsMetaDescription} ${containingJudges} ${metaJudges}." />
</c:if>


<meta name="description" content="${detailsMetaDescription}" />


<%-- Open graph ogp.me --%>
<%@ include file="../common/openGraphMeta.jsp" %>
<meta property="og:title" content="<c:out value="${metaCaseNumbers}" /> <spring:message code='pageTitle.judgmentDetails' /> - <spring:message code="saos.fullnameAndShortcut"/>">
<meta property="og:description" content="${detailsMetaDescription}">



<%-- title --%>
<title><c:out value="${metaCaseNumbers}" /> <spring:message code='pageTitle.judgmentDetails' /> - <spring:message code="saos.fullnameAndShortcut"/></title>
