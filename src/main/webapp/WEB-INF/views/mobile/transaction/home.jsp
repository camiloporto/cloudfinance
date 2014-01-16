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
	<!-- TODO return date value submitted or save search intervalo on session -->
	<fmt:formatDate value="${response.beginDateFilter}" pattern="dd/MM/yyyy" var="beginDateFilter"/>
	<fmt:formatDate value="${response.endDateFilter}" pattern="dd/MM/yyyy" var="endDateFilter"/>
	<form class="searchFilterForm" id="filterForm" action="${transactionUrl}" method="GET">
		<input class="form-control form-group" type="text" placeholder="Data Inicial" name="begin" value="${beginDateFilter}">
		<input class="form-control form-group" type="text" placeholder="Data Final" name="end" value="${endDateFilter}">
		<input class="btn btn-default" type="submit" value="Filtrar">
	</form>
	<c:forEach var="t" items="${response.transactions}">
		<div class="list-group transactionList"> 
			<a href="${transactionUrl}/${t.id}" class="list-group-item">
				<p class="transaction-value pull-right"><fmt:formatNumber value="${t.destin.entryValue}" type="currency" pattern="#,#00.00#"/> </p>
				<p class="transaction-date"><fmt:formatDate value="${t.origin.transactionDate}" pattern="dd/MM/yyyy"/> </p>
				<p>
					<span class="transaction-from"><spring:message code="${t.origin.account.name}"  text="${t.origin.account.name}"></spring:message></span>
					<span class="glyphicon glyphicon-arrow-right"></span>
					<span class="transaction-to"><spring:message code="${t.destin.account.name}"  text="${t.destin.account.name}"></spring:message></span>
				</p>
				<p class="transaction-comment">${t.destin.comment}</p>
			</a>
		</div>
	</c:forEach>
</section>