<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url value="/user/logoff" var="logoff"></c:url>
<c:url value="/account" var="accountHome"></c:url>
<c:url value="/transaction" var="transactionHome"></c:url>
<c:url value="/report" var="reportHome"></c:url>
<header class="navbar navbar-default navbar-fixed-top">
	<div class="container">
		<a href="#" class="navbar-brand">Bufunfa</a>
		<form action="${logoff}" method="POST">
			<input class="btn btn-link navbar-btn navbar-right" type="submit" value="Sair" id="logoffBtn">
		</form>
	</div>
</header>
