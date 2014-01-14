<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<c:url var="newAccountUrl" value="/account"></c:url>
<section class="content-inner">
	<h2 id="pageTitle">Nova Conta</h2>
	<c:if test="${not empty response.errors}">
		<ul id="errors" class="error-list">
			<c:forEach var="error" items="${response.errors}">
				<li>${error}</li>
			</c:forEach>
		</ul>
	</c:if>
	
	<h3><spring:message code="${response.account.name}" text="${response.account.name}"></spring:message></h3>
	<form class="newAccountForm" id="newAccountForm" action="${newAccountUrl}" method="POST">
		<input class="form-control form-group" type="text" name="name" placeholder="Account Name">
		<input class="form-control form-group" type="text" name="description" placeholder="Account description">
		<input type="hidden" name="parentAccount.id" value="${response.account.id}">
		<input class="btn btn-success" type="submit" value="Create">
	</form>
	
</section>