<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<c:set var="cookiePolicyAccepted" value="false" />
  
 <c:forEach var="cookies" items="${cookie}">
    <c:if test="${cookies.value.name == 'cookiePolicyAccepted' }" >
        <c:set var="cookiePolicyAccepted" value="${cookies.value.value}" />
    </c:if>
 </c:forEach>

<div id="cookie-message" class="cookie-message" >
    <div class="container" >
        <div class="row" >
            <div class="col-sm-10 text">
                <spring:message code="cookie.policy.message" />
            </div>
            <div class="col-sm-2">
                <button id="cookie-accept" class="btn btn-primary btn-sm"><spring:message code="cookie.policy.accept" /></button>
            </div>
        </div>
    </div>
</div>

<c:if test="${!cookiePolicyAccepted}" >
    <div class="cookie-window" id="cookie-window">
        <spring:message code="cookie.policy.previewMessage" />
        <button id="cookie-preview-accept" class="btn button-blue btn-xs" ><spring:message code="cookie.policy.accept" /></button>
        <button id="cookie-show-message" class="btn button-blue btn-xs" ><spring:message code="cookie.policy.show" /></button>
    </div>
</c:if>

