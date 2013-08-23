<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<c:url var="newAccountUrl" value="/account"></c:url>
<section>
	<h2 id="pageTitle">Nova Conta</h2>
	<c:if test="${not response.success}">
		<p id="statusMessage">Operação não realizada</p>
	</c:if>
	<ul id="errors">
		<c:forEach var="error" items="${response.errors}">
			<li>${error}</li>
		</c:forEach>
	</ul>
	<form id="newAccountForm" action="${newAccountUrl}" method="POST">
		<h3><spring:message code="${response.account.name}" text="${response.account.name}"></spring:message></h3>
		<input type="text" name="name" placeholder="Account Name">
		<input type="text" name="description" placeholder="Account description">
		<input type="hidden" name="parentAccount.id" value="${response.account.id}">
		<input type="submit" value="Create">
	</form>
	
</section>