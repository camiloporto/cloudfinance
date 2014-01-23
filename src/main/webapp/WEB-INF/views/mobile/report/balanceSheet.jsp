<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cf" tagdir="/WEB-INF/tags/cloudfinance" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:url var="balanceUrl" value="/report/balanceSheet"></c:url>
<section class="content-inner">
	<h2>Balanço</h2>
	
	<fmt:formatDate value="${sessionBalanceDate}" pattern="dd/MM/yyyy" var="sessionBalanceDateFormatted"/>
	<form id="balanceForm" action="${balanceUrl}" method="GET">
		<c:if test="${not empty response.errors}">
			<ul id="errors" class="error-list">
				<c:forEach var="error" items="${response.errors}">
					<li>${error}</li>
				</c:forEach>
			</ul>
		</c:if>
		<input class="form-control form-group" name="balanceDate" placeholder="Data do Balanço" type="text" value="${sessionBalanceDateFormatted}">
		<input class="btn btn-primary" type="submit" value="Balanço">
	</form>
	<c:if test="${not empty response.balanceSheet}">
		<div id="balanceSheetDiv">
			<cf:balanceSheet balanceSheet="${response.balanceSheet}"></cf:balanceSheet>
		</div>
	</c:if>
</section>