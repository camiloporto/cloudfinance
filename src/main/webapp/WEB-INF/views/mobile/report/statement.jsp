<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cf" tagdir="/WEB-INF/tags/cloudfinance" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:url var="statementUrl" value="/report/statement"></c:url>
<section class="content-inner">
	<h2>Extrato de Conta</h2>
	
	<fmt:formatDate value="${statementBeginDate}" pattern="dd/MM/yyyy" var="beginDateFilterFormatted"/>
	<fmt:formatDate value="${statementEndDate}" pattern="dd/MM/yyyy" var="endDateFilterFormatted"/>
	
	<form class="statementForm" id="statementForm" action="${statementUrl}" method="GET">
		<div class="form-group">
			<label>
				Conta:
				<select name="accountId" class="form-control">
					<cf:accountOptionList accounts="${response.accountList}" accountSelected="${formAccountId}"></cf:accountOptionList>
				</select>
			</label>
		</div>
		<input class="form-control form-group" type="text" placeholder="Data Inicial" name="begin" value="${beginDateFilterFormatted}">
		<input class="form-control form-group" type="text" placeholder="Data Final" name="end" value="${endDateFilterFormatted}">
		<input class="btn btn-primary" type="submit" value="Extrato">
	</form>
	<c:if test="${not empty response.accountStatement}">
	 <div class="panel statementList">
		<c:if test="${response.accountStatement.balanceBeforeInterval >= 0}">
		 	<c:set var="valueColorClass" value="credit-color"></c:set>
		</c:if>
		<c:if test="${response.accountStatement.balanceBeforeInterval < 0}">
		 	<c:set var="valueColorClass" value="debit-color"></c:set>
		</c:if>
		 <div class="panel-heading statementList-balance-panel">
		 	<h4 class="statementList-balance-panel">Saldo Anterior:</h4>
		 	<span class="pull-right ${valueColorClass}"><fmt:formatNumber value="${response.accountStatement.balanceBeforeInterval}" type="currency" pattern="#,#00.00#"/></span>
		 </div>
		 <div class="panel-body">
		 	<c:forEach var="entry" items="${response.accountStatement.entries}">
		 		<c:if test="${entry.amount >= 0}">
		 			<c:set var="arrowColorClass" value="glyphicon-arrow-up"></c:set>
		 			<c:set var="valueColorClass" value="credit-color"></c:set>
		 		</c:if>
		 		<c:if test="${entry.amount < 0}">
		 			<c:set var="arrowColorClass" value="glyphicon-arrow-down"></c:set>
		 			<c:set var="valueColorClass" value="debit-color"></c:set>
		 		</c:if>
		 		<div class="list-group statementList">
		 			<div class="list-group-item">
		 				<p class="statement-entry-value pull-right ${valueColorClass}"><fmt:formatNumber value="${entry.amount}" type="currency" pattern="#,#00.00#"/> </p>
						<p class="statement-entry-date"><fmt:formatDate value="${entry.date}" pattern="dd/MM/yyyy"/> </p>
						<p>
							<span class="glyphicon ${arrowColorClass}"></span>
							<span class="entry-involvedAccount">
								<spring:message code="${entry.involvedAccount.name}"  text="${entry.involvedAccount.name}"></spring:message>
							</span>
						</p>
						<p class="statement-comment">${entry.description}</p>
		 			</div>
		 		</div>
		 	</c:forEach>
		 </div>
		 <div class="panel-footer statementList-balance-panel">
		 	<c:if test="${response.accountStatement.balanceAfterInterval >= 0}">
			 	<c:set var="valueColorClass" value="credit-color"></c:set>
			</c:if>
			<c:if test="${response.accountStatement.balanceAfterInterval < 0}">
			 	<c:set var="valueColorClass" value="debit-color"></c:set>
			</c:if>
		 	<h4>Saldo Final:</h4>
		 	<span class="pull-right ${valueColorClass}"><fmt:formatNumber value="${response.accountStatement.balanceAfterInterval}" type="currency" pattern="#,#00.00#"/></span>
		 </div>
	 </div>
	</c:if>
</section>