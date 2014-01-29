<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cf" tagdir="/WEB-INF/tags/cloudfinance" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:url var="transactionUrl" value="/transaction"></c:url>
<section class="content-inner">
	<h2>Detalhes Transação</h2>
	
	<div id="detail" class="list-group transactionList"> 
		<div class="list-group-item">
			<p class="transaction-value pull-right"><fmt:formatNumber value="${response.transaction.destin.entryValue}" type="currency" pattern="#,#00.00#"/> </p>
			<p class="transaction-date"><fmt:formatDate value="${response.transaction.origin.transactionDate}" pattern="dd/MM/yyyy"/> </p>
			<p>
				<span class="transaction-from"><spring:message code="${response.transaction.origin.account.name}"  text="${response.transaction.origin.account.name}"></spring:message></span>
				<span class="glyphicon glyphicon-arrow-right"></span>
				<span class="transaction-to"><spring:message code="${response.transaction.destin.account.name}"  text="${response.transaction.destin.account.name}"></spring:message></span>
			</p>
			<p class="transaction-comment">${response.transaction.destin.comment}</p>
		</div>
	</div>
	<!-- 
	<div id="detail" class="list-group list-group-item">
		<p class="transaction-date"><fmt:formatDate value="${response.transaction.origin.transactionDate}" pattern="dd/MM/yyyy"/> </p>
		<p class="transaction-from">De: <spring:message code="${response.transaction.origin.account.name}"  text="${response.transaction.origin.account.name}"></spring:message></p>
		<p class="transaction-to">Para: <spring:message code="${response.transaction.destin.account.name}"  text="${response.transaction.destin.account.name}"></spring:message></p>
		<p class="transaction-value"><fmt:formatNumber value="${response.transaction.destin.entryValue}" type="currency" pattern="#,#00.00#"/> </p>
		<p class="transaction-comment">${response.transaction.destin.comment}</p>
	</div>
	-->
	<form id="deleteForm" action="${transactionUrl}/delete" method="POST">
		<input class="form-group" type="hidden" name="transactionId" value="${response.transaction.id}">
		<input class="btn btn-default" type="submit" value="Apagar" style="margin-top: 15px;">
	</form>
</section>