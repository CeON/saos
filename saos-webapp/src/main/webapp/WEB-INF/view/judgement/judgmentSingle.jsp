<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>


<div class="container judgment-page block">
	
	
	<!-- 
	<div>Powrót do listy rezultatów</div>
	 -->
	
	<h4><c:out value="${judgment.judgmentType}" escapeXml="false" /></h2>
	<h2><c:out value="${judgment.caseNumber}" escapeXml="false" /></h2>
	
	<div class="col-md-12" >
	
		<ul class="judgment-data">
		<!-- 
			<li>
				<div class="" >
					<div class="label-title" >Numer sprawy:</div>
					<div class="desc" ><c:out value="${judgment.caseNumber}" /></div>
				</div>
			</li>
		 -->
		 
			<li>
				<div class="" >
					<div class="label-title" >Sąd:</div>
					<div class="desc" >Sąd Rejonowy w Piasecznie</div>
				</div>
			</li>
			<li>
				<div class="" >
					<div class="label-title" >Data orzeczenia:</div>
					<div class="desc" ><c:out value="${judgment.judgmentDate}" /></div>
				</div>
			</li>
			
			<li>
				<div class="" >
					<div class="label-title" >Skład sędziowski:</div>
					<div class="desc" >
						<c:forEach items="${judgment.judges}" var="judge" >
							<p><c:out value="${judge.name}" /></p>
						</c:forEach>
					</div>
				</div>
			</li>
			
			<li>
				<div class="" >
					<div class="label-title" >Protokolanci:</div>
					<div class="desc" >
						<c:forEach items="${judgment.courtReporters}" var="reporter" >
							<p><c:out value="${reporter}" /></p>
						</c:forEach>
					</div>
				</div>
			</li>
			<li>
				<div class="" >
					<div class="label-title" >Rozstrzygnięcie:</div>
					<div class="desc" ><c:out value="${judgment.decision}" />&nbsp;</div>
				</div>
			</li>
			<li>
				<div class="" >
					<div class="label-title" >Uzasadnienie:</div>
					<div class="desc" ><c:out value="${judgment.reasoning.judgment.caseNumber}" /> <c:out value="${judgment.reasoning.text}" /></div>
				</div>
			</li>
			
			
		</ul>
	
		<h4>Słowa kluczowe</h4>
		<div class="keywords">	
			<c:forEach items="${keywords}" var="keyword" >
				<div class="keyword"><c:out value="${keyword}" /></div>
			</c:forEach>
		</div>
		
		<h4>Podstawa prawna:</h4>
		<div class="legalBases">	
			<c:forEach items="${judgment.legalBases}" var="legalBase" >
				<div class="legalBase"><c:out value="${legalBase}" /></div>
			</c:forEach>
		</div>
		
		<h4>Powołane przepisy:</h4>
		<ul class="referencedRegulations">	
			<c:forEach items="${judgment.referencedRegulations}" var="referencedRegulation" >
				<li class="legalBase"><c:out value="${referencedRegulation.judgment.caseNumber}" /></li>
			</c:forEach>
		</ul>
	
	
		</br>
		</br>
		</br>
		</br>
	
		 <!-- Button trigger modal -->
		<button class="btn btn-primary" data-toggle="modal" data-target="#myModal">
		  Zobacz pełny tekst orzeczenia
		</button>
	
		<!-- Modal -->
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
		        <h4 class="modal-title" id="myModalLabel">Orzeczenie <c:out value="${judgment.caseNumber}" /></h4>
		      </div>
		      <div class="modal-body">
		      	<c:out value="${judgment.textContent}" escapeXml="false" />
		      </div>
		      <!-- 
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		        <button type="button" class="btn btn-primary">Save changes</button>
		      </div>
		       -->
		    </div>
		  </div>
		</div>
			 
			 
	
		<%--
		
		<div class="case-number"></div>
		<div><c:out value="${judgment.judgmentDate}" /></div>
		<div><c:out value="${judgment.judgmentType}" /></div>
		
		<c:forEach items="${judgment.judges}" var="judge" >
			<p><c:out value="${judge.name}" /></p>
		</c:forEach>
		
		 --%>
		
	</div>
	
	<%--
	<div class="col-md-12" >
		<c:out value="${judgment.summary}" />
		
		<c:out value="${judgment.textContent}" escapeXml="false" />
	</div>
	 --%>
	 
	 
	 



	 
	 
</div>