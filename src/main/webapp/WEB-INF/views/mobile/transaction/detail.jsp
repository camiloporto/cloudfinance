<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cf" tagdir="/WEB-INF/tags/cloudfinance" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:url var="newTransactionUrl" value="/transaction"></c:url>
<section>
	<h2>Detalhes Transação</h2>
	<div id="detail">
		<p><fmt:formatDate value="${response.transaction.origin.transactionDate}" pattern="dd/MM/yyyy"/> </p>
		<p>De: <spring:message code="${response.transaction.origin.account.name}"  text="${response.transaction.origin.account.name}"></spring:message></p>
		<p>Para: <spring:message code="${response.transaction.destin.account.name}"  text="${response.transaction.destin.account.name}"></spring:message></p>
		<p><fmt:formatNumber value="${response.transaction.destin.entryValue}" type="currency" pattern="#,#00.00#"/> </p>
		<p>${response.transaction.destin.comment}</p>
	</div>
</section>