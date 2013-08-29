<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cf" tagdir="/WEB-INF/tags/cloudfinance" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:url var="transactionUrl" value="/transaction"></c:url>
<section>
	<h2>Transações</h2>
	<ul id="menu">
		<li><a href="${transactionUrl}/newForm">Nova</a></li>
	</ul>
	<form id="filterForm" action="${transactionUrl}" method="GET">
		<input type="text" placeholder="Data Inicial" name="begin">
		<input type="text" placeholder="Data Final" name="end">
		<input type="submit" value="Pesquisar">
	</form>
	<ul>
		<c:forEach var="t" items="${response.transactions}">
			<li> 
				<a href="${transactionUrl}/${t.id}">
					<p><fmt:formatDate value="${t.origin.transactionDate}" pattern="dd/MM/yyyy"/> </p>
					<p>De: <spring:message code="${t.origin.account.name}"  text="${t.origin.account.name}"></spring:message></p>
					<p>Para: <spring:message code="${t.destin.account.name}"  text="${t.destin.account.name}"></spring:message></p>
					<p><fmt:formatNumber value="${t.destin.entryValue}" type="currency" pattern="#,#00.00#"/> </p>
					<p>${t.destin.comment}</p>
				</a>
			</li>
		</c:forEach>
	</ul>
</section>