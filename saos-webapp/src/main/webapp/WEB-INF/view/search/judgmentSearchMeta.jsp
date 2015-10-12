<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<saos:metaTag code="search.meta.keywords" name="keywords" />
<saos:metaTag code="search.meta.pageDescription" name="description" />


<%-- Open graph ogp.me --%>
<%@ include file="../common/openGraphMeta.jsp" %>
<meta property="og:title" content="<spring:message code="pageTitle.judgmentSearch" /> - <spring:message code="saos.fullnameAndShortcut"/>">
<meta property="og:description" content="<spring:message code='search.meta.pageDescription'/>">


<title><spring:message code="pageTitle.judgmentSearch" /> - <spring:message code="saos.fullnameAndShortcut"/></title>
