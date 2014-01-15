<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cf" tagdir="/WEB-INF/tags/cloudfinance" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:url var="transactionUrl" value="/transaction"></c:url>
<section class="content-inner">
	<h2>Transações</h2>
	<ul id="menu" class="transactionInnerMenu">
		<li><a class="btn btn-primary btn-lg" href="${transactionUrl}/newForm">Nova</a></li>
	</ul>
	<form class="searchFilterForm" id="filterForm" action="${transactionUrl}" method="GET">
		<input class="form-control form-group" type="text" placeholder="Data Inicial" name="begin">
		<input class="form-control form-group" type="text" placeholder="Data Final" name="end">
		<input class="btn btn-default" type="submit" value="Filtrar">
	</form>
	<ul class="transactionList list-unstyled list-group">
		<c:forEach var="t" items="${response.transactions}">
			<li class="list-group-item"> 
				<a href="${transactionUrl}/${t.id}">
					<p class="transaction-value pull-right"><fmt:formatNumber value="${t.destin.entryValue}" type="currency" pattern="#,#00.00#"/> </p>
					<p class="transaction-date"><fmt:formatDate value="${t.origin.transactionDate}" pattern="dd/MM/yyyy"/> </p>
					<p class="transaction-from"><spring:message code="${t.origin.account.name}"  text="${t.origin.account.name}"></spring:message></p>
					<span class="glyphicon glyphicon-arrow-right"></span>
					<p class="transaction-to"><spring:message code="${t.destin.account.name}"  text="${t.destin.account.name}"></spring:message></p>
					<p class="transaction-comment">${t.destin.comment}</p>
				</a>
			</li>
		</c:forEach>
	</ul>
</section>