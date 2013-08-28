<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cf" tagdir="/WEB-INF/tags/cloudfinance" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<section>
	<h2>Transações</h2>
	<ul>
		<c:forEach var="t" items="${response.transactions}">
			<li>
				<p><fmt:formatDate value="${t.origin.transactionDate}" pattern="dd/MM/yyyy"/> </p>
				<p>De: <spring:message code="${t.origin.account.name}"  text="${t.origin.account.name}"></spring:message></p>
				<p>Para: <spring:message code="${t.destin.account.name}"  text="${t.destin.account.name}"></spring:message></p>
				<p><fmt:formatNumber value="${t.destin.entryValue}" type="currency" pattern="#,#00.00#"/> </p>
				<p>${t.destin.comment}</p>
			</li>
		</c:forEach>
	</ul>
</section>